package cs.superleague.user.integration;

import cs.superleague.integration.security.JwtUtil;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
//import cs.superleague.shopping.requests.SaveStoreToRepoRequest;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.exceptions.ShopperDoesNotExistException;
import cs.superleague.user.repos.ShopperRepo;
import cs.superleague.user.requests.UpdateShopperShiftRequest;
import cs.superleague.user.responses.UpdateShopperShiftResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class UpdateShopperShiftIntegrationTest {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    ShopperRepo shopperRepo;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    JwtUtil jwtTokenUtil;

    UpdateShopperShiftRequest request;
    UpdateShopperShiftResponse response;
    UUID shopperID= UUID.fromString("17a3d7eb-9af6-434a-ab33-973619a62b2c");
    Shopper shopper;
    String shopperJWT;
    Store store;
    UUID storeID = UUID.fromString("da6ea7a1-6a33-4a51-89c5-2d1925357398");

    @Value("${env.SECRET}")
    private String SECRET = "stub";

    @Value("${env.HEADER}")
    private String HEADER = "stub";

    @BeforeEach
    void setUp() {
        store = new Store();
        store.setStoreID(storeID);
        shopper=new Shopper();
        shopper.setShopperID(shopperID);
        shopper.setEmail("hello@gmail.com");
        shopper.setOnShift(true);
        request=new UpdateShopperShiftRequest(true, storeID);
//        SaveStoreToRepoRequest saveStoreToRepoRequest = new SaveStoreToRepoRequest(store);
//        rabbitTemplate.convertAndSend("ShoppingEXCHANGE", "RK_SaveStoreToRepo", saveStoreToRepoRequest);
        String jwt=jwtTokenUtil.generateJWTTokenShopper(shopper);
        jwt = jwt.replace(HEADER,"");
        Claims claims= Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwt).getBody();
        List<String> authorities = (List) claims.get("authorities");
        String userType= (String) claims.get("userType");
        String email = (String) claims.get("email");
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
        shopperRepo.deleteAll();
        SecurityContextHolder.clearContext();

    }

    @Test
    @DisplayName("When request object is not specified")
    void IntegrationTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateShopperShift(null));
        assertEquals("UpdateShopperShiftRequest object is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When onShift parameter is not specified")
    void IntegrationTest_testingNullRequestOnShiftParameter(){
        request.setOnShift(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateShopperShift(request));
        assertEquals("onShift in UpdateShopperShiftRequest is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When storeID parameter is not specified")
    void IntegrationTest_testingNullRequestStoreIDParameter(){
        request.setStoreID(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateShopperShift(request));
        assertEquals("StoreID in UpdateShopperShiftRequest is null", thrown.getMessage());
    }

    @Test
    @DisplayName("When shopper with shopperID does not exist")
    void IntegrationTest_testingShopperDoesNotExist(){
        Throwable thrown = Assertions.assertThrows(ShopperDoesNotExistException.class, ()-> userService.updateShopperShift(request));
        assertEquals("Shopper with shopperID does not exist in database", thrown.getMessage());
    }

    @Test
    @DisplayName("When shopper is already on Shift")
    void IntegrationTest_testingShopperAlreadyOnShift() throws InvalidRequestException, ShopperDoesNotExistException, StoreDoesNotExistException, URISyntaxException {
        shopperRepo.save(shopper);
        response= userService.updateShopperShift(request);
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Shopper is already on shift",response.getMessage());
    }

    @Test
    @DisplayName("When shopper is already not on Shift")
    void IntegrationTest_testingShopperAlreadyNotOnShift() throws InvalidRequestException, ShopperDoesNotExistException, StoreDoesNotExistException, URISyntaxException {
        request.setOnShift(false);
        shopper.setOnShift(false);
        shopperRepo.save(shopper);
        response= userService.updateShopperShift(request);
        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Shopper is already not on shift",response.getMessage());
    }

    @Test
    @DisplayName("When shopper's shift successfully changed to false")
    void IntegrationTest_testingShopperShiftSuccessfullyUpdatedToFalse() throws InvalidRequestException, ShopperDoesNotExistException, StoreDoesNotExistException, URISyntaxException {
        request.setOnShift(false);
        shopper.setOnShift(true);
        shopperRepo.save(shopper);
        response= userService.updateShopperShift(request);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Shopper's shift correctly updated",response.getMessage());
    }

    @Test
    @DisplayName("When shopper's shift successfully changed to true")
    void IntegrationTest_testingShopperShiftSuccessfullyUpdatedToTrue() throws InvalidRequestException, ShopperDoesNotExistException, StoreDoesNotExistException, URISyntaxException {
        request.setOnShift(true);
        shopper.setOnShift(false);
        shopperRepo.save(shopper);
        response= userService.updateShopperShift(request);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertNotNull(response.getTimestamp());
        assertEquals("Shopper's shift correctly updated",response.getMessage());
    }


    @Test
    @DisplayName("When the shop isn't in the database")
    void IntegrationTest_ShopIsNotInTheDataBase() {
        request.setStoreID(UUID.randomUUID());
        request.setOnShift(false);
        shopper.setOnShift(true);
        shopperRepo.save(shopper);
        Throwable thrown = Assertions.assertThrows(StoreDoesNotExistException.class, ()-> userService.updateShopperShift(request));
        assertEquals(thrown.getMessage(), "Store is not saved in database.");
    }
}
