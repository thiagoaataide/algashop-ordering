package com.algaworks.algashop.ordering.infrastructure.persistence.repository;

import com.algaworks.algashop.ordering.infrastructure.persistence.entity.OrderPersistenceEntity;
import org.springframework.data.repository.Repository;

public interface OrdersPersistenceEntityRepository extends Repository<OrderPersistenceEntity, Long> {

}
