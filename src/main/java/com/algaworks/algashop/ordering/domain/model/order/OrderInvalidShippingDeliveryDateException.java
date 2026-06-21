package com.algaworks.algashop.ordering.domain.model.order;

import com.algaworks.algashop.ordering.domain.model.DomainExcpetion;
import com.algaworks.algashop.ordering.domain.model.ErrorMessages;

public class OrderInvalidShippingDeliveryDateException extends DomainExcpetion {
    public OrderInvalidShippingDeliveryDateException(OrderId id) {
        super(String.format(ErrorMessages.ERROR_ORDER_DELIVERY_DATE_CANNOT_BE_IN_THE_PAST, id));
    }
}
