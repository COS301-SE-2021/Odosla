package cs.superleague.payment;

import cs.superleague.integration.security.JwtUtil;
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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class GetCustomersActiveOrdersUnitTest {

    @Mock
    OrderRepo orderRepo;

    @InjectMocks
    JwtUtil jwtTokenUtil;

    @Mock
    UserService userService;

    @InjectMocks
    UserServiceImpl userServiceImpl;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    UUID orderID;
    Order order;
    UUID customerID;
    Customer customer;
    String jwtToken;
    List<Order> orderList;
    GetCustomersActiveOrdersRequest request;
    Order order2;

    @BeforeEach
    void setUp() {
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
    }

    @AfterEach
    void tearDown(){

    }

    @Test
    @Description("Tests that the request object is created correctly.")
    @DisplayName("Request object creation")
    void testsTheCreationOfTheRequestObject_UnitTest(){
        assertEquals(request.getJwtToken(), jwtToken);
    }

    @Test
    @Description("Tests for when the request object is not specified.")
    @DisplayName("Null request object")
    void testingNullRequestObject_UnitTest(){
        Throwable thrown = Assertions.assertThrows(PaymentException.class, ()-> paymentService.getCustomersActiveOrders(null));
        assertEquals("Get Customers Active Orders Request cannot be null - Retrieval of Order unsuccessful", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the request object contains a null jwtToken.")
    @DisplayName("Null jwtToken")
    void nullJwtTokenInRequestObject_UnitTest(){
        request.setJwtToken(null);
        Throwable thrown = Assertions.assertThrows(PaymentException.class, ()-> paymentService.getCustomersActiveOrders(request));
        assertEquals("JWTToken of Get Customers Active Orders is null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the jwtToken passed in is not valid.")
    @DisplayName("Invalid jwtToken")
    void invalidJwtTokenPassedInRequestObject_UnitTest() throws InvalidRequestException, OrderDoesNotExist, cs.superleague.user.exceptions.InvalidRequestException {
        request.setJwtToken("");
        GetCurrentUserResponse getCurrentUserResponse = new GetCurrentUserResponse(null,false, Calendar.getInstance().getTime(),"User could not be returned");
        Mockito.when(userService.getCurrentUser(Mockito.any())).thenReturn(getCurrentUserResponse);
        Throwable thrown = Assertions.assertThrows(UserDoesNotExistException.class, ()-> paymentService.getCustomersActiveOrders(request));
        assertEquals(thrown.getMessage(), "Invalid jwtToken, no user found");
    }

    @Test
    @Description("Tests for when there is no order in the repo for the user.")
    @DisplayName("No orders")
    void noOrdersFoundInDatabaseForCustomer_UnitTest() throws cs.superleague.user.exceptions.InvalidRequestException {
        Mockito.when(orderRepo.findAllByUserID(Mockito.any())).thenReturn(null);
        GetCurrentUserResponse getCurrentUserResponse = new GetCurrentUserResponse(customer,true, Calendar.getInstance().getTime(),"User successfully returned");
        Mockito.when(userService.getCurrentUser(Mockito.any())).thenReturn(getCurrentUserResponse);
        Throwable thrown = Assertions.assertThrows(OrderDoesNotExist.class, ()-> paymentService.getCustomersActiveOrders(request));
        assertEquals(thrown.getMessage(), "No Orders found for this user in the database.");
    }

    @Test
    @Description("Tests for when there are no active orders in the database.")
    @DisplayName("No active orders")
    void noActiveOrdersInDatabase_UnitTest() throws cs.superleague.user.exceptions.InvalidRequestException, InvalidRequestException, UserDoesNotExistException, OrderDoesNotExist {
        order.setStatus(OrderStatus.CUSTOMER_COLLECTED);
        orderList.add(order);
        Mockito.when(orderRepo.findAllByUserID(Mockito.any())).thenReturn(orderList);
        GetCurrentUserResponse getCurrentUserResponse = new GetCurrentUserResponse(customer,true, Calendar.getInstance().getTime(),"User successfully returned");
        Mockito.when(userService.getCurrentUser(Mockito.any())).thenReturn(getCurrentUserResponse);
        GetCustomersActiveOrdersResponse response = paymentService.getCustomersActiveOrders(request);
        assertEquals(response.getMessage(), "This customer has no active orders.");
        assertEquals(response.isHasActiveOrder(), false);
        assertEquals(response.getOrderID(), null);
    }

    @Test
    @Description("Tests for when the user has an active order")
    @DisplayName("Active order returned")
    void activeOrderReturnedFromRequest_UnitTest()throws cs.superleague.user.exceptions.InvalidRequestException, InvalidRequestException, UserDoesNotExistException, OrderDoesNotExist{
        order.setStatus(OrderStatus.PACKING);
        order2.setStatus(OrderStatus.CUSTOMER_COLLECTED);
        orderList.add(order);
        orderList.add(order2);
        Mockito.when(orderRepo.findAllByUserID(Mockito.any())).thenReturn(orderList);
        GetCurrentUserResponse getCurrentUserResponse = new GetCurrentUserResponse(customer,true, Calendar.getInstance().getTime(),"User successfully returned");
        Mockito.when(userService.getCurrentUser(Mockito.any())).thenReturn(getCurrentUserResponse);
        GetCustomersActiveOrdersResponse response = paymentService.getCustomersActiveOrders(request);
        assertEquals(response.getMessage(), "Order successfully returned to customer.");
        assertEquals(response.isHasActiveOrder(), true);
        assertEquals(response.getOrderID(), orderID);
    }

}
