package com.algaworks.algashop.ordering.domain.model.factory;


import com.algaworks.algashop.ordering.domain.model.entity.Order;
import com.algaworks.algashop.ordering.domain.model.entity.OrderTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.entity.PaymentMethod;
import com.algaworks.algashop.ordering.domain.model.entity.ProductTestDataBuilder;
import com.algaworks.algashop.ordering.domain.model.valueobject.Billing;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.Shipping;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class OrderFactoryTest {

    @Test
    public void shouldGenerateFiiledOrderThatCanBePlaced(){
        Shipping shipping = OrderTestDataBuilder.aShipping();
        Billing billing = OrderTestDataBuilder.aBilling();
        Product product = ProductTestDataBuilder.aProduct().build();
        PaymentMethod gatewayBalance = PaymentMethod.GATEWAY_BALANCE;
        Quantity quantity = new Quantity(1);
        CustomerId customerId = new CustomerId();
        Order order = OrderFactory.filled(customerId, shipping, billing, gatewayBalance, product, quantity);

        Assertions.assertWith(order,
                o-> Assertions.assertThat(o.billing()).isEqualTo(billing),
                o-> Assertions.assertThat(o.shipping()).isEqualTo(shipping),
                o-> Assertions.assertThat(o.paymentMethod()).isEqualTo(gatewayBalance),
                o-> Assertions.assertThat(o.items()).isNotEmpty(),
                o-> Assertions.assertThat(o.customerId()).isNotNull(),
                o-> Assertions.assertThat(o.isDraft()).isTrue());

        order.place();

        Assertions.assertThat(order.isPlaced()).isTrue();
    }

}