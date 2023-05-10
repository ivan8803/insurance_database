package com.insuranceApp.mappers;

import com.insuranceApp.dto.InsuredPersonDto;
import com.insuranceApp.dto.InsuredPersonFormDto;
import com.insuranceApp.dto.InsuredPersonResponseDto;
import com.insuranceApp.models.InsuredPerson;
import org.springframework.data.domain.Page;

import java.util.List;

public final class InsuredPersonMapper {

    public static InsuredPerson mapToModel(InsuredPersonDto insuredPersonDto) {
        return InsuredPerson.builder()
                .id(insuredPersonDto.getId())
                .firstname(insuredPersonDto.getFirstname())
                .lastname(insuredPersonDto.getLastname())
                .address(insuredPersonDto.getAddress())
                .email(insuredPersonDto.getEmail())
                .phone(insuredPersonDto.getPhone())
                .createdBy(insuredPersonDto.getCreatedBy())
                .build();
    }

    public static InsuredPersonDto maptoDto(InsuredPerson insuredPerson) {
        return InsuredPersonDto.builder()
                .id(insuredPerson.getId())
                .firstname(insuredPerson.getFirstname())
                .lastname(insuredPerson.getLastname())
                .address(insuredPerson.getAddress())
                .email(insuredPerson.getEmail())
                .phone(insuredPerson.getPhone())
                .createdBy(insuredPerson.getCreatedBy())
                .build();
    }

    public static InsuredPersonResponseDto mapToResponseDto(Page<InsuredPerson> insuredPersonsPage, List<InsuredPersonDto> content) {
        return InsuredPersonResponseDto.builder()
                .content(content)
                .pageNo(insuredPersonsPage.getNumber())
                .pageSize(insuredPersonsPage.getSize())
                .totalElements(insuredPersonsPage.getTotalElements())
                .totalPages(insuredPersonsPage.getTotalPages())
                .last(insuredPersonsPage.isLast())
                .build();
    }

    public static InsuredPersonFormDto mapToInsuredPersonFormDto(InsuredPersonDto insuredPersonDto) {
        return InsuredPersonFormDto.builder()
                .id(insuredPersonDto.getId())
                .firstname(insuredPersonDto.getFirstname())
                .lastname(insuredPersonDto.getLastname())
                .street(insuredPersonDto.getAddress().getStreet())
                .city(insuredPersonDto.getAddress().getCity())
                .postcode(insuredPersonDto.getAddress().getPostcode())
                .email(insuredPersonDto.getEmail())
                .phone(insuredPersonDto.getPhone())
                .build();
    }
}
