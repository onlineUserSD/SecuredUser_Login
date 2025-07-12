package com.saad.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "tbl_users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(unique = true)
    private String userId;
    private String username;
    @Column(unique = true)
    private String email;
    private String password;
    private String verifyOtp;
    private long verifyOtpExpiredAt;
    private Boolean isAccountVerified;
    private String resetOtp;
    private long resetOtpExpiredAt;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;
    @UpdateTimestamp
    private  Timestamp updatedAt;

//    public UserEntity() {
//    }
//
//    public UserEntity(Timestamp createdAt, String email, long id, Boolean isAccountVerified, String password, String resetOtp, long resetOtpExpiredAt, Timestamp updatedAt, String userId, String username, String verifyOtp) {
//        this.createdAt = createdAt;
//        this.email = email;
//        this.id = id;
//        this.isAccountVerified = isAccountVerified;
//        this.password = password;
//        this.resetOtp = resetOtp;
//        this.resetOtpExpiredAt = resetOtpExpiredAt;
//        this.updatedAt = updatedAt;
//        this.userId = userId;
//        this.username = username;
//        this.verifyOtp = verifyOtp;
//    }
//
//    public Timestamp getCreatedAt() {
//        return createdAt;
//    }
//
//    public void setCreatedAt(Timestamp createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public long getId() {
//        return id;
//    }
//
//    public void setId(long id) {
//        this.id = id;
//    }
//
//    public Boolean getAccountVerified() {
//        return isAccountVerified;
//    }
//
//    public void setAccountVerified(Boolean accountVerified) {
//        isAccountVerified = accountVerified;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public String getResetOtp() {
//        return resetOtp;
//    }
//
//    public void setResetOtp(String resetOtp) {
//        this.resetOtp = resetOtp;
//    }
//
//    public long getResetOtpExpiredAt() {
//        return resetOtpExpiredAt;
//    }
//
//    public void setResetOtpExpiredAt(long resetOtpExpiredAt) {
//        this.resetOtpExpiredAt = resetOtpExpiredAt;
//    }
//
//    public Timestamp getUpdatedAt() {
//        return updatedAt;
//    }
//
//    public void setUpdatedAt(Timestamp updatedAt) {
//        this.updatedAt = updatedAt;
//    }
//
//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getVerifyOtp() {
//        return verifyOtp;
//    }
//
//    public void setVerifyOtp(String verifyOtp) {
//        this.verifyOtp = verifyOtp;
//    }
//
//    @Override
//    public String toString() {
//        return "UserEntity{" +
//                "createdAt=" + createdAt +
//                ", id=" + id +
//                ", userId='" + userId + '\'' +
//                ", username='" + username + '\'' +
//                ", email='" + email + '\'' +
//                ", password='" + password + '\'' +
//                ", verifyOtp='" + verifyOtp + '\'' +
//                ", isAccountVerified=" + isAccountVerified +
//                ", resetOtp='" + resetOtp + '\'' +
//                ", resetOtpExpiredAt=" + resetOtpExpiredAt +
//                ", updatedAt=" + updatedAt +
//                '}';
//    }
}
