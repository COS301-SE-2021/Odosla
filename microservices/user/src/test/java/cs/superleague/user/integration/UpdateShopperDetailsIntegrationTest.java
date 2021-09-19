//package cs.superleague.user.integration;
//
//import cs.superleague.integration.security.JwtUtil;
//import cs.superleague.user.UserServiceImpl;
//import cs.superleague.user.dataclass.Shopper;
//import cs.superleague.user.dataclass.UserType;
//import cs.superleague.user.exceptions.InvalidRequestException;
//import cs.superleague.user.exceptions.ShopperDoesNotExistException;
//import cs.superleague.user.repos.ShopperRepo;
//import cs.superleague.user.requests.UpdateShopperDetailsRequest;
//import cs.superleague.user.responses.UpdateShopperDetailsResponse;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//@SpringBootTest
//@Transactional
//public class UpdateShopperDetailsIntegrationTest {
//
//    @Autowired
//    ShopperRepo shopperRepo;
//
//    @Autowired
//    private UserServiceImpl userService;
//
//    @Autowired
//    JwtUtil jwtTokenUtil;
//
//    BCryptPasswordEncoder passwordEncoder;
//    UpdateShopperDetailsRequest request;
//    UUID shopperId = UUID.randomUUID();
//    Shopper shopper;
//    UpdateShopperDetailsResponse response;
//
//    String jwtTokenShopper;
//
//    Claims claims;
//
//    @Value("${env.SECRET}")
//    private String SECRET = "stub";
//
//    @Value("${env.HEADER}")
//    private String HEADER = "stub";
//
//    @BeforeEach
//    void setUp() {
//        passwordEncoder = new BCryptPasswordEncoder(15);
//
//        request = new UpdateShopperDetailsRequest("name","surname","email@gmail.com","phoneNumber","password", "validPassword@1");
//        shopper = new Shopper("", "", "validEmail@gmail.com", "0721234567", passwordEncoder.encode("validPassword@1"), "", UserType.SHOPPER, shopperId);
//        shopper.setAccountType(UserType.SHOPPER);
//
//        jwtTokenShopper = jwtTokenUtil.generateJWTTokenShopper(shopper).replace(HEADER,"");
//        claims = Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtTokenShopper).getBody();
//
//        List<String> authorities = (List) claims.get("authorities");
//
//        String userType= (String) claims.get("userType");
//        String email = (String) claims.get("email");
//
//        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
//                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
//        HashMap<String, Object> info=new HashMap<String, Object>();
//        info.put("userType",userType);
//        info.put("email",email);
//        auth.setDetails(info);
//        SecurityContextHolder.getContext().setAuthentication(auth);
//
//        shopperRepo.save(shopper);
//    }
//
//    @AfterEach
//    void tearDown(){
//
//    }
//
//    @Test
//    @DisplayName("When request object is not specified")
//    void IntegrationTest_testingNullRequestObject(){
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateShopperDetails(null));
//        assertEquals("UpdateShopper Request is null - Could not update shopper", thrown.getMessage());
//    }
//
//    @Test
//    @DisplayName("Request object created correctly")
//    void IntegrationTest_requestObjectCorrectlyCreated(){
//        UpdateShopperDetailsRequest req=new UpdateShopperDetailsRequest("n","s","e","pass","pN", "currentPassword");
//        assertNotNull(req);
//        assertEquals("n",req.getName());
//        assertEquals("s",req.getSurname());
//        assertEquals("e",req.getEmail());
//        assertEquals("pass",req.getPhoneNumber());
//        assertEquals("pN",req.getPassword());
//        assertEquals("currentPassword", req.getCurrentPassword());
//    }
//
//    @Test
//    @DisplayName("When shopper with given Email does not exist")
//    void IntegrationTest_testingInvalidUser(){
//        shopper.setEmail("superleague301@gmail.com");
//        shopperRepo.save(shopper);
//
//        Throwable thrown = Assertions.assertThrows(ShopperDoesNotExistException.class, ()-> userService.updateShopperDetails(request));
//        assertEquals("User with the given email does not exist - could not update shopper", thrown.getMessage());
//    }
//
//    @Test
//    @DisplayName("When an Invalid email is given")
//    void IntegrationTest_testingInvalidEmail(){
//        request.setEmail("invalid");
//        shopper.setShopperID(shopperId);
//        shopperRepo.save(shopper);
//
//        try {
//            response = userService.updateShopperDetails(request);
//            assertEquals("Email is not valid", response.getMessage());
//            assertFalse(response.isSuccess());
//            assertNotNull(response.getTimestamp());
//        }catch(Exception e){
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    @Test
//    @DisplayName("When an Invalid password is given")
//    void IntegrationTest_testingInvalidPassword(){
//
//        shopper.setShopperID(shopperId);
//        shopper.setPassword(passwordEncoder.encode(request.getCurrentPassword()));
//        shopperRepo.save(shopper);
//
//        try {
//            response = userService.updateShopperDetails(request);
//            assertEquals("Password is not valid", response.getMessage());
//            assertFalse(response.isSuccess());
//            assertNotNull(response.getTimestamp());
//        }catch(Exception e){
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    @Test
//    @DisplayName("When null update values are given")
//    void IntegrationTest_testingNullUpdates(){
//        request = new UpdateShopperDetailsRequest(null, null, null,
//                null, null, "currentPassword");
//
//        shopper.setShopperID(shopperId);
//        shopperRepo.save(shopper);
//
//        try {
//            response = userService.updateShopperDetails(request);
//            assertEquals("Null values submitted - Nothing updated", response.getMessage());
//            assertFalse(response.isSuccess());
//            assertNotNull(response.getMessage());
//        }catch(Exception e){
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    @Test
//    @DisplayName("When user tries to update to existingEmail")
//    void IntegrationTest_testingExistingEmailUpdateAttempt(){
//        shopper.setEmail("validEmail@gmail.com");
//        shopper.setShopperID(shopperId);
//        shopperRepo.save(shopper);
//
//        Shopper newShopper = new Shopper();
//        newShopper.setEmail(request.getEmail());
//        newShopper.setShopperID(UUID.randomUUID());
//        shopperRepo.save(newShopper);
//
//        try {
//            response = userService.updateShopperDetails(request);
//            assertEquals("Email is already taken", response.getMessage());
//            assertFalse(response.isSuccess());
//            assertNotNull(response.getTimestamp());
//        }catch(Exception e){
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    @Test
//    @DisplayName("When nonnull update values are given")
//    void IntegrationTest_testingSuccessfulUpdate(){
//
//        request.setPassword("validPassword@3");
//
//        try {
//            response = userService.updateShopperDetails(request);
//            assertEquals("Shopper successfully updated", response.getMessage());
//            assertTrue(response.isSuccess());
//            assertNotNull(response.getTimestamp());
//
//            /* Ensure shopper with same ID's details have been changed */
//            Optional<Shopper> checkShopper=shopperRepo.findById(shopperId);
//            assertNotNull(checkShopper);
//            assertEquals(shopperId, checkShopper.get().getShopperID());
//            assertEquals(request.getEmail(),checkShopper.get().getEmail());
//            assertEquals(request.getName(),checkShopper.get().getName());
//            assertEquals(request.getSurname(),checkShopper.get().getSurname());
//            assertEquals(request.getPhoneNumber(),checkShopper.get().getPhoneNumber());
//            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(15);
//            passwordEncoder.matches(request.getPassword(),checkShopper.get().getPassword());
//
//        }catch(Exception e){
//            e.printStackTrace();
//            fail();
//        }
//    }
//}
