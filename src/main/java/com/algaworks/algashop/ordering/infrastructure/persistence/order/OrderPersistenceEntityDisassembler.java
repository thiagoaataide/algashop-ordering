package com.algaworks.algashop.ordering.infrastructure.persistence.order;

import com.algaworks.algashop.ordering.domain.model.commons.*;
import com.algaworks.algashop.ordering.domain.model.order.*;
import com.algaworks.algashop.ordering.domain.model.product.ProductName;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.domain.model.product.ProductId;
import com.algaworks.algashop.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderPersistenceEntityDisassembler {

    public Order toDomainEntity(OrderPersistenceEntity persistenceEntity) {
        return Order.existing()
                .id(new OrderId(persistenceEntity.getId()))
                .customerId(new CustomerId(persistenceEntity.getCustomerId()))
                .status(OrderStatus.valueOf(persistenceEntity.getStatus()))
                .totalAmount(new Money(persistenceEntity.getTotalAmount()))
                .totalItems(new Quantity(persistenceEntity.getTotalItems()))
                .paymentMethod(PaymentMethod.valueOf(persistenceEntity.getPaymentMethod()))
                .placedAt(persistenceEntity.getPlaceAt())
                .canceledAt(persistenceEntity.getCanceledAt())
                .paidAt(persistenceEntity.getPaidAt())
                .readyAt(persistenceEntity.getReadyAt())
                .items(new HashSet<>())
                .version(persistenceEntity.getVersion())
                .billing(toBillingValueObject(persistenceEntity.getBilling()))
                .shipping(toShippingValueObject(persistenceEntity.getShipping()))
                .items(toDomainEntities(persistenceEntity.getItems()))
                .build();
    }

    private Set<OrderItem> toDomainEntities(Set<OrderItemPersistenceEntity> items) {
        return items.stream().map(i -> toDomainEntity(i)).collect(Collectors.toSet());
    }

    private OrderItem toDomainEntity(OrderItemPersistenceEntity persistenceEntity) {
        return OrderItem.existing()
                .id(new OrderItemId(persistenceEntity.getId()))
                .orderId(new OrderId(persistenceEntity.getOrderId()))
                .productId(new ProductId(persistenceEntity.getProductId()))
                .productName(new ProductName(persistenceEntity.getProductName()))
                .quantity(new Quantity(persistenceEntity.getQuantity()))
                .price(new Money(persistenceEntity.getPrice()))
                .totalAmount(new Money(persistenceEntity.getTotalAmount()))
                .build();
    }


    private Billing toBillingValueObject(BillingEmbeddable billingEmbeddable) {
        if (billingEmbeddable == null) {
            return null;
        }
        return Billing.builder()
                .fullName(new FullName(billingEmbeddable.getFirstName(), billingEmbeddable.getLastName()))
                .document(new Document(billingEmbeddable.getDocument()))
                .phone(new Phone(billingEmbeddable.getPhone()))
                .email(new Email(billingEmbeddable.getEmail()))
                .address(toAddress(billingEmbeddable.getAddress()))
                .build();

    }


    private Shipping toShippingValueObject(ShippingEmbeddable shippingEmbeddable) {
        if (shippingEmbeddable == null) {
            return null;
        }

        var builder = Shipping.builder()
                .cost(new Money(shippingEmbeddable.getCost()))
                .expectedDate(shippingEmbeddable.getExpectedDate())
                .address(toAddress(shippingEmbeddable.getAddress()));

        if (shippingEmbeddable.getRecipient() != null) {
            Recipient recipient = Recipient.builder()
                    .fullName(new FullName(shippingEmbeddable.getRecipient().getFirstName(), shippingEmbeddable.getRecipient().getLastName()))
                    .document(new Document(shippingEmbeddable.getRecipient().getDocument()))
                    .phone(new Phone(shippingEmbeddable.getRecipient().getPhone()))
                    .build();

            builder.recipient(recipient);
        }


        return builder.build();
    }

    private Address toAddress(AddressEmbeddable addressEmbeddable) {
        if (addressEmbeddable == null) {
            return null;
        }

        return Address.builder()
                .city(addressEmbeddable.getCity())
                .state(addressEmbeddable.getState())
                .street(addressEmbeddable.getStreet())
                .neighborhood(addressEmbeddable.getNeighborhood())
                .complement(addressEmbeddable.getComplement())
                .zipCode(new ZipCode(addressEmbeddable.getZipCode()))
                .number(addressEmbeddable.getNumber())
                .build();
    }
}
