package cs.superleague.user;

import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.GroceryList;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.exceptions.CustomerDoesNotExistException;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.requests.UpdateCustomerDetailsRequest;
import cs.superleague.user.responses.UpdateCustomerDetailsResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/** Testing use cases with JUnit testing and Mockito */
@ExtendWith(MockitoExtension.class)
public class UpdateCustomerDetailsUnitTest {

    @Mock
    CustomerRepo customerRepo;

    @InjectMocks
    private UserServiceImpl userService;

    GroceryList groceryList;
    Customer customer, existingCustomer;
    Item I1;
    Item I2;

    UUID userID;
    UUID groceryListID;
    UUID expectedS1;

    GeoPoint deliveryAddress;

    @InjectMocks
    private JwtUtil jwtUtil;

    BCryptPasswordEncoder passwordEncoder;
    List<Item> listOfItems = new ArrayList<>();
    List<GroceryList> groceryLists = new ArrayList<>();
    List<Item> shoppingCart = new ArrayList<>();
    List<Item> shoppingCartNULL = new ArrayList<>();

    UpdateCustomerDetailsRequest request;
    UpdateCustomerDetailsResponse response;

    @BeforeEach
    void setUp() {
        userID = UUID.randomUUID();
        groceryListID = UUID.randomUUID();
        expectedS1 = UUID.randomUUID();

        I1=new Item("Heinz Tamatoe Sauce","123456","123456",expectedS1,36.99,1,"description","img/");
        I2=new Item("Bar one","012345","012345",expectedS1,14.99,3,"description","img/");
        listOfItems.add(I1);
        listOfItems.add(I2);

        shoppingCartNULL = null;
        shoppingCart.add(I1);
        shoppingCart.add(I2);

        deliveryAddress = new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");

        groceryList = new GroceryList(groceryListID, "Seamus' party", listOfItems);
        groceryLists.add(groceryList);

        passwordEncoder = new BCryptPasswordEncoder(15);

        customer = new Customer("D", "S", "ds@smallClub.com", "0721234567", passwordEncoder.encode("currentPassword"), new Date(), "", "", "", true,
                UserType.CUSTOMER, userID, deliveryAddress, groceryLists, shoppingCart, null, null);
        existingCustomer = new Customer("Davido", "Styles", "ds@smallSpursy.com", "0721234567", "", new Date(), "", "", "", true,
                UserType.CUSTOMER, UUID.randomUUID(), deliveryAddress, null, null, null, null);
    }

    @AfterEach
    void tearDown(){
    }


    @Test
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateCustomerDetails(null));
        assertEquals("UpdateCustomer Request is null - Could not update customer", thrown.getMessage());
    }

    @Test
    @DisplayName("When an Invalid email is given")
    void UnitTest_testingInvalidEmail(){
        request = new UpdateCustomerDetailsRequest("Dean", "Smith", "dsSmallFC.com",
                "0712345678", customer.getPassword(), deliveryAddress, "currentPassword");
        when(customerRepo.findByEmail(Mockito.any())).thenReturn(Optional.ofNullable(customer));

        try {
            response = userService.updateCustomerDetails(request);
            assertEquals("Email is not valid", response.getMessage());
            assertFalse(response.isSuccess());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When an Invalid password is given")
    void UnitTest_testingInvalidPassword(){
        request = new UpdateCustomerDetailsRequest("Dean", "Smith", "ds@smallFC.com",
                "0712345678", "password", deliveryAddress, "currentPassword");
        when(customerRepo.findByEmail(Mockito.any())).thenReturn(Optional.ofNullable(customer));
        when(customerRepo.findByEmail(request.getEmail())).thenReturn(Optional.ofNullable(null));

        try {
            response = userService.updateCustomerDetails(request);
            assertEquals("Password is not valid", response.getMessage());
            assertFalse(response.isSuccess());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When null update values are given")
    void UnitTest_testingNullUpdates(){
        request = new UpdateCustomerDetailsRequest( null, null, null,
                null, null, null, null);
        when(customerRepo.findByEmail(Mockito.any())).thenReturn(Optional.ofNullable(customer));

        try {
            response = userService.updateCustomerDetails(request);
            assertEquals("Null values submitted - Nothing updated", response.getMessage());
            assertFalse(response.isSuccess());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When user tries to update to existingEmail")
    void IntegrationTest_testingExistingEmailUpdateAttempt(){
        request = new UpdateCustomerDetailsRequest("Dean", "Smith", "ds@smallSpursy.com",
                "0712345678", "loL7&lol",deliveryAddress, "currentPassword");
        when(customerRepo.findByEmail(Mockito.any())).thenReturn(Optional.ofNullable(customer));
        when(customerRepo.findByEmail(request.getEmail())).thenReturn(Optional.ofNullable(existingCustomer));
        //when(customerRepo.findByEmail(Mockito.any())).thenReturn(existingCustomer);
        try {
            response = userService.updateCustomerDetails(request);
            assertEquals("Email is already taken", response.getMessage());
            assertFalse(response.isSuccess());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When nonnull update values are given")
    void UnitTest_testingSuccessfulUpdate(){
        request = new UpdateCustomerDetailsRequest("Dean", "Smith", "ds@smallFC.com",
                "0712345678", "loL7&lol", deliveryAddress, "currentPassword");
        when(customerRepo.findByEmail(Mockito.any())).thenReturn(Optional.ofNullable(customer));
        when(customerRepo.findByEmail(request.getEmail())).thenReturn(Optional.ofNullable(null));

        try {
            response = userService.updateCustomerDetails(request);
            assertEquals("Customer successfully updated", response.getMessage());
            assertTrue(response.isSuccess());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
}