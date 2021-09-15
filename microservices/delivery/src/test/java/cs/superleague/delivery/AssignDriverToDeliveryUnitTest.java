//package cs.superleague.delivery;
//
//import cs.superleague.delivery.dataclass.Delivery;
//import cs.superleague.delivery.dataclass.DeliveryDetail;
//import cs.superleague.delivery.dataclass.DeliveryStatus;
//import cs.superleague.delivery.repos.DeliveryDetailRepo;
//import cs.superleague.delivery.repos.DeliveryRepo;
//import cs.superleague.delivery.requests.AddDeliveryDetailRequest;
//import cs.superleague.delivery.exceptions.InvalidRequestException;
//import cs.superleague.delivery.requests.AssignDriverToDeliveryRequest;
//import cs.superleague.delivery.responses.AddDeliveryDetailResponse;
//import cs.superleague.delivery.responses.AssignDriverToDeliveryResponse;
//import cs.superleague.integration.security.CurrentUser;
//import cs.superleague.integration.security.JwtUtil;
//import cs.superleague.payment.dataclass.GeoPoint;
//import cs.superleague.payment.dataclass.Order;
//import cs.superleague.payment.exceptions.PaymentException;
//import cs.superleague.payment.repos.OrderRepo;
//import cs.superleague.shopping.dataclass.Store;
//import cs.superleague.shopping.repos.StoreRepo;
//import cs.superleague.user.UserService;
//import cs.superleague.user.UserServiceImpl;
//import cs.superleague.user.dataclass.*;
//import cs.superleague.user.repos.AdminRepo;
//import cs.superleague.user.repos.CustomerRepo;
//import cs.superleague.user.repos.DriverRepo;
//import cs.superleague.user.requests.GetCurrentUserRequest;
//import cs.superleague.user.responses.GetCurrentUserResponse;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.context.annotation.Description;
//
//import java.util.Calendar;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class AssignDriverToDeliveryUnitTest {
//    @InjectMocks
//    private DeliveryServiceImpl deliveryService;
//
//    @Mock
//    DeliveryRepo deliveryRepo;
//    @Mock
//    DeliveryDetailRepo deliveryDetailRepo;
//    @Mock
//    AdminRepo adminRepo;
//    @Mock
//    DriverRepo driverRepo;
//    @Mock
//    CustomerRepo customerRepo;
//    @Mock
//    StoreRepo storeRepo;
//    @InjectMocks
//    JwtUtil jwtTokenUtil;
//    @Mock
//    UserServiceImpl userService;
//    @Mock
//    OrderRepo orderRepo;
//
//    UUID deliveryID;
//    UUID orderID;
//    UUID adminID;
//    UUID driverID;
//    String jwtToken;
//    Calendar time;
//    Delivery delivery;
//    GeoPoint invalidDropOffLocation;
//    GeoPoint pickUpLocation;
//    GeoPoint dropOffLocation;
//    UUID customerID;
//    UUID storeID;
//    DeliveryStatus status;
//    double cost;
//    Customer customer;
//    Store store;
//    Admin admin;
//    DeliveryDetail deliveryDetail1;
//    DeliveryDetail deliveryDetail2;
//    Driver driver;
//    GeoPoint driverLocation;
//    Order order;
//
//    @BeforeEach
//    void setUp() {
//        cost = 0.0;
//        adminID = UUID.randomUUID();
//        driverID = UUID.randomUUID();
//        deliveryID = UUID.randomUUID();
//        orderID = UUID.randomUUID();
//        time = Calendar.getInstance();
//        status = DeliveryStatus.WaitingForShoppers;
//        invalidDropOffLocation = new GeoPoint(-91.0, -91.0, "address");
//        pickUpLocation = new GeoPoint(0.0, 0.0, "address");
//        dropOffLocation = new GeoPoint(1.0, 1.0, "address");
//        customerID = UUID.randomUUID();
//        storeID = UUID.randomUUID();
//        delivery = new Delivery(deliveryID, orderID, pickUpLocation, dropOffLocation, customerID, storeID, status, cost);
//        customer = new Customer("Seamus", "Brennan", "u19060468@tuks.co.za", "0743149813", "Hello123$$$", "123", UserType.CUSTOMER, customerID);
//        store = new Store(storeID, 1, 2, "Woolworth's", 10, 10, true, "/store/woolworths.png");
//        store.setStoreLocation(pickUpLocation);
//        admin = new Admin("John", "Doe", "u19060468@tuks.co.za", "0743149813", "Hello123", "123", UserType.ADMIN, adminID);
//        deliveryDetail1 = new DeliveryDetail(deliveryID, time, status, "detail");
//        deliveryDetail2 = new DeliveryDetail(deliveryID, Calendar.getInstance(), DeliveryStatus.CollectedByDriver, "detail");
//        driver = new Driver("John", "Doe", "u19060468@tuks.co.za", "0743149813", "Hello123", "123", UserType.DRIVER, driverID);
//        driverLocation = new GeoPoint(50.0, 50.0, "address");
//        jwtToken=jwtTokenUtil.generateJWTTokenDriver(driver);
//        customer=new Customer();
//        customer.setCustomerID(UUID.randomUUID());
//        customer.setEmail("hello@gmail.com");
//        customer.setAccountType(UserType.CUSTOMER);
//        order = new Order();
//        order.setOrderID(orderID);
//    }
//
//    @AfterEach
//    void tearDown() {
//
//    }
//
//    @Test
//    @Description("Tests that the request object is created successfully.")
//    @DisplayName("Request object creation.")
//    void checkRequestObjectCreation_UnitTest(){
//        AssignDriverToDeliveryRequest request = new AssignDriverToDeliveryRequest(deliveryID);
//        assertEquals(request.getDeliveryID(), deliveryID);
//    }
//
//    @Test
//    @Description("Tests null request object, should throw an error.")
//    @DisplayName("Null request object")
//    void nullRequestObject_UnitTest(){
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.assignDriverToDelivery(null));
//        assertEquals("Null request object.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests for when null deliveryID passed in, should throw an exception.")
//    @DisplayName("Null deliveryID")
//    void nullParametersInRequestObject_UnitTest(){
//        AssignDriverToDeliveryRequest request2 = new AssignDriverToDeliveryRequest(null);
//        Throwable thrown2 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.assignDriverToDelivery(request2));
//        assertEquals("Null parameters.", thrown2.getMessage());
//    }
//
//
//    @Test
//    @Description("Tests when there is an invalid deliveryID passed in, should throw an exception.")
//    @DisplayName("Invalid DeliveryID")
//    void invalidDeliveryIDPassedInRequestObject_UnitTest(){
//        when(deliveryRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(null));
//        AssignDriverToDeliveryRequest request1 = new AssignDriverToDeliveryRequest(deliveryID);
//        Throwable thrown1 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.assignDriverToDelivery(request1));
//        assertEquals("Delivery does not exist in the database.", thrown1.getMessage());
//    }
////
////    @Test
////    @Description("Tests when there is already a driver assigned to the delivery, should throw an exception.")
////    @DisplayName("Different driver already assigned")
////    void driverAlreadyAssignedRequestObject_UnitTest() throws cs.superleague.user.exceptions.InvalidRequestException {
////        delivery.setDriverId(UUID.randomUUID());
////        when(deliveryRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(delivery));
////        when(driverRepo.findDriverByEmail(Mockito.any())).thenReturn(driver);
////        GetCurrentUserResponse getCurrentUserResponse = new GetCurrentUserResponse((User) driver, true, null, "");
////        when(userService.getCurrentUser(Mockito.any())).thenReturn(getCurrentUserResponse);
////        AssignDriverToDeliveryRequest request1 = new AssignDriverToDeliveryRequest(deliveryID);
////        Throwable thrown1 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.assignDriverToDelivery(request1));
////        assertEquals("This delivery has already been taken by another driver.", thrown1.getMessage());
////    }
//
////    @Test
////    @Description("Tests when there is no driver assigned to deliver and the driver passed in is assigned.")
////    @DisplayName("Driver successfully assigned")
////    void driverAssignedSuccessfullyToTheDelivery_UnitTest() throws InvalidRequestException, cs.superleague.user.exceptions.InvalidRequestException, PaymentException {
////        delivery.setDriverId(null);
////        when(driverRepo.findDriverByEmail(Mockito.any())).thenReturn((driver));
////        when(deliveryRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(delivery));
////        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(order));
////        GetCurrentUserResponse getCurrentUserResponse = new GetCurrentUserResponse((User) driver, true, null, "");
////        when(userService.getCurrentUser(Mockito.any())).thenReturn(getCurrentUserResponse);
////        AssignDriverToDeliveryRequest request1 = new AssignDriverToDeliveryRequest(jwtToken, deliveryID);
////        AssignDriverToDeliveryResponse response = deliveryService.assignDriverToDelivery(request1);
////        assertEquals(response.getMessage(), "Driver successfully assigned to delivery.");
////        assertEquals(response.isAssigned(), true);
////        assertEquals(response.getDropOffLocation(), delivery.getDropOffLocation());
////        assertEquals(response.getPickUpLocation(), delivery.getPickUpLocation());
////    }
////
////    @Test
////    @Description("Tests for when the driver is already assigned to the delivery")
////    @DisplayName("Same driver already assigned")
////    void sameDriverIsAlreadyAssignedToDelivery_UnitTest() throws InvalidRequestException, cs.superleague.user.exceptions.InvalidRequestException, PaymentException {
////        delivery.setDriverId(driverID);
////        when(driverRepo.findDriverByEmail(Mockito.any())).thenReturn((driver));
////        when(deliveryRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(delivery));
////        GetCurrentUserResponse getCurrentUserResponse = new GetCurrentUserResponse((User) driver, true, null, "");
////        when(userService.getCurrentUser(Mockito.any())).thenReturn(getCurrentUserResponse);
////        AssignDriverToDeliveryRequest request1 = new AssignDriverToDeliveryRequest(deliveryID);
////        AssignDriverToDeliveryResponse response = deliveryService.assignDriverToDelivery(request1);
////        assertEquals(response.getMessage(), "Driver was already assigned to delivery.");
////        assertEquals(response.isAssigned(), true);
////        assertEquals(response.getDropOffLocation(), delivery.getDropOffLocation());
////        assertEquals(response.getPickUpLocation(), delivery.getPickUpLocation());
////    }
////
////    @Test
////    @Description("Tests for when there is no order corresponding to the delivery.")
////    @DisplayName("No order")
////    void noOrderAssignedToDelivery_UnitTest() throws cs.superleague.user.exceptions.InvalidRequestException {
////        delivery.setDriverId(null);
////        when(driverRepo.findDriverByEmail(Mockito.any())).thenReturn((driver));
////        when(deliveryRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(delivery));
////        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(null));
////        GetCurrentUserResponse getCurrentUserResponse = new GetCurrentUserResponse((User) driver, true, null, "");
////        when(userService.getCurrentUser(Mockito.any())).thenReturn(getCurrentUserResponse);
////        AssignDriverToDeliveryRequest request1 = new AssignDriverToDeliveryRequest(deliveryID);
////        Throwable thrown1 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.assignDriverToDelivery(request1));
////        assertEquals(thrown1.getMessage(), "Invalid order.");
////    }
////
////    @Test
////    @Description("Tests for when no drop off location is specified.")
////    @DisplayName("No drop off location")
////    void noDropOffLocationSpecified_UnitTest() throws cs.superleague.user.exceptions.InvalidRequestException {
////        delivery.setDriverId(null);
////        delivery.setDropOffLocation(null);
////        when(driverRepo.findDriverByEmail(Mockito.any())).thenReturn((driver));
////        when(deliveryRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(delivery));
////        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(order));
////        GetCurrentUserResponse getCurrentUserResponse = new GetCurrentUserResponse((User) driver, true, null, "");
////        when(userService.getCurrentUser(Mockito.any())).thenReturn(getCurrentUserResponse);
////        AssignDriverToDeliveryRequest request1 = new AssignDriverToDeliveryRequest(deliveryID);
////        Throwable thrown1 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.assignDriverToDelivery(request1));
////        assertEquals(thrown1.getMessage(), "No pick up or drop off location specified with delivery.");
////    }
////
////    @Test
////    @Description("Tests for when no pick up location is specified.")
////    @DisplayName("No pick up location")
////    void noPickUpLocationSpecified_UnitTest() throws cs.superleague.user.exceptions.InvalidRequestException {
////        delivery.setDriverId(null);
////        delivery.setPickUpLocation(null);
////        when(driverRepo.findDriverByEmail(Mockito.any())).thenReturn((driver));
////        when(deliveryRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(delivery));
////        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(order));
////        GetCurrentUserResponse getCurrentUserResponse = new GetCurrentUserResponse((User) driver, true, null, "");
////        when(userService.getCurrentUser(Mockito.any())).thenReturn(getCurrentUserResponse);
////        AssignDriverToDeliveryRequest request1 = new AssignDriverToDeliveryRequest(deliveryID);
////        Throwable thrown1 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.assignDriverToDelivery(request1));
////        assertEquals(thrown1.getMessage(), "No pick up or drop off location specified with delivery.");
////    }
//}
