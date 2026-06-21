package com.algaworks.algashop.ordering.domain.model.product;

import com.algaworks.algashop.ordering.domain.model.DomainExcpetion;
import com.algaworks.algashop.ordering.domain.model.ErrorMessages;

public class ProductOutOfStockException extends DomainExcpetion {
    public ProductOutOfStockException(ProductId id) {
        super(String.format(ErrorMessages.ERROR_PRODUCT_IS_OUT_OF_STOCK, id));
    }
}
