package com.algaworks.algashop.ordering.infrastructure.persistence.disassembler;

import com.algaworks.algashop.ordering.domain.model.entity.ShoppingCart;
import com.algaworks.algashop.ordering.domain.model.entity.ShoppingCartItem;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartItemId;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.ShoppingCartItemPersistenceEntity;
import com.algaworks.algashop.ordering.infrastructure.persistence.entity.ShoppingCartPersistenceEntity;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ShoppingCartPersistenceEntityDisassembler {

    public ShoppingCart toDomainEntity(ShoppingCartPersistenceEntity shoppingCartPersistenceEntity) {
        return ShoppingCart.existing()
                .id(new ShoppingCartId(shoppingCartPersistenceEntity.getId()))
                .version(shoppingCartPersistenceEntity.getVersion())
                .createdAt(shoppingCartPersistenceEntity.getCreatedAt())
                .totalAmount(new Money(shoppingCartPersistenceEntity.getTotalAmount()))
                .totalItems(new Quantity(shoppingCartPersistenceEntity.getTotalItems()))
                .customerId(new CustomerId(shoppingCartPersistenceEntity.getCustomer().getId()))
                .items(new HashSet<>())
                .items(toDomainEntities(shoppingCartPersistenceEntity.getItems()))
                .build();
    }

    private Set<ShoppingCartItem> toDomainEntities(Set<ShoppingCartItemPersistenceEntity> items) {
        return items.stream().map(i -> toDomainEntity(i)).collect(Collectors.toSet());
    }

    private ShoppingCartItem toDomainEntity(ShoppingCartItemPersistenceEntity persistenceEntity) {
        return ShoppingCartItem.existing()
                .id(new ShoppingCartItemId(persistenceEntity.getId()))
                .shoppingCartId(new ShoppingCartId(persistenceEntity.getShoppingCartId()))
                .price(new Money(persistenceEntity.getPrice()))
                .quantity(new Quantity(persistenceEntity.getQuantity()))
                .totalAmount(new Money(persistenceEntity.getTotalAmount()))
                .productName(new ProductName(persistenceEntity.getName()))
                .available(persistenceEntity.getAvailable())
                .productId(new ProductId(persistenceEntity.getProductId()))
                .build();
    }
}
