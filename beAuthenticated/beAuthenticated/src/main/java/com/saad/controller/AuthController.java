package com.saad.controller;

import com.saad.io.AuthRequest;
import com.saad.io.AuthResponse;
import com.saad.service.MyUserDetailService;
import com.saad.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailService myUserDetailService;
    private final JwtUtil jwtUtil;
@PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request){
    try{
        authenticate(request.getEmail(),request.getPassword());
       final UserDetails userDetails = myUserDetailService.loadUserByUsername(request.getEmail());
       final String jwtToken = jwtUtil.generateToken(userDetails);
        ResponseCookie cookie = ResponseCookie.from("jwt",jwtToken)
                .httpOnly(true)
                .path("/")
                .maxAge(Duration.ofDays(1))
                .sameSite("Strict")
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,cookie.toString())
                .body(new AuthResponse(request.getEmail(),jwtToken));
    } catch (BadCredentialsException ex){
        Map<String,Object> error = new HashMap<>();
        error.put("error",true);
        error.put("messsage", "Email or password is incorrect");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
    catch ( DisabledException ex){
        Map<String,Object> error = new HashMap<>();
        error.put("error",true);
        error.put("messsage", "Account is Disabled");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }
    catch (Exception ex){
        Map<String,Object> error = new HashMap<>();
        error.put("error",true);
        error.put("messsage", "Authentication Failed");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }


}

    private void authenticate(String email, String password) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
    }
}
