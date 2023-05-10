package com.insuranceApp.services.impl;

import com.insuranceApp.dto.RegistrationDto;
import com.insuranceApp.models.Role;
import com.insuranceApp.models.UserEntity;
import com.insuranceApp.repository.RoleRepository;
import com.insuranceApp.repository.UserRepository;
import com.insuranceApp.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(RegistrationDto registrationDto) {
        Role role = roleRepository.findByName(registrationDto.getRole());
        UserEntity user = UserEntity.builder()
                .username(registrationDto.getUsername())
                .email(registrationDto.getEmail())
                .password(passwordEncoder.encode(registrationDto.getPassword()))
                .roles(Collections.singletonList(role))
                .build();
        userRepository.save(user);
    }

    @Override
    public UserEntity getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserEntity getByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
