package com.insuranceApp.mappers;

import com.insuranceApp.dto.InsuranceDto;
import com.insuranceApp.dto.InsuranceResponseDto;
import com.insuranceApp.models.Insurance;
import org.springframework.data.domain.Page;

import java.util.List;

public final class InsuranceMapper {

    public static InsuranceDto mapToDto(Insurance insurance) {
        return InsuranceDto.builder()
                .id(insurance.getId())
                .name(insurance.getName())
                .amount(insurance.getAmount())
                .subject(insurance.getSubject())
                .validFrom(insurance.getValidFrom())
                .validUntil(insurance.getValidUntil())
                .insuredPerson(insurance.getInsuredPerson())
                .build();
    }

    public static InsuranceResponseDto mapToResponseDto(Page<Insurance> insurancePage, List<InsuranceDto> content) {
        return InsuranceResponseDto.builder()
                .content(content)
                .pageNo(insurancePage.getNumber())
                .pageSize(insurancePage.getSize())
                .totalElements(insurancePage.getTotalElements())
                .totalPages(insurancePage.getTotalPages())
                .last(insurancePage.isLast())
                .build();
    }

    public static Insurance mapToModel(InsuranceDto insuranceDto) {
        return Insurance.builder()
                .id(insuranceDto.getId())
                .name(insuranceDto.getName())
                .amount(insuranceDto.getAmount())
                .subject(insuranceDto.getSubject())
                .validFrom(insuranceDto.getValidFrom())
                .validUntil(insuranceDto.getValidUntil())
                .insuredPerson(insuranceDto.getInsuredPerson())
                .build();
    }
}
