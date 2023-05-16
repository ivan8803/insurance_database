package com.insuranceApp.services;

import com.insuranceApp.dto.InsuredPersonDto;
import com.insuranceApp.dto.InsuredPersonFormDto;
import com.insuranceApp.dto.InsuredPersonResponseDto;
import com.insuranceApp.models.Address;
import com.insuranceApp.models.*;
import com.insuranceApp.repository.InsuranceRepository;
import com.insuranceApp.repository.InsuredPersonRepository;
import com.insuranceApp.repository.UserRepository;
import com.insuranceApp.services.impl.InsuranceServiceImpl;
import com.insuranceApp.services.impl.InsuredPersonServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class InsuredPersonServiceTest {

    @Mock
    private InsuranceRepository insuranceRepository;
    @Mock
    private InsuredPersonRepository insuredPersonRepository;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private InsuranceServiceImpl insuranceService;
    @InjectMocks
    private InsuredPersonServiceImpl insuredPersonService;
    private Role role_admin;
    private Role role_user;
    private UserEntity user_1;
    private Insurance insurance_1;
    private InsuredPerson insuredPerson_1;
    private InsuredPersonFormDto insuredPersonFormDto;

    @BeforeEach
    public void _init_() {

        role_admin = Role.builder().name("ADMIN").build();
        role_user = Role.builder().name("USER").build();

        user_1 = UserEntity.builder().username("Arthur").email("nikam.se.nehnu@proc_rucnik.com").password("42").roles(Collections.singletonList(role_user)).build();

        insuredPerson_1 = InsuredPerson.builder().firstname("Arthur").lastname("Dent").address(new Address()).email("nikam.se.nehnu@proc_rucnik.com").phone("12101979").insurances(Collections.emptyList()).createdBy(user_1).build();

        insuredPersonFormDto = InsuredPersonFormDto.builder()
                .firstname("Arthur").lastname("Dent")
                .street("Country Lane 155").city("Cottingham").postcode("HU16 4QD")
                .email("nikam.se.nehnu@proc_rucnik.com").phone("12101979")
                .build();

        Calendar validFrom = Calendar.getInstance();
        Calendar validUntil = Calendar.getInstance();
        validUntil.add(Calendar.YEAR, 1);
        insurance_1 = Insurance.builder().name(InsuranceType.POJISTENI_MAJETKU).amount(BigDecimal.valueOf(3000000)).subject("Rodinný dům").validFrom(validFrom.getTime()).validUntil(validUntil.getTime()).insuredPerson(insuredPerson_1).build();
    }

    @Test
    public void InsuredPersonService_GetAllInsuredPersons_ReturnsResponseDto() {
        int pageNo = 0;
        int pageSize = 5;
        Page<InsuredPerson> insuredPersons = Mockito.mock(Page.class);

        when(insuredPersonRepository.findAll(Mockito.any(Pageable.class))).thenReturn(insuredPersons);

        InsuredPersonResponseDto foundResponse = insuredPersonService.getAllInsuredPersons(pageNo, pageSize);

        Assertions.assertThat(foundResponse).isNotNull();
    }

    @Test
    public void InsuredPersonService_GetAllInsuredPersonsCreatedById_ReturnsResponseDto() {
        int pageNo = 0;
        int pageSize = 5;
        long userId = 1L;
        Page<InsuredPerson> insuredPersons = Mockito.mock(Page.class);

        when(insuredPersonRepository.findAllByCreatedById(Mockito.eq(userId), Mockito.any(Pageable.class))).thenReturn(insuredPersons);

        InsuredPersonResponseDto foundResponse = insuredPersonService.getAllInsuredPersonsCreatedById(userId, pageNo, pageSize);

        Assertions.assertThat(foundResponse).isNotNull();
    }

    @Test
    public void InsuredPersonService_GetInsuredPersonById_ReturnsInsuredPersonDto() {
        long insuredPersonId = 1L;

        when(insuredPersonRepository.findById(insuredPersonId)).thenReturn(Optional.of(insuredPerson_1));

        InsuredPersonDto foundInsuredPerson = insuredPersonService.getInsuredPersonById(insuredPersonId);

        Assertions.assertThat(foundInsuredPerson).isNotNull();
    }

    @Test
    public void InsuredPersonService_GetInsuredPersonByEmail_ReturnsInsuredPersonDto() {
        String email = "nepropadejte_panice@intergalacticSpaceAgency.isa";

        when(insuredPersonRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(insuredPerson_1));

        InsuredPersonDto foundInsuredPerson = insuredPersonService.getInsuredPersonByEmail(email);

        Assertions.assertThat(foundInsuredPerson).isNotNull();
    }

    @Test
    void InsuredPersonService_IsEmail_ReturnsBoolean() {
        String email = "nepropadejte_panice@intergalacticSpaceAgency.isa";

        when(insuredPersonRepository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(insuredPerson_1));

        Boolean response = insuredPersonService.isEmail(email);

        Assertions.assertThat(response).isTrue();
    }


    @Test
    public void InsuredPersonService_CreateInsuredPerson_ReturnsInsuredPersonDto() {

        Authentication authentication = new UsernamePasswordAuthenticationToken("username", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(user_1);
        when(insuredPersonRepository.save(Mockito.any(InsuredPerson.class))).thenReturn(insuredPerson_1);

        InsuredPersonDto createdInsuredPerson = insuredPersonService.createInsuredPerson(insuredPersonFormDto);

        SecurityContextHolder.clearContext();

        Assertions.assertThat(createdInsuredPerson).isNotNull();
    }

    @Test
    public void InsuredPersonService_EditInsuredPerson_ReturnsInsuredPersonDto() {
        long insuredPersonId = 1L;

        Authentication authentication = new UsernamePasswordAuthenticationToken("username", "password");
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(insuredPersonRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(insuredPerson_1));
        when(userRepository.findByUsername(Mockito.anyString())).thenReturn(user_1);
        when(insuredPersonRepository.save(Mockito.any(InsuredPerson.class))).thenReturn(insuredPerson_1);

        InsuredPersonDto editedInsuredPerson = insuredPersonService.editInsuredPersonById(insuredPersonFormDto, insuredPersonId);

        SecurityContextHolder.clearContext();

        Assertions.assertThat(editedInsuredPerson).isNotNull();
    }

    @Test
    public void InsuredPersonService_deleteInsuredPersonById_ReturnsVoid() {

        when(insuranceRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(insurance_1));

        assertAll(() -> insuranceService.deleteInsuranceById(1L));
    }
}
