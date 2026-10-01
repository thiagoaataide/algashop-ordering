package com.algaworks.algashop.ordering.domain.model;

public class DomainEntityNotFoundExcpetion extends RuntimeException{

    public DomainEntityNotFoundExcpetion() {
    }

    public DomainEntityNotFoundExcpetion(Throwable cause) {
        super(cause);
    }

    public DomainEntityNotFoundExcpetion(String message) {
        super(message);
    }

    public DomainEntityNotFoundExcpetion(String message, Throwable cause) {
        super(message, cause);
    }
}
