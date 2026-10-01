package com.algaworks.algashop.ordering.presentation;

import com.algaworks.algashop.ordering.application.commons.AddressData;
import com.algaworks.algashop.ordering.application.customer.management.CustomerInput;
import com.algaworks.algashop.ordering.application.customer.management.CustomerManagementApplicationService;
import com.algaworks.algashop.ordering.application.customer.query.*;
import com.algaworks.algashop.ordering.domain.model.DomainExcpetion;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerEmailIsInUseException;
import com.algaworks.algashop.ordering.domain.model.customer.CustomerNotFoundException;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@WebMvcTest(controllers = CustomerController.class)
class CustomerControllerContractTest {

    @Autowired
    private WebApplicationContext context;

    @MockitoBean
    private CustomerManagementApplicationService customerManagementApplicationService;

    @MockitoBean
    private CustomerQueryService customerQueryService;

    @BeforeEach
    public void setupAll() {
        RestAssuredMockMvc.mockMvc(MockMvcBuilders.webAppContextSetup(context)
                .defaultResponseCharacterEncoding(StandardCharsets.UTF_8)
                .build()
        );

        RestAssuredMockMvc.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

    @Test
    public void createCustomerContract() {

        CustomerOutput customerOutput = CustomerOutputTestDataBuilder.existing().build();

        UUID customerId = UUID.randomUUID();

        Mockito.when(customerManagementApplicationService.create(Mockito.any(CustomerInput.class)))
                .thenReturn(customerId);

        Mockito.when(customerQueryService.findById(Mockito.any(UUID.class)))
                .thenReturn(customerOutput);

        String jsonInput = """
                {
                  "firstName": "John",
                  "lastName": "Doe",
                  "email": "john.doe@gmail.com",
                  "document": "12345",
                  "phone": "31991953046",
                  "birthDate": "1989-07-25",
                  "promotionNotificationsAllowed": false,
                  "address": {
                    "street": "Bourbon Street",
                    "number": "2000",
                    "complement": "apt 122",
                    "neighborhood": "North Ville",
                    "city": "Yostfort",
                    "state": "South Carolina",
                    "zipCode": "12321"
                  }
                }
                """;


        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonInput)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                    .post("/api/v1/customers")
                .then()
                    .assertThat()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .statusCode(HttpStatus.CREATED.value())
                    .headers("Location", Matchers.containsString("/api/v1/customers/" + customerId))
                    .body(
                        "id", Matchers.notNullValue(),
                        "registeredAt", Matchers.notNullValue(),
                        "firstName", Matchers.is("John"),
                        "lastName", Matchers.is("Doe"),
                        "email", Matchers.is("john.doe@gmail.com"),
                        "document", Matchers.is("12345"),
                        "phone", Matchers.is("31991953046"),
                        "birthDate", Matchers.is("1989-07-25"),
                        "promotionNotificationsAllowed", Matchers.is(false),
                        "loyaltyPoints", Matchers.is(0),
                        "address.street", Matchers.is("Bourbon Street"),
                        "address.city", Matchers.is("Yostfort"),
                        "address.state", Matchers.is("South Carolina"),
                        "address.zipCode", Matchers.is("12321"),
                        "address.neighborhood", Matchers.is("North Ville"),
                        "address.number", Matchers.is("2000"),
                        "address.complement", Matchers.is("apt 122")
                );
    }

    @Test
    public void createCustomerError400Contract() {
        String jsonInput = """
                {
                  "firstName": "",
                  "lastName": "",
                  "email": "john.doe@gmail.com",
                  "document": "12345",
                  "phone": "31991953046",
                  "birthDate": "1989-07-25",
                  "promotionNotificationsAllowed": false,
                  "address": {
                    "street": "Bourbon Street",
                    "number": "2000",
                    "complement": "apt 122",
                    "neighborhood": "North Ville",
                    "city": "Yostfort",
                    "state": "South Carolina",
                    "zipCode": "12321"
                  }
                }
                """;

        RestAssuredMockMvc.given().
                accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).
                body(jsonInput).
                when()
                .post("/api/v1/customers").
                then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .body(
                        "status", Matchers.is(HttpStatus.BAD_REQUEST.value()),
                        "type", Matchers.is("/errors/invalid-fields"),
                        "title", Matchers.notNullValue(),
                        "detail", Matchers.notNullValue(),
                        "instance", Matchers.notNullValue(),
                        "fields", Matchers.notNullValue()
                );

    }

    @Test
    public void findCustomersContract() {
        int sizeLimit = 5;
        int pageNumber = 0;

        CustomerSummaryOutput customer1 = CustomerSummaryOutputTestDataBuilder.existing().build();
        CustomerSummaryOutput customer2 = CustomerSummaryOutputTestDataBuilder.existingAlt1().build();

        Mockito.when(customerQueryService.filter(Mockito.any(CustomerFilter.class)))
                .thenReturn(new PageImpl<>(List.of(customer1, customer2)));


        RestAssuredMockMvc
                .given()
                    .accept(MediaType.APPLICATION_JSON)
                    .queryParam("size", sizeLimit)
                    .queryParam("page", pageNumber)
                .when()
                    .get("/api/v1/customers")
                .then()
                    .assertThat()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .statusCode(HttpStatus.OK.value())
                .body(
                        "number", Matchers.equalTo(pageNumber),
                        "size", Matchers.lessThan(sizeLimit),
                        "totalPages", Matchers.equalTo(1),
                        "totalElements", Matchers.equalTo(2),

                        "content[0].id", Matchers.equalTo(customer1.getId().toString()),
                        "content[0].firstName", Matchers.equalTo(customer1.getFirstName()),
                        "content[0].lastName", Matchers.equalTo(customer1.getLastName()),
                        "content[0].email", Matchers.equalTo(customer1.getEmail()),
                        "content[0].document", Matchers.equalTo(customer1.getDocument()),
                        "content[0].phone", Matchers.equalTo(customer1.getPhone()),
                        "content[0].birthDate", Matchers.equalTo(customer1.getBirthDate().toString()),
                        "content[0].loyaltyPoints", Matchers.equalTo(customer1.getLoyaltyPoints()),
                        "content[0].archived", Matchers.is(customer1.getArchived()),
                        "content[0].promotionNotificationsAllowed", Matchers.equalTo(customer1.getPromotionNotificationsAllowed()),
                        "content[0].registeredAt", Matchers.equalTo(formatter.format(customer1.getRegisteredAt())),

                        "content[1].id", Matchers.equalTo(customer2.getId().toString()),
                        "content[1].firstName", Matchers.equalTo(customer2.getFirstName()),
                        "content[1].lastName", Matchers.equalTo(customer2.getLastName()),
                        "content[1].email", Matchers.equalTo(customer2.getEmail()),
                        "content[1].document", Matchers.equalTo(customer2.getDocument()),
                        "content[1].phone", Matchers.equalTo(customer2.getPhone()),
                        "content[1].birthDate", Matchers.equalTo(customer2.getBirthDate().toString()),
                        "content[1].loyaltyPoints", Matchers.equalTo(customer2.getLoyaltyPoints()),
                        "content[1].archived", Matchers.is(customer2.getArchived()),
                        "content[1].promotionNotificationsAllowed", Matchers.equalTo(customer2.getPromotionNotificationsAllowed()),
                        "content[1].registeredAt", Matchers.equalTo(formatter.format(customer2.getRegisteredAt()))
                );


    }


    @Test
    public void findByIdContract() {
        CustomerOutput customer = CustomerOutputTestDataBuilder.existing().build();
        AddressData address = customer.getAddress();

        Mockito.when(customerQueryService.findById(customer.getId())).thenReturn(customer);

        RestAssuredMockMvc
                            .given()
                            .accept(MediaType.APPLICATION_JSON)
                .when()
                    .get("/api/v1/customers/{customerId}", customer.getId())
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .statusCode(HttpStatus.OK.value())
                .body(
                        "id", Matchers.equalTo(customer.getId().toString()),
                        "registeredAt", Matchers.is(formatter.format(customer.getRegisteredAt())),
                        "firstName", Matchers.is(customer.getFirstName()),
                        "lastName", Matchers.is(customer.getLastName()),
                        "email", Matchers.is(customer.getEmail()),
                        "document", Matchers.is(customer.getDocument()),
                        "phone", Matchers.is(customer.getPhone()),
                        "birthDate", Matchers.is(customer.getBirthDate().toString()),
                        "promotionNotificationsAllowed", Matchers.is(customer.getPromotionNotificationsAllowed()),
                        "loyaltyPoints", Matchers.is(customer.getLoyaltyPoints()),
                        "address.street", Matchers.is(address.getStreet()),
                        "address.city", Matchers.is(address.getCity()),
                        "address.state", Matchers.is(address.getState()),
                        "address.zipCode", Matchers.is(address.getZipCode()),
                        "address.neighborhood", Matchers.is(address.getNeighborhood()),
                        "address.number", Matchers.is(address.getNumber()),
                        "address.complement", Matchers.is(address.getComplement())
                );


    }


    @Test
    public void findByIdError404Contract() {
        UUID invalidCustomerId = UUID.randomUUID();

        Mockito.when(customerQueryService.findById(invalidCustomerId))
                .thenThrow(new CustomerNotFoundException());

        RestAssuredMockMvc
                .given()
                    .accept(MediaType.APPLICATION_JSON)
                .when()
                    .get("/api/v1/customers/{customerId}", invalidCustomerId)
                .then()
                .assertThat()
                    .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                    .statusCode(HttpStatus.NOT_FOUND.value())
                .body(
                        "status", Matchers.is(HttpStatus.NOT_FOUND.value()),
                        "type", Matchers.is("/errors/not-found"),
                        "title", Matchers.notNullValue(),
                        "instance", Matchers.notNullValue()
                );


    }

    @Test
    public void createCustomerError409Contract() {

        Mockito.when(customerManagementApplicationService.create(Mockito.any(CustomerInput.class)))
                .thenThrow(CustomerEmailIsInUseException.class);


        String jsonInput = """
                {
                  "firstName": "John",
                  "lastName": "Doe",
                  "email": "john.doe@gmail.com",
                  "document": "12345",
                  "phone": "31991953046",
                  "birthDate": "1989-07-25",
                  "promotionNotificationsAllowed": false,
                  "address": {
                    "street": "Bourbon Street",
                    "number": "2000",
                    "complement": "apt 122",
                    "neighborhood": "North Ville",
                    "city": "Yostfort",
                    "state": "South Carolina",
                    "zipCode": "12321"
                  }
                }
                """;

        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonInput)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/v1/customers")
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.CONFLICT.value())
                .body(
                        "status", Matchers.is(HttpStatus.CONFLICT.value()),
                        "type", Matchers.is("/errors/conflict"),
                        "title", Matchers.notNullValue(),
                        "instance", Matchers.notNullValue()
                );


    }

    @Test
    public void createCustomerError422Contract() {
        Mockito.when(customerManagementApplicationService.create(Mockito.any(CustomerInput.class)))
                .thenThrow(DomainExcpetion.class);


        String jsonInput = """
                {
                  "firstName": "John",
                  "lastName": "Doe",
                  "email": "john.doe@gmail.com",
                  "document": "12345",
                  "phone": "31991953046",
                  "birthDate": "1989-07-25",
                  "promotionNotificationsAllowed": false,
                  "address": {
                    "street": "Bourbon Street",
                    "number": "2000",
                    "complement": "apt 122",
                    "neighborhood": "North Ville",
                    "city": "Yostfort",
                    "state": "South Carolina",
                    "zipCode": "12321"
                  }
                }
                """;

        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonInput)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/v1/customers")
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .body(
                        "status", Matchers.is(HttpStatus.UNPROCESSABLE_ENTITY.value()),
                        "type", Matchers.is("/errors/unprocessable-entity"),
                        "title", Matchers.notNullValue(),
                        "instance", Matchers.notNullValue()
                );
    }

    @Test
    public void createCustomerError500Contract() {
        Mockito.when(customerManagementApplicationService.create(Mockito.any(CustomerInput.class)))
                .thenThrow(RuntimeException.class);


        String jsonInput = """
                {
                  "firstName": "John",
                  "lastName": "Doe",
                  "email": "john.doe@gmail.com",
                  "document": "12345",
                  "phone": "31991953046",
                  "birthDate": "1989-07-25",
                  "promotionNotificationsAllowed": false,
                  "address": {
                    "street": "Bourbon Street",
                    "number": "2000",
                    "complement": "apt 122",
                    "neighborhood": "North Ville",
                    "city": "Yostfort",
                    "state": "South Carolina",
                    "zipCode": "12321"
                  }
                }
                """;

        RestAssuredMockMvc
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .body(jsonInput)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .post("/api/v1/customers")
                .then()
                .assertThat()
                .contentType(MediaType.APPLICATION_PROBLEM_JSON_VALUE)
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .body(
                        "status", Matchers.is(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                        "type", Matchers.is("/errors/internal"),
                        "title", Matchers.notNullValue(),
                        "instance", Matchers.notNullValue()
                );

    }
}