package cs.superleague.delivery.integration;

import cs.superleague.delivery.DeliveryServiceImpl;
import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.dataclass.DeliveryDetail;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.repos.DeliveryDetailRepo;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.GetDeliveryDetailRequest;
import cs.superleague.delivery.responses.GetDeliveryDetailResponse;
import cs.superleague.delivery.stub.dataclass.Admin;
import cs.superleague.delivery.stub.dataclass.Driver;
import cs.superleague.delivery.stub.dataclass.GeoPoint;
import cs.superleague.delivery.stub.dataclass.UserType;
import cs.superleague.integration.security.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.apache.http.Header;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.junit.jupiter.api.*;
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
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class GetDeliveryDetailIntegrationTest {
    @Autowired
    private DeliveryServiceImpl deliveryService;

    @Autowired
    DeliveryRepo deliveryRepo;

    @Autowired
    DeliveryDetailRepo deliveryDetailRepo;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    RestTemplate restTemplate;

    @Value("${env.SECRET}")
    private String SECRET = "stub";

    @Value("${env.HEADER}")
    private String HEADER = "stub";

    Admin admin;
    UUID adminID;
    UUID deliveryID;
    Delivery delivery;
    UUID orderID;
    GeoPoint pickUpLocation;
    GeoPoint dropOffLocation;
    UUID customerID, storeID;
    DeliveryStatus status;
    Calendar time;
    DeliveryDetail deliveryDetail1;
    DeliveryDetail deliveryDetail2;
    Driver driver;

    @BeforeEach
    void setUp(){
        time = Calendar.getInstance();
        status = DeliveryStatus.WaitingForShoppers;
        orderID = UUID.randomUUID();
        customerID = UUID.randomUUID();
        storeID = UUID.randomUUID();
        adminID=UUID.randomUUID();
        deliveryID = UUID.randomUUID();
        pickUpLocation = new GeoPoint(0.0, 0.0, "address");
        dropOffLocation = new GeoPoint(1.0, 1.0, "address");
        admin = new Admin("John", "Doe", "u19060468@tuks.co.za", "0743149813", "Hello123", "123", UserType.ADMIN, adminID);
        delivery = new Delivery(deliveryID, orderID, pickUpLocation, dropOffLocation, customerID, storeID, status, 0.0);
        deliveryDetail1 = new DeliveryDetail(deliveryID, time, DeliveryStatus.CollectedByDriver, "detail1");
        deliveryDetail2 = new DeliveryDetail(UUID.randomUUID(), Calendar.getInstance(), DeliveryStatus.DeliveringToCustomer, "detail2");
//        adminRepo.save(admin);
        deliveryRepo.save(delivery);
        deliveryDetailRepo.save(deliveryDetail1);
        deliveryDetailRepo.save(deliveryDetail2);

        String jwt = jwtUtil.generateJWTTokenAdmin(admin);

        System.out.println(jwt);

        Header header = new BasicHeader("Authorization", jwt);
        List<Header> headers = new ArrayList<>();
        headers.add(header);
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

        jwt = jwt.replace(HEADER,"");
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
    }

    @AfterEach
    void tearDown(){
    }

    @Test
    @Description("Tests for when the user looking for the data is not an admin.")
    @DisplayName("Invalid adminID")
    void invalidAdminIDInRequestObject_IntegrationTest(){
        admin.setAdminID(UUID.randomUUID());
        admin.setEmail("hello@gmail.com");
        JwtUtil jwtUtil = new JwtUtil();
        String jwt = jwtUtil.generateJWTTokenAdmin(admin);
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
        GetDeliveryDetailRequest request = new GetDeliveryDetailRequest(deliveryID);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.getDeliveryDetail(request));
        assertEquals("User is not an admin.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when there are no details for the delivery or delivery does not exist.")
    @DisplayName("Invalid deliveryID")
    void invalidDeliveryIDPassedInRequestObject_IntegrationTest(){
        GetDeliveryDetailRequest request = new GetDeliveryDetailRequest(UUID.randomUUID());
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.getDeliveryDetail(request));
        assertEquals("No details found for this delivery.", thrown.getMessage());
    }

    @Test
    @Description("Tests for delivery details are found.")
    @DisplayName("Delivery details returned")
    void detailsReturnedSuccessfully_IntegrationTest() throws InvalidRequestException{
        GetDeliveryDetailRequest request = new GetDeliveryDetailRequest(deliveryID);
        GetDeliveryDetailResponse response = deliveryService.getDeliveryDetail(request);
        assertEquals(response.getMessage(), "Details successfully found.");
        assertEquals(response.getDetail().get(0).getDetail(), "detail1");
    }
}
