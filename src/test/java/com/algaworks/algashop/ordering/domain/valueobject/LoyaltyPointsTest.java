package com.algaworks.algashop.ordering.domain.valueobject;



import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class LoyaltyPointsTest {

    @Test
    void shouldGenerateWithValue(){
        LoyaltyPoints loyaltyPoints = new LoyaltyPoints(10);
        Assertions.assertThat(loyaltyPoints.value()).isEqualTo(10);
    }

    @Test
    void shouldGenerateAddValue(){
        LoyaltyPoints loyaltyPoints = new LoyaltyPoints(10);

        Assertions.assertThat(loyaltyPoints.add(40).value()).isEqualTo(50);
    }

    @Test
    void shouldNotAddValue(){
        LoyaltyPoints loyaltyPoints = new LoyaltyPoints(50);

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->loyaltyPoints.add(-5));

        Assertions.assertThat(loyaltyPoints.value()).isEqualTo(50);
    }

    @Test
    void shouldNotAddValueZero(){
        LoyaltyPoints loyaltyPoints = new LoyaltyPoints(50);

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()->loyaltyPoints.add(0));

        Assertions.assertThat(loyaltyPoints.value()).isEqualTo(50);
    }

}