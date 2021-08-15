package cs.superleague.user;

import cs.superleague.integration.security.JwtUtil;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.GroceryList;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.exceptions.UserException;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.ShopperRepo;
import cs.superleague.user.requests.GetGroceryListsRequest;
import cs.superleague.user.responses.GetGroceryListsResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetGroceryListsUnitTest {

    @Mock
    private ShopperRepo shopperRepo;

    @Mock
    private CustomerRepo customerRepo;

    @InjectMocks
    private UserServiceImpl userService;

    @InjectMocks
    JwtUtil jwtTokenUtil;

    Shopper shopper;
    Customer customer;

    UUID customerUUID = UUID.randomUUID();
    UUID shopperUUID=UUID.randomUUID();

    String shopperJWT;
    String customerJWT;

    List<GroceryList> groceryLists = new ArrayList<>();

    @BeforeEach
    void setUp()
    {
        shopper = new Shopper();
        shopper.setShopperID(shopperUUID);
        shopper.setName("JJ");
        shopper.setAccountType(UserType.SHOPPER);

        customer = new Customer();
        customer.setCustomerID(customerUUID);
        customer.setName("Pep");
        customer.setGroceryLists(groceryLists);
        customer.setAccountType(UserType.CUSTOMER);

        shopperJWT = jwtTokenUtil.generateJWTTokenShopper(shopper);
        customerJWT = jwtTokenUtil.generateJWTTokenCustomer(customer);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests for when GetUsers is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.getGroceryLists(null));
        assertEquals("GetGroceryList request is null - could not return groceryList", thrown.getMessage());
    }

    @Test
    @DisplayName("Invalid User Type JWT")
    void UnitTest_invalidUserTypeJWT(){

        GetGroceryListsRequest request = new GetGroceryListsRequest(shopperJWT);
        when(shopperRepo.findByEmail(Mockito.any())).thenReturn(Optional.ofNullable(shopper));

        try{
            GetGroceryListsResponse response = userService.getGroceryLists(request);
            assertNull(response.getGroceryLists());
            assertFalse(response.isSuccess());
            assertEquals("Invalid JWTToken for Customer Account type", response.getMessage());
        }catch (UserException e){
            e.printStackTrace();
        }

    }

    @Test
    @DisplayName("Valid User Type JWT")
    void UnitTest_ValidUserTypeJWT(){

        GetGroceryListsRequest request = new GetGroceryListsRequest(customerJWT);

        when(customerRepo.findByEmail(Mockito.any())).thenReturn(Optional.ofNullable(customer));
        try{
            GetGroceryListsResponse response = userService.getGroceryLists(request);
            assertNotNull(response.getGroceryLists());
            assertTrue(response.isSuccess());
            assertEquals("Grocery list successfully retrieved", response.getMessage());
        }catch (UserException e){
            e.printStackTrace();
        }

    }
}
