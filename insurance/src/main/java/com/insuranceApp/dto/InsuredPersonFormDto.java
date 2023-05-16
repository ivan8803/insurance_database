package com.insuranceApp.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InsuredPersonFormDto {
    private Long id;
    @NotEmpty(message = "Vyplňte jméno")
    private String firstname;
    @NotEmpty(message = "Vyplňte příjmení")
    private String lastname;
    @NotEmpty(message = "Vyplňte ulici")
    private String street;
    @NotEmpty(message = "Vyplňte město")
    private String city;
    @NotEmpty(message = "Vyplňte poštovní směrovací číslo")
    private String postcode;
    @NotEmpty(message = "vyplňte email")
    @Email(message = "Email musí obsahovat znak @")
    private String email;
    @NotEmpty(message = "Vyplňte telefon")
    private String phone;
}
