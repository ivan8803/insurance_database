package com.insuranceApp.repository;

import com.insuranceApp.models.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;


import java.math.BigDecimal;
import java.util.*;


@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class InsuranceRepositoryTests {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private InsuredPersonRepository insuredPersonRepository;
    @Autowired
    private InsuranceRepository insuranceRepository;
    @Autowired
    private ApplicationContext applicationContext;

    private Role role_admin;
    private Role role_user;
    private UserEntity user_1;
    private UserEntity user_2;
    private Insurance insurance_1;
    private Insurance insurance_2;
    private Insurance insurance_3;
    private InsuredPerson insuredPerson_1;
    private InsuredPerson insuredPerson_2;
    private InsuredPerson insuredPerson_3;
    private Pageable pageable;

    @BeforeEach
    public void _init_() {

        role_admin = Role.builder().name("ADMIN").build();
        role_user = Role.builder().name("USER").build();

        user_1 = UserEntity.builder().username("Arthur").email("nikam.se.nehnu@proc_rucnik.com").password("42").roles(Collections.singletonList(role_user)).build();
        user_2 = UserEntity.builder().username("Ford").email("nepropadejte_panice@intergalacticSpaceAgency.isa").password("42").roles(Collections.singletonList(role_admin)).build();

        insuredPerson_1 = InsuredPerson.builder().firstname("Arthur").lastname("Dent").address(new Address()).email("nikam.se.nehnu@proc_rucnik.com").phone("12101979").insurances(Collections.emptyList()).createdBy(user_1).build();
        insuredPerson_2 = InsuredPerson.builder().firstname("Ford").lastname("Prefect").address(new Address()).email("nepropadejte_panice@intergalacticSpaceAgency.isa").phone("00000042").insurances(Collections.emptyList()).createdBy(user_2).build();
        insuredPerson_3 = InsuredPerson.builder().firstname("Marvin").lastname("Depresivní robot").address(new Address()).email("nema.to.smysl@vzdej_to.isa").phone("00000000").insurances(Collections.emptyList()).createdBy(user_2).build();

        Calendar validFrom = Calendar.getInstance();
        Calendar validUntil = Calendar.getInstance();
        validUntil.add(Calendar.YEAR, 1);
        insurance_1 = Insurance.builder().name(InsuranceType.POJISTENI_MAJETKU).amount(BigDecimal.valueOf(3000000)).subject("Rodinný dům").validFrom(validFrom.getTime()).validUntil(validUntil.getTime()).insuredPerson(insuredPerson_1).build();
        insurance_2 = Insurance.builder().name(InsuranceType.CESTOVNI_POJISTENI).amount(BigDecimal.valueOf(300000000)).subject("Ford Prefect").validFrom(validFrom.getTime()).validUntil(validUntil.getTime()).insuredPerson(insuredPerson_2).build();
        insurance_3 = Insurance.builder().name(InsuranceType.ZIVOTNI_POJISTENI).amount(BigDecimal.valueOf(15000000)).subject("Marvin - Depresivní robot").validFrom(validFrom.getTime()).validUntil(validUntil.getTime()).insuredPerson(insuredPerson_2).build();

        int pageNo = 0;
        int pageSize = 5;
        pageable = PageRequest.of(pageNo, pageSize);

        roleRepository.save(role_admin);
        roleRepository.save(role_user);
        userRepository.save(user_1);
        userRepository.save(user_2);
        insuredPersonRepository.save(insuredPerson_1);
        insuredPersonRepository.save(insuredPerson_2);
        insuranceRepository.save(insurance_1);
        insuranceRepository.save(insurance_2);
    }

    @Test
    public void InsuranceRepository_FindAllByInsuredPersonId_ReturnInsuranceResponseDto() {

        long id = 1L;

        Page foundPage = insuranceRepository.findAllByInsuredPersonId(id, pageable);
        InsuredPerson foundInsuredPerson = insuredPersonRepository.findById(id).get();
        List<Insurance> insuranceList = foundPage.getContent();

        Assertions.assertThat(foundPage).isNotNull();
        Assertions.assertThat(foundPage.getContent().size()).isGreaterThan(0);
        Assertions.assertThat(foundPage.getContent().get(0)).isInstanceOf(Insurance.class);

        for (Insurance i : insuranceList) {
            Assertions.assertThat(i.getInsuredPerson()).isEqualTo(foundInsuredPerson);
        }
    }

    @Test
    public void InsuranceRepository_FindAllByInsuredPersonCreatedById_ReturnsPageOfInsurances() {

        long id = 2L;

        Page foundPage = insuranceRepository.findAllByInsuredPersonCreatedById(id, pageable);
        UserEntity foundUser = userRepository.findById(id).get();
        List<Insurance> insuranceList = foundPage.getContent();

        Assertions.assertThat(foundPage).isNotNull();
        Assertions.assertThat(foundPage.getContent().size()).isGreaterThan(0);
        Assertions.assertThat(foundPage.getContent().get(0)).isInstanceOf(Insurance.class);

        for (Insurance i : insuranceList) {
            Assertions.assertThat(i.getInsuredPerson().getCreatedBy()).isEqualTo(foundUser);
        }
    }

}
