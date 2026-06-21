package com.algaworks.algashop.ordering.infrastructure.persistence.customer;

import com.algaworks.algashop.ordering.domain.model.commons.*;
import com.algaworks.algashop.ordering.domain.model.customer.BirthDate;
import com.algaworks.algashop.ordering.domain.model.customer.LoyaltyPoints;
import com.algaworks.algashop.ordering.domain.model.customer.Customer;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerId;
import com.algaworks.algashop.ordering.infrastructure.persistence.commons.AddressEmbeddable;
import org.springframework.stereotype.Component;

@Component
public class CustomerPersistenceEntityDisassembler {

    public Customer toDomainEntity(CustomerPersistenceEntity entity){
        return  Customer.existing()
                .id(new CustomerId(entity.getId()))
                .birthDate(entity.getBirthDate() != null ? new BirthDate(entity.getBirthDate()) : null)
                .email(new Email(entity.getEmail()))
                .document(new Document(entity.getDocument()))
                .phone(new Phone(entity.getPhone()))
                .archived(entity.getArchived())
                .archivedAt(entity.getArchivedAt())
                .promotionNotificationsAllowed(entity.getPromotionNotificationsAllowed())
                .loyaltyPoints(new LoyaltyPoints(entity.getLoyaltyPoints()))
                .fullName(new FullName(entity.getFirstName(), entity.getLastName()))
                .address(toAddressValueobject(entity.getAddress()))
                .version(entity.getVersion())
                .registeredAt(entity.getRegisteredAt())
                .build();
    }

    private Address toAddressValueobject(AddressEmbeddable addressEmbeddable){
        return Address.builder()
                .city(addressEmbeddable.getCity())
                .state(addressEmbeddable.getState())
                .street(addressEmbeddable.getStreet())
                .neighborhood(addressEmbeddable.getNeighborhood())
                .complement(addressEmbeddable.getComplement())
                .zipCode(new ZipCode(addressEmbeddable.getZipCode()))
                .number(addressEmbeddable.getNumber())
                .build();
    }

}
