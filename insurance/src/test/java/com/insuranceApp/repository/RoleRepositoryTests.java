package com.insuranceApp.repository;

import com.insuranceApp.models.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class RoleRepositoryTests {

    @Autowired
    private RoleRepository roleRepository;
    private Role role_admin;
    private Role role_user;

    @BeforeEach
    public void _init_() {

        role_admin = Role.builder().name("ADMIN").build();
        role_user = Role.builder().name("USER").build();

        roleRepository.save(role_admin);
        roleRepository.save(role_user);
    }

    @Test
    public void RoleRepository_FindByName_ReturnRole() {
        Role foundRole = roleRepository.findByName("ADMIN");

        Assertions.assertThat(foundRole).isNotNull();
        Assertions.assertThat(foundRole).isEqualTo(role_admin);
    }
}
