//package cs.superleague.user.integration;
//
//import cs.superleague.integration.security.JwtUtil;
//import cs.superleague.user.UserServiceImpl;
//import cs.superleague.user.dataclass.Driver;
//import cs.superleague.user.dataclass.Shopper;
//import cs.superleague.user.dataclass.UserType;
//import cs.superleague.user.exceptions.DriverDoesNotExistException;
//import cs.superleague.user.exceptions.InvalidRequestException;
//import cs.superleague.user.exceptions.UserException;
//import cs.superleague.user.repos.DriverRepo;
//import cs.superleague.user.repos.ShopperRepo;
//import cs.superleague.user.requests.UpdateDriverShiftRequest;
//import cs.superleague.user.responses.UpdateDriverShiftResponse;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import org.junit.jupiter.api.*;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//import static org.junit.Assert.*;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//@SpringBootTest
//@Transactional
//public class UpdateDriverShiftIntegrationTest {
//
//
//    @Autowired
//    DriverRepo driverRepo;
//
//    @Autowired
//    ShopperRepo shopperRepo;
//
//    @Autowired
//    JwtUtil jwtTokenUtil;
//
//    @Autowired
//    private UserServiceImpl userService;
//
//    UpdateDriverShiftRequest request;
//    UpdateDriverShiftResponse response;
//    UUID driverID= UUID.randomUUID();
//    Driver driver;
//    Shopper shopper;
//
//
//    String shopperJWT;
//    String driverJWT;
//
//    @Value("${env.SECRET}")
//    private String SECRET = "stub";
//
//    @Value("${env.HEADER}")
//    private String HEADER= "stub";
//
//    @BeforeEach
//    void setUp() {
//
//        driver=new Driver();
//        driver.setDriverID(driverID);
//        driver.setOnShift(true);
//        driver.setAccountType(UserType.DRIVER);
//        driver.setEmail("driver@gmaill.com");
//        driverRepo.save(driver);
//
//        shopper = new Shopper();
//        shopper.setShopperID(UUID.randomUUID());
//        shopper.setAccountType(UserType.SHOPPER);
//        shopper.setEmail("Email@shhhopper.com");
//        shopperRepo.save(shopper);
//
//        driverJWT = jwtTokenUtil.generateJWTTokenDriver(driver);
//        shopperJWT = jwtTokenUtil.generateJWTTokenShopper(shopper);
//
//        String jwt = driverJWT.replace(HEADER,"");
//        Claims claims= Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwt).getBody();
//        List<String> authorities = (List) claims.get("authorities");
//        String userType= (String) claims.get("userType");
//        String email = (String) claims.get("email");
//        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
//                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
//        HashMap<String, Object> info=new HashMap<String, Object>();
//        info.put("userType",userType);
//        info.put("email",email);
//        auth.setDetails(info);
//        SecurityContextHolder.getContext().setAuthentication(auth);
//
//        request=new UpdateDriverShiftRequest(true);
//    }
//
//    @AfterEach
//    void tearDown(){
//
//        driverRepo.deleteAll();
//        SecurityContextHolder.clearContext();
//    }
//
//    @Test
//    @DisplayName("When request object is not specified")
//    void IntegrationTest_testingNullRequestObject(){
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateDriverShift(null));
//        assertEquals("UpdateDriverShiftRequest object is null", thrown.getMessage());
//    }
//
//
//    @Test
//    @DisplayName("When onShift parameter is not specified")
//    void IntegrationTest_testingNullRequestOnShiftParameter(){
//        request.setOnShift(null);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateDriverShift(request));
//        assertEquals("onShift in UpdateDriverShiftRequest is null", thrown.getMessage());
//    }
//
//    @Test
//    @DisplayName("When driver is already on Shift")
//    void IntegrationTest_testingDriverAlreadyOnShift() throws InvalidRequestException, DriverDoesNotExistException {
//        driverRepo.save(driver);
//        response= userService.updateDriverShift(request);
//        assertNotNull(response);
//        assertFalse(response.isSuccess());
//        assertNotNull(response.getTimestamp());
//        assertEquals("Driver is already on shift",response.getMessage());
//    }
//
//    @Test
//    @DisplayName("When driver is already not on Shift")
//    void IntegrationTest_testingDriverAlreadyNotOnShift() throws InvalidRequestException, DriverDoesNotExistException {
//        request.setOnShift(false);
//        driver.setOnShift(false);
//        driverRepo.save(driver);
//        response= userService.updateDriverShift(request);
//        assertNotNull(response);
//        assertFalse(response.isSuccess());
//        assertNotNull(response.getTimestamp());
//        assertEquals("Driver is already not on shift",response.getMessage());
//    }
//
//    @Test
//    @DisplayName("When driver's shift successfully updated")
//    void IntegrationTest_testingDriverShiftSuccessfullyUpdated() throws InvalidRequestException, DriverDoesNotExistException {
//        request.setOnShift(false);
//        driver.setOnShift(true);
//        driverRepo.save(driver);
//        response= userService.updateDriverShift(request);
//        assertNotNull(response);
//        assertTrue(response.isSuccess());
//        assertNotNull(response.getTimestamp());
//        assertEquals("Driver's shift correctly updated",response.getMessage());
//    }
//}
