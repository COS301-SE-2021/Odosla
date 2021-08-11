package cs.superleague.user.integration;

import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.exceptions.ShopperDoesNotExistException;
import cs.superleague.user.repos.ShopperRepo;
import cs.superleague.user.requests.CompletePackagingOrderRequest;
import cs.superleague.user.requests.GetShopperByUUIDRequest;
import cs.superleague.user.responses.GetShopperByUUIDResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class GetShopperByUUIDIntegrationTest {

    @Autowired
    ShopperRepo shopperRepo;

    @Autowired
    private UserServiceImpl userService;

    GetShopperByUUIDRequest request;
    GetShopperByUUIDResponse response;

    Shopper shopper;
    UUID shopperUUID=UUID.randomUUID();

    @BeforeEach
    void setUp()
    {
        shopper = new Shopper();
        shopper.setShopperID(shopperUUID);
        shopper.setName("JJ");
        shopperRepo.save(shopper);
    }

    @AfterEach
    void tearDown() {
        shopperRepo.deleteAll();
    }

    @Test
    @Description("Tests for when GetShopperByUUID is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is null")
    void IntegrationTest_GetShopperByUUIDRequestObject(){
        request = null;
        assertThrows(InvalidRequestException.class, ()-> {
            response = userService.getShopperByUUIDRequest(request);
        });
    }

    @Test
    @Description("Tests for when GetShopperByUUID is submitted with a null request parameter- exception should be thrown")
    @DisplayName("When userID parameter is not specified")
    void IntegrationTest_testingNullRequestUserIDParameter(){
        request = new GetShopperByUUIDRequest(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.getShopperByUUIDRequest(request));
        assertEquals("UserID is null in GetShopperByUUIDRequest request - could not return shopper entity", thrown.getMessage());
    }

    @Test
    @Description("Test for when Shopper with userID does not exist in database - ShopperDoesNotExistException should be thrown")
    @DisplayName("When Shopper with ID doesn't exist")
    void IntegrationTest_testingInvalidShopper(){
        request = new GetShopperByUUIDRequest(UUID.randomUUID());
        Throwable thrown = Assertions.assertThrows(ShopperDoesNotExistException.class, ()-> userService.getShopperByUUIDRequest(request));
        assertEquals("User with ID does not exist in repository - could not get Shopper entity", thrown.getMessage());
    }

    @Test
    @Description("Test for when Shopper exists in database - should return correct response")
    @DisplayName("When Shopper exists and response is returned")
    void IntegrationTest_Shopper_exist_returning_Shopper() throws InvalidRequestException, ShopperDoesNotExistException {
        request = new GetShopperByUUIDRequest(shopperUUID);
        response= userService.getShopperByUUIDRequest(request);
        assertNotNull(response);
        assertEquals(shopper.getShopperID(),response.getShopper().getShopperID());
        assertEquals("Shopper entity with corresponding user id was returned",response.getMessage());

    }

    /** Checking request object is created correctly */
    @Test
    @Description("Tests whether the GetShopperByUUID request object was created correctly")
    @DisplayName("GetShopperByUUIDRequest correctly constructed")
    void IntegrationTest_GetShopperByUUIDRequestConstruction() {
        request = new GetShopperByUUIDRequest(shopperUUID);
        assertNotNull(request);
    }
}
