package com.algaworks.algashop.ordering.domain.model.order;

import com.algaworks.algashop.ordering.domain.model.Specification;
import com.algaworks.algashop.ordering.domain.model.customer.Customer;
import com.algaworks.algashop.ordering.domain.model.customer.LoyaltyPoints;

public class CustomerHaveFreeShippingSpecification  implements Specification<Customer> {

    private final CustomerHasEnoughLoyaltyPointsSpecification hasEnoughBasicLoyaltyPoints;
    private final CustomerHasEnoughLoyaltyPointsSpecification hasEnoughPremiumLoyaltyPoints;
    private final CustomerHasOrderedEnoughAtYearSpecification hasOrderedEnoughAtYear;


    public CustomerHaveFreeShippingSpecification(Orders orders,
                                                 LoyaltyPoints basicLoyaltyPoints,
                                                 long salesQuantityForFreeShipping,
                                                 LoyaltyPoints premiumLoyaltyPoints) {
        this.hasOrderedEnoughAtYear = new CustomerHasOrderedEnoughAtYearSpecification(orders, salesQuantityForFreeShipping);

        this.hasEnoughBasicLoyaltyPoints = new CustomerHasEnoughLoyaltyPointsSpecification(basicLoyaltyPoints);

        this.hasEnoughPremiumLoyaltyPoints = new CustomerHasEnoughLoyaltyPointsSpecification(premiumLoyaltyPoints);

    }

    @Override
    public boolean isSatisfiiedBy(Customer customer) {

        return hasEnoughBasicLoyaltyPoints.
                and(hasOrderedEnoughAtYear)
                .or(hasEnoughPremiumLoyaltyPoints)
                .isSatisfiiedBy(customer);

//        return customer.loyaltyPoints().compareTo(new LoyaltyPoints(minPointsForFreeShippingRule1)) >= 0 &&
//                orders.salesQuantityByCustomerInYear(customer.id(), Year.now()) >= salesQuantityForFreeShipping
//                || customer.loyaltyPoints().compareTo(new LoyaltyPoints(minPointsForFreeShippingRule2)) >= 0;
    }
}
