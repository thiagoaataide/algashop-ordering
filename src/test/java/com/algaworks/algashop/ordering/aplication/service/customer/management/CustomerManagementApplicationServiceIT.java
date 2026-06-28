package com.algaworks.algashop.ordering.aplication.service.customer.management;

import com.algaworks.algashop.ordering.aplication.commons.AddressData;
import com.algaworks.algashop.ordering.aplication.customer.management.CustomerInput;
import com.algaworks.algashop.ordering.aplication.customer.management.CustomerManagementApplicationService;
import com.algaworks.algashop.ordering.aplication.customer.management.CustomerOutput;
import com.algaworks.algashop.ordering.aplication.customer.management.CustomerUpdateInput;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

@SpringBootTest
@Transactional
class CustomerManagementApplicationServiceIT {

    @Autowired
    private CustomerManagementApplicationService customerManagementApplicationService;

    @Test
    public void shouldRegister() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();

        UUID customerId = customerManagementApplicationService.create(input);
        Assertions.assertThat(customerId).isNotNull();

        CustomerOutput customerOutput = customerManagementApplicationService.findById(customerId);

        Assertions.assertThat(customerOutput)
                .extracting(
                        CustomerOutput::getId,
                        CustomerOutput::getFirstName,
                        CustomerOutput::getLastName,
                        CustomerOutput::getEmail,
                        CustomerOutput::getBirthDate
                ).containsExactly(
                        customerId,
                        "John",
                        "Doe",
                        "johndoe@email.com",
                        LocalDate.of(1991, 7,5));

//        Assertions.assertThat(customerOutput.getId()).isEqualTo(customerId);
//        Assertions.assertThat(customerOutput.getFirstName()).isEqualTo("John");
//        Assertions.assertThat(customerOutput.getLastName()).isEqualTo("Doe");
//        Assertions.assertThat(customerOutput.getEmail()).isEqualTo("johndoe@email.com");
//        Assertions.assertThat(customerOutput.getBirthDate()).isEqualTo(LocalDate.of(1991, 7,5));

        Assertions.assertThat(customerOutput.getRegisteredAt()).isNotNull();
    }


    @Test
    public void shouldUpdate() {
        CustomerInput input = CustomerInputTestDataBuilder.aCustomer().build();
        CustomerUpdateInput updateInput = CustomerUpdateInputTestDataBuilder.aCustomerUpdate().build();

        UUID customerId = customerManagementApplicationService.create(input);
        Assertions.assertThat(customerId).isNotNull();

        customerManagementApplicationService.update(customerId, updateInput);

        CustomerOutput customerOutput = customerManagementApplicationService.findById(customerId);

        Assertions.assertThat(customerOutput)
                .extracting(
                        CustomerOutput::getId,
                        CustomerOutput::getFirstName,
                        CustomerOutput::getLastName,
                        CustomerOutput::getEmail,
                        CustomerOutput::getBirthDate
                ).containsExactly(
                        customerId,
                        "Matt",
                        "Damon",
                        "johndoe@email.com",
                        LocalDate.of(1991, 7,5));


        Assertions.assertThat(customerOutput.getRegisteredAt()).isNotNull();
    }

}