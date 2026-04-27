package com.algaworks.algashop.ordering.infrastructure.persistence.provider;

import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderStatus;
import com.algaworks.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.infrastructure.persistence.assembler.OrderPersistenceEntityAssembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.config.SpringDataAuditingConfig;
import com.algaworks.algashop.ordering.infrastructure.persistence.disassembler.OrderPersistenceEntityDisassembler;
import com.algaworks.algashop.ordering.infrastructure.persistence.repository.OrdersPersistenceEntityRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Import({
        OrdersPersistenceProvider.class,
        OrderPersistenceEntityAssembler.class,
        OrderPersistenceEntityDisassembler.class,
        SpringDataAuditingConfig.class
})
class OrdersPersistenceProviderIT {
    private OrdersPersistenceProvider persistenceProvider;
    private OrdersPersistenceEntityRepository entityRepository;

    @Autowired
    public OrdersPersistenceProviderIT(OrdersPersistenceProvider persistenceProvider, OrdersPersistenceEntityRepository entityRepository) {
        this.persistenceProvider = persistenceProvider;
        this.entityRepository = entityRepository;
    }

    @Test
    public void shouldUpdateAndKeepPersistenceEntityState() {
        Order order = OrderTestDataBuilder.anOrder().status(OrderStatus.PLACED).build();
        long orderId = order.id().value().toLong();
        persistenceProvider.add(order);

        var persistenteEntity = entityRepository.findById(orderId).orElseThrow();
        Assertions.assertThat(persistenteEntity.getStatus()).isEqualTo(OrderStatus.PLACED.name());

        Assertions.assertThat(persistenteEntity.getCreatedByUserId()).isNotNull();
        Assertions.assertThat(persistenteEntity.getLastModifiedAt()).isNotNull();
        Assertions.assertThat(persistenteEntity.getLastModifiedByUserId()).isNotNull();

        order = persistenceProvider.ofId(order.id()).orElseThrow();
        order.markAsPaid();
        persistenceProvider.add(order);

        persistenteEntity = entityRepository.findById(orderId).orElseThrow();

        Assertions.assertThat(persistenteEntity.getStatus()).isEqualTo(OrderStatus.PAID.name());

        Assertions.assertThat(persistenteEntity.getCreatedByUserId()).isNotNull();
        Assertions.assertThat(persistenteEntity.getLastModifiedAt()).isNotNull();
        Assertions.assertThat(persistenteEntity.getLastModifiedByUserId()).isNotNull();
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void shouldAddFindAndNotFailWhenNoTransaction() {
        Order order = OrderTestDataBuilder.anOrder().build();
        persistenceProvider.add(order);

        Assertions.assertThatNoException().isThrownBy(
                () -> persistenceProvider.ofId(order.id()).orElseThrow()
        );
    }
}