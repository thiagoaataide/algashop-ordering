package com.algaworks.algashop.ordering.contract.base;

import com.algaworks.algashop.ordering.application.shoppingcart.management.ShoppingCartItemInput;
import com.algaworks.algashop.ordering.application.shoppingcart.management.ShoppingCartManagementApplicationService;
import com.algaworks.algashop.ordering.application.shoppingcart.query.ShoppingCartOutputTestDataBuilder;
import com.algaworks.algashop.ordering.application.shoppingcart.query.ShoppingCartQueryService;
import com.algaworks.algashop.ordering.domain.model.shoppingcart.ShoppingCartNotFoundException;
import com.algaworks.algashop.ordering.presentation.ApiExceptionHandler;
import com.algaworks.algashop.ordering.presentation.shoppingcart.ShoppingCartController;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@WebMvcTest(controllers = ShoppingCartController.class)
@Import(ApiExceptionHandler.class)
public class ShoppingcartBase {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private ShoppingCartQueryService shoppingCartQueryService;

    @MockitoBean
    private ShoppingCartManagementApplicationService shoppingCartManagementApplicationService;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(
                MockMvcBuilders.webAppContextSetup(context)
                        .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                        .build()
        );

        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();

        mockQueries();
        mockCreate();
        mockWriteOperations();
    }

    private void mockQueries() {
        Mockito.when(shoppingCartQueryService.findById(ShoppingCartOutputTestDataBuilder.VALID_SHOPPING_CART_ID))
                .thenReturn(ShoppingCartOutputTestDataBuilder.cartWithItems());

        Mockito.when(shoppingCartQueryService.findById(ShoppingCartOutputTestDataBuilder.NOT_FOUND_SHOPPING_CART_ID))
                .thenThrow(new ShoppingCartNotFoundException());
    }

    private void mockCreate() {
        Mockito.when(shoppingCartManagementApplicationService.createNew(Mockito.any(UUID.class)))
                .thenAnswer(invocation -> {
                    UUID customerId = invocation.getArgument(0);
                    Mockito.when(shoppingCartQueryService.findById(ShoppingCartOutputTestDataBuilder.CREATED_SHOPPING_CART_ID))
                            .thenReturn(ShoppingCartOutputTestDataBuilder.emptyCart(
                                    ShoppingCartOutputTestDataBuilder.CREATED_SHOPPING_CART_ID,
                                    customerId
                            ));
                    return ShoppingCartOutputTestDataBuilder.CREATED_SHOPPING_CART_ID;
                });
    }

    private void mockWriteOperations() {
        Mockito.doNothing()
                .when(shoppingCartManagementApplicationService)
                .delete(ShoppingCartOutputTestDataBuilder.VALID_SHOPPING_CART_ID);

        Mockito.doNothing()
                .when(shoppingCartManagementApplicationService)
                .empty(ShoppingCartOutputTestDataBuilder.VALID_SHOPPING_CART_ID);

        Mockito.doNothing()
                .when(shoppingCartManagementApplicationService)
                .addItem(Mockito.any(ShoppingCartItemInput.class));

        Mockito.doNothing()
                .when(shoppingCartManagementApplicationService)
                .removeItem(
                        ShoppingCartOutputTestDataBuilder.VALID_SHOPPING_CART_ID,
                        ShoppingCartOutputTestDataBuilder.DEFAULT_ITEM_ID
                );
    }
}
