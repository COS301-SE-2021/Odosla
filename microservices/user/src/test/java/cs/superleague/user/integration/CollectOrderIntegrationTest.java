package cs.superleague.user.integration;

import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.requests.SaveOrderToRepoRequest;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.DriverRepo;
import cs.superleague.user.requests.CollectOrderRequest;
import cs.superleague.user.responses.CollectOrderResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.http.Header;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.junit.jupiter.api.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class CollectOrderIntegrationTest {


    @Autowired
    DriverRepo driverRepo;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Value("${paymentPort}")
    private String paymentPort;
    @Value("${paymentHost}")
    private String paymentHost;
    @Value("${deliveryPort}")
    private String deliveryPort;
    @Value("${deliveryHost}")
    private String deliveryHost;
    @Value("${shoppingPort}")
    private String shoppingPort;
    @Value("${shoppingHost}")
    private String shoppingHost;

    @Autowired
    private UserServiceImpl userService;

    CollectOrderResponse response;
    CollectOrderRequest request;
    CollectOrderRequest request1;
    Order order;
    Order order2;
    UUID orderId = UUID.fromString("31b06384-ce8d-4c6e-9082-7da75cec27fa");
    UUID orderId2 = UUID.fromString("83f7ba72-c16e-4313-945f-642dea428ee9");

    @BeforeEach
    void setup(){
        request = new CollectOrderRequest(orderId2);
        request1 = new CollectOrderRequest(orderId);
        order = new Order();
        order2 = new Order();
        order2.setOrderID(orderId2);
        order.setOrderID(orderId);
        SaveOrderToRepoRequest saveOrderRequest = new SaveOrderToRepoRequest(order);
        rabbitTemplate.convertAndSend("PaymentEXCHANGE", "RK_SaveOrderToRepo", saveOrderRequest);
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
    void IntegrationTest_testingCorrectlyCollected() throws InvalidRequestException, OrderDoesNotExist, URISyntaxException {
        order.setStatus(OrderStatus.DELIVERY_COLLECTED);
        response=userService.collectOrder(request1);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Order successfully been collected and status has been changed",response.getMessage());
    }
}
