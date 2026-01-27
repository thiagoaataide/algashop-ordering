package com.algaworks.algashop.ordering.domain.entity;


import com.algaworks.algashop.ordering.domain.exception.OrderInvalidShippingDeliveryDateException;
import com.algaworks.algashop.ordering.domain.exception.OrderStatusCannotBeChangedException;
import com.algaworks.algashop.ordering.domain.valueobject.*;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

class OrderTest {

    @Test
    public void shouldGenerate(){
        Order order = Order.draft(new CustomerId());
    }

    @Test
    public void shouldAddItem(){
        Order order = Order.draft(new CustomerId());
        ProductId productId = new ProductId();
        order.addItem(
                productId,
                new ProductName("Caneca Stanley"),
                new Quantity(1),
                new Money("250"));

        Assertions.assertThat(order.items()).isNotEmpty();
        Assertions.assertThat(order.items()).size().isEqualTo(1);
        OrderItem ordemItem = order.items().iterator().next();
        Assertions.assertWith(ordemItem,
                (i)-> Assertions.assertThat(i.id()).isNotNull(),
                (i)-> Assertions.assertThat(i.productName()).isEqualTo(new ProductName("Caneca Stanley")),
                (i)-> Assertions.assertThat(i.price()).isEqualTo(new Money("250")),
                (i)-> Assertions.assertThat(i.quantity()).isEqualTo(new Quantity(1)),
                (i)-> Assertions.assertThat(i.productId()).isEqualTo(productId));

    }


    @Test
    public void shouldGenerateExceptionWhenTryToChangeItemSet(){
        Order order = Order.draft(new CustomerId());
        ProductId productId = new ProductId();
        order.addItem(
                productId,
                new ProductName("Caneca Stanley"),
                new Quantity(1),
                new Money("250"));
        Set<OrderItem> items = order.items();

        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(()->items.clear());
    }

    @Test
    public void shouldCalculateTotals(){
        Order order = Order.draft(new CustomerId());
        ProductId productId = new ProductId();

        order.addItem(
                productId,
                new ProductName("Camisa Adidas"),
                new Quantity(1),
                new Money("90"));

        order.addItem(
                productId,
                new ProductName("Caneca Stanley"),
                new Quantity(2),
                new Money("250"));


        Assertions.assertThat(order.totalAmount()).isEqualTo(new Money("590"));
        Assertions.assertThat(order.totalItems()).isEqualTo(new Quantity(3));
    }

    @Test
    public void givenDraftOrder_whenPlace_shouldChangeStatusToPlaced(){
        Order order = Order.draft(new CustomerId());
        order.place();
        Assertions.assertThat(order.isPlaced()).isTrue();
    }

    @Test
    public void givenPlacedOrder_whenTryToPlace_shouldGenerateException(){
        Order order = Order.draft(new CustomerId());
        order.place();
        Assertions.assertThatExceptionOfType(OrderStatusCannotBeChangedException.class)
                .isThrownBy(order::place);
    }

    @Test
    public void givenDraftOrder_whenChangePaymentMethod_souldAllowed(){
        Order order = Order.draft(new CustomerId());
        order.changePaymentMethod(PaymentMethod.CREDIT_CARD);
        Assertions.assertThat(order.paymentMethod()).isEqualTo(PaymentMethod.CREDIT_CARD);
    }

    @Test
    public void givenDraftOrder_whenChangeBillingInfo_shouldAllowChange(){
        Address address = Address.builder()
                .street("Av. Joaquim José Diniz")
                .number("20")
                .neighborhood("Fernão Dias")
                .complementm("Torre 03 Apto 801")
                .city("Belo Horizonte")
                .state("Minas Gerais")
                .zipCode(new ZipCode("31910"))
                .build();

        BillingInfo billingInfo = BillingInfo.builder()
                .address(address)
                .document(new Document("083.388.654-19"))
                .phone(new Phone("31 99195-3046"))
                .fullName(new FullName("Thiago", "Alberto"))
                .build();

        Order order = Order.draft(new CustomerId());
        order.changeBillingInfo(billingInfo);

        BillingInfo expectedBillingInfo = BillingInfo.builder()
                .address(address)
                .document(new Document("083.388.654-19"))
                .phone(new Phone("31 99195-3046"))
                .fullName(new FullName("Thiago", "Alberto"))
                .build();

        Assertions.assertThat(order.billing()).isEqualTo(expectedBillingInfo);
    }

    @Test
    public void givenDraftOrder_whenChangeShippingInfo_shouldAllowChange(){
        Address address = Address.builder()
                .street("Av. Joaquim José Diniz")
                .number("20")
                .neighborhood("Fernão Dias")
                .complementm("Torre 03 Apto 801")
                .city("Belo Horizonte")
                .state("Minas Gerais")
                .zipCode(new ZipCode("31910"))
                .build();

        ShippingInfo shippingInfo = ShippingInfo.builder()
                .fullName(new FullName("Thiago", "Alberto"))
                .document(new Document("083.388.654-19"))
                .phone(new Phone("31 99195-3046"))
                .address(address)
                .build();

        Order order = Order.draft(new CustomerId());
        Money shippingCost = Money.ZERO;
        LocalDate expectedDeliveryDate = LocalDate.now().plusDays(1);

        order.changeShipping(shippingInfo, shippingCost, expectedDeliveryDate);

        Assertions.assertWith(order,
                o-> Assertions.assertThat(o.shipping()).isEqualTo(shippingInfo),
                o-> Assertions.assertThat(o.shippingCost()).isEqualTo(shippingCost),
                o-> Assertions.assertThat(o.expectedDeliveryDate()).isEqualTo(expectedDeliveryDate)
                );
    }


    @Test
    public void givenDraftOrderAndDeliveryDateInThePast_whenChangeShippingInfo_shouldNotAllowChange(){
        Address address = Address.builder()
                .street("Av. Joaquim José Diniz")
                .number("20")
                .neighborhood("Fernão Dias")
                .complementm("Torre 03 Apto 801")
                .city("Belo Horizonte")
                .state("Minas Gerais")
                .zipCode(new ZipCode("31910"))
                .build();

        ShippingInfo shippingInfo = ShippingInfo.builder()
                .fullName(new FullName("Thiago", "Alberto"))
                .document(new Document("083.388.654-19"))
                .phone(new Phone("31 99195-3046"))
                .address(address)
                .build();

        Order order = Order.draft(new CustomerId());
        Money shippingCost = Money.ZERO;
        LocalDate expectedDeliveryDate = LocalDate.now().minusDays(2);

        Assertions.assertThatExceptionOfType(OrderInvalidShippingDeliveryDateException.class)
                .isThrownBy(()->order.changeShipping(shippingInfo, shippingCost, expectedDeliveryDate));


    }
}