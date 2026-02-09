package com.algaworks.algashop.ordering.domain.exception;

import com.algaworks.algashop.ordering.domain.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.valueobject.id.ShoppingCartItemId;

public class ShippingCartDoesNotContainShippingCartItemException extends DomainExcpetion{
    public ShippingCartDoesNotContainShippingCartItemException(ShoppingCartId id, ShoppingCartItemId shoppingCartItemId) {
        super(String.format("ERROR_SHOPPING_CART_DOES_NOT_CONTAIN_SHOPPING_CART_ITEM", id, shoppingCartItemId));
    }
}
