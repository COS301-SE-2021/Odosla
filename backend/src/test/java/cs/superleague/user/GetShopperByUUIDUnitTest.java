package cs.superleague.user;

import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.exceptions.ShopperDoesNotExistException;
import cs.superleague.user.repos.ShopperRepo;
import cs.superleague.user.requests.CompletePackagingOrderRequest;
import cs.superleague.user.requests.GetShopperByUUIDRequest;
import cs.superleague.user.responses.CompletePackagingOrderResponse;
import cs.superleague.user.responses.GetShopperByUUIDResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetShopperByUUIDUnitTest {

    @Mock
    private ShopperRepo shopperRepo;

    @InjectMocks
    private UserServiceImpl userService;

    Shopper shopper;
    UUID shopperUUID=UUID.randomUUID();

    @BeforeEach
    void setUp()
    {
        shopper = new Shopper();
        shopper.setShopperID(shopperUUID);
        shopper.setName("JJ");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests for when GetShopperByUUID is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.getShopperByUUIDRequest(null));
        assertEquals("GetShopperByUUID request is null - could not return Shopper entity", thrown.getMessage());
    }

    @Test
    @Description("Tests for when shopperID in request object is null- exception should be thrown")
    @DisplayName("When request object parameter -shopperID - is not specified")
    void UnitTest_testingNull_shopperID_Parameter_RequestObject(){
        GetShopperByUUIDRequest request=new GetShopperByUUIDRequest(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.getShopperByUUIDRequest(request));
        assertEquals("UserID is null in GetShopperByUUIDRequest request - could not return shopper entity", thrown.getMessage());
    }

    @Test
    @Description("Test for when Shopper with userID does not exist in database - ShopperDoesNotExistException should be thrown")
    @DisplayName("When Order with ID doesn't exist")
    void UnitTest_Shopper_doesnt_exist(){

        GetShopperByUUIDRequest request=new GetShopperByUUIDRequest(UUID.randomUUID());
        when(shopperRepo.findById(Mockito.any())).thenReturn(null);
        Throwable thrown = Assertions.assertThrows(ShopperDoesNotExistException.class, ()-> userService.getShopperByUUIDRequest(request));
        assertEquals("User with ID does not exist in repository - could not get Shopper entity", thrown.getMessage());
    }

    @Test
    @Description("Test for when shopper does exist")
    @DisplayName("When Shopper with ID does exist")
    void UnitTest_Shopper_does_exist() throws ShopperDoesNotExistException, InvalidRequestException {

        GetShopperByUUIDRequest request= new GetShopperByUUIDRequest(shopperUUID);
        when(shopperRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(shopper));
        GetShopperByUUIDResponse response= userService.getShopperByUUIDRequest(request);
        assertNotNull(response);
        assertEquals(shopper,response.getShopper());
        assertEquals("Shopper entity with corresponding user id was returned",response.getMessage());
    }
}
