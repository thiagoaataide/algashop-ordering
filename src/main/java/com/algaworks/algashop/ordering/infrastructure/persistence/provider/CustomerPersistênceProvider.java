package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import com.algaworks.algashop.ordering.domain.model.entity.Customer;
import com.algaworks.algashop.ordering.domain.model.repository.Customers;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;

import java.util.Optional;

public class CustomerPersistênceProvider implements Customers {
    @Override
    public Optional<Customer> ofId(CustomerId customerId) {
        return Optional.empty();
    }

    @Override
    public boolean exists(CustomerId customerId) {
        return false;
    }

    @Override
    public void add(Customer aggregateRoot) {

    }

    @Override
    public long count() {
        return 0;
    }
}
