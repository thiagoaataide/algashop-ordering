package com.algaworks.algashop.ordering.domain.model.entity;

import com.algaworks.algashop.ordering.domain.model.exception.ShoppingCartItemIncompatibleProductException;
import com.algaworks.algashop.ordering.domain.model.valueobject.Money;
import com.algaworks.algashop.ordering.domain.model.valueobject.Product;
import com.algaworks.algashop.ordering.domain.model.valueobject.ProductName;
import com.algaworks.algashop.ordering.domain.model.valueobject.Quantity;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ProductId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartId;
import com.algaworks.algashop.ordering.domain.model.valueobject.id.ShoppingCartItemId;
import lombok.Builder;

import java.util.Objects;

public class ShoppingCartItem {
    private ShoppingCartItemId id;
    private ShoppingCartId shoppingCartId;
    private ProductId productId;
    private ProductName name;
    private Money price;
    private Quantity quantity;
    private Money totalAmount;
    private Boolean available;

    @Builder(builderClassName = "ExistingShoppingCartItem", builderMethodName = "existing")
    public ShoppingCartItem(ShoppingCartItemId id, ShoppingCartId shoppingCartId, ProductId productId, ProductName productName,
                            Money price, Quantity quantity, Boolean available, Money totalAmount) {
        this.setId(id);
        this.setShoppingCartId(shoppingCartId);
        this.setProductId(productId);
        this.setProductName(name);
        this.setPrice(price);
        this.setQuantity(quantity);
        this.setTotalAmount(totalAmount);
        this.setAvailable(available);
    }

    @Builder(builderClassName = "BrandNewShoppingCartItem", builderMethodName = "brandNew")
    public ShoppingCartItem (ShoppingCartId shoppingCartId, ProductId productId, ProductName productName,
                                           Money price, Quantity quantity, Boolean available) {
        this(new ShoppingCartItemId(), shoppingCartId, productId, productName, price, quantity, available, Money.ZERO);
        this.recalculateTotals();
    }


    void refresh(Product product) {
        Objects.requireNonNull(product);
        Objects.requireNonNull(product.id());

        if (!product.id().equals(this.productId())) {
            throw new ShoppingCartItemIncompatibleProductException(this.id(), this.productId());
        }

        this.setPrice(product.price());
        this.setAvailable(product.inStock());
        this.setProductName(product.name());
        this.recalculateTotals();
    }

    void changeQuantity(Quantity quantity) {
        Objects.requireNonNull(quantity);
        this.setQuantity(quantity);
        this.recalculateTotals();
    }

    public Boolean isAvailable() {
        return available;
    }

    private void recalculateTotals() {
        this.setTotalAmount(price.multiply(quantity));
    }


    private void setId(ShoppingCartItemId id) {
        this.id = id;
    }

    private void setShoppingCartId(ShoppingCartId shoppingCartId) {
        this.shoppingCartId = shoppingCartId;
    }

    private void setProductId(ProductId productId) {
        this.productId = productId;
    }

    private void setProductName(ProductName name) {
        this.name = name;
    }

    private void setPrice(Money price) {
        this.price = price;
    }

    private void setQuantity(Quantity quantity) {
        this.quantity = quantity;
    }

    private void setTotalAmount(Money totalAmount) {
        this.totalAmount = totalAmount;
    }

    private void setAvailable(Boolean available) {
        this.available = available;
    }

    public ShoppingCartItemId id() {
        return id;
    }

    public ShoppingCartId shoppingCartId() {
        return shoppingCartId;
    }

    public ProductId productId() {
        return productId;
    }

    public ProductName name() {
        return name;
    }

    public Money price() {
        return price;
    }

    public Quantity quantity() {
        return quantity;
    }

    public Money totalAmount() {
        return totalAmount;
    }

    public Boolean available() {
        return available;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ShoppingCartItem that = (ShoppingCartItem) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
