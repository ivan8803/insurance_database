package com.insuranceApp.services;

import com.insuranceApp.models.*;
import com.insuranceApp.repository.RoleRepository;
import com.insuranceApp.services.impl.RoleServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTests {

    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private RoleServiceImpl roleService;
    private Role role_admin;

    @BeforeEach
    public void _init_() {

        role_admin = Role.builder().name("ADMIN").build();
    }

    @Test
    public void RoleServiceTests_GetRole_ReturnRole() {
        when(roleRepository.findByName(Mockito.anyString())).thenReturn(role_admin);

        Role foundRole = roleService.getRole("ADMIN");

        Assertions.assertThat(foundRole).isNotNull();
    }
}
