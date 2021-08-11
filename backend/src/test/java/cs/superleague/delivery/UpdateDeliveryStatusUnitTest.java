package cs.superleague.delivery;

import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.dataclass.DeliveryDetail;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.repos.DeliveryDetailRepo;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.TrackDeliveryRequest;
import cs.superleague.delivery.requests.UpdateDeliveryStatusRequest;
import cs.superleague.delivery.responses.UpdateDeliveryStatusResponse;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.repos.StoreRepo;
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

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateDeliveryStatusUnitTest {
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

    UUID deliveryID;
    UUID orderID;
    UUID adminID;
    UUID driverID;
    Calendar time;
    Delivery delivery;
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

    @BeforeEach
    void setUp() {
        cost = 0.0;
        adminID = UUID.randomUUID();
        driverID = UUID.randomUUID();
        deliveryID = UUID.randomUUID();
        orderID = UUID.randomUUID();
        time = Calendar.getInstance();
        status = DeliveryStatus.WaitingForShoppers;
        invalidDropOffLocation = new GeoPoint(-91.0, -91.0, "address");
        pickUpLocation = new GeoPoint(0.0, 0.0, "address");
        dropOffLocation = new GeoPoint(1.0, 1.0, "address");
        customerID = UUID.randomUUID();
        storeID = UUID.randomUUID();
        delivery = new Delivery(deliveryID, orderID, pickUpLocation, dropOffLocation, customerID, storeID, status, cost);
        customer = new Customer("Seamus", "Brennan", "u19060468@tuks.co.za", "0743149813", "Hello123$$$", "123", UserType.CUSTOMER, customerID);
        store = new Store(storeID, 1, 2, "Woolworth's", 10, 10, true);
        store.setStoreLocation(pickUpLocation);
        admin = new Admin("John", "Doe", "u19060468@tuks.co.za", "0743149813", "Hello123", "123", UserType.ADMIN, adminID);
        deliveryDetail1 = new DeliveryDetail(deliveryID, time, status, "detail");
        deliveryDetail2 = new DeliveryDetail(deliveryID, Calendar.getInstance(), DeliveryStatus.CollectedByDriver, "detail");
        driver = new Driver("John", "Doe", "u19060468@tuks.co.za", "0743149813", "Hello123", "123", UserType.DRIVER, driverID);
        driverLocation = new GeoPoint(50.0, 50.0, "address");
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    @Description("Tests that the request object is created successfully.")
    @DisplayName("Request object creation.")
    void checkRequestObjectCreation_UnitTest() {
        UpdateDeliveryStatusRequest request = new UpdateDeliveryStatusRequest(status, deliveryID, "detail");
        assertEquals(request.getDeliveryID(), deliveryID);
        assertEquals(request.getStatus(), status);
        assertEquals(request.getDetail(), "detail");
    }

    @Test
    @Description("Tests for when a null request object is passed in.")
    @DisplayName("Null request")
    void nullRequestObject_UnitTest() {
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, () -> deliveryService.updateDeliveryStatus(null));
        assertEquals("Null request object.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when there is null parameters in the request object")
    @DisplayName("Null parameters")
    void nullParametersInRequestObject_UnitTest(){
        UpdateDeliveryStatusRequest request1 = new UpdateDeliveryStatusRequest(null, deliveryID, "detail");
        Throwable thrown1 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.updateDeliveryStatus(request1));
        assertEquals("Null parameters.", thrown1.getMessage());

        UpdateDeliveryStatusRequest request2 = new UpdateDeliveryStatusRequest(status, null, "detail");
        Throwable thrown2 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.updateDeliveryStatus(request2));
        assertEquals("Null parameters.", thrown2.getMessage());

        UpdateDeliveryStatusRequest request3 = new UpdateDeliveryStatusRequest(status, deliveryID, null);
        Throwable thrown3 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.updateDeliveryStatus(request3));
        assertEquals("Null parameters.", thrown3.getMessage());
    }

    @Test
    @Description("Tests for when the delivery is not found in the repo.")
    @DisplayName("Delivery not found")
    void deliveryNotFoundInDatabase_UnitTest(){
        when(deliveryRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(null));
        UpdateDeliveryStatusRequest request1 = new UpdateDeliveryStatusRequest(status, deliveryID, "detail");
        Throwable thrown1 = Assertions.assertThrows(InvalidRequestException.class, ()->deliveryService.updateDeliveryStatus(request1));
        assertEquals("Delivery does not exist in database.", thrown1.getMessage());
    }

    @Test
    @Description("Tests that the response object returns for valid delivery update")
    @DisplayName("Valid update")
    void deliveryStatusSuccessfullyUpdated_UnitTest() throws InvalidRequestException{
        when(deliveryRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(delivery));
        UpdateDeliveryStatusRequest request1 = new UpdateDeliveryStatusRequest(status, deliveryID, "detail");
        UpdateDeliveryStatusResponse response = deliveryService.updateDeliveryStatus(request1);
        assertEquals(response.getMessage(), "Successful status update.");
    }
}
