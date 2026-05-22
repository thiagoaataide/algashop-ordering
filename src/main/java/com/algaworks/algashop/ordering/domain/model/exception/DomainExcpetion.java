package com.algaworks.algashop.ordering.domain.model.exception;

public class DomainExcpetion extends RuntimeException{

    public DomainExcpetion() {
    }

    public DomainExcpetion(Throwable cause) {
        super(cause);
    }

    public DomainExcpetion(String message) {
        super(message);
    }

    public DomainExcpetion(String message, Throwable cause) {
        super(message, cause);
    }
}
