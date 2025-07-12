package com.saad.io;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProfileRequest {
    @NotBlank(message = "Name should not be empty")
    private String name;
    @NotBlank(message = "Email is required")
    @Email(message = "Enter a valid email ")
    private String email;
    @Size(min = 6 , message = "Password atleast have 6 characters")
    private String password;
}
