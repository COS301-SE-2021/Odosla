package cs.superleague.delivery;
import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.dataclass.DeliveryDetail;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.repos.DeliveryDetailRepo;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.GetNextOrderForDriverRequest;
import cs.superleague.delivery.requests.TrackDeliveryRequest;
import cs.superleague.delivery.requests.UpdateDeliveryStatusRequest;
import cs.superleague.delivery.responses.GetNextOrderForDriverResponse;
import cs.superleague.delivery.responses.UpdateDeliveryStatusResponse;
import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Admin;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.repos.AdminRepo;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.DriverRepo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetNextOrderForDriverUnitTest {
    @InjectMocks
    private DeliveryServiceImpl deliveryService;

    @Mock
    DeliveryRepo deliveryRepo;
    @Mock
    DeliveryDetailRepo deliveryDetailRepo;
    @Mock
    AdminRepo adminRepo;
    @Mock
    DriverRepo driverRepo;
    @Mock
    CustomerRepo customerRepo;
    @Mock
    StoreRepo storeRepo;
    @InjectMocks
    JwtUtil jwtTokenUtil;
    @Mock
    UserServiceImpl userService;
    @Mock
    OrderRepo orderRepo;

    UUID deliveryID1;
    UUID deliveryID2;
    Delivery delivery2;
    UUID orderID;
    UUID adminID;
    UUID driverID;
    Calendar time;
    Delivery delivery1;
    GeoPoint invalidDropOffLocation;
    GeoPoint pickUpLocation;
    GeoPoint dropOffLocation;
    UUID customerID;
    UUID storeID;
    DeliveryStatus status;
    double cost;
    Customer customer;
    Store store;
    Admin admin;
    DeliveryDetail deliveryDetail1;
    DeliveryDetail deliveryDetail2;
    Driver driver;
    GeoPoint driverLocation;
    List<Delivery> deliveries = new ArrayList<>();
    String jwtToken;

    @BeforeEach
    void setUp() {

        cost = 0.0;
        adminID = UUID.randomUUID();
        driverID = UUID.randomUUID();
        deliveryID1 = UUID.randomUUID();
        deliveryID2 = UUID.randomUUID();
        orderID = UUID.randomUUID();
        time = Calendar.getInstance();
        status = DeliveryStatus.WaitingForShoppers;
        invalidDropOffLocation = new GeoPoint(-91.0, -91.0, "address");
        pickUpLocation = new GeoPoint(0.0, 0.0, "address");
        dropOffLocation = new GeoPoint(1.0, 1.0, "address");
        customerID = UUID.randomUUID();
        storeID = UUID.randomUUID();
        delivery1 = new Delivery(deliveryID1, orderID, pickUpLocation, dropOffLocation, customerID, storeID, status, cost);
        delivery2 = new Delivery(deliveryID2, orderID, pickUpLocation, dropOffLocation, customerID, storeID, status, cost);
        customer = new Customer("Seamus", "Brennan", "u19060468@tuks.co.za", "0743149813", "Hello123$$$", "123", UserType.CUSTOMER, customerID);
        store = new Store(storeID, 1, 2, "Woolworth's", 10, 10, true, "");
        store.setStoreLocation(pickUpLocation);
        admin = new Admin("John", "Doe", "u19060468@tuks.co.za", "0743149813", "Hello123", "123", UserType.ADMIN, adminID);
        deliveryDetail1 = new DeliveryDetail(deliveryID1, time, status, "detail");
        deliveryDetail2 = new DeliveryDetail(deliveryID1, Calendar.getInstance(), DeliveryStatus.CollectedByDriver, "detail");
        driver = new Driver("John", "Doe", "u19060468@tuks.co.za", "0743149813", "Hello123", "123", UserType.DRIVER, driverID);
        driverLocation = new GeoPoint(50.0, 50.0, "address");
        jwtToken = jwtTokenUtil.generateJWTTokenDriver(driver);
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    @Description("Tests that the request object is created successfully.")
    @DisplayName("Request object creation.")
    void checkRequestObjectCreation_UnitTest() {
        GetNextOrderForDriverRequest request1 = new GetNextOrderForDriverRequest(jwtToken, driverLocation, 20.0);
        assertEquals(request1.getJwtToken(), jwtToken);
        assertEquals(request1.getCurrentLocation(), driverLocation);
        assertEquals(request1.getRangeOfDelivery(), 20.0);
        GetNextOrderForDriverRequest request2 = new GetNextOrderForDriverRequest(jwtToken, driverLocation);
        assertEquals(request2.getJwtToken(), jwtToken);
        assertEquals(request2.getCurrentLocation(), driverLocation);
        assertEquals(request2.getRangeOfDelivery(), 10.0);
    }

    @Test
    @Description("Tests for when a null request object is passed in.")
    @DisplayName("Null request")
    void nullRequestObject_UnitTest() {
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, () -> deliveryService.getNextOrderForDriver(null));
        assertEquals("Null request object.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when there are null parameters in the request object.")
    @DisplayName("Null parameters test")
    void nullParametersPassedInToRequestObject_UnitTest(){
        GetNextOrderForDriverRequest request1 = new GetNextOrderForDriverRequest(null, driverLocation, 10.0);
        Throwable thrown1 = Assertions.assertThrows(InvalidRequestException.class, () -> deliveryService.getNextOrderForDriver(request1));
        assertEquals("Null parameters.", thrown1.getMessage());

        GetNextOrderForDriverRequest request2 = new GetNextOrderForDriverRequest(jwtToken, null, 10.0);
        Throwable thrown2 = Assertions.assertThrows(InvalidRequestException.class, () -> deliveryService.getNextOrderForDriver(request2));
        assertEquals("Null parameters.", thrown2.getMessage());
    }

    @Test
    @Description("Tests for when the driver is not in the repo.")
    @DisplayName("Invalid DriverID")
    void invalidDriverIDDriverNotFound_UnitTest(){

        GetNextOrderForDriverRequest request1 = new GetNextOrderForDriverRequest(jwtToken, driverLocation, 10.0);
        Throwable thrown1 = Assertions.assertThrows(InvalidRequestException.class, () -> deliveryService.getNextOrderForDriver(request1));
        assertEquals("Driver not found in database.", thrown1.getMessage());
    }

//    @Test
//    @Description("Tests for when there are no available deliveries in the repo.")
//    @DisplayName("No available deliveries")
//    void noAvailableDeliveriesForDriver_UnitTest() throws InvalidRequestException, cs.superleague.user.exceptions.InvalidRequestException {
//        when(driverRepo.findDriverByEmail(Mockito.any())).thenReturn(driver);
//        when(deliveryRepo.findAllByDriverIdIsNull()).thenReturn(null);
//        GetNextOrderForDriverRequest request1 = new GetNextOrderForDriverRequest(jwtToken, driverLocation, 10.0);
//        GetNextOrderForDriverResponse response = deliveryService.getNextOrderForDriver(request1);
//        assertEquals(response.getDelivery(), null);
//        assertEquals(response.getMessage(), "No available deliveries in the database.");
//    }
//
//    @Test
//    @Description("Tests for when there is an empty list of deliveries.")
//    @DisplayName("Empty list of deliveries")
//    void emptyListOfDeliveries_UnitTest() throws InvalidRequestException, cs.superleague.user.exceptions.InvalidRequestException {
//        when(driverRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(driver));
//        when(deliveryRepo.findAllByDriverIdIsNull()).thenReturn(deliveries);
//        GetNextOrderForDriverRequest request1 = new GetNextOrderForDriverRequest(jwtToken, driverLocation, 10.0);
//        GetNextOrderForDriverResponse response = deliveryService.getNextOrderForDriver(request1);
//        assertEquals(response.getDelivery(), null);
//        assertEquals(response.getMessage(), "No available deliveries in the database.");
//    }
//
//    @Test
//    @Description("Tests for when the driver is too far away to take a delivery.")
//    @DisplayName("Driver too far for any delivery")
//    void driverIsTooFarAwayFromAnyDeliveryies_UnitTest() throws InvalidRequestException, cs.superleague.user.exceptions.InvalidRequestException {
//        deliveries.add(delivery1);
//        deliveries.add(delivery2);
//        when(driverRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(driver));
//        when(deliveryRepo.findAllByDriverIdIsNull()).thenReturn(deliveries);
//        GetNextOrderForDriverRequest request1 = new GetNextOrderForDriverRequest(jwtToken, driverLocation, 10.0);
//        GetNextOrderForDriverResponse response = deliveryService.getNextOrderForDriver(request1);
//        assertEquals(response.getDelivery(), null);
//        assertEquals(response.getMessage(), "No available deliveries in the range specified.");
//    }
//
//    @Test
//    @Description("Successful allocation of delivery to driver.")
//    @DisplayName("Successful allocation")
//    void successfulAllocationOfDelivery_UnitTest() throws InvalidRequestException, cs.superleague.user.exceptions.InvalidRequestException {
//        deliveries.add(delivery2);
//        driverLocation = new GeoPoint(0.0, 0.0, "address");
//        when(driverRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(driver));
//        when(deliveryRepo.findAllByDriverIdIsNull()).thenReturn(deliveries);
//        GetNextOrderForDriverRequest request1 = new GetNextOrderForDriverRequest(jwtToken, driverLocation, 10.0);
//        GetNextOrderForDriverResponse response = deliveryService.getNextOrderForDriver(request1);
//        assertEquals(response.getDelivery(), delivery2);
//        assertEquals(response.getMessage(), "Driver can take the following delivery.");
//    }
}
