package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.valueobject.*;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class CustomerTesteDataBuilder {

    public static Customer.BrandNewCustomerBuilder brandNewCustomer(){
        return Customer.brandNew()
                .fullName(new FullName("John", "Doe"))
                .birthDate(new BirthDate(LocalDate.of(1990, 1, 1)))
                .email(new Email("john.doe@gmail.com"))
                .phone(new Phone("1234567890"))
                .document(new Document("1234567890123456"))
                .promotionNotificationsAllowed(true)
                .adress(Adress.builder()
                        .street("Bourbon Street")
                        .number("123")
                        .neighborhood("North Ville")
                        .city("York")
                        .state("South California")
                        .zipCode(new ZipCode("12345"))
                        .complement("Apartment 123")
                        .build());
    }

    public static Customer.ExistingCustomerBuilder existingCustomer(){
        return Customer.existing()
                .id(new CustomerId())
                .fullName(new FullName("John", "Doe"))
                .birthDate(new BirthDate(LocalDate.of(1990, 1, 1)))
                .email(new Email("john.doe@gmail.com"))
                .phone(new Phone("1234567890"))
                .document(new Document("1234567890123456"))
                .promotionNotificationsAllowed(true)
                .archivedAt(null)
                .registeredAt(OffsetDateTime.now())
                .archived(false)
                .loyaltyPoints(LoyaltyPoints.ZERO)
                .adress(Adress.builder()
                        .street("Bourbon Street")
                        .number("123")
                        .neighborhood("North Ville")
                        .city("York")
                        .state("South California")
                        .zipCode(new ZipCode("12345"))
                        .complement("Apartment 123")
                        .build());


    }

    public static Customer.ExistingCustomerBuilder existingAnonymized(){
        return Customer.existing()
                .id(new CustomerId())
                .fullName(new FullName("Anonymous", "Anonymous"))
                .birthDate(null)
                .email(new Email("anonymous@anonymous.com"))
                .phone(new Phone("000-000-0000"))
                .document(new Document("000-000-0000"))
                .promotionNotificationsAllowed(true)
                .archived(true)
                .registeredAt(OffsetDateTime.now())
                .archivedAt(OffsetDateTime.now())
                .loyaltyPoints(new LoyaltyPoints(10))
                .adress(Adress.builder()
                        .street("Bourbon Street")
                        .number("123")
                        .neighborhood("North Ville")
                        .city("York")
                        .state("South California")
                        .zipCode(new ZipCode("12345"))
                        .complement("Apartment 123")
                        .build());
    }
}
