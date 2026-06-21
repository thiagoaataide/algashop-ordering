package com.algaworks.algashop.ordering.domain.model.product;

import com.algaworks.algashop.ordering.domain.model.commons.Money;

public class ProductTestDataBuilder {
    public static final ProductId DEFAULT_PRODUCT_ID = new ProductId();

    private ProductTestDataBuilder(){}

    public static Product.ProductBuilder aProduct(){
        return Product.builder()
                .id(DEFAULT_PRODUCT_ID)
                .inStock(true)
                .name(new ProductName("Samsung Galaxy S25 Ultra"))
                .price(new Money("2500"));
    }

    public static Product.ProductBuilder aProductUnavailable(){
        return Product.builder()
                .name(new ProductName("Garrafa Stanley"))
                .price(new Money("250"))
                .inStock(false)
                .id(new ProductId());
    }

    public static Product.ProductBuilder aProductAltRamMemory(){
        return Product.builder()
                .id(new ProductId())
                .inStock(true)
                .name(new ProductName("RAM 8 GB"))
                .price(new Money("1000"));

    }

    public static Product.ProductBuilder aProductAltMousePad(){
        return Product.builder()
                .id(new ProductId())
                .inStock(true)
                .name(new ProductName("Mouse Pad"))
                .price(new Money("100"));

    }
}
