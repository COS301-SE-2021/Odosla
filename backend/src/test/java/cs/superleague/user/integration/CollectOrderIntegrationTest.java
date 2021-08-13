package cs.superleague.user.integration;

import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.DriverRepo;
import cs.superleague.user.requests.CollectOrderRequest;
import cs.superleague.user.responses.CollectOrderResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class CollectOrderIntegrationTest {

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    DriverRepo driverRepo;

    @Autowired
    private UserServiceImpl userService;

    CollectOrderResponse response;
    CollectOrderRequest request;
    Order order;
    UUID orderId= UUID.randomUUID();

    @BeforeEach
    void setup(){
        request=new CollectOrderRequest(orderId);
        order=new Order();

        order.setOrderID(orderId);

    }

    @AfterEach
    void teardown(){}

    @Test
    @DisplayName("When request object is not specified")
    void IntegrationTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.collectOrder(null));
        assertEquals("CollectOrderRequest object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When driverID parameter is not specified")
    void IntegrationTest_testingNullRequestOrderIDParameter(){
        request.setOrderID(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.collectOrder(request));
        assertEquals("OrderID in CollectOrderRequest object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When order with OrderID does not exist - OrderDoesNotExistException")
    void IntegrationTest_testingOrderDoesNotExistException(){
        Throwable thrown = Assertions.assertThrows(OrderDoesNotExist.class, ()-> userService.collectOrder(request));
        assertEquals("Order does not exist in database", thrown.getMessage());
    }


    @Test
    @DisplayName("Order correctly collected")
    void IntegrationTest_testingCorrectlyCollected() throws InvalidRequestException, OrderDoesNotExist {
        order.setStatus(OrderStatus.DELIVERY_COLLECTED);
        orderRepo.save(order);
        response=userService.collectOrder(request);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Order successfully been collected and status has been changed",response.getMessage());
    }
}
