package contracts.order

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method POST()
        headers {
            accept 'application/json'
            contentType 'application/vnd.order-with-product.v1+json'
        }
        urlPath('/api/v1/orders') {
            body([
                    customerId   : value(
                            test('6e148bd5-47f6-4022-b9da-07cfaa294f7a'),
                            stub(anyUuid())
                    ),
                    productId    : value(
                            test('28fcd9fb-4ce7-44d6-9583-14d8b3dc5aff'),
                            stub(anyUuid())
                    ),
                    quantity     : value(
                            test(2),
                            stub(anyNumber())
                    ),
                    paymentMethod: value(
                            test('GATEWAY_BALANCE'),
                            stub(nonBlank())
                    ),
                    shipping     : [
                            recipient: [
                                    firstName: value(test('John'), stub(nonBlank())),
                                    lastName : value(test('Doe'), stub(nonBlank())),
                                    document : value(test('12345'), stub(nonBlank())),
                                    phone    : value(test('5511912341234'), stub(nonBlank()))
                            ],
                            address  : [
                                    street      : value(test('Bourbon Street'), stub(nonBlank())),
                                    number      : value(test('2000'), stub(nonBlank())),
                                    complement  : value(test('apt 122'), stub(optional(nonBlank()))),
                                    neighborhood: value(test('North Ville'), stub(nonBlank())),
                                    city        : value(test('Yostfort'), stub(nonBlank())),
                                    state       : value(test('South Carolina'), stub(nonBlank())),
                                    zipCode     : value(test('12321'), stub(nonBlank()))
                            ]
                    ],
                    billing      : [
                            firstName: value(test('John'), stub(nonBlank())),
                            lastName : value(test('Doe'), stub(nonBlank())),
                            document : value(test('12345'), stub(nonBlank())),
                            email    : value(test('johndoe@email.com'), stub(nonBlank())),
                            phone    : value(test('5511912341234'), stub(nonBlank())),
                            address  : [
                                    street      : value(test('Bourbon Street'), stub(nonBlank())),
                                    number      : value(test('2000'), stub(nonBlank())),
                                    complement  : value(test('apt 122'), stub(optional(nonBlank()))),
                                    neighborhood: value(test('North Ville'), stub(nonBlank())),
                                    city        : value(test('Yostfort'), stub(nonBlank())),
                                    state       : value(test('South Carolina'), stub(nonBlank())),
                                    zipCode     : value(test('12321'), stub(nonBlank()))
                            ]
                    ]
            ])
        }
    }

    response {
        status 201
        headers {
            contentType 'application/json'
        }
        body([
                id           : '01226N0693HDE',
                customer     : [
                        id       : anyUuid(),
                        firstName: 'John',
                        lastName : 'Doe',
                        document : '12345',
                        email    : 'johndoe@email.com',
                        phone    : '1191234564'
                ],
                totalItems   : 2,
                totalAmount  : 41.98,
                placedAt     : anyIso8601WithOffset(),
                canceledAt   : null,
                paidAt       : null,
                readyAt      : null,
                status       : 'PLACED',
                paymentMethod: fromRequest().body('$.paymentMethod'),
                shipping     : [
                        cost        : 20.5,
                        expectedDate: anyDate(),
                        recipient   : [
                                firstName: fromRequest().body('$.shipping.recipient.firstName'),
                                lastName : fromRequest().body('$.shipping.recipient.lastName'),
                                document : fromRequest().body('$.shipping.recipient.document'),
                                phone    : fromRequest().body('$.shipping.recipient.phone')
                        ],
                        address     : [
                                street      : fromRequest().body('$.shipping.address.street'),
                                number      : fromRequest().body('$.shipping.address.number'),
                                complement  : fromRequest().body('$.shipping.address.complement'),
                                neighborhood: fromRequest().body('$.shipping.address.neighborhood'),
                                city        : fromRequest().body('$.shipping.address.city'),
                                state       : fromRequest().body('$.shipping.address.state'),
                                zipCode     : fromRequest().body('$.shipping.address.zipCode')
                        ]
                ],
                billing      : [
                        firstName: fromRequest().body('$.billing.firstName'),
                        lastName : fromRequest().body('$.billing.lastName'),
                        document : fromRequest().body('$.billing.document'),
                        phone    : fromRequest().body('$.billing.phone'),
                        address  : [
                                street      : fromRequest().body('$.billing.address.street'),
                                number      : fromRequest().body('$.billing.address.number'),
                                complement  : fromRequest().body('$.billing.address.complement'),
                                neighborhood: fromRequest().body('$.billing.address.neighborhood'),
                                city        : fromRequest().body('$.billing.address.city'),
                                state       : fromRequest().body('$.billing.address.state'),
                                zipCode     : fromRequest().body('$.billing.address.zipCode')
                        ]
                ],
                items        : [
                        [
                                id         : anyNonBlankString(),
                                productId  : anyUuid(),
                                orderId    : '01226N0693HDE',
                                price      : 19.99,
                                productName: 'Notebook Dive Gamer X11',
                                quantity   : fromRequest().body('$.quantity'),
                                totalAmount: 19.99
                        ]
                ]
        ])
    }
}
