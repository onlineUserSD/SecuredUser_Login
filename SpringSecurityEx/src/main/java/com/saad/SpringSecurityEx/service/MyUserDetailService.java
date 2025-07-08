package com.saad.SpringSecurityEx.service;
import com.saad.SpringSecurityEx.model.Users;
import com.saad.SpringSecurityEx.model.UsersPrincipals;
import com.saad.SpringSecurityEx.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users user = userRepo.findByUsername(username);

        if(user==null){
            System.out.println("USERNAME NOT FOUND");
            throw new UsernameNotFoundException("USERNAME NOT FOUND");
        }

        return new UsersPrincipals(user);
    }
}
