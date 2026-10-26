package contracts.shoppingcart

import org.springframework.cloud.contract.spec.Contract

Contract.make {
    request {
        method GET()
        headers {
            accept 'application/json'
        }
        url('/api/v1/shopping-carts/c5f7ea67-cf06-4574-aaeb-b3df0ad4baa4')
    }

    response {
        status 404
        body([
                instance: fromRequest().path(),
                type    : '/errors/not-found',
                title   : 'Not found'
        ])
    }
}
