package com.algaworks.algashop.ordering.domain.entity;


import com.algaworks.algashop.ordering.domain.exception.OrderStatusCannotBeChangedException;
import com.algaworks.algashop.ordering.domain.valueobject.Money;
import com.algaworks.algashop.ordering.domain.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

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
}