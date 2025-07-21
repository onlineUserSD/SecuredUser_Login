package com.saad.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class EmailService {
private final JavaMailSender javaMailSender;

@Value("${spring.mail.properties.mail.smtp.from}")
private String fromEmail;

public void sendWelcomeEmail(String name,String toEmail){
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(fromEmail);
    message.setTo(toEmail);
    message.setSubject("WELCOME TO OUT PLATFORM");
    message.setText("Hello "+name+"\n\nThanks for registering with us!\n\n Regards, \nSecuredUser team!");
    javaMailSender.send(message);
}

public void sendResetOtpEmail(String toEmail , String otp){
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(fromEmail);
    message.setTo(toEmail);
    message.setSubject("Password reset OTP");
    message.setText("Your OTP for resetting your password is "+otp+".Don't share this otp with anyone else");
    javaMailSender.send(message);
}
}
