package com.insuranceApp.dto;

import com.insuranceApp.models.InsuredPerson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private Long id;
    private String street;
    private String city;
    private String postcode;
    private InsuredPerson insuredPerson;
}
