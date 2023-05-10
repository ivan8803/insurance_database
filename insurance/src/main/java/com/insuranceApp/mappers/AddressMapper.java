package com.insuranceApp.mappers;

import com.insuranceApp.dto.AddressDto;
import com.insuranceApp.models.Address;

public final class AddressMapper {

    public static AddressDto mapToDto(Address address) {
        return AddressDto.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .postcode(address.getPostcode())
                .build();
    }
}
