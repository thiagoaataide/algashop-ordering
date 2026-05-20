package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.model.valueobject.*;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.CustomerId;

import java.time.LocalDate;

import static com.algaworks.algashop.ordering.domain.model.entity.CustomerTestDataBuilder.DEFAULT_CUSTOMER_ID;

public class OrderTestDataBuilder {

    private CustomerId customerId = DEFAULT_CUSTOMER_ID;
    private PaymentMethod paymentMethod = PaymentMethod.GATEWAY_BALANCE;
    private Shipping shipping = aShipping();
    private Billing billing = aBilling();

    private OrderStatus status = OrderStatus.DRAFT;

    private boolean withItems = true;

    private OrderTestDataBuilder() {}

    public static OrderTestDataBuilder anOrder(){
        return new OrderTestDataBuilder();
    }

    public Order build(){
        Order order = Order.draft(customerId);
        order.changeShipping(shipping);
        order.changeBilling(billing);
        order.changePaymentMethod(paymentMethod);

        if(withItems){
            order.addItem(ProductTestDataBuilder.aProduct().build(), new Quantity(1));
            order.addItem(ProductTestDataBuilder.aProductAltRamMemory().build(), new Quantity(1));
            order.addItem(ProductTestDataBuilder.aProductAltMousePad().build(), new Quantity(2));
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

    public static Shipping aShipping() {
        return Shipping.builder()
                .recipient(Recipient.builder()
                        .fullName(new FullName("Thiago", "Alberto"))
                        .document(new Document("083.388.654-19"))
                        .phone(new Phone("31 99195-3046"))
                        .build())
                .address(anAddress())
                .cost(new Money("10.00"))
                .expectedDate(LocalDate.now().plusWeeks(1))
                .build();
    }

    public static Shipping aShippingAlt() {
        return Shipping.builder()
                .recipient(Recipient.builder()
                        .fullName(new FullName("Fernando", "Saliba"))
                        .document(new Document("123.456.789-19"))
                        .phone(new Phone("31 99195-3046"))
                        .build())
                .address(anAddressAlt())
                .cost(new Money("20.00"))
                .expectedDate(LocalDate.now().plusWeeks(2))
                .build();
    }

    public static Billing aBilling() {
        return Billing.builder()
                .address(anAddress())
                .document(new Document("083.388.654-19"))
                .phone(new Phone("31 99195-3046"))
                .fullName(new FullName("Thiago", "Alberto"))
                .email(new Email("thiagoaataide@gmail.com"))
                .build();
    }

    public static Address anAddress() {
        return Address.builder()
                .street("Av. Joaquim José Diniz")
                .number("20")
                .neighborhood("Fernão Dias")
                .complement("Torre 03 Apto 801")
                .city("Belo Horizonte")
                .state("Minas Gerais")
                .zipCode(new ZipCode("31910"))
                .build();
    }

    public static Address anAddressAlt() {
        return Address.builder()
                .street("Av. Del Rey")
                .number("111")
                .neighborhood("Caiçaras")
                .complement("Bloco 4b Sala 403")
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

    public OrderTestDataBuilder shipping(Shipping shipping) {
        this.shipping = shipping;
        return this;
    }

    public OrderTestDataBuilder billing(Billing billing) {
        this.billing = billing;
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
