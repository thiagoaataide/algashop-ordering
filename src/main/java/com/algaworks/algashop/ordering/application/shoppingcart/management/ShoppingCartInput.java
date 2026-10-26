package com.algaworks.algashop.ordering.application.shoppingcart.management;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShoppingCartInput {

    @NotNull
    private UUID customerId;
}
