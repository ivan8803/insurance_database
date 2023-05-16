package com.insuranceApp.services;

import com.insuranceApp.dto.InsuranceDto;
import com.insuranceApp.dto.InsuranceResponseDto;
import com.insuranceApp.models.*;
import com.insuranceApp.repository.InsuranceRepository;
import com.insuranceApp.repository.InsuredPersonRepository;
import com.insuranceApp.services.impl.InsuranceServiceImpl;
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

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
public class InsuranceServiceTests {

    @Mock
    private InsuranceRepository insuranceRepository;
    @Mock
    private InsuredPersonRepository insuredPersonRepository;
    @InjectMocks
    private InsuranceServiceImpl insuranceService;

    private Role role_admin;
    private Role role_user;
    private UserEntity user_1;
    private UserEntity user_2;
    private Insurance insurance_1;
    private InsuranceDto insuranceDto_1;
    private InsuredPerson insuredPerson_1;
    private InsuredPerson insuredPerson_2;

    @BeforeEach
    public void _init_() {

        role_admin = Role.builder().name("ADMIN").build();
        role_user = Role.builder().name("USER").build();

        user_1 = UserEntity.builder().username("Arthur").email("nikam.se.nehnu@proc_rucnik.com").password("42").roles(Collections.singletonList(role_user)).build();
        user_2 = UserEntity.builder().username("Ford").email("nepropadejte_panice@intergalacticSpaceAgency.isa").password("42").roles(Collections.singletonList(role_admin)).build();

        insuredPerson_1 = InsuredPerson.builder().firstname("Arthur").lastname("Dent").address(new Address()).email("nikam.se.nehnu@proc_rucnik.com").phone("12101979").insurances(Collections.emptyList()).createdBy(user_1).build();
        insuredPerson_2 = InsuredPerson.builder().firstname("Ford").lastname("Prefect").address(new Address()).email("nepropadejte_panice@intergalacticSpaceAgency.isa").phone("00000042").insurances(Collections.emptyList()).createdBy(user_2).build();

        Calendar validFrom = Calendar.getInstance();
        Calendar validUntil = Calendar.getInstance();
        validUntil.add(Calendar.YEAR, 1);
        insurance_1 = Insurance.builder().name(InsuranceType.POJISTENI_MAJETKU).amount(BigDecimal.valueOf(3000000)).subject("Rodinný dům").validFrom(validFrom.getTime()).validUntil(validUntil.getTime()).insuredPerson(insuredPerson_1).build();

        insuranceDto_1 = InsuranceDto.builder().name(InsuranceType.POJISTENI_MAJETKU).amount(BigDecimal.valueOf(3000000)).subject("Rodinný dům").validFrom(validFrom.getTime()).validUntil(validUntil.getTime()).insuredPerson(insuredPerson_1).build();
    }

    @Test
    public void InsuranceService_GetAllInsurance_ReturnsResponseDto() {
        int pageNo = 0;
        int pageSize = 5;
        Page<Insurance> insurances = Mockito.mock(Page.class);

        when(insuranceRepository.findAll(Mockito.any(Pageable.class))).thenReturn(insurances);

        InsuranceResponseDto foundInsurances = insuranceService.getAllInsurances(pageNo, pageSize);

        Assertions.assertThat(foundInsurances).isNotNull();
    }

    @Test
    public void InsuranceService_GetAllInsurancesByInsuredPersonId_ReturnsResponseDto() {
        int pageNo = 0;
        int pageSize = 5;
        long insuredPersonId = 1L;
        Page<Insurance> insurances = Mockito.mock(Page.class);

        when(insuranceRepository.findAllByInsuredPersonId(Mockito.eq(insuredPersonId), Mockito.any(Pageable.class))).thenReturn(insurances);

        InsuranceResponseDto foundInsurances = insuranceService.getAllInsurancesByInsuredPersonId(insuredPersonId, pageNo, pageSize);

        Assertions.assertThat(foundInsurances).isNotNull();
    }

    @Test
    public void InsuranceService_GetAllInsurancesCreatedBy_ReturnsResponseDto() {
        int pageNo = 0;
        int pageSize = 5;
        long userId = 1L;

        Page<Insurance> insurances = Mockito.mock(Page.class);

        when(insuranceRepository.findAllByInsuredPersonCreatedById(Mockito.eq(userId), Mockito.any(Pageable.class))).thenReturn(insurances);

        InsuranceResponseDto foundInsurances = insuranceService.getAllInsurancesCreatedBy(userId, pageNo, pageSize);

        Assertions.assertThat(foundInsurances).isNotNull();
    }

    @Test
    public void InsuranceService_CreateInsurance_ReturnsInsuranceDto() {

        long insuredPersonId = 1L;


        when(insuredPersonRepository.findById(insuredPersonId)).thenReturn(Optional.of(insuredPerson_1));
        when(insuranceRepository.save(Mockito.any(Insurance.class))).thenReturn(insurance_1);

        InsuranceDto createdInsurance = insuranceService.createInsurance(insuredPersonId, insuranceDto_1);

        Assertions.assertThat(createdInsurance).isNotNull();
    }

    @Test
    public void InsuranceService_GetInsuranceById_ReturnsInsuranceDto() {
        long insuranceId = 1L;

        when(insuranceRepository.findById(insuranceId)).thenReturn(Optional.of(insurance_1));

        InsuranceDto foundInsurance = insuranceService.getInsuranceById(insuranceId);

        Assertions.assertThat(foundInsurance).isNotNull();
    }

    @Test
    public void InsuranceService_editInsuranceById_ReturnsInsuranceDto() {
        long insuranceId = 1L;
        long insuredPersonId = 1L;

        when(insuranceRepository.findById(insuranceDto_1.getId())).thenReturn(Optional.of(insurance_1));
        when(insuredPersonRepository.findById(insuredPersonId)).thenReturn(Optional.of(insuredPerson_1));
        when(insuranceRepository.save(Mockito.any(Insurance.class))).thenReturn(insurance_1);

        InsuranceDto editedInsurance = insuranceService.editInsuranceById(insuranceDto_1, insuredPersonId);

        Assertions.assertThat(editedInsurance).isNotNull();
    }

    @Test
    public void InsuranceService_deleteInsuranceById_ReturnsVoid() {
        long insuranceId = 1L;

        when(insuranceRepository.findById(insuranceId)).thenReturn(Optional.of(insurance_1));

        assertAll(() -> insuranceService.deleteInsuranceById(insuranceId));
    }
}
