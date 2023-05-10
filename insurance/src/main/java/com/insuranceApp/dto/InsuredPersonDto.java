package com.insuranceApp.dto;

import com.insuranceApp.models.Address;
import com.insuranceApp.models.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsuredPersonDto {

    private Long id;
    @NotEmpty(message = "Vyplňte jméno")
    private String firstname;
    @NotEmpty(message = "Vyplňte příjmení")
    private String lastname;
    @NotEmpty(message = "Vyplňte adresu")
    private Address address;
    @Email(message = "vyplňte email")
    private String email;
    @NotEmpty(message = "Vyplňte telefon")
    private String phone;
    private UserEntity createdBy;
}
