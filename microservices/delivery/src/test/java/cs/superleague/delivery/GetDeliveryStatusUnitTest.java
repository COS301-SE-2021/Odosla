//package cs.superleague.delivery;
//
//import cs.superleague.delivery.dataclass.Delivery;
//import cs.superleague.delivery.dataclass.DeliveryDetail;
//import cs.superleague.delivery.dataclass.DeliveryStatus;
//import cs.superleague.delivery.exceptions.InvalidRequestException;
//import cs.superleague.delivery.repos.DeliveryDetailRepo;
//import cs.superleague.delivery.repos.DeliveryRepo;
//import cs.superleague.delivery.requests.GetDeliveryStatusRequest;
//import cs.superleague.delivery.responses.GetDeliveryStatusResponse;
//import cs.superleague.payment.dataclass.GeoPoint;
//import cs.superleague.shopping.dataclass.Store;
//import cs.superleague.shopping.repos.StoreRepo;
//import cs.superleague.user.dataclass.Admin;
//import cs.superleague.user.dataclass.Customer;
//import cs.superleague.user.dataclass.UserType;
//import cs.superleague.user.repos.AdminRepo;
//import cs.superleague.user.repos.CustomerRepo;
//import cs.superleague.user.repos.DriverRepo;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.context.annotation.Description;
//
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class GetDeliveryStatusUnitTest {
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
//
//    UUID deliveryID;
//    UUID orderID;
//    UUID adminID;
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
//    List<DeliveryDetail> deliveryDetails = new ArrayList<>();
//
//    @BeforeEach
//    void setUp(){
//        cost = 0.0;
//        adminID = UUID.randomUUID();
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
//        customer = new Customer("Seamus", "Brennan", "u19060468@tuks.co.za", "0743149813", "Hello123$$$", "123", UserType.CUSTOMER,customerID);
//        store = new Store(storeID, 1, 2, "Woolworth's", 10, 10, true, "");
//        store.setStoreLocation(pickUpLocation);
//        admin = new Admin("John", "Doe", "u19060468@tuks.co.za", "0743149813", "Hello123", "123", UserType.ADMIN, adminID);
//        deliveryDetail1 = new DeliveryDetail(deliveryID, time, status, "detail");
//        deliveryDetail2 = new DeliveryDetail(deliveryID, Calendar.getInstance(), DeliveryStatus.CollectedByDriver, "detail");
//
//    }
//
//    @AfterEach
//    void tearDown(){
//
//    }
//
//    @Test
//    @Description("Tests that the request object is created successfully.")
//    @DisplayName("Request object creation.")
//    void checkRequestObjectCreation_UnitTest(){
//        GetDeliveryStatusRequest request = new GetDeliveryStatusRequest(deliveryID);
//        assertEquals(request.getDeliveryID(), deliveryID);
//    }
//
//    @Test
//    @Description("Tests for when a null request object is passed in.")
//    @DisplayName("Null request")
//    void nullRequestObject_UnitTest(){
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.getDeliveryStatus(null));
//        assertEquals("Null request object.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests for when there is a null deliveryID in the request object")
//    @DisplayName("Null deliveryID")
//    void nullDeliveryIDInRequestObject_UnitTest(){
//        GetDeliveryStatusRequest request = new GetDeliveryStatusRequest(null);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.getDeliveryStatus(request));
//        assertEquals("No delivery Id specified.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests for when the deliveryID is invalid.")
//    @DisplayName("Invalid DeliveryID")
//    void invalidDeliveryIDInRequestObject_UnitTest(){
//        when(deliveryRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(null));
//        GetDeliveryStatusRequest request = new GetDeliveryStatusRequest(deliveryID);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.getDeliveryStatus(request));
//        assertEquals("Delivery not found in database.", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests for when the deliveryID is valid, should return response object.")
//    @DisplayName("Valid DeliveryID")
//    void validDeliveryIDInRequestObject_UnitTest() throws InvalidRequestException{
//        when(deliveryRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(delivery));
//        GetDeliveryStatusRequest request = new GetDeliveryStatusRequest(deliveryID);
//        GetDeliveryStatusResponse response = deliveryService.getDeliveryStatus(request);
//        assertEquals(response.getMessage(), "Delivery Found.");
//        assertEquals(response.getStatus(), delivery.getStatus());
//    }
//}
