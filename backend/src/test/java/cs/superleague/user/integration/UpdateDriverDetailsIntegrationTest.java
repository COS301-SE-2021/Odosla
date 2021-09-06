package cs.superleague.user.integration;

import cs.superleague.integration.security.JwtUtil;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.exceptions.DriverDoesNotExistException;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.DriverRepo;
import cs.superleague.user.requests.UpdateDriverDetailsRequest;
import cs.superleague.user.responses.UpdateDriverDetailsResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UpdateDriverDetailsIntegrationTest {

    @Autowired
    DriverRepo driverRepo;

    @Autowired
    private UserServiceImpl userService;@Autowired
    JwtUtil jwtTokenUtil;


    BCryptPasswordEncoder passwordEncoder;
    UpdateDriverDetailsRequest request;
    UUID driverId=UUID.randomUUID();
    Driver driver;
    Driver driverExisting;
    UpdateDriverDetailsResponse response;

    String jwtTokenDriver;

    Claims claims;
    private final String SECRET = "uQmMa86HgOi6uweJ1JSftIN7TBHFDa3KVJh6kCyoJ9bwnLBqA0YoCAhMMk";

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder(15);

        request = new UpdateDriverDetailsRequest("name","surname","superleague301@gmail.com","password","phoneNumber", "currentPassword");
        driver = new Driver();
        driver.setEmail("email@gmail.com");
        driver.setDriverID(driverId);
        driver.setPassword(passwordEncoder.encode(request.getCurrentPassword()));
        driverRepo.save(driver);

        jwtTokenDriver = jwtTokenUtil.generateJWTTokenDriver(driver).replace("Bearer ","");
        claims = Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtTokenDriver).getBody();

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

        driverExisting = new Driver();
        driverExisting.setDriverID(UUID.randomUUID());
        driverExisting.setEmail("validEmail@gmail.com");
        driverRepo.save(driverExisting);
    }

    @AfterEach
    void tearDown(){
    }

    @Test
    @DisplayName("When request object is not specified")
    void IntegrationTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateDriverDetails(null));
        assertEquals("UpdateDriver Request is null - Could not update driver", thrown.getMessage());
    }

    @Test
    @DisplayName("Request object created correctly")
    void IntegrationTest_requestObjectCorrectlyCreated(){
        UpdateDriverDetailsRequest req=new UpdateDriverDetailsRequest("n","s","e","pass","pN", "currentPassword");
        assertNotNull(req);
        assertEquals("n",req.getName());
        assertEquals("s",req.getSurname());
        assertEquals("e",req.getEmail());
        assertEquals("pass",req.getPassword());
        assertEquals("pN",req.getPhoneNumber());
        assertEquals("currentPassword", req.getCurrentPassword());
    }

    @Test
    @DisplayName("When driver with given email does not exist")
    void IntegrationTest_testingInvalidUser(){
        driver.setEmail("superleague301@gmail.com");
        driverRepo.save(driver);

        Throwable thrown = Assertions.assertThrows(DriverDoesNotExistException.class, ()-> userService.updateDriverDetails(request));
        assertEquals("User with given email does not exist - could not update driver", thrown.getMessage());
    }

    @Test
    @DisplayName("When an Invalid email is given")
    void IntegrationTest_testingInvalidEmail(){
        request.setEmail("invalid");

        try {
            response = userService.updateDriverDetails(request);
            System.out.println(request.getEmail());
            System.out.println(driver.getEmail());
            assertEquals("Email is not valid", response.getMessage());
            assertFalse(response.isSuccess());
            assertNotNull(response.getTimestamp());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When an Invalid password is given")
    void IntegrationTest_testingInvalidPassword(){


        try {
            response = userService.updateDriverDetails(request);
            assertEquals("Password is not valid", response.getMessage());
            assertFalse(response.isSuccess());
            assertNotNull(response.getTimestamp());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When null update values are given")
    void IntegrationTest_testingNullUpdates(){
        request = new UpdateDriverDetailsRequest( null, null, null,
                null, null, "currentPassword");

        try {
            response = userService.updateDriverDetails(request);
            assertEquals("Null values submitted - Nothing updated", response.getMessage());
            assertFalse(response.isSuccess());
            assertNotNull(response.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When user tries to update to existingEmail")
    void IntegrationTest_testingExistingEmailUpdateAttempt(){

        Driver newDriver=new Driver();
        newDriver.setEmail(request.getEmail());
        newDriver.setDriverID(UUID.randomUUID());
        driverRepo.save(newDriver);

        try {
            response = userService.updateDriverDetails(request);
            assertEquals("Email is already taken", response.getMessage());
            assertFalse(response.isSuccess());
            assertNotNull(response.getTimestamp());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When nonnull update values are given")
    void IntegrationTest_testingSuccessfulUpdate(){

        request.setPassword("validPassword@1");

        try {
            response = userService.updateDriverDetails(request);
            assertEquals("Driver successfully updated", response.getMessage());
            assertTrue(response.isSuccess());
            assertNotNull(response.getTimestamp());

            /* Ensure driver with same ID's details have been changed */
            Optional<Driver> checkDriver=driverRepo.findById(driverId);
            assertNotNull(checkDriver);
            assertEquals(driverId, checkDriver.get().getDriverID());
            assertEquals(request.getEmail(),checkDriver.get().getEmail());
            assertEquals(request.getName(),checkDriver.get().getName());
            assertEquals(request.getSurname(),checkDriver.get().getSurname());
            assertEquals(request.getPhoneNumber(),checkDriver.get().getPhoneNumber());
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(15);
            passwordEncoder.matches(request.getPassword(),checkDriver.get().getPassword());

        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
}
