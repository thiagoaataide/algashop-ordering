package com.algaworks.algashop.ordering.domain.model.exception;

import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartItemId;

public class ShippingCartDoesNotContainShippingCartItemException extends DomainExcpetion{
    public ShippingCartDoesNotContainShippingCartItemException(ShoppingCartId id, ShoppingCartItemId shoppingCartItemId) {
        super(String.format("ERROR_SHOPPING_CART_DOES_NOT_CONTAIN_SHOPPING_CART_ITEM", id, shoppingCartItemId));
    }
}
