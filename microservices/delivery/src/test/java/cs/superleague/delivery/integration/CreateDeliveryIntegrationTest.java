package cs.superleague.delivery.integration;

import cs.superleague.delivery.DeliveryServiceImpl;
import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.CreateDeliveryRequest;
import cs.superleague.delivery.responses.CreateDeliveryResponse;
import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.requests.SaveOrderToRepoRequest;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.requests.SaveStoreToRepoRequest;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.requests.SaveCustomerToRepoRequest;
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

@SpringBootTest
@Transactional
public class CreateDeliveryIntegrationTest {
    @Autowired
    private DeliveryServiceImpl deliveryService;
    @Autowired
    DeliveryRepo deliveryRepo;
    @Autowired
    RabbitTemplate rabbitTemplate;
    @Autowired
    RestTemplate restTemplate;
    @Value("${jwt.secret}")
    private String SECRET;


    UUID deliveryID;
    UUID orderID;
    Calendar time;
    GeoPoint invalidDropOffLocation;
    GeoPoint pickUpLocation;
    GeoPoint dropOffLocation;
    UUID customerID;
    UUID storeID;
    DeliveryStatus status;
    double cost;
    Customer customer;
    Store store;
    Order order;
    UUID shopperID;

    @BeforeEach
    void setUp(){
        cost = 0.0;
        shopperID = UUID.fromString("3e068fa6-bdb4-4d02-b0b8-517da498934a");
        deliveryID = UUID.fromString("e6732473-7e91-4949-8811-d71e6fb6255d");
        orderID = UUID.fromString("54287c26-ae7d-4137-afbe-353789533a47");
        time = Calendar.getInstance();
        status = DeliveryStatus.WaitingForShoppers;
        invalidDropOffLocation = new GeoPoint(-91.0, -91.0, "address");
        pickUpLocation = new GeoPoint(0.0, 0.0, "address");
        dropOffLocation = new GeoPoint(1.0, 1.0, "address");
        customerID = UUID.fromString("26634e65-21ca-4c6f-932a-ca3c48755123");
        storeID = UUID.fromString("0b3837de-a2d7-4fdc-a819-63ebd6dea1aa");
        customer = new Customer("Seamus", "Brennan", "u19060468@tuks.co.za", "0743149813", "Hello123$$$", "123", UserType.CUSTOMER,customerID);
        store = new Store(storeID, 1, 2, "Woolworth's", 10, 10, true, "");
        store.setStoreLocation(pickUpLocation);
        order = new Order(orderID, customerID, storeID, shopperID, new Date(), null, 50.0, OrderType.DELIVERY, OrderStatus.PURCHASED, 0.0, dropOffLocation, pickUpLocation, false);
        JwtUtil jwtUtil = new JwtUtil();
        String jwt = jwtUtil.generateJWTTokenCustomer(customer);

        Header header = new BasicHeader("Authorization", jwt);
        List<Header> headers = new ArrayList<>();
        headers.add(header);
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

        jwt = jwt.replace("Bearer ","");
        Claims claims= Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwt).getBody();
        List<String> authorities = (List) claims.get("authorities");
        String userType= (String) claims.get("userType");
        String email = (String) claims.get("email");
        System.out.println(userType);
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        HashMap<String, Object> info=new HashMap<String, Object>();
        info.put("userType",userType);
        info.put("email",email);
        auth.setDetails(info);
        SecurityContextHolder.getContext().setAuthentication(auth);
        SaveOrderToRepoRequest saveOrderToRepoRequest = new SaveOrderToRepoRequest(order);
        rabbitTemplate.convertAndSend("PaymentEXCHANGE", "RK_SaveOrderToRepo", saveOrderToRepoRequest);
        SaveStoreToRepoRequest saveStoreToRepoRequest = new SaveStoreToRepoRequest(store);
        rabbitTemplate.convertAndSend("ShoppingEXCHANGE", "RK_SaveStoreToRepo", saveStoreToRepoRequest);
        SaveCustomerToRepoRequest saveCustomerToRepoRequest = new SaveCustomerToRepoRequest(customer);
        rabbitTemplate.convertAndSend("UserEXCHANGE", "RK_SaveCustomerToRepoTest", saveCustomerToRepoRequest);
    }

    @AfterEach
    void tearDown(){
        deliveryRepo.deleteAll();
        SecurityContextHolder.clearContext();
    }

    @Test
    @Description("Tests for when customer is not in database, should throw an exception.")
    @DisplayName("Invalid customer")
    void invalidCustomerIDPassedInRequest_IntegrationTest(){
        CreateDeliveryRequest request = new CreateDeliveryRequest(orderID, UUID.randomUUID(), storeID, time, dropOffLocation);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.createDelivery(request));
        assertEquals("Invalid customerID.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the orderID is not found in the database, should throw an exception.")
    @DisplayName("Invalid orderID")
    void invalidOrderIDPassedInRequestObject_IntegrationTest(){
        CreateDeliveryRequest request = new CreateDeliveryRequest(UUID.randomUUID(), customerID, storeID, time, dropOffLocation);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.createDelivery(request));
        assertEquals("Invalid orderID.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the store is not found in the database, should throw an exception.")
    @DisplayName("Invalid storeID")
    void invalidStoreIDPassedInRequestObject_IntegrationTest(){
        CreateDeliveryRequest request = new CreateDeliveryRequest(orderID, customerID, UUID.randomUUID(), time, dropOffLocation);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.createDelivery(request));
        assertEquals("Invalid storeID.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the store does not have a location saved, should throw an exception.")
    @DisplayName("Null store location")
    void nullStoreLocationOnStoreObjectFound_IntegrationTest(){
        store.setStoreLocation(null);
        SaveStoreToRepoRequest saveStoreToRepoRequest = new SaveStoreToRepoRequest(store);
        rabbitTemplate.convertAndSend("ShoppingEXCHANGE", "RK_SaveStoreToRepo", saveStoreToRepoRequest);
        CreateDeliveryRequest request = new CreateDeliveryRequest(orderID, customerID, storeID, time, dropOffLocation);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.createDelivery(request));
        assertEquals("Store has no location set.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when valid delivery is placed.")
    @DisplayName("Valid delivery created")
    void validDeliveryCreatedAndSaved_IntegrationTest() throws InvalidRequestException, URISyntaxException {
        CreateDeliveryRequest request = new CreateDeliveryRequest(orderID, customerID, storeID, time, dropOffLocation);
        CreateDeliveryResponse response = deliveryService.createDelivery(request);
        assertEquals(response.isSuccess(), true);
        assertEquals(response.getMessage(), "Delivery request placed.");
        List<Delivery> delivery = deliveryRepo.findAll();
        assertEquals(delivery.get(0).getDeliveryID(), response.getDeliveryID());
    }
}
