package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.exception.CustomerArchivedException;
import com.algaworks.algashop.ordering.domain.utility.IdGenerator;
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
                            IdGenerator.genarateTimeBasedUUID(),
                            "John Doe",
                            LocalDate.of(1990, 1, 1),
                            "invalid",
                            "1234567890",
                            "1234567890123456",
                            false,
                            OffsetDateTime.now()
                    );
                });


    }

    @Test
    void giver_invalidEmail_whenTryUpdateCustomerEmail_shouldGenerateException(){

        Customer customer = new Customer(
                IdGenerator.genarateTimeBasedUUID(),
                "John Doe",
                LocalDate.of(1990, 1, 1),
                "john.doe@gmail.com",
                "1234567890",
                "1234567890123456",
                false,
                OffsetDateTime.now());

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->{
                    customer.changeEmail("invalid");

                });

//        customer.changeEmail("invalid");

    }

    @Test
    void given_unarchivedCustomer_whenArchive_shouldAnonymize(){
        Customer customer = new Customer(
                IdGenerator.genarateTimeBasedUUID(),
                "John Doe",
                LocalDate.of(1990, 1, 1),
                "john.doe@gmail.com",
                "1234567890",
                "1234567890123456",
                false,
                OffsetDateTime.now());

        customer.archive();

        Assertions.assertWith(customer,
                c-> assertThat(c.fullName()).isEqualTo("Anonymous"),
                c-> assertThat(c.email()).isNotEqualTo("john.doe@gmail.com"),
                c-> assertThat(c.phone()).isEqualTo("000-000-0000"),
                c-> assertThat(c.document()).isEqualTo("000-000-0000"),
                c-> assertThat(c.birthDate()).isNull(),
                c-> assertThat(c.isPromotionNotificationsAllowed()).isFalse()
                );
    }

    @Test
    void given_archivedCustomer_whenTryToUpdate_shouldGenerateException(){
        Customer customer = new Customer(
                IdGenerator.genarateTimeBasedUUID(),
                "Anonymous",
                null,
                "anonymous@anonymous.com",
                "000-000-0000",
                "000-000-0000",
                true,
                true,
                OffsetDateTime.now(),
                OffsetDateTime.now(),
                10
        );

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(customer::archive);

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(()->customer.changeName("John Doe"));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(()->customer.changePhone("555-777-1234"));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(()->customer.changeEmail("john.doe@gmail.com"));

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(()->customer.disablePromotionNotifications());

        Assertions.assertThatExceptionOfType(CustomerArchivedException.class)
                .isThrownBy(()->customer.enablePromotionNotifications());
    }

    @Test
    void given_brandNewCustomer_whenAddLoyaltyPoints_shouldSumPoints(){
        Customer customer = new Customer(
                IdGenerator.genarateTimeBasedUUID(),
                "John Doe",
                LocalDate.of(1990, 1, 1),
                "john.doe@gmail.com",
                "1234567890",
                "1234567890123456",
                false,
                OffsetDateTime.now());

        customer.addLoyaltyPoints(10);
        customer.addLoyaltyPoints(30);

       Assertions.assertThat(customer.loyaltyPoints()).isEqualTo(40);
    }

    @Test
    void given_brandNewCustomer_whenAddInvalidLoyaltyPoints_shouldGenerateException(){
        Customer customer = new Customer(
                IdGenerator.genarateTimeBasedUUID(),
                "John Doe",
                LocalDate.of(1990, 1, 1),
                "john.doe@gmail.com",
                "1234567890",
                "1234567890123456",
                false,
                OffsetDateTime.now());

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(
                        ()->customer.addLoyaltyPoints(-10)
                );

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(
                        ()->customer.addLoyaltyPoints(0)
                );
    }

}