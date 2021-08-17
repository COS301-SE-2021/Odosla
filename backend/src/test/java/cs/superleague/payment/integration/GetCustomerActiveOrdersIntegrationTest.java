package cs.superleague.payment.integration;

import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.PaymentServiceImpl;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.exceptions.PaymentException;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.GetCustomersActiveOrdersRequest;
import cs.superleague.payment.responses.GetCustomersActiveOrdersResponse;
import cs.superleague.user.UserService;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.exceptions.UserDoesNotExistException;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.responses.GetCurrentUserResponse;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class GetCustomerActiveOrdersIntegrationTest {
    @Autowired
    private PaymentServiceImpl paymentService;

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    JwtUtil jwtTokenUtil;

    @Autowired
    UserService userService;

    @Autowired
    UserServiceImpl userServiceImpl;

    UUID orderID;
    Order order;
    UUID customerID;
    Customer customer;
    String jwtToken;
    List<Order> orderList;
    GetCustomersActiveOrdersRequest request;
    Order order2;
    String invalidJWTToken;

    @BeforeEach
    void setUp() {
        Customer customer2 = new Customer();
        customer2.setCustomerID(UUID.randomUUID());
        customer2.setEmail("jj@gmail.com");
        invalidJWTToken = jwtTokenUtil.generateJWTTokenCustomer(customer2);
        orderID = UUID.randomUUID();
        customerID = UUID.randomUUID();
        customer = new Customer();
        customer.setCustomerID(customerID);
        customer.setEmail("hello@gmail.com");
        jwtToken = jwtTokenUtil.generateJWTTokenCustomer(customer);
        order = new Order();
        order2 = new Order();
        order.setOrderID(orderID);
        order2.setOrderID(UUID.randomUUID());
        order.setUserID(customerID);
        order2.setUserID(customerID);
        request = new GetCustomersActiveOrdersRequest(jwtToken);
        orderList = new ArrayList<>();
        orderRepo.save(order);
        orderRepo.save(order2);
        customerRepo.save(customer);
    }

    @AfterEach
    void tearDown(){
        orderRepo.deleteAll();
        customerRepo.deleteAll();
    }

    @Test
    @Description("Test for when there is a null request object.")
    @DisplayName("Null request")
    void nullRequestObjectPassedIn_IntegrationTest(){
        Throwable thrown = Assertions.assertThrows(PaymentException.class, ()-> paymentService.getCustomersActiveOrders(null));
        assertEquals("Get Customers Active Orders Request cannot be null - Retrieval of Order unsuccessful", thrown.getMessage());
    }

    @Test
    @Description("Tests for when an invalid jwtToken is passed in.")
    @DisplayName("Invalid jwtToken")
    void invalidJwtTokenPassedInRequestObject_IntegrationTest() {
        request.setJwtToken(invalidJWTToken);
        Throwable thrown = Assertions.assertThrows(UserDoesNotExistException.class, ()-> paymentService.getCustomersActiveOrders(request));
        assertEquals(thrown.getMessage(), "Invalid jwtToken, no user found");
    }

    @Test
    @Description("Tests for when the customer has no orders in the database")
    @DisplayName("No active orders")
    void customerHasNoOrdersInDatabase_IntegrationTest() throws InvalidRequestException, cs.superleague.user.exceptions.InvalidRequestException, UserDoesNotExistException, OrderDoesNotExist {
        order.setUserID(UUID.randomUUID());
        order2.setUserID(UUID.randomUUID());
        orderRepo.save(order);
        orderRepo.save(order2);
        GetCustomersActiveOrdersResponse response = paymentService.getCustomersActiveOrders(request);
        assertEquals(response.getMessage(), "This customer has no active orders.");
        assertEquals(response.isHasActiveOrder(), false);
        assertEquals(response.getOrderID(), null);
    }

    @Test
    @Description("Tests for when there is no orders for the customer.")
    @DisplayName("No orders with customer")
    void customerHasNoActiveOrdersInDatabase_IntegrationTest() throws InvalidRequestException, cs.superleague.user.exceptions.InvalidRequestException, UserDoesNotExistException, OrderDoesNotExist {
        order.setUserID(customerID);
        order2.setUserID(customerID);
        order.setStatus(OrderStatus.CUSTOMER_COLLECTED);
        order2.setStatus(OrderStatus.DELIVERED);
        orderRepo.save(order);
        orderRepo.save(order2);
        GetCustomersActiveOrdersResponse response = paymentService.getCustomersActiveOrders(request);
        assertEquals(response.getMessage(), "This customer has no active orders.");
        assertEquals(response.isHasActiveOrder(), false);
        assertEquals(response.getOrderID(), null);
    }

    @Test
    @Description("Tests for when the user has an order in the database and the orderID is returned.")
    @DisplayName("Order returned")
    void orderSuccessfullyReturnedToTheCustomer_IntegrationTest() throws InvalidRequestException, cs.superleague.user.exceptions.InvalidRequestException, UserDoesNotExistException, OrderDoesNotExist {
        order.setUserID(customerID);
        order2.setUserID(customerID);
        order.setStatus(OrderStatus.PACKING);
        order2.setStatus(OrderStatus.DELIVERED);
        orderRepo.save(order);
        orderRepo.save(order2);
        GetCustomersActiveOrdersResponse response = paymentService.getCustomersActiveOrders(request);
        assertEquals(response.getMessage(), "Order successfully returned to customer.");
        assertEquals(response.isHasActiveOrder(), true);
        assertEquals(response.getOrderID(), orderID);
    }
}
