package com.insuranceApp.services.impl;

import com.insuranceApp.dto.InsuranceDto;
import com.insuranceApp.dto.InsuranceResponseDto;
import com.insuranceApp.exceptions.InsuranceNotFoundException;
import com.insuranceApp.exceptions.InsuredPersonNotFoundException;
import com.insuranceApp.mappers.InsuranceMapper;
import com.insuranceApp.models.Insurance;
import com.insuranceApp.models.InsuredPerson;
import com.insuranceApp.repository.InsuranceRepository;
import com.insuranceApp.repository.InsuredPersonRepository;
import com.insuranceApp.services.InsuranceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InsuranceServiceImpl implements InsuranceService {

    private final InsuranceRepository insuranceRepository;
    private final InsuredPersonRepository insuredPersonRepository;

    @Override
    public InsuranceResponseDto getAllInsurances(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("id"));
        Page<Insurance> page = insuranceRepository.findAll(pageable);
        List<Insurance> insuranceList = page.getContent();
        List<InsuranceDto> content = insuranceList.stream().map(InsuranceMapper::mapToDto).collect(Collectors.toList());
        return InsuranceMapper.mapToResponseDto(page, content);
    }

    @Override
    public InsuranceResponseDto getAllInsurancesByInsuredPersonId(long insuredPersonId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Insurance> page = insuranceRepository.findAllByInsuredPersonId(insuredPersonId, pageable);
        List<Insurance> insuranceList = page.getContent();
        List<InsuranceDto> content = insuranceList.stream().map(InsuranceMapper::mapToDto).collect(Collectors.toList());
        return InsuranceMapper.mapToResponseDto(page, content);
    }

    @Override
    public InsuranceResponseDto getAllInsurancesCreatedBy(long userId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Page<Insurance> page = insuranceRepository.findAllByInsuredPersonCreatedById(userId, pageable);
        List<Insurance> insuranceList = page.getContent();
        List<InsuranceDto> content = insuranceList.stream().map(InsuranceMapper::mapToDto).collect(Collectors.toList());
        return InsuranceMapper.mapToResponseDto(page, content);
    }

    @Override
    public InsuranceDto createInsurance(long insuredPersonId, InsuranceDto insuranceDto) {
        InsuredPerson insuredPerson = insuredPersonRepository.findById(insuredPersonId).orElseThrow(() -> new InsuredPersonNotFoundException("Uživatel nenalezen"));
        Insurance insurance = Insurance.builder()
                .name(insuranceDto.getName())
                .subject(insuranceDto.getSubject())
                .amount(insuranceDto.getAmount())
                .validFrom(insuranceDto.getValidFrom())
                .validUntil(insuranceDto.getValidUntil())
                .insuredPerson(insuredPerson)
                .build();
        Insurance createdInsurance = insuranceRepository.save(insurance);
        return InsuranceMapper.mapToDto(createdInsurance);
    }

    @Override
    public InsuranceDto getInsuranceById(long insuranceId) {
        return InsuranceMapper.mapToDto(insuranceRepository.findById(insuranceId).orElseThrow(() -> new InsuranceNotFoundException("Pojištění nebylo nalezeno")));
    }

    @Override
    public InsuranceDto editInsuranceById(InsuranceDto insuranceDto, long insuredPersonId) {
        insuranceRepository.findById(insuranceDto.getId()).orElseThrow(() -> new InsuranceNotFoundException("Pojištění nebylo nalezeno v databázy"));
        InsuredPerson insuredPerson = insuredPersonRepository.findById(insuredPersonId).orElseThrow(() -> new InsuredPersonNotFoundException("Pojištěná osoba nebyla nalezena v databázi"));

        Insurance insurance = Insurance.builder()
                .id(insuranceDto.getId())
                .name(insuranceDto.getName())
                .amount(insuranceDto.getAmount())
                .subject(insuranceDto.getSubject())
                .validFrom(insuranceDto.getValidFrom())
                .validUntil(insuranceDto.getValidUntil())
                .insuredPerson(insuredPerson)
                .build();

        Insurance editedInsurance = insuranceRepository.save(insurance);
        return InsuranceMapper.mapToDto(editedInsurance);
    }

    @Override
    public void deleteInsuranceById(long insuranceId) {
        insuranceRepository.findById(insuranceId).orElseThrow(() -> new InsuranceNotFoundException("Pojštění nebylo nalezeno v databázy"));
        insuranceRepository.deleteById(insuranceId);
    }
}
