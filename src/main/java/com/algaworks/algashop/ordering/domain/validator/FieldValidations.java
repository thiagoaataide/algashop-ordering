package com.algaworks.algashop.ordering.domain.validator;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.Objects;

public class FieldValidations {
    private FieldValidations() {

    }

    public static void requiresValidaEmail(String email){
        requiresValidaEmail(email, null);
    }

    public static void requiresValidaEmail(String email, String errorMessage){
        Objects.requireNonNull(email, errorMessage);
        if(email.isBlank()){
            throw new IllegalArgumentException(errorMessage);
        }

        if(!EmailValidator.getInstance().isValid(email)){
            throw new IllegalArgumentException(errorMessage);
        }
    }


    public static void requiresNonBlank(String value){
        Objects.requireNonNull(value, "");
    }

    public static void requiresNonBlank(String value, String errorMessage){
        Objects.requireNonNull(value);
        if(value.isBlank()){
            throw new IllegalArgumentException(errorMessage);
        }
    }

}
