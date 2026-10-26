package contracts.shoppingcart

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method POST()
        headers {
            accept 'application/json'
            contentType 'application/json'
        }
        urlPath('/api/v1/shopping-carts') {
            body([
                    customerId: value(
                            test('6e148bd5-47f6-4022-b9da-07cfaa294f7a'),
                            stub(anyUuid())
                    )
            ])
        }
    }

    response {
        status 201
        headers {
            contentType 'application/json'
        }
        body([
                id         : 'b55c3717-506c-4615-89db-b7f1c0c8e354',
                customerId : fromRequest().body('$.customerId'),
                totalItems : 0,
                totalAmount: 0,
                items      : []
        ])
    }
}
