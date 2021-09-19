package cs.superleague.user;

import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.responses.GetOrderResponse;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.DriverRepo;
import cs.superleague.user.requests.CollectOrderRequest;
import cs.superleague.user.responses.CollectOrderResponse;
import cs.superleague.payment.dataclass.*;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class CollectOrderUnitTest {

    @Mock
    DriverRepo driverRepo;

    @Mock
    RestTemplate restTemplate;

    @Mock
    RabbitTemplate rabbitTemplate;

    @Value("${paymentPort}")
    private String paymentPort;
    @Value("${paymentHost}")
    private String paymentHost;

    @InjectMocks
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
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.collectOrder(null));
        assertEquals("CollectOrderRequest object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When driverID parameter is not specified")
    void UnitTest_testingNullRequestOrderIDParameter(){
        request.setOrderID(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.collectOrder(request));
        assertEquals("OrderID in CollectOrderRequest object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When order with OrderID does not exist - OrderDoesNotExistException")
    void UnitTest_testingOrderDoesNotExistException() throws URISyntaxException {
        //Mockito.when(orderRepo.findById(Mockito.any())).thenReturn(null);
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("orderID", request.getOrderID());
        String uriString = "http://"+paymentHost+":"+paymentPort+"/payment/getOrder";
        URI uri = new URI(uriString);
        GetOrderResponse getOrderResponse = new GetOrderResponse(null, true, new Date(), "");
        ResponseEntity<GetOrderResponse> useCaseResponseEntity = new ResponseEntity<>(getOrderResponse, HttpStatus.OK);
        Mockito.when(restTemplate.postForEntity(uri, parts, GetOrderResponse.class)).thenReturn(useCaseResponseEntity);

        Throwable thrown = Assertions.assertThrows(OrderDoesNotExist.class, ()-> userService.collectOrder(request));
        assertEquals("Order does not exist in database", thrown.getMessage());
    }

    @Test
    @DisplayName("When order with OrderID exists - successful update")
    void UnitTest_testingCouldntUpdateOrder() throws InvalidRequestException, OrderDoesNotExist, URISyntaxException {
        Order newOrder = new Order();
        newOrder.setStatus(OrderStatus.AWAITING_COLLECTION);
        order.setStatus(OrderStatus.AWAITING_COLLECTION);
        //Mockito.when(orderRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(order)).thenReturn(java.util.Optional.of(newOrder));
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("orderID", request.getOrderID());
        String uriString = "http://"+paymentHost+":"+paymentPort+"/payment/getOrder";
        URI uri = new URI(uriString);
        GetOrderResponse getOrderResponse = new GetOrderResponse(order, true, new Date(), "");
        ResponseEntity<GetOrderResponse> useCaseResponseEntity = new ResponseEntity<>(getOrderResponse, HttpStatus.OK);
        Mockito.when(restTemplate.postForEntity(uri, parts, GetOrderResponse.class)).thenReturn(useCaseResponseEntity);
        response=userService.collectOrder(request);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Order successfully been collected and status has been changed",response.getMessage());
    }
}
