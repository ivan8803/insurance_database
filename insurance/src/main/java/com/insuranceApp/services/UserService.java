package com.insuranceApp.services;

import com.insuranceApp.dto.RegistrationDto;
import com.insuranceApp.models.UserEntity;

public interface UserService {

    void saveUser(RegistrationDto registrationDto);

    UserEntity getByEmail(String email);

    UserEntity getByUsername(String username);
}
