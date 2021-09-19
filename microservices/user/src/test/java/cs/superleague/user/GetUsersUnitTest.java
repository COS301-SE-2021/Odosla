//package cs.superleague.user;
//
//import cs.superleague.user.dataclass.Admin;
//import cs.superleague.user.dataclass.Customer;
//import cs.superleague.user.dataclass.Driver;
//import cs.superleague.user.dataclass.Shopper;
//import cs.superleague.user.exceptions.AdminDoesNotExistException;
//import cs.superleague.user.exceptions.InvalidRequestException;
//import cs.superleague.user.exceptions.ShopperDoesNotExistException;
//import cs.superleague.user.repos.AdminRepo;
//import cs.superleague.user.repos.CustomerRepo;
//import cs.superleague.user.repos.DriverRepo;
//import cs.superleague.user.repos.ShopperRepo;
//import cs.superleague.user.requests.GetShopperByUUIDRequest;
//import cs.superleague.user.requests.GetUsersRequest;
//import cs.superleague.user.responses.GetShopperByUUIDResponse;
//import cs.superleague.user.responses.GetUsersResponse;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Description;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class GetUsersUnitTest {
//    @Mock
//    RestTemplate restTemplate;
//    @Mock
//    RabbitTemplate rabbitTemplate;
//    @Value("${paymentPort}")
//    private String paymentPort;
//    @Value("${paymentHost}")
//    private String paymentHost;
//    @Value("${deliveryPort}")
//    private String deliveryPort;
//    @Value("${deliveryHost}")
//    private String deliveryHost;
//
//    @Mock
//    private ShopperRepo shopperRepo;
//
//    @Mock
//    private AdminRepo adminRepo;
//
//    @Mock
//    private DriverRepo driverRepo;
//
//    @Mock
//    private CustomerRepo customerRepo;
//
//    @InjectMocks
//    private UserServiceImpl userService;
//
//    Shopper shopper;
//    Driver driver;
//    Customer customer;
//    Admin admin;
//
//    UUID adminUUID = UUID.randomUUID();
//    UUID customerUUID = UUID.randomUUID();
//    UUID driverUUID = UUID.randomUUID();
//    UUID shopperUUID=UUID.randomUUID();
//
//    @BeforeEach
//    void setUp()
//    {
//        shopper = new Shopper();
//        shopper.setShopperID(shopperUUID);
//        shopper.setName("JJ");
//
//        driver = new Driver();
//        driver.setDriverID(driverUUID);
//        driver.setName("Kane");
//
//        admin = new Admin();
//        admin.setAdminID(adminUUID);
//        admin.setName("Levy");
//
//        customer = new Customer();
//        customer.setCustomerID(customerUUID);
//        customer.setName("Pep");
//    }
//
//    @AfterEach
//    void tearDown() {
//    }
//
//    @Test
//    @Description("Tests for when GetUsers is submitted with a null request object- exception should be thrown")
//    @DisplayName("When request object is not specified")
//    void UnitTest_testingNullRequestObject(){
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.getUsers(null));
//        assertEquals("GetUser request is null - could not return users", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Test for when admin does exist")
//    @DisplayName("When admin with ID does exist")
//    void UnitTest_Shopper_does_exist() throws Exception {
//
//        List<Admin> admins = new ArrayList<>();
//        admins.add(admin);
//
//        List<Customer> customers = new ArrayList<>();
//        customers.add(customer);
//
//        List<Shopper> shoppers = new ArrayList<>();
//        shoppers.add(shopper);
//
//        List<Driver> drivers = new ArrayList<>();
//        drivers.add(driver);
//
//        GetUsersRequest request= new GetUsersRequest();
//        when(adminRepo.findAll()).thenReturn(admins);
//        when(customerRepo.findAll()).thenReturn(customers);
//        when(shopperRepo.findAll()).thenReturn(shoppers);
//        when(driverRepo.findAll()).thenReturn(drivers);
//        GetUsersResponse response= userService.getUsers(request);
//        assertNotNull(response);
//        assertTrue(response.isSuccess());
//        assertEquals("Users successfully returned", response.getMessage());
//        assertEquals(4, response.getUsers().size());
//    }
//}
