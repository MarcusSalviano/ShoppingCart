package com.pulse.checkout.domain;

public record CustomerAddressDto(
        Long id,
        String addressName,
        String street,
        String number,
        String complement,
        String neighborhood,
        String city,
        String state,
        String zipCode
) {
    public CustomerAddressDto (CustomerAddress address) {
        this(address.getId(), address.getAddressName(), address.getStreet(), address.getNumber(),
                address.getComplement(), address.getNeighborhood(), address.getCity(), address.getState(),
                address.getZipCode());
    }
}
