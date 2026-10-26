package contracts.shoppingcart

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method POST()
        headers {
            accept 'application/json'
            contentType 'application/json'
        }
        urlPath('/api/v1/shopping-carts/e021ed5e-324d-4322-ae47-c46c95382628/items') {
            body([
                    shoppingCartId: 'e021ed5e-324d-4322-ae47-c46c95382628',
                    productId     : value(
                            test('28fcd9fb-4ce7-44d6-9583-14d8b3dc5aff'),
                            stub(anyUuid())
                    ),
                    quantity      : value(
                            test(2),
                            stub(anyNumber())
                    )
            ])
        }
    }

    response {
        status 204
    }
}
