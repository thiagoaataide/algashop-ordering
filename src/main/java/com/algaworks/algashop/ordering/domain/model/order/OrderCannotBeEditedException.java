package com.algaworks.algashop.ordering.domain.model.order;

import com.algaworks.algashop.ordering.domain.model.DomainExcpetion;
import com.algaworks.algashop.ordering.domain.model.ErrorMessages;

public class OrderCannotBeEditedException extends DomainExcpetion {

    public OrderCannotBeEditedException(OrderId id, OrderStatus status) {
        super(String.format(ErrorMessages.ERROR_ORDER_CANNOT_BE_EDITED, id, status));
    }
}
