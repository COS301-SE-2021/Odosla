package cs.superleague.user;

import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.responses.GetOrderByUUIDResponse;
import cs.superleague.payment.responses.GetOrderResponse;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.DriverRepo;
import cs.superleague.user.requests.CollectOrderRequest;
import cs.superleague.user.requests.CompleteDeliveryRequest;
import cs.superleague.user.responses.CompleteDeliveryResponse;
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
public class CompleteDeliveryUnitTest {

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

    CompleteDeliveryResponse response;
    CompleteDeliveryRequest request;

    Order order;
    UUID orderId= UUID.randomUUID();

    Driver driver;
    UUID driverId= UUID.randomUUID();

    @BeforeEach
    void setup(){
        request=new CompleteDeliveryRequest(orderId);
        order=new Order();
        driver = new Driver();
        driver.setDriverID(driverId);
        order.setOrderID(orderId);
        order.setDriverID(driverId);
    }

    @AfterEach
    void teardown(){}

    @Test
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.completeDelivery(null));
        assertEquals("CompleteDeliveryRequest object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When orderID parameter is not specified")
    void UnitTest_testingNullRequestUserIDParameter(){
        request.setOrderID(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.completeDelivery(request));
        assertEquals("OrderID in CompleteDeliveryRequest object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When order with OrderID does not exist - OrderDoesNotExistException")
    void UnitTest_testingOrderDoesNotExistException() throws URISyntaxException {
        //Mockito.when(orderRepo.findById(Mockito.any())).thenReturn(null);
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("orderID", request.getOrderID());
        String uriString = "http://"+paymentHost+":"+paymentPort+"/payment/getOrderByUUID";
        URI uri = new URI(uriString);
        GetOrderByUUIDResponse getOrderResponse = new GetOrderByUUIDResponse(null, new Date(), "");
        ResponseEntity<GetOrderByUUIDResponse> useCaseResponseEntity = new ResponseEntity<>(getOrderResponse, HttpStatus.OK);
        Mockito.when(restTemplate.postForEntity(uri, parts, GetOrderByUUIDResponse.class)).thenReturn(useCaseResponseEntity);

        Throwable thrown = Assertions.assertThrows(OrderDoesNotExist.class, ()-> userService.completeDelivery(request));
        assertEquals("Order does not exist in database", thrown.getMessage());
    }

    @Test
    @DisplayName("Order correctly completed")
    void UnitTest_testingCorrectlyCollected() throws InvalidRequestException, OrderDoesNotExist, URISyntaxException {
        order.setStatus(OrderStatus.DELIVERY_COLLECTED);
        Mockito.when(driverRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(driver));
        //Mockito.when(orderRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(order));
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("orderID", request.getOrderID());
        String uriString = "http://"+paymentHost+":"+paymentPort+"/payment/getOrderByUUID";
        URI uri = new URI(uriString);
        GetOrderByUUIDResponse getOrderResponse = new GetOrderByUUIDResponse(order, new Date(), "");
        ResponseEntity<GetOrderByUUIDResponse> useCaseResponseEntity = new ResponseEntity<>(getOrderResponse, HttpStatus.OK);
        Mockito.when(restTemplate.postForEntity(uri, parts, GetOrderByUUIDResponse.class)).thenReturn(useCaseResponseEntity);

        response=userService.completeDelivery(request);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Order successfully been delivered and status has been changed",response.getMessage());
    }
}
