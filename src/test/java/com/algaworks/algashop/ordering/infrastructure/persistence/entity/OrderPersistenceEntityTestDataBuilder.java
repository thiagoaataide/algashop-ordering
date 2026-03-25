package com.algaworks.algashop.ordering.infrastructure.persistence.entity;

import com.algaworks.algashop.ordering.domain.model.utility.IdGenerator;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;

import static com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity.*;

public class OrderPersistenceEntityTestDataBuilder {

    private OrderPersistenceEntityTestDataBuilder() {
    }

    public static OrderPersistenceEntityBuilder existingOrder(){
        return OrderPersistenceEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .customerId(IdGenerator.genarateTimeBasedUUID())
                .totalItems(3)
                .totalAmount(new BigDecimal(1250))
                .status("DRAFT")
                .paymentMethod("CREDIT_CARD")
                .placeAt(OffsetDateTime.now())
                .items(Set.of(
                        existingItem().build(),
                        existingItemAlt().build()
                ));
    }

    public static OrderItemPersistenceEntity.OrderItemPersistenceEntityBuilder existingItem(){
        return OrderItemPersistenceEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .productName("Notebook")
                .productId(IdGenerator.genarateTimeBasedUUID())
                .quantity(2)
                .price(new BigDecimal(500))
                .totalAmount(new BigDecimal(1000));
    }

    public static OrderItemPersistenceEntity.OrderItemPersistenceEntityBuilder existingItemAlt(){
        return OrderItemPersistenceEntity.builder()
                .id(IdGenerator.generateTSID().toLong())
                .productName("Mouse pad")
                .productId(IdGenerator.genarateTimeBasedUUID())
                .quantity(1)
                .price(new BigDecimal(250))
                .totalAmount(new BigDecimal(250));
    }
}
