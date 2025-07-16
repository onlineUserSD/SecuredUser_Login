package com.saad.service;

import com.saad.io.ProfileRequest;
import com.saad.io.ProfileResponse;

public interface ProfileService {
   ProfileResponse createProfile(ProfileRequest request);
   ProfileResponse getProfile(String email);
}
