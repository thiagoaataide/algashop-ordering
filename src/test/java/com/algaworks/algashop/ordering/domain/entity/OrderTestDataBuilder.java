package com.algaworks.algashop.ordering.domain.entity;

import com.algaworks.algashop.ordering.domain.valueobject.*;
import com.algaworks.algashop.ordering.domain.valueobject.id.CustomerId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ProductId;

import java.time.LocalDate;

public class OrderTestDataBuilder {

    private CustomerId customerId = new CustomerId();
    private PaymentMethod paymentMethod = PaymentMethod.GATEWAY_BALANCE;
    private Money shippingCost = new Money("10.00");
    private LocalDate expectedDeliveryDate = LocalDate.now().plusDays(2);
    private ShippingInfo shippingInfo = aShippingInfo();
    private BillingInfo billingInfo = aBillingInfo();

    private OrderStatus status = OrderStatus.DRAFT;

    private boolean withItems = true;

    private OrderTestDataBuilder() {}

    public static OrderTestDataBuilder anOrder(){
        return new OrderTestDataBuilder();
    }

    public Order build(){
        Order order = Order.draft(customerId);
        order.changeShipping(shippingInfo, shippingCost, expectedDeliveryDate);
        order.changeBillingInfo(billingInfo);
        order.changePaymentMethod(paymentMethod);

        if(withItems){
            order.addItem(new ProductId(), new ProductName("Samsung S24 Ultra"), new Quantity(1), new Money("4000.00"));
            order.addItem(new ProductId(), new ProductName("Samsung S25 Ultra"), new Quantity(1), new Money("6000.00"));
            order.addItem(new ProductId(), new ProductName("Case S25 Ultra"), new Quantity(2), new Money("150.00"));
        }

        switch (this.status){
            case DRAFT -> {

            }
            case PLACED -> {
                order.place();
            }
            case PAID -> {
                order.place();
                order.markAsPaid();
            }
            case READY -> {

            }
            case CANCELED -> {

            }


        }

        return order;
    }

    public static ShippingInfo aShippingInfo() {
        return ShippingInfo.builder()
                .fullName(new FullName("Thiago", "Alberto"))
                .document(new Document("083.388.654-19"))
                .phone(new Phone("31 99195-3046"))
                .address(anAddress())
                .build();
    }

    public static BillingInfo aBillingInfo() {
        return BillingInfo.builder()
                .address(anAddress())
                .document(new Document("083.388.654-19"))
                .phone(new Phone("31 99195-3046"))
                .fullName(new FullName("Thiago", "Alberto"))
                .build();
    }

    public static Address anAddress() {
        return Address.builder()
                .street("Av. Joaquim José Diniz")
                .number("20")
                .neighborhood("Fernão Dias")
                .complementm("Torre 03 Apto 801")
                .city("Belo Horizonte")
                .state("Minas Gerais")
                .zipCode(new ZipCode("31910"))
                .build();
    }

    public OrderTestDataBuilder customerId(CustomerId customerId) {
        this.customerId = customerId;
        return this;
    }

    public OrderTestDataBuilder paymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public OrderTestDataBuilder shippingCost(Money shippingCost) {
        this.shippingCost = shippingCost;
        return this;
    }

    public OrderTestDataBuilder expectedDeliveryDate(LocalDate expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
        return this;
    }

    public OrderTestDataBuilder shippingInfo(ShippingInfo shippingInfo) {
        this.shippingInfo = shippingInfo;
        return this;
    }

    public OrderTestDataBuilder billingInfo(BillingInfo billingInfo) {
        this.billingInfo = billingInfo;
        return this;
    }

    public OrderTestDataBuilder status(OrderStatus status) {
        this.status = status;
        return this;
    }

    public OrderTestDataBuilder withItems(boolean withItems) {
        this.withItems = withItems;
        return this;
    }
}
