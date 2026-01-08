package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.exception.CustomerArchivedException;
import com.algaworks.algashop.ordering.domain.utility.IdGenerator;
import com.algaworks.algashop.ordering.domain.valueobject.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.OffsetDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void giver_invalidEmail_whenTryCreateCustomer_shouldGenerateException(){

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->{
                    new Customer(
                            new CustomerID(),
                            new FullName("John", "Doe"),
                            new BirthDate(LocalDate.of(1990, 1, 1)),
                            new Email("invalid"),
                            new Phone("1234567890"),
                            new Document("1234567890123456"),
                            false,
                            OffsetDateTime.now(),
                            Adress.builder()
                                    .street("Bourbon Street")
                                    .number("123")
                                    .neighborhood("North Ville")
                                    .city("York")
                                    .state("South California")
                                    .zipCode(new ZipCode("12345"))
                                    .complement("Apartment 123")
                                    .build()
                    );
                });


    }

    @Test
    void giver_invalidEmail_whenTryUpdateCustomerEmail_shouldGenerateException(){

        Customer customer = new Customer(
                new CustomerID(),
                new FullName("John", "Doe"),
                new BirthDate(LocalDate.of(1990, 1, 1)),
                new Email("john.doe@gmail.com"),
                new Phone("1234567890"),
                new Document("1234567890123456"),
                false,
                OffsetDateTime.now(),
                Adress.builder()
                        .street("Bourbon Street")
                        .number("123")
                        .neighborhood("North Ville")
                        .city("York")
                        .state("South California")
                        .zipCode(new ZipCode("12345"))
                        .complement("Apartment 123")
                        .build());

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->{
                    customer.changeEmail(new Email("invalid"));

                });

//        customer.changeEmail("invalid");

    }

    @Test
    void given_unarchivedCustomer_whenArchive_shouldAnonymize(){
        Customer customer = new Customer(
                new CustomerID(),
                new FullName("John", "Doe"),
                new BirthDate(LocalDate.of(1990, 1, 1)),
                new Email("john.doe@gmail.com"),
                new Phone("1234567890"),
                new Document("1234567890123456"),
                false,
                OffsetDateTime.now(),
                Adress.builder()
                        .street("Bourbon Street")
                        .number("123")
                        .neighborhood("North Ville")
                        .city("York")
                        .state("South California")
                        .zipCode(new ZipCode("12345"))
                        .complement("Apartment 123")
                        .build());

        customer.archive();

        Assertions.assertWith(customer,
                c-> assertThat(c.fullName()).isEqualTo(new FullName("Anonymous", "Anonymous")),
                c-> assertThat(c.email()).isNotEqualTo(new Email( "john.doe@gmail.com")),
                c-> assertThat(c.phone()).isEqualTo(new Phone("000-000-0000")),
                c-> assertThat(c.document()).isEqualTo(new Document("000-000-0000")),
                c-> assertThat(c.birthDate()).isNull(),
                c-> assertThat(c.isPromotionNotificationsAllowed()).isFalse(),
                c-> assertThat(c.adress()).isEqualTo(Adress.builder()
                        .street("Bourbon Street")
                        .number("Anonymized")
                        .neighborhood("North Ville")
                        .city("York")
                        .state("South California")
                        .zipCode(new ZipCode("12345"))
                        .complement(null)
                        .build())
                );
    }

    @Test
    void given_archivedCustomer_whenTryToUpdate_shouldGenerateException(){
        Customer customer = new Customer(
                new CustomerID(),
                new FullName("Anonymous", "Anonymous"),
                new BirthDate(null),
                new Email("anonymous@anonymous.com"),
                new Phone("000-000-0000"),
                new Document("000-000-0000"),
                true,
                true,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                new LoyaltyPoints(10),
                Adress.builder()
                        .street("Bourbon Street")
                        .number("123")
                        .neighborhood("North Ville")
                        .city("York")
                        .state("South California")
                        .zipCode(new ZipCode("12345"))
                        .complement("Apartment 123")
                        .build()
        );

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::archive);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(()->customer.changeName(new FullName("John", "Doe")));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(()->customer.changePhone(new Phone("555-777-1234")));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(()->customer.changeEmail(new Email("john.doe@gmail.com")));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::disablePromotionNotifications);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::enablePromotionNotifications);
    }

    @Test
    void given_brandNewCustomer_whenAddLoyaltyPoints_shouldSumPoints(){
        Customer customer = new Customer(
                new CustomerID(),
                new FullName("John", "Doe"),
                new BirthDate(LocalDate.of(1990, 1, 1)),
                new Email("john.doe@gmail.com"),
                new Phone("1234567890"),
                new Document("1234567890123456"),
                false,
                OffsetDateTime.now(),
                Adress.builder()
                        .street("Bourbon Street")
                        .number("123")
                        .neighborhood("North Ville")
                        .city("York")
                        .state("South California")
                        .zipCode(new ZipCode("12345"))
                        .complement("Apartment 123")
                        .build());

        customer.addLoyaltyPoints(new LoyaltyPoints(10));
        customer.addLoyaltyPoints(new LoyaltyPoints(30));

       Assertions.assertThat(customer.loyaltyPoints()).isEqualTo(new LoyaltyPoints(40));
    }

    @Test
    void given_brandNewCustomer_whenAddInvalidLoyaltyPoints_shouldGenerateException(){
        Customer customer = new Customer(
                new CustomerID(),
                new FullName("John", "Doe"),
                new BirthDate(LocalDate.of(1990, 1, 1)),
                new Email("john.doe@gmail.com"),
                new Phone("1234567890"),
                new Document("1234567890123456"),
                false,
                OffsetDateTime.now(),
                Adress.builder()
                        .street("Bourbon Street")
                        .number("123")
                        .neighborhood("North Ville")
                        .city("York")
                        .state("South California")
                        .zipCode(new ZipCode("12345"))
                        .complement("Apartment 123")
                        .build());

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(
                        ()->customer.addLoyaltyPoints(new LoyaltyPoints(-10))
                );

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(
                        ()->customer.addLoyaltyPoints(LoyaltyPoints.ZERO)
                );
    }

}