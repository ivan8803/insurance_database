package com.insuranceApp.services;

import com.insuranceApp.dto.InsuredPersonDto;
import com.insuranceApp.dto.InsuredPersonFormDto;
import com.insuranceApp.dto.InsuredPersonResponseDto;

public interface InsuredPersonService {

    InsuredPersonResponseDto getAllInsuredPersons(int pageNo, int pageSize);
    InsuredPersonResponseDto getAllInsuredPersonsCreatedById(long userId, int pageNo, int pageSize);
    InsuredPersonDto getInsuredPersonById(long insuredPersonId);
    InsuredPersonDto getInsuredPersonByEmail(String email);
    boolean isEmail(String email);
    InsuredPersonDto editInsuredPersonById(InsuredPersonFormDto insuredPersonFormDto, long insuredPersonId);
    void deleteInsuredPersonById(long id);
}
