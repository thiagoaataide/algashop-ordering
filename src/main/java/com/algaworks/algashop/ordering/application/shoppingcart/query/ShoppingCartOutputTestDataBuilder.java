package com.algaworks.algashop.ordering.application.shoppingcart.query;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class ShoppingCartOutputTestDataBuilder {

    public static final UUID VALID_SHOPPING_CART_ID =
            UUID.fromString("e021ed5e-324d-4322-ae47-c46c95382628");
    public static final UUID CREATED_SHOPPING_CART_ID =
            UUID.fromString("b55c3717-506c-4615-89db-b7f1c0c8e354");
    public static final UUID NOT_FOUND_SHOPPING_CART_ID =
            UUID.fromString("c5f7ea67-cf06-4574-aaeb-b3df0ad4baa4");
    public static final UUID DEFAULT_CUSTOMER_ID =
            UUID.fromString("6e148bd5-47f6-4022-b9da-07cfaa294f7a");
    public static final UUID DEFAULT_ITEM_ID =
            UUID.fromString("fffe6ec2-7103-48b3-8e4f-3b58e43fb75a");
    public static final UUID DEFAULT_PRODUCT_ID =
            UUID.fromString("28fcd9fb-4ce7-44d6-9583-14d8b3dc5aff");
    public static final UUID ALT_PRODUCT_ID =
            UUID.fromString("46b35641-b0d3-45d9-8ef3-793f9ce89c25");

    public static ShoppingCartOutput emptyCart(UUID cartId, UUID customerId) {
        ShoppingCartOutput output = new ShoppingCartOutput();
        output.setId(cartId);
        output.setCustomerId(customerId);
        output.setTotalItems(0);
        output.setTotalAmount(BigDecimal.ZERO);
        output.setItems(List.of());
        return output;
    }

    public static ShoppingCartOutput cartWithItems() {
        ShoppingCartItemOutput item1 = new ShoppingCartItemOutput();
        item1.setId(DEFAULT_ITEM_ID);
        item1.setProductId(DEFAULT_PRODUCT_ID);
        item1.setName("Notebook Dive Gamer X11");
        item1.setPrice(new BigDecimal("19.99"));
        item1.setQuantity(2);
        item1.setTotalAmount(new BigDecimal("39.98"));
        item1.setAvailable(true);

        ShoppingCartItemOutput item2 = new ShoppingCartItemOutput();
        item2.setId(UUID.fromString("9676e7f6-1080-40fd-b487-0ca92dacfb83"));
        item2.setProductId(ALT_PRODUCT_ID);
        item2.setName("Desktop I9000");
        item2.setPrice(new BigDecimal("3500.00"));
        item2.setQuantity(1);
        item2.setTotalAmount(new BigDecimal("3500.00"));
        item2.setAvailable(false);

        ShoppingCartOutput output = new ShoppingCartOutput();
        output.setId(VALID_SHOPPING_CART_ID);
        output.setCustomerId(DEFAULT_CUSTOMER_ID);
        output.setTotalItems(3);
        output.setTotalAmount(new BigDecimal("3539.98"));
        output.setItems(List.of(item1, item2));
        return output;
    }
}
