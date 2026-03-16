package com.algaworks.algashop.ordering.infrastructure.persistence.assembler;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderPersistenceEntityAssemblerTest {

    private OrderPersistenceEntityAssembler assembler = new OrderPersistenceEntityAssembler();

    @Test
    void shouldConvertToDomain(){
        Order order = OrderTestDataBuilder.anOrder().build();
        OrderPersistenceEntity orderPersistenceEntity = assembler.fromDomain(order);
        assertThat(orderPersistenceEntity).satisfies(
                p -> assertThat(p.getId()).isEqualTo(order.id().value().toLong()),
                p -> assertThat(p.getCustomerId()).isEqualTo(order.customerId().value()),
                p -> assertThat(p.getStatus()).isEqualTo(order.status().name()),
                p -> assertThat(p.getPaymentMethod()).isEqualTo(order.paymentMethod().name()),
                p -> assertThat(p.getTotalAmount()).isEqualTo(order.totalAmount().value()),
                p -> assertThat(p.getTotalItems()).isEqualTo(order.totalItems().value()),
                p -> assertThat(p.getPlaceAt()).isEqualTo(order.placedAt()),
                p -> assertThat(p.getReadyAt()).isEqualTo(order.readyAt()),
                p -> assertThat(p.getCanceledAt()).isEqualTo(order.canceledAt()),
                p -> assertThat(p.getPaidAt()).isEqualTo(order.paidAt())
        );
    }

    @Test
    void shouldMerge(){

    }
}