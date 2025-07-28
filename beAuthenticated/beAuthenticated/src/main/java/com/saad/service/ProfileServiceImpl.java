package com.saad.service;

import com.saad.entity.UserEntity;
import com.saad.io.ProfileRequest;
import com.saad.io.ProfileResponse;
import com.saad.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Builder
@Service
public class ProfileServiceImpl implements ProfileService{
    @Autowired
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;


    @Override
    public ProfileResponse createProfile(ProfileRequest request) {
        UserEntity userEntity = convertToUserEntity(request);
        if(!userRepo.existsByEmail(request.getEmail())){
        userEntity = userRepo.save(userEntity);
        return convertToProfileResponse(userEntity);
        }
        throw new ResponseStatusException(HttpStatus.CONFLICT,"Email ALready Exist");

    }

    @Override
    public ProfileResponse getProfile(String email) {
        UserEntity existingUser = userRepo.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"+email));
        return convertToProfileResponse(existingUser);
    }

    @Override
    public void sendResetOtp(String email) {
        UserEntity existingUser= userRepo.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found "+email));

        String otp =  String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));

       long expiryTime = System.currentTimeMillis() + (15 * 60 * 1000);

       existingUser.setResetOtp(otp);
       existingUser.setResetOtpExpiredAt(expiryTime);

       userRepo.save(existingUser);
       try{
           emailService.sendResetOtpEmail(existingUser.getEmail(),otp);
       } catch(Exception ex){
           throw new RuntimeException("Unable to send email");
       }
    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {
       UserEntity existingUser =  userRepo.findByEmail(email)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"+email));
        if(existingUser.getResetOtp() == null || !existingUser.getResetOtp().equals(otp)){
            throw new RuntimeException("Invalid OTP");
        }
        if(existingUser.getResetOtpExpiredAt()<System.currentTimeMillis()){
            throw new RuntimeException("OTP Expired");
        }
        existingUser.setPassword(passwordEncoder.encode(newPassword));
        existingUser.setResetOtp(null);
        existingUser.setResetOtpExpiredAt(0L);

        userRepo.save(existingUser);
    }

    @Override
    public void sendOtp(String email) {
       UserEntity existingUser =  userRepo.findByEmail(email)
               .orElseThrow(()-> new UsernameNotFoundException("User not found: "+email));
       if(existingUser.getIsAccountVerified() != null && existingUser.getIsAccountVerified()){
           return;
       }

        String otp =  String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));

        long expiryTime = System.currentTimeMillis() + (24 * 60 * 60 * 1000);

        existingUser.setVerifyOtp(otp);
        existingUser.setVerifyOtpExpiredAt(expiryTime);

        userRepo.save(existingUser);

        try{
            emailService.sendOtpEmail(existingUser.getEmail(),otp);
        } catch (Exception e){
               throw new RuntimeException("Unable to send email");
        }
    }

    @Override
    public void veriftOtp(String email, String otp) {
         UserEntity existingUser =  userRepo.findByEmail(email)
                 .orElseThrow(()-> new UsernameNotFoundException("User not found: "+email));
         if(existingUser.getVerifyOtp() == null || !existingUser.getVerifyOtp().equals(otp)){
             throw new RuntimeException("Invalid OTP");
         }
         if(existingUser.getVerifyOtpExpiredAt() < System.currentTimeMillis()){
             throw new RuntimeException("OTP expired");
         }
         existingUser.setIsAccountVerified(true);
         existingUser.setVerifyOtp(null);
         existingUser.setVerifyOtpExpiredAt(0L);

         userRepo.save(existingUser);
    }

    @Override
    public String getLoggedinUserId(String email) {
        UserEntity existingUser =  userRepo.findByEmail(email)
                .orElseThrow(()->new UsernameNotFoundException("User not found: "+email));
        return existingUser.getUserId();
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
                .password(passwordEncoder.encode(request.getPassword()))
                .isAccountVerified(false)
                .resetOtpExpiredAt(0L)
                .verifyOtp(null)
                .verifyOtpExpiredAt(0L)
                .resetOtp(null)
                .build();
    }
}
