package cs.superleague.delivery;

import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.exceptions.DeliveryDoesNotExistException;
import cs.superleague.delivery.exceptions.DeliveryException;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.GetDeliveryByUUIDRequest;
import cs.superleague.delivery.responses.GetDeliveryByUUIDResponse;
import cs.superleague.delivery.stub.dataclass.GeoPoint;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetDeliveryByUUIDUnitTest {

    @Mock
    private DeliveryRepo deliveryRepo;

    @InjectMocks
    private DeliveryServiceImpl deliveryService;

    UUID deliveryID;
    UUID customerID;
    UUID orderID;
    UUID storeID;

    Delivery delivery;

    GeoPoint pickUpLocation;
    GeoPoint dropOffLocation;
    @BeforeEach
    void setUp() {
        deliveryID = UUID.randomUUID();
        customerID = UUID.randomUUID();
        orderID = UUID.randomUUID();
        storeID = UUID.randomUUID();

        pickUpLocation = new GeoPoint(0.0, 0.0, "address");
        dropOffLocation = new GeoPoint(1.0, 1.0, "address");
        delivery=  new Delivery(deliveryID, orderID, pickUpLocation, dropOffLocation, customerID, storeID, DeliveryStatus.CollectedByDriver, 0.0);
    }

    @AfterEach
    void tearDown(){
    }

    @Test
    @Description("Tests for when getDeliveryByUUID is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> deliveryService.getDeliveryByUUID(null));
        assertEquals("GetDeliveryByUUID request is null - could not return delivery entity", thrown.getMessage());
    }

    @Test
    @Description("Tests for whether the request object is submitted with a null parameter for DeliveryID in request object- exception should be thrown")
    @DisplayName("When request object parameter -userID - is not specified")
    void UnitTest_testingNull_deliveryID_Parameter_RequestObject(){
        GetDeliveryByUUIDRequest request = new GetDeliveryByUUIDRequest(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> deliveryService.getDeliveryByUUID(request));
        assertEquals("DeliveryID is null in GetDeliveryByUUID request - could not return delivery entity", thrown.getMessage());
    }

    @Test
    @Description("Test for when Delivery with deliveryID does not exist in database - DeliveryDoesNotExist Exception should be thrown")
    @DisplayName("When Store with ID doesn't exist")
    void UnitTest_Delivery_doesnt_exist(){
        GetDeliveryByUUIDRequest request = new GetDeliveryByUUIDRequest(UUID.randomUUID());
        when(deliveryRepo.findById(Mockito.any())).thenReturn(null);
        Throwable thrown = Assertions.assertThrows(DeliveryDoesNotExistException.class, ()-> deliveryService.getDeliveryByUUID(request));
        assertEquals("Delivery with given ID does not exist in the repository - could not return delivery", thrown.getMessage());
    }

    @Test
    @Description("Test for when Delivery with deliveryID does exist in database - should return correct delivery entity")
    @DisplayName("When Delivery with ID does exist")
    void UnitTest_Delivery_does_exist(){
        GetDeliveryByUUIDRequest request = new GetDeliveryByUUIDRequest(deliveryID);
        when(deliveryRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(delivery));

        try {
            GetDeliveryByUUIDResponse response = deliveryService.getDeliveryByUUID(request);
            assertNotNull(response);
            assertEquals(delivery, response.getDelivery());
            assertEquals("Delivery with given ID successfully returned.", response.getMessage());
        }catch(DeliveryException e){
            e.printStackTrace();
            fail();
        }
    }

    /** Checking request object is created correctly */
    @Test
    @Description("Tests whether the GetDeliveryByUUID request object was created correctly")
    @DisplayName("GetDeliveryByUUIDRequest correctly constructed")
    void UnitTest_GetStoreByUUIDRequestConstruction() {

        GetDeliveryByUUIDRequest request = new GetDeliveryByUUIDRequest(deliveryID);

        assertNotNull(request);
        assertEquals(deliveryID, request.getDeliveryID());
    }

}