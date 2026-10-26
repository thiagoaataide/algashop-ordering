package contracts.shoppingcart

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        headers {
            accept 'application/json'
        }
        url('/api/v1/shopping-carts/e021ed5e-324d-4322-ae47-c46c95382628')
    }

    response {
        status 200
        headers {
            contentType 'application/json'
        }
        body([
                id         : 'e021ed5e-324d-4322-ae47-c46c95382628',
                customerId : '6e148bd5-47f6-4022-b9da-07cfaa294f7a',
                totalItems : 3,
                totalAmount: 3539.98,
                items      : [
                        [
                                id         : 'fffe6ec2-7103-48b3-8e4f-3b58e43fb75a',
                                productId  : '28fcd9fb-4ce7-44d6-9583-14d8b3dc5aff',
                                name       : 'Notebook Dive Gamer X11',
                                price      : 19.99,
                                quantity   : 2,
                                totalAmount: 39.98,
                                available  : true
                        ],
                        [
                                id         : '9676e7f6-1080-40fd-b487-0ca92dacfb83',
                                productId  : '46b35641-b0d3-45d9-8ef3-793f9ce89c25',
                                name       : 'Desktop I9000',
                                price      : 3500.00,
                                quantity   : 1,
                                totalAmount: 3500.00,
                                available  : false
                        ]
                ]
        ])
    }
}
