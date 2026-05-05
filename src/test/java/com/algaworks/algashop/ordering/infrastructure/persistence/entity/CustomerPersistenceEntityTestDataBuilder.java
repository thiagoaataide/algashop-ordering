package com.algaworks.algashop.ordering.infrastructure.persistence.entity;

import com.algaworks.algashop.ordering.infrastructure.persistence.embeddable.AddressEmbeddable;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static com.algaworks.algashop.ordering.domain.model.entity.CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;

public class CustomerPersistenceEntityTestDataBuilder {

    private CustomerPersistenceEntityTestDataBuilder() {}

    public static CustomerPersistenceEntity.CustomerPersistenceEntityBuilder aCustomer() {
        return CustomerPersistenceEntity.builder()
                .id(DEFAULT_CUSTOMER_ID.value())
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1990, 1, 1))
                .email("john.doe@gmail.com")
                .phone("1234567890")
                .document("1234567890123456")
                .archived(false)
                .archivedAt(null)
                .promotionNotificationsAllowed(true)
                .loyaltyPoints(0)
                .registeredAt(OffsetDateTime.now())
                .address(AddressEmbeddable.builder()
                        .street("Bourbon Street")
                        .number("123")
                        .neighborhood("North Ville")
                        .city("York")
                        .state("South California")
                        .zipCode("12345")
                        .complement("Apartment 123")
                        .build());
    }
}
