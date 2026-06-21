package com.algaworks.algashop.ordering.domain.model.service;

import com.algaworks.algashop.ordering.domain.model.valueobject.ZipCode;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ShippingCostServiceTestIT {

    @Autowired
    private ShippingCostService shippingCostService;

    @Autowired
    private OriginAddressService originAddressService;

    @Test
    public void shouldCalculate() {
        ZipCode origin = originAddressService.originAddress().zipCode();
        ZipCode destination = new ZipCode("12345");

        ShippingCostService.CalculationResult response = shippingCostService.calculate(new ShippingCostService.CalculationRequest(origin, destination));

        Assertions.assertThat(response.cost()).isNotNull();
        Assertions.assertThat(response.expectedDate()).isNotNull();

    }
}