package com.algaworks.algashop.ordering.domain.model;

public interface Specification<T> {
    boolean isSatisfiiedBy(T t);

    default Specification<T> and(Specification<T> other) {
        return t-> this.isSatisfiiedBy(t) && other.isSatisfiiedBy(t);
    }

    default Specification<T> or(Specification<T> other) {
        return t-> this.isSatisfiiedBy(t) || other.isSatisfiiedBy(t);
    }

    default Specification<T> andNot(Specification<T> other) {
        return t ->  this.isSatisfiiedBy(t) && !other.isSatisfiiedBy(t);
    }

    default Specification<T> not(Specification<T> other) {
        return t-> !this.isSatisfiiedBy(t);
    }
}
