package com.insuranceApp.services;

import com.insuranceApp.dto.*;

public interface InsuranceService {

    InsuranceResponseDto getAllInsurances(int pageNo, int pageSize);
    InsuranceResponseDto getAllInsurancesByInsuredPersonId(long insuredPersonId, int pageNo, int pageSize);
    InsuranceResponseDto getAllInsurancesCreatedBy(long userId, int pageNo, int pageSize);
    InsuranceDto createInsurance(long insuredPersonId, InsuranceDto insuranceDto);
    InsuranceDto getInsuranceById(long insuredPersonId);
    InsuranceDto editInsuranceById(InsuranceDto insuranceDto, long insuredPersonId);
    void deleteInsuranceById(long id);
}
