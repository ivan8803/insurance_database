package com.insuranceApp.repository;

import com.insuranceApp.models.InsuredPerson;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InsuredPersonRepository extends JpaRepository<InsuredPerson, Long> {

    Optional<InsuredPerson> findByEmail(String email);
    Page<InsuredPerson> findAllByCreatedById(Long userId, Pageable pageable);
}
