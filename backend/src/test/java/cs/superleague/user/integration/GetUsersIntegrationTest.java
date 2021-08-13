package cs.superleague.user.integration;

import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.*;
import cs.superleague.user.exceptions.AdminDoesNotExistException;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.AdminRepo;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.DriverRepo;
import cs.superleague.user.repos.ShopperRepo;
import cs.superleague.user.requests.GetUsersRequest;
import cs.superleague.user.responses.GetUsersResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
public class GetUsersIntegrationTest {

    @Autowired
    private ShopperRepo shopperRepo;

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private DriverRepo driverRepo;

    @Autowired
    private CustomerRepo customerRepo;

    @Autowired
    private UserServiceImpl userService;

    Shopper shopper;
    Driver driver;
    Customer customer;
    Admin admin;

    UUID adminUUID = UUID.randomUUID();
    UUID customerUUID = UUID.randomUUID();
    UUID driverUUID = UUID.randomUUID();
    UUID shopperUUID=UUID.randomUUID();

    @BeforeEach
    void setUp()
    {
        shopper = new Shopper();
        shopper.setShopperID(shopperUUID);
        shopper.setName("JJ");
        shopperRepo.save(shopper);

        driver = new Driver();
        driver.setDriverID(driverUUID);
        driver.setName("Kane");
        driverRepo.save(driver);

        admin = new Admin();
        admin.setAdminID(adminUUID);
        admin.setName("Levy");
        adminRepo.save(admin);

        customer = new Customer();
        customer.setCustomerID(customerUUID);
        customer.setName("Pep");
        customerRepo.save(customer);

//        customer = new Customer("D", "S", "ds@smallClub.com", "0721234567", "", new Date(), "", "", "", true,
//            UserType.CUSTOMER, null, null, null, null, null, null);

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests for when GetUsers is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void IntegrationTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.getUsers(null));
        assertEquals("GetUser request is null - could not return users", thrown.getMessage());
    }

    @Test
    @Description("Tests for when adminID in request object is null- exception should be thrown")
    @DisplayName("When request object parameter - adminID - is not specified")
    void IntegrationTest_testingNull_adminID_Parameter_RequestObject(){
        GetUsersRequest request=new GetUsersRequest(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.getUsers(request));
        assertEquals("AdminID is null in GetUsersRequest request - could not return users", thrown.getMessage());
    }

    @Test
    @Description("Tests for when adminID does not exist- exception should be thrown")
    @DisplayName("When request object parameter - adminID - is not specified")
    void IntegrationTest_testing_adminID_Parameter_RequestObject_Does_Not_Exist(){
        GetUsersRequest request=new GetUsersRequest(UUID.randomUUID().toString());
        Throwable thrown = Assertions.assertThrows(AdminDoesNotExistException.class, ()-> userService.getUsers(request));
        assertEquals("admin with given userID does not exist - could not return users", thrown.getMessage());
    }

    @Test
    @Description("Test for when admin does exist")
    @DisplayName("When admin with ID does exist")
    void IntegrationTest_Admin_does_exist() throws Exception {
        GetUsersRequest request= new GetUsersRequest(adminUUID.toString());
        GetUsersResponse response= userService.getUsers(request);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Users successfully returned", response.getMessage());
    }
}
