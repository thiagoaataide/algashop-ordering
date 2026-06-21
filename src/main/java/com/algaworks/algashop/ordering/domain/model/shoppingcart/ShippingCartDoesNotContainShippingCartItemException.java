package com.algaworks.algashop.ordering.domain.model.shoppingcart;

import com.algaworks.algashop.ordering.domain.model.DomainExcpetion;

public class ShippingCartDoesNotContainShippingCartItemException extends DomainExcpetion {
    public ShippingCartDoesNotContainShippingCartItemException(ShoppingCartId id, ShoppingCartItemId shoppingCartItemId) {
        super(String.format("ERROR_SHOPPING_CART_DOES_NOT_CONTAIN_SHOPPING_CART_ITEM", id, shoppingCartItemId));
    }
}
