package com.algaworks.algashop.ordering.infrastructure.listener.shoppingcart;

import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartCreatedEvent;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartEmptiedEvent;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartItemAddedEvent;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartItemRemovedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ShoppingCartEventListener {

    @EventListener
    public void listen(ShoppingCartCreatedEvent event){

    }

    @EventListener
    public void listen(ShoppingCartItemAddedEvent event){
    }

    @EventListener
    public void listen(ShoppingCartItemRemovedEvent event){

    }

    @EventListener
    public void listen(ShoppingCartEmptiedEvent event){

    }

}
