package com.saad.service;

import com.saad.entity.UserEntity;
import com.saad.io.ProfileRequest;
import com.saad.io.ProfileResponse;
import com.saad.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RequiredArgsConstructor
@Builder
@Service
public class ProfileServiceImpl implements ProfileService{
    @Autowired
    private final UserRepository userRepo;


    @Override
    public ProfileResponse createProfile(ProfileRequest request) {
        UserEntity userEntity = convertToUserEntity(request);
        if(!userRepo.existsByEmail(request.getEmail())){
        userEntity = userRepo.save(userEntity);
        return convertToProfileResponse(userEntity);
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT,"Email ALready Exist");

    }

    private ProfileResponse convertToProfileResponse(UserEntity userEntity) {
       return ProfileResponse.builder()
               .name(userEntity.getUsername())
               .email(userEntity.getEmail())
               .userId(userEntity.getUserId())
               .isAccountVerified(userEntity.getIsAccountVerified())
               .build();
    }

    private UserEntity convertToUserEntity(ProfileRequest request) {
        return UserEntity.builder()
                .email(request.getEmail())
                .userId(UUID.randomUUID().toString())
                .username(request.getName())
                .password(request.getPassword())
                .isAccountVerified(false)
                .resetOtpExpiredAt(0L)
                .verifyOtp(null)
                .verifyOtpExpiredAt(0L)
                .resetOtp(null)
                .build();
    }
}
