package com.insuranceApp.repository;

import com.insuranceApp.models.Insurance;
import com.insuranceApp.models.InsuredPerson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InsuranceRepository extends JpaRepository<Insurance, Long> {

    Page<Insurance> findAllByInsuredPersonId(long insuredPersonId, Pageable pageable);
    Page<Insurance> findAllByInsuredPersonCreatedById(long userId, Pageable pageable);
}
