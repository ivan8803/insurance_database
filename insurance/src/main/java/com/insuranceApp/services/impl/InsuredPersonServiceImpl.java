package com.insuranceApp.services.impl;

import com.insuranceApp.dto.InsuredPersonDto;
import com.insuranceApp.dto.InsuredPersonFormDto;
import com.insuranceApp.dto.InsuredPersonResponseDto;
import com.insuranceApp.exceptions.InsuredPersonNotFoundException;
import com.insuranceApp.mappers.InsuredPersonMapper;
import com.insuranceApp.models.Address;
import com.insuranceApp.models.InsuredPerson;
import com.insuranceApp.models.UserEntity;
import com.insuranceApp.repository.InsuredPersonRepository;
import com.insuranceApp.repository.UserRepository;
import com.insuranceApp.security.SecurityUtil;
import com.insuranceApp.services.InsuredPersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InsuredPersonServiceImpl implements InsuredPersonService {

    private final InsuredPersonRepository insuredPersonRepository;
    private final UserRepository userRepository;

    @Override
    public InsuredPersonResponseDto getAllInsuredPersons(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("id"));
        Page<InsuredPerson> page = insuredPersonRepository.findAll(pageable);
        List<InsuredPerson> insuredPersonList = page.getContent();
        List<InsuredPersonDto> content = insuredPersonList.stream().map(InsuredPersonMapper::maptoDto).collect(Collectors.toList());
        return InsuredPersonMapper.mapToResponseDto(page, content);
    }

    @Override
    public InsuredPersonResponseDto getAllInsuredPersonsCreatedById(long userId, int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("id"));
        Page<InsuredPerson> page = insuredPersonRepository.findAllByCreatedById(userId, pageable);
        List<InsuredPerson> insuredPersonList = page.getContent();
        List<InsuredPersonDto> content = insuredPersonList.stream().map(InsuredPersonMapper::maptoDto).collect(Collectors.toList());
        return InsuredPersonMapper.mapToResponseDto(page, content);
    }

    @Override
    public InsuredPersonDto getInsuredPersonById(long insuredPersonId) {
        return InsuredPersonMapper.maptoDto(insuredPersonRepository.findById(insuredPersonId).orElseThrow(() -> new InsuredPersonNotFoundException("Uživatel nebyl nalezen")));
    }

    @Override
    public InsuredPersonDto getInsuredPersonByEmail(String email) {
        InsuredPerson insuredPerson = insuredPersonRepository.findByEmail(email).orElseThrow(() -> new InsuredPersonNotFoundException("Uživatel nebyl nalezen"));
        return InsuredPersonMapper.maptoDto(insuredPerson);
    }

    @Override
    public boolean isEmail(String email) {
        Optional<InsuredPerson> insuredPerson = insuredPersonRepository.findByEmail(email);
        if (insuredPerson.isEmpty()) {
            return false;
        }
        return true;
    }

    public InsuredPersonDto createInsuredPerson(InsuredPersonFormDto insuredPersonFormDto) {

        String username = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findByUsername(username);

        Address address = Address.builder()
                .street(insuredPersonFormDto.getStreet())
                .city(insuredPersonFormDto.getCity())
                .postcode(insuredPersonFormDto.getPostcode())
                .build();

        InsuredPerson insuredPerson = InsuredPerson.builder()
                .firstname(insuredPersonFormDto.getFirstname())
                .lastname(insuredPersonFormDto.getLastname())
                .address(address)
                .email(insuredPersonFormDto.getEmail())
                .phone(insuredPersonFormDto.getPhone())
                .createdBy(user)
                .build();

        InsuredPerson createdInsuredPerson = insuredPersonRepository.save(insuredPerson);
        return InsuredPersonMapper.maptoDto(createdInsuredPerson);
    }

    @Override
    public InsuredPersonDto editInsuredPersonById(InsuredPersonFormDto insuredPersonFormDto, long insuredPersonId) {
        InsuredPerson insuredPerson = insuredPersonRepository.findById(insuredPersonId).orElseThrow(() -> new InsuredPersonNotFoundException("Uživatel nebyl nalezen"));

        String username = SecurityUtil.getSessionUser();
        UserEntity user = userRepository.findByUsername(username);

        Address address = insuredPerson.getAddress();
        address.setStreet(insuredPersonFormDto.getStreet());
        address.setCity(insuredPersonFormDto.getCity());
        address.setPostcode(insuredPersonFormDto.getPostcode());

        insuredPerson.setFirstname(insuredPersonFormDto.getFirstname());
        insuredPerson.setLastname(insuredPersonFormDto.getLastname());
        insuredPerson.setAddress(address);
        insuredPerson.setEmail(insuredPersonFormDto.getEmail());
        insuredPerson.setPhone(insuredPersonFormDto.getPhone());
        insuredPerson.setCreatedBy(user);

        InsuredPerson editedInsuredPerson = insuredPersonRepository.save(insuredPerson);

        return InsuredPersonMapper.maptoDto(editedInsuredPerson);
    }

    public void deleteInsuredPersonById(long insuredPersonId) {
        insuredPersonRepository.findById(insuredPersonId).orElseThrow(() -> new InsuredPersonNotFoundException("Uživatel nebyl nalezen"));

        insuredPersonRepository.deleteById(insuredPersonId);
    }
}
