package com.algaworks.algashop.ordering.domain.valueobject;

import com.algaworks.algashop.ordering.domain.validator.FieldValidations;

import java.util.Objects;


public record Email(String value) {

    public Email {
        Objects.requireNonNull(value);
        FieldValidations.requiresValidaEmail(value);

    }

    @Override
    public String toString() {
        return value;
    }
}
