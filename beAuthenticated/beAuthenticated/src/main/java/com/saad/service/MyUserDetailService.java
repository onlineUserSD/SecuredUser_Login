package com.saad.service;

import com.saad.entity.UserEntity;
import com.saad.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {
    @Autowired
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity existinguser =userRepository.findbyEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("Email not found for email: "+email));
        return new User(existinguser.getEmail(),existinguser.getPassword(),new ArrayList<>());
    }


}
