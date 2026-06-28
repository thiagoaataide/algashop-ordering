package com.algaworks.algashop.ordering.aplication.service.customer.management;

import com.algaworks.algashop.ordering.aplication.commons.AddressData;
import com.algaworks.algashop.ordering.aplication.customer.management.CustomerUpdateInput;
import com.algaworks.algashop.ordering.domain.model.commons.ZipCode;

public class CustomerUpdateInputTestDataBuilder {

    public static CustomerUpdateInput.CustomerUpdateInputBuilder aCustomerUpdate() {
        return CustomerUpdateInput
                .builder()
                .firstName("Matt")
                .lastName("Damon")
                .phone("123-321-1112")
                .promotionNotificationsAllowed(true)
                .address(AddressData.builder()
                        .street("Amphitheatre Parkway")
                        .number("1600")
                        .complement("")
                        .neighborhood("Mountain View")
                        .city("Mountain View")
                        .state("California")
                        .zipCode("94043")
                        .build());
    }
}
