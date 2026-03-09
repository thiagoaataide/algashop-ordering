package com.algaworks.algashop.ordering.infrastructure.persistence.repository;

import com.algaworks.algashop.ordering.domain.model.utility.IdGenerator;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //não substituir a configuração do banco
class OrdersPersistenceEntityRepositoryIT {


    private final OrdersPersistenceEntityRepository ordersPersistenceEntityRepository;


    @Autowired
    public OrdersPersistenceEntityRepositoryIT(OrdersPersistenceEntityRepository ordersPersistenceEntityRepository) {
        this.ordersPersistenceEntityRepository = ordersPersistenceEntityRepository;
    }

    @Test
    public void shouldPersist() {
        long orderId = IdGenerator.generateTSID().toLong();
        OrderPersistenceEntity entity = OrderPersistenceEntity.builder()
                .id(orderId)
                .customerId(IdGenerator.genarateTimeBasedUUID())
                .totalItems(2)
                .totalAmount(new BigDecimal(1000))
                .status("DRAFT")
                .paymentMethod("CREDIT_CARD")
                .placeAt(OffsetDateTime.now())
                .build();

        ordersPersistenceEntityRepository.saveAndFlush(entity);
        Assertions.assertThat(ordersPersistenceEntityRepository.existsById(orderId )).isTrue();

    }

    @Test
    public void shouldCount(){
        long ordersCount = ordersPersistenceEntityRepository.count();
        Assertions.assertThat(ordersCount).isZero();
    }

}