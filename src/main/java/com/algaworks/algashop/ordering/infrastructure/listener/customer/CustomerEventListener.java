package com.algaworks.algashop.ordering.infrastructure.listener.customer;

import com.algaworks.algashop.ordering.application.customer.loyaltypoints.CustomerLoyaltyPointsApplicationService;
import com.algaworks.algashop.ordering.application.customer.notification.CustomerNotificationApplicationService;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerArchivedEvent;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerRegisteredEvent;
import com.algaworks.algashop.ordering.domain.model.order.OrderReadyEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomerEventListener {

    private final CustomerNotificationApplicationService customerNotificationApplicationService;

    private final CustomerLoyaltyPointsApplicationService customerLoyaltyPointsApplicationService;

    @EventListener
    public void listen(CustomerRegisteredEvent event) {
        log.info("Customer registered listen 1: {}", event.customerId());
        customerNotificationApplicationService.notifyNewRegistration(
                new CustomerNotificationApplicationService.NotifyNewRegistrationInput(
                        event.customerId().value(),
                        event.fullname().firstName(),
                        event.email().value()
                        )
        );
    }

    @EventListener
    public void listenSencodery(CustomerRegisteredEvent event) {
        log.info("Customer registered listen 2: {}", event.customerId());
    }

    @EventListener
    public void listen(CustomerArchivedEvent event){
        log.info("Customer archived: {}", event.customerId());
    }

    @EventListener
    public void listen(OrderReadyEvent event){
        customerLoyaltyPointsApplicationService.addLoyaltyPoints(event.customerId().value(),
                event.orderId().toString());

    }
}
