package cs.superleague.user.integration;

import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.exceptions.ShopperDoesNotExistException;
import cs.superleague.user.repos.ShopperRepo;
import cs.superleague.user.requests.UpdateShopperDetailsRequest;
import cs.superleague.user.responses.UpdateShopperDetailsResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
public class UpdateShopperDetailsIntegrationTest {

    @Autowired
    ShopperRepo shopperRepo;

    @Autowired
    private UserServiceImpl userService;

    UpdateShopperDetailsRequest request;
    UUID shopperId= UUID.randomUUID();
    Shopper shopper;
    UpdateShopperDetailsResponse response;

    @BeforeEach
    void setUp(){
        request=new UpdateShopperDetailsRequest(shopperId,"name","surname","email@gmail.com","password","phoneNumber");
        shopper=new Shopper();
    }

    @AfterEach
    void tearDown(){
        shopperRepo.deleteAll();
    }

    @Test
    @DisplayName("When request object is not specified")
    void IntegrationTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateShopperDetails(null));
        assertEquals("UpdateShopper Request is null - Could not update shopper", thrown.getMessage());
    }

    @Test
    @DisplayName("When userID parameter is not specified")
    void IntegrationTest_testingNullRequestUserIDParameter(){
        request.setShopperID(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateShopperDetails(request));
        assertEquals("ShopperId is null - could not update shopper", thrown.getMessage());
    }

    @Test
    @DisplayName("Request object created correctly")
    void IntegrationTest_requestObjectCorrectlyCreated(){
        UUID storeID=UUID.randomUUID();
        UpdateShopperDetailsRequest req=new UpdateShopperDetailsRequest(shopperId,"n","s","e","pass","pN",storeID);
        assertNotNull(req);
        assertEquals(shopperId,req.getShopperID());
        assertEquals("n",req.getName());
        assertEquals("s",req.getSurname());
        assertEquals("e",req.getEmail());
        assertEquals("pass",req.getPassword());
        assertEquals("pN",req.getPhoneNumber());
        assertEquals(storeID,req.getStoreID());
    }

    @Test
    @DisplayName("When shopper with given UserID does not exist")
    void IntegrationTest_testingInvalidUser(){
        Throwable thrown = Assertions.assertThrows(ShopperDoesNotExistException.class, ()-> userService.updateShopperDetails(request));
        assertEquals("User with given userID does not exist - could not update shopper", thrown.getMessage());
    }

    @Test
    @DisplayName("When an Invalid email is given")
    void IntegrationTest_testingInvalidEmail(){
        request.setEmail("invalid");

        shopper.setShopperID(shopperId);
        shopperRepo.save(shopper);

        try {
            response = userService.updateShopperDetails(request);
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

        shopper.setShopperID(shopperId);
        shopperRepo.save(shopper);

        try {
            response = userService.updateShopperDetails(request);
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

        request = new UpdateShopperDetailsRequest(shopperId, null, null, null,
                null, null, null);

        shopper.setShopperID(shopperId);
        shopperRepo.save(shopper);

        try {
            response = userService.updateShopperDetails(request);
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

        shopper.setEmail("validEmail@gmail.com");
        shopper.setShopperID(shopperId);
        shopperRepo.save(shopper);

        Shopper newShopper = new Shopper();
        newShopper.setEmail(request.getEmail());
        newShopper.setShopperID(UUID.randomUUID());
        shopperRepo.save(newShopper);

        try {
            response = userService.updateShopperDetails(request);
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
        shopper.setEmail("validEmail@gmail.com");
        shopper.setShopperID(shopperId);
        shopperRepo.save(shopper);

        try {
            response = userService.updateShopperDetails(request);
            assertEquals("Shopper successfully updated", response.getMessage());
            assertTrue(response.isSuccess());
            assertNotNull(response.getTimestamp());

            Optional<Shopper> checkShopper=shopperRepo.findById(shopperId);
            assertNotNull(checkShopper);
            assertEquals(shopperId, checkShopper.get().getShopperID());
            assertEquals(request.getEmail(),checkShopper.get().getEmail());
            assertEquals(request.getName(),checkShopper.get().getName());
            assertEquals(request.getSurname(),checkShopper.get().getSurname());
            assertEquals(request.getPhoneNumber(),checkShopper.get().getPhoneNumber());
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(15);
            passwordEncoder.matches(request.getPassword(),checkShopper.get().getPassword());

        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
}
