package com.insuranceApp.repository;

import com.insuranceApp.models.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserRepositoryTests {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    private Role role_admin;
    private Role role_user;
    private UserEntity user_1;
    private UserEntity user_2;

    @BeforeEach
    public void _init_() {

        role_admin = Role.builder().name("ADMIN").build();
        role_user = Role.builder().name("USER").build();

        user_1 = UserEntity.builder().username("Arthur").email("nikam.se.nehnu@proc_rucnik.com").password("42").roles(Collections.singletonList(role_user)).build();
        user_2 = UserEntity.builder().username("Ford").email("nepropadejte_panice@intergalacticSpaceAgency.isa").password("42").roles(Collections.singletonList(role_admin)).build();

        roleRepository.save(role_admin);
        roleRepository.save(role_user);
        userRepository.save(user_1);
        userRepository.save(user_2);
    }

    @Test
    public void UserRepository_FindByUsername_ReturnUserEntity() {
        UserEntity foundUser = userRepository.findByUsername("Arthur");

        Assertions.assertThat(foundUser).isNotNull();
        Assertions.assertThat(foundUser.getEmail()).isEqualTo("nikam.se.nehnu@proc_rucnik.com");
    }

    @Test
    public void UserRepository_FindByEmail_ReturnUserEntity() {
        UserEntity foundUser = userRepository.findByEmail("nepropadejte_panice@intergalacticSpaceAgency.isa");

        Assertions.assertThat(foundUser).isNotNull();
        Assertions.assertThat(foundUser.getUsername()).isEqualTo("Ford");
    }
}
