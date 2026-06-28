package com.algaworks.algashop.ordering.aplication.checkout;

import com.algaworks.algashop.ordering.aplication.commons.AddressData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShippingInput {
    private RecipientData recipient;
    private AddressData address;

}
