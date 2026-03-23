package com.algaworks.algashop.ordering.infrastructure.persistence.embeddable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillingEmbeddable {
    private String firstName;
    private String lastName;
    private String document;
    private String phone;
    private String email;
    @Embedded
    private AddressEmbeddable address;
}
