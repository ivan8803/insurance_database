package com.insuranceApp.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Insurance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private InsuranceType name;
    private BigDecimal amount;
    private String subject;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date validFrom;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date validUntil;
    @ManyToOne
    @JoinColumn(name = "ip_id", referencedColumnName = "id")
    private InsuredPerson insuredPerson;
}
