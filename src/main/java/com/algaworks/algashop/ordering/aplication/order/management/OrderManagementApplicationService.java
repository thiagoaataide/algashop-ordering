package com.algaworks.algashop.ordering.aplication.order.management;

import com.algaworks.algashop.ordering.domain.model.order.Order;
import com.algaworks.algashop.ordering.domain.model.order.OrderId;
import com.algaworks.algashop.ordering.domain.model.order.OrderNotFoundException;
import com.algaworks.algashop.ordering.domain.model.order.Orders;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderManagementApplicationService {

    private final Orders orders;

    @Transactional
    public void cancel(String rawOrderId){
        Order order = orders.ofId(new OrderId(rawOrderId)).orElseThrow(() -> new OrderNotFoundException());
        order.cancel();
        orders.add(order);
    }

    @Transactional
    public void markAsPaid(String rawOrderId){
        Order order = orders.ofId(new OrderId(rawOrderId)).orElseThrow(() -> new OrderNotFoundException());
        order.markAsPaid();
        orders.add(order);
    }

    @Transactional
    public void markAsReady(String rawOrderId){
        Order order = orders.ofId(new OrderId(rawOrderId)).orElseThrow(() -> new OrderNotFoundException());
        order.markAsReady();
        orders.add(order);
    }
}
