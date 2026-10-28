package com.algaworks.algashop.ordering.contract.base;

import com.algaworks.algashop.ordering.application.checkout.BuyNowApplicationService;
import com.algaworks.algashop.ordering.application.checkout.BuyNowInput;
import com.algaworks.algashop.ordering.application.checkout.CheckoutApplicationService;
import com.algaworks.algashop.ordering.application.checkout.CheckoutInput;
import com.algaworks.algashop.ordering.application.order.query.OrderDetailOutput;
import com.algaworks.algashop.ordering.application.order.query.OrderDetailOutputTestDataBuilder;
import com.algaworks.algashop.ordering.application.order.query.OrderFilter;
import com.algaworks.algashop.ordering.application.order.query.OrderQueryService;
import com.algaworks.algashop.ordering.application.order.query.OrderSummaryOutput;
import com.algaworks.algashop.ordering.domain.model.order.OrderId;
import com.algaworks.algashop.ordering.domain.model.order.OrderNotFoundException;
import com.algaworks.algashop.ordering.presentation.order.OrderController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.List;

@WebMvcTest(controllers = OrderController.class)
public class OrderBase {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private OrderQueryService orderQueryService;

    @MockitoBean
    private BuyNowApplicationService buyNowApplicationService;

    @MockitoBean
    private CheckoutApplicationService checkoutApplicationService;

    public static final String validOrderId = "01226N0640J7Q";
    public static final String notFoundOrderId = "01226N0693HDH";
    public static final String buyNowCreatedOrderId = "01226N0693HDE";
    public static final String checkoutCreatedOrderId = "01226N0693HDF";

    @BeforeEach
    void setUp(){
        RestAssuredMockMvc.mockMvc(
                MockMvcBuilders.webAppContextSetup(context)
                        .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                        .build()
        );

        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();

        Mockito.when(orderQueryService.findById(validOrderId))
                .thenReturn(OrderDetailOutputTestDataBuilder.placedOrder(validOrderId).build());

        Mockito.when(orderQueryService.findById(notFoundOrderId))
                .thenThrow(new OrderNotFoundException());

        Mockito.when(orderQueryService.findById(buyNowCreatedOrderId))
                .thenReturn(OrderDetailOutputTestDataBuilder.placedOrder(buyNowCreatedOrderId)
                        .paymentMethod("GATEWAY_BALANCE")
                        .build());

        Mockito.when(orderQueryService.findById(checkoutCreatedOrderId))
                .thenReturn(OrderDetailOutputTestDataBuilder.placedOrder(checkoutCreatedOrderId)
                        .paymentMethod("GATEWAY_BALANCE")
                        .build());

        Mockito.when(buyNowApplicationService.buyNow(Mockito.any(BuyNowInput.class)))
                .thenReturn(buyNowCreatedOrderId);

        Mockito.when(checkoutApplicationService.checkout(Mockito.any(CheckoutInput.class)))
                .thenReturn(checkoutCreatedOrderId);

        mockFindOrders();
    }

    private void mockFindOrders() {
        Mockito.when(orderQueryService.filter(Mockito.any(OrderFilter.class)))
                .thenAnswer(invocation -> {
                    OrderFilter filter = invocation.getArgument(0);
                    OrderDetailOutput placedDetail =
                            OrderDetailOutputTestDataBuilder.placedOrder(validOrderId).build();

                    OrderSummaryOutput placed = OrderSummaryOutput.builder()
                            .id(placedDetail.getId())
                            .customer(placedDetail.getCustomer())
                            .totalItems(placedDetail.getTotalItems())
                            .totalAmount(placedDetail.getTotalAmount())
                            .placedAt(placedDetail.getPlacedAt())
                            .paidAt(placedDetail.getPaidAt())
                            .canceledAt(placedDetail.getCanceledAt())
                            .readyAt(placedDetail.getReadyAt())
                            .status(placedDetail.getStatus())
                            .paymentMethod(placedDetail.getPaymentMethod())
                            .build();

                    OrderSummaryOutput paid = OrderSummaryOutput.builder()
                            .id(new OrderId().toString())
                            .customer(placedDetail.getCustomer())
                            .totalItems(2)
                            .totalAmount(new BigDecimal("41.98"))
                            .placedAt(OffsetDateTime.now())
                            .paidAt(OffsetDateTime.now())
                            .canceledAt(null)
                            .readyAt(null)
                            .status("PAID")
                            .paymentMethod("GATEWAY_BALANCE")
                            .build();

                    return new PageImpl<>(
                            List.of(placed, paid),
                            PageRequest.of(filter.getPage(), filter.getSize()),
                            2
                    );
                });
    }
}
