package cs.superleague.delivery;

import cs.superleague.delivery.dataclass.Delivery;
import cs.superleague.delivery.dataclass.DeliveryDetail;
import cs.superleague.delivery.dataclass.DeliveryStatus;
import cs.superleague.delivery.exceptions.InvalidRequestException;
import cs.superleague.delivery.repos.DeliveryDetailRepo;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.delivery.requests.UpdateDeliveryStatusRequest;
import cs.superleague.delivery.responses.UpdateDeliveryStatusResponse;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.responses.GetOrderResponse;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.user.dataclass.Admin;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.responses.GetDriverByUUIDResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
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
    RestTemplate restTemplate;

    @Mock
    RabbitTemplate rabbitTemplate;

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
    Store store;
    Admin admin;
    DeliveryDetail deliveryDetail1;
    DeliveryDetail deliveryDetail2;
    Driver driver;
    GeoPoint driverLocation;


    String uri;
    String exchange;
    String routingKey;

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
        delivery.setDriverId(driverID);
        store = new Store(storeID, 1, 2, "Woolworth's", 10, 10, true, "");
        store.setStoreLocation(pickUpLocation);
        admin = new Admin("John", "Doe", "u19060468@tuks.co.za", "0743149813", "Hello123", "123", UserType.ADMIN, adminID);
        deliveryDetail1 = new DeliveryDetail(deliveryID, time, status, "detail");
        deliveryDetail2 = new DeliveryDetail(deliveryID, Calendar.getInstance(), DeliveryStatus.CollectedByDriver, "detail");
        driver = new Driver("John", "Doe", "u19060468@tuks.co.za", "0743149813", "Hello123", "123", UserType.DRIVER, driverID);
        driverLocation = new GeoPoint(50.0, 50.0, "address");

        uri = "http://localhost:8089/user/getDriverByUUID";
        List<HttpMessageConverter<?>> converters = new ArrayList<>();
        converters.add(new MappingJackson2HttpMessageConverter());

        restTemplate.setMessageConverters(converters);

        exchange = "userExchange";
        routingKey = "RK_saveDriverToRepo";
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

//    @Test
//    @Description("Tests for when the request delivery status is 'DELIVERED', Successful update")
//    @DisplayName("Successful update")
//    void successfulUpdate_UnitTest(){
//        when(deliveryRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(delivery));
//        UpdateDeliveryStatusRequest request1 = new UpdateDeliveryStatusRequest(DeliveryStatus.Delivered, deliveryID, "detail");
//
//        GetDriverByUUIDResponse getDriverByUUIDResponse = new GetDriverByUUIDResponse(driver,
//                new Date(), "Driver successfully retrieved");
//        GetOrderResponse getOrderResponse = new GetOrderResponse(new Order(), true, new Date(), "");
//
//        ResponseEntity<GetDriverByUUIDResponse> responseEntity = new ResponseEntity<>(getDriverByUUIDResponse,
//                HttpStatus.OK);
//        ResponseEntity<GetOrderResponse> responseEntityOrder = new ResponseEntity<>(getOrderResponse,
//                HttpStatus.OK);
//
//        Map<String, Object> parts = new HashMap<>();
//        parts.put("driverID", delivery.getDriverId());
//
//        MultiValueMap<String, Object> orderRequest = new LinkedMultiValueMap<String, Object>();
//        orderRequest.add("orderId", delivery.getOrderID());
//
//        Mockito.when(restTemplate.postForEntity(uri, parts, GetDriverByUUIDResponse.class))
//                .thenReturn(responseEntity);
//
//        uri = "http://localhost:8086/payment/getOrder";
//        Mockito.when(restTemplate.postForEntity(uri, orderRequest, GetOrderResponse.class))
//                .thenReturn(responseEntityOrder);
//
//        try {
//            UpdateDeliveryStatusResponse response = deliveryService.updateDeliveryStatus(request1);
//            assertEquals(response.getMessage(), "Successful status update.");
//        }catch (Exception e){
//            e.printStackTrace();
//            fail();
//        }
//    }

    @Test
    @Description("Tests that the response object returns for valid delivery update")
    @DisplayName("Valid update")
    void deliveryStatusSuccessfullyUpdated_UnitTest() throws InvalidRequestException {
        when(deliveryRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(delivery));
        UpdateDeliveryStatusRequest request1 = new UpdateDeliveryStatusRequest(status, deliveryID, "detail");
        UpdateDeliveryStatusResponse response = deliveryService.updateDeliveryStatus(request1);
        assertEquals(response.getMessage(), "Successful status update.");
    }
}
