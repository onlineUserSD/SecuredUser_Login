package com.saad.controller;

import com.saad.io.ProfileRequest;
import com.saad.io.ProfileResponse;
import com.saad.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
//@RequestMapping("api/v1.0")
public class ProfileController {
    @Autowired
    private final ProfileService profileService;
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ProfileResponse register(@Valid  @RequestBody ProfileRequest request){
         ProfileResponse response = profileService.createProfile(request);
         return response;
    }
}
