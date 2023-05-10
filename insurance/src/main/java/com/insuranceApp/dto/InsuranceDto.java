package com.insuranceApp.dto;

import com.insuranceApp.models.InsuranceType;
import com.insuranceApp.models.InsuredPerson;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
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
public class InsuranceDto {

    private Long id;
    @NotNull(message = "Vyberte pojištění")
    private InsuranceType name;
    @NotNull(message = "Vyplňte částku")
    private BigDecimal amount;
    @NotEmpty(message = "Vyplňte předmět")
    private String subject;
    @NotNull(message = "Vyplňte platnost od")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date validFrom;
    @NotNull(message = "Vyplňte platnost do")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date validUntil;
    private InsuredPerson insuredPerson;
}
