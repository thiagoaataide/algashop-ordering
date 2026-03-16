package com.algaworks.algashop.ordering.infrastructure.persistence.repository;

import com.algaworks.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntityTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //não substituir a configuração do banco
@Import(SpringDataAuditingConfig.class)
class OrdersPersistenceEntityRepositoryIT {


    private final OrdersPersistenceEntityRepository ordersPersistenceEntityRepository;


    @Autowired
    public OrdersPersistenceEntityRepositoryIT(OrdersPersistenceEntityRepository ordersPersistenceEntityRepository) {
        this.ordersPersistenceEntityRepository = ordersPersistenceEntityRepository;
    }

    @Test
    public void shouldPersist() {
        OrderPersistenceEntity entity = OrderPersistenceEntityTestDataBuilder.existingOrder().build();

        ordersPersistenceEntityRepository.saveAndFlush(entity);
        Assertions.assertThat(ordersPersistenceEntityRepository.existsById(entity.getId())).isTrue();

    }

    @Test
    public void shouldCount() {
        long ordersCount = ordersPersistenceEntityRepository.count();
        Assertions.assertThat(ordersCount).isZero();
    }

    @Test
    public void shouldSetAuditingValues() {
        OrderPersistenceEntity entity = OrderPersistenceEntityTestDataBuilder.existingOrder().build();
        entity = ordersPersistenceEntityRepository.saveAndFlush(entity);

        Assertions.assertThat(entity.getCreatedByUserId()).isNotNull();
        Assertions.assertThat(entity.getLastModifiedAt()).isNotNull();
        Assertions.assertThat(entity.getLastModifiedByUserId()).isNotNull();

    }

}