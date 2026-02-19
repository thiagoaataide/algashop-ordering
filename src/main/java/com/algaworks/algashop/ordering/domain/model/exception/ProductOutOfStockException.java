package com.algaworks.algashop.ordering.domain.model.exception;

import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;

public class ProductOutOfStockException extends DomainExcpetion {
    public ProductOutOfStockException(ProductId id) {
        super(String.format(ErrorMessages.ERROR_PRODUCT_IS_OUT_OF_STOCK, id));
    }
}
