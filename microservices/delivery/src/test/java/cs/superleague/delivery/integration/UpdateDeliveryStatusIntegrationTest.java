package cs.superleague.delivery.integration;

import cs.superleague.delivery.DeliveryServiceImpl;
import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.UpdateDeliveryStatusRequest;
import cs.superleague.delivery.responses.UpdateDeliveryStatusResponse;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.requests.SaveOrderToRepoRequest;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.requests.SaveDriverToRepoRequest;
import cs.superleague.integration.security.JwtUtil;
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
import org.springframework.context.annotation.Description;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@Transactional
public class UpdateDeliveryStatusIntegrationTest {
    @Autowired
    private DeliveryServiceImpl deliveryService;

    @Autowired
    DeliveryRepo deliveryRepo;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    JwtUtil jwtUtil;

    @Value("${env.SECRET}")
    private String SECRET = "stub";

    @Value("${env.HEADER}")
    private String HEADER = "stub";

    UUID deliveryID;
    Delivery delivery;
    Calendar time;
    UUID customerID;
    UUID orderID;
    UUID storeID;
    UUID driverID;
    Driver driver;
    Order order;
    DeliveryStatus status;
    GeoPoint pickUpLocation;
    GeoPoint dropOffLocation;

    String uri;

    @BeforeEach
    void setUp(){
        time = Calendar.getInstance();
        deliveryID = UUID.fromString("e6732473-7e91-4949-8811-d71e6fb6255d");
        orderID = UUID.fromString("54287c26-ae7d-4137-afbe-353789533a47");
        customerID = UUID.fromString("26634e65-21ca-4c6f-932a-ca3c48755123");
        storeID = UUID.fromString("0b3837de-a2d7-4fdc-a819-63ebd6dea1aa");
        driverID = UUID.fromString("f1299f1e-27bc-42fe-af16-d68d8a91df35");
        status = DeliveryStatus.CollectedByDriver;
        pickUpLocation = new GeoPoint(0.0, 0.0, "address");
        dropOffLocation = new GeoPoint(1.0, 1.0, "address");
        delivery = new Delivery(deliveryID, orderID, pickUpLocation, dropOffLocation, customerID, storeID, DeliveryStatus.WaitingForShoppers, 0.0);
        delivery.setDriverId(driverID);

        delivery.setOrderID(orderID);
        deliveryRepo.save(delivery);

        uri = "http://localhost:8089/user/getDriverByUUID";

        driver = new Driver();
        driver.setDriverID(driverID);
        driver.setEmail("u14254922@tuks.co.za");
        driver.setAccountType(UserType.DRIVER);

        String jwt = jwtUtil.generateJWTTokenDriver(driver);

        Header header = new BasicHeader("Authorization", jwt);
        List<Header> headers = new ArrayList<>();
        headers.add(header);
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

        jwt = jwt.replace(HEADER, "");
        Claims claims = Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwt).getBody();
        List<String> authorities = (List) claims.get("authorities");
        String userType = (String) claims.get("userType");
        String email = (String) claims.get("email");

        UsernamePasswordAuthenticationToken auth = new
                UsernamePasswordAuthenticationToken(claims.getSubject(),
                null, authorities.stream().map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));

        HashMap<String, Object> info = new HashMap<>();
        info.put("userType", userType);
        info.put("email", email);
        auth.setDetails(info);
        SecurityContextHolder.getContext().setAuthentication(auth);


        order = new Order();
        order.setOrderID(orderID);

        // Store Driver
        SaveDriverToRepoRequest saveDriverToRepoRequest = new SaveDriverToRepoRequest(driver);
        rabbitTemplate.convertAndSend("UserEXCHANGE", "RK_SaveDriverToRepo", saveDriverToRepoRequest);

        // Save Order
        SaveOrderToRepoRequest saveOrderToRepoRequest = new SaveOrderToRepoRequest(order);
        rabbitTemplate.convertAndSend("PaymentEXCHANGE", "RK_saveOrderToRepo", saveOrderToRepoRequest);
    }

    @AfterEach
    void tearDown(){
        SecurityContextHolder.clearContext();
    }

    @Test
    @Description("Tests that the request object is created successfully.")
    @DisplayName("Request object creation.")
    void checkRequestObjectCreation_IntegrationTest() {
        UpdateDeliveryStatusRequest request = new UpdateDeliveryStatusRequest(status, deliveryID, "detail");
        assertEquals(request.getDeliveryID(), deliveryID);
        assertEquals(request.getStatus(), status);
        assertEquals(request.getDetail(), "detail");
    }

    @Test
    @Description("Tests for when a null request object is passed in.")
    @DisplayName("Null request")
    void nullRequestObject_IntegrationTest() {
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, () -> deliveryService.updateDeliveryStatus(null));
        assertEquals("Null request object.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when there is null parameters in the request object")
    @DisplayName("Null parameters")
    void nullParametersInRequestObject_IntegrationTest(){
        UpdateDeliveryStatusRequest request1 = new UpdateDeliveryStatusRequest(null, deliveryID, "detail");
        Throwable thrown1 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.updateDeliveryStatus(request1));
        assertEquals("Null parameters.", thrown1.getMessage());

        UpdateDeliveryStatusRequest request2 = new UpdateDeliveryStatusRequest(status, null, "detail");
        Throwable thrown2 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.updateDeliveryStatus(request2));
        assertEquals("Null parameters.", thrown2.getMessage());

        UpdateDeliveryStatusRequest request3 = new UpdateDeliveryStatusRequest(status, deliveryID, null);
        Throwable thrown3 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.updateDeliveryStatus(request3));
        assertEquals("Null parameters.", thrown3.getMessage());
    }

    @Test
    @Description("Tests for when there is no delivery with the ID in the database.")
    @DisplayName("Invalid deliveryID")
    void invalidDeliveryIDPassedInRequestObject_IntegrationTest(){
        UpdateDeliveryStatusRequest request = new UpdateDeliveryStatusRequest(status, UUID.randomUUID(), "detail");
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.updateDeliveryStatus(request));
        assertEquals("Delivery does not exist in database.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the request delivery status is 'DELIVERED', Driver not found")
    @DisplayName("Successful update -> DELIVERED")
    void deliveryStatusUpdatedSuccessfully_DELIVERED_IntegrationTest(){
        UpdateDeliveryStatusRequest request1 = new UpdateDeliveryStatusRequest(DeliveryStatus.Delivered, deliveryID, "detail");

        try {
            UpdateDeliveryStatusResponse response = deliveryService.updateDeliveryStatus(request1);
            assertEquals(response.getMessage(), "Successful status update.");
            Optional<Delivery> delivery1 = deliveryRepo.findById(deliveryID);
            assertEquals(delivery1.get().getStatus(), DeliveryStatus.Delivered);
        }catch (Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @Description("Delivery successfully updated in the database")
    @DisplayName("Successful update")
    void deliveryStatusUpdatedSuccessfully_IntegrationTest() throws InvalidRequestException, URISyntaxException {
        UpdateDeliveryStatusRequest request = new UpdateDeliveryStatusRequest(DeliveryStatus.Delivered, deliveryID, "detail");
        UpdateDeliveryStatusResponse response = deliveryService.updateDeliveryStatus(request);
        assertEquals(response.getMessage(), "Successful status update.");
        Optional<Delivery> delivery1 = deliveryRepo.findById(deliveryID);
        assertEquals(delivery1.get().getStatus(), DeliveryStatus.Delivered);
    }
}
