package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.domain.model.entity.CustomerTesteDataBuilder;
import com.algaworks.algashop.ordering.domain.model.valueobject.FullName;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.CustomerPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.CustomerPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.CustomerPersistenceEntityTestDataBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import({
        CustomerPersistenceEntityAssembler.class,
        CustomerPersistenceEntityDisassembler.class,
        CustomerPersistenceProvider.class,
        SpringDataAuditingConfig.class
})
class CustomerPersistenceProviderIT {

    @Autowired
    private CustomerPersistenceProvider provider;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void shouldPersistCustomerWithAllDataCorrectly() {
        Customer customer = CustomerTesteDataBuilder.brandNewCustomer().build();

        provider.add(customer);
        testEntityManager.flush();
        testEntityManager.clear();

        Optional<Customer> found = provider.ofId(customer.id());

        assertThat(found).isPresent();
        Customer saved = found.get();
        assertThat(saved.id()).isEqualTo(customer.id());
        assertThat(saved.fullName().firstName()).isEqualTo("John");
        assertThat(saved.fullName().lastName()).isEqualTo("Doe");
        assertThat(saved.email().value()).isEqualTo("john.doe@gmail.com");
        assertThat(saved.phone().value()).isEqualTo("1234567890");
        assertThat(saved.document().value()).isEqualTo("1234567890123456");
        assertThat(saved.isPromotionNotificationsAllowed()).isTrue();
        assertThat(saved.isAarchived()).isFalse();
        assertThat(saved.archivedAt()).isNull();
        assertThat(saved.registeredAt()).isNotNull();
        assertThat(saved.loyaltyPoints().value()).isZero();
        assertThat(saved.birthDate().value()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(saved.address().street()).isEqualTo("Bourbon Street");
        assertThat(saved.address().number()).isEqualTo("123");
        assertThat(saved.address().neighborhood()).isEqualTo("North Ville");
        assertThat(saved.address().city()).isEqualTo("York");
        assertThat(saved.address().state()).isEqualTo("South California");
        assertThat(saved.address().zipCode().value()).isEqualTo("12345");
        assertThat(saved.address().complement()).isEqualTo("Apartment 123");
    }

    @Test
    void shouldIncrementVersionWhenUpdatingExistingCustomer() {
        Customer customer = CustomerTesteDataBuilder.brandNewCustomer().build();
        provider.add(customer);
        Long versionAfterInsert = customer.version();

        customer.changeName(new FullName("Jane", "Doe"));
        provider.add(customer);
        testEntityManager.flush();
        testEntityManager.clear();

        Optional<Customer> reloaded = provider.ofId(customer.id());
        assertThat(reloaded).isPresent();
        assertThat(reloaded.get().version()).isGreaterThan(versionAfterInsert);
    }

    @Test
    void shouldThrowObjectOptimisticLockingFailureExceptionWhenUpdatingWithStaleVersion() {
        Customer customer = CustomerTesteDataBuilder.brandNewCustomer().build();
        provider.add(customer);

        customer.changeName(new FullName("Jane", "Doe"));
        provider.add(customer);

        setVersion(customer, 0L);

        assertThatThrownBy(() -> provider.add(customer))
                .isInstanceOf(ObjectOptimisticLockingFailureException.class);
    }

    @Test
    void shouldReturnCustomerWithCompleteMappingWhenFoundById() {
        CustomerPersistenceEntity entity = CustomerPersistenceEntityTestDataBuilder.existingCustomer().build();
        testEntityManager.persistAndFlush(entity);
        testEntityManager.clear();

        Optional<Customer> found = provider.ofId(new CustomerId(entity.getId()));

        assertThat(found).isPresent();
        Customer customer = found.get();
        assertThat(customer.id().value()).isEqualTo(entity.getId());
        assertThat(customer.fullName().firstName()).isEqualTo(entity.getFirstName());
        assertThat(customer.fullName().lastName()).isEqualTo(entity.getLastName());
        assertThat(customer.email().value()).isEqualTo(entity.getEmail());
        assertThat(customer.phone().value()).isEqualTo(entity.getPhone());
        assertThat(customer.document().value()).isEqualTo(entity.getDocument());
        assertThat(customer.isPromotionNotificationsAllowed()).isEqualTo(entity.getPromotionNotificationsAllowed());
        assertThat(customer.isAarchived()).isEqualTo(entity.getArchived());
        assertThat(customer.loyaltyPoints().value()).isEqualTo(entity.getLoyaltyPoints());
        assertThat(customer.birthDate().value()).isEqualTo(entity.getBirthDate());
        assertThat(customer.address().street()).isEqualTo(entity.getAddress().getStreet());
        assertThat(customer.address().number()).isEqualTo(entity.getAddress().getNumber());
        assertThat(customer.address().neighborhood()).isEqualTo(entity.getAddress().getNeighborhood());
        assertThat(customer.address().city()).isEqualTo(entity.getAddress().getCity());
        assertThat(customer.address().state()).isEqualTo(entity.getAddress().getState());
        assertThat(customer.address().zipCode().value()).isEqualTo(entity.getAddress().getZipCode());
        assertThat(customer.address().complement()).isEqualTo(entity.getAddress().getComplement());
        assertThat(customer.version()).isEqualTo(entity.getVersion());
    }

    @Test
    void shouldReturnCorrectCountAndExistence() {
        Customer customer = CustomerTesteDataBuilder.brandNewCustomer().build();

        assertThat(provider.count()).isZero();
        assertThat(provider.exists(customer.id())).isFalse();

        provider.add(customer);

        assertThat(provider.count()).isOne();
        assertThat(provider.exists(customer.id())).isTrue();
    }

    private void setVersion(Customer customer, Long version) {
        Field versionField = ReflectionUtils.findField(Customer.class, "version");
        versionField.setAccessible(true);
        ReflectionUtils.setField(versionField, customer, version);
        versionField.setAccessible(false);
    }
}
