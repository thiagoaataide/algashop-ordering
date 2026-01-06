package com.algaworks.algashop.ordering.domain.exception;

public class DomainExcpetion extends RuntimeException{
    public DomainExcpetion(String message) {
        super(message);
    }

    public DomainExcpetion(String message, Throwable cause) {
        super(message, cause);
    }
}
