package com.saad.filter;

import com.saad.service.MyUserDetailService;
import com.saad.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtResponseFilter extends OncePerRequestFilter {
    private final MyUserDetailService myUserDetailService;
    private final JwtUtil jwtUtil;

    private static final List<String> PUBLIC_URLS = List.of("/login","/register","/send-reset-otp", "/reset-password","/logout");
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       String path = request.getServletPath();

       if(PUBLIC_URLS.contains(path)){
           filterChain.doFilter(request,response);
           return;
       }
       String jwt = null;
       String email = null;

       final String authorizationHeader = request.getHeader("Authorization");
       if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
           jwt=authorizationHeader.substring(7);
       }

       //2 If not found in header , check cookeis
       if(jwt == null){
           Cookie[] cookie = request.getCookies();
           if(cookie != null){
               for(Cookie cookie1 : cookie){
                   if("jwt".equals(cookie1.getName())){
                       jwt = cookie1.getValue();
                       break;
                   }
               }
           }
       }

       // validate the token adn send the security context

        if(jwt != null){
            email = jwtUtil.extractEmail(jwt);
            if(email != null && SecurityContextHolder.getContext().getAuthentication() == null ){
                UserDetails userDetails = myUserDetailService.loadUserByUsername(email);
                if(jwtUtil.validateToken(jwt,userDetails)){
                    UsernamePasswordAuthenticationToken authenticationToken=
                            new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
        }

        filterChain.doFilter(request,response);

    }
}
