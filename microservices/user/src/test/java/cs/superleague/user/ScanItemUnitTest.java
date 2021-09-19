package cs.superleague.user;

import cs.superleague.payment.dataclass.*;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.responses.GetOrderResponse;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.requests.CompletePackagingOrderRequest;
import cs.superleague.user.requests.ScanItemRequest;
import cs.superleague.user.responses.CompletePackagingOrderResponse;
import cs.superleague.user.responses.ScanItemResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ScanItemUnitTest {

    @Mock
    RestTemplate restTemplate;
    @Mock
    RabbitTemplate rabbitTemplate;
    @Value("${paymentPort}")
    private String paymentPort;
    @Value("${paymentHost}")
    private String paymentHost;
    @Value("${deliveryPort}")
    private String deliveryPort;
    @Value("${deliveryHost}")
    private String deliveryHost;
    @Value("${shoppingPort}")
    private String shoppingPort;
    @Value("${shoppingHost}")
    private String shoppingHost;

    @InjectMocks
    private UserServiceImpl userService;

    Shopper shopper;
    Order o;
    UUID o1UUID=UUID.randomUUID();
    UUID storeUUID1= UUID.randomUUID();
    UUID expectedU1=UUID.randomUUID();
    UUID expectedS1=UUID.randomUUID();
    UUID expectedShopper1=UUID.randomUUID();
    Double expectedDiscount;
    Double totalC;
    Item i1;
    Item i2;
    List<Item> listOfItems=new ArrayList<>();
    String expectedMessage;
    OrderStatus expectedStatus;
    OrderType expectedType;
    GeoPoint deliveryAddress=new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");
    GeoPoint storeAddress=new GeoPoint(3.0, 3.0, "Woolworth's, Hillcrest Boulevard");

    @BeforeEach
    void setUp()
    {
        i1=new Item("Heinz Tomato Sauce","123456","123456",storeUUID1,36.99,1,"description","img/");
        i2=new Item("Bar one","012345","012345",storeUUID1,14.99,3,"description","img/");
        listOfItems.add(i1);
        listOfItems.add(i2);
        totalC=133.99;
        expectedDiscount=0.0;
        Calendar c1=Calendar.getInstance();
        Date d1=new Date(2021,06,1,14,30);
        c1.setTime(d1);

        o=new Order(o1UUID, expectedU1, expectedS1, expectedShopper1, new Date(), new Date(), totalC, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, 0.0, deliveryAddress, storeAddress, false);
        CartItem cartItem = new CartItem();
        CartItem cartItem1 = new CartItem();
        cartItem.setBarcode("012345");
        cartItem1.setBarcode("123456");
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(cartItem);
        cartItems.add(cartItem1);
        o.setCartItems(cartItems);
        shopper = new Shopper();
        shopper.setShopperID(expectedShopper1);
        shopper.setName("JJ");
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests for when ScanItem is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.scanItem(null));
        assertEquals("ScanItemRequest is null - could not fetch order entity", thrown.getMessage());
    }

    @Test
    @Description("Tests for when orderID in request object is null- exception should be thrown")
    @DisplayName("When request object parameter -orderID - is not specified")
    void UnitTest_testingNull_orderID_Parameter_RequestObject(){
        ScanItemRequest request=new ScanItemRequest("rjiw2141", null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.scanItem(request));
        assertEquals("Order ID is null in ScanItemRequest request - could not retrieve order entity", thrown.getMessage());
    }

    @Test
    @Description("Tests for when barcode in request object is null- exception should be thrown")
    @DisplayName("When request object parameter -barcode - is not specified")
    void UnitTest_testingNull_barcode_Parameter_RequestObject(){
        ScanItemRequest request=new ScanItemRequest(null, o1UUID);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.scanItem(request));
        assertEquals("Barcode is null in ScanItemRequest request - could not scan item", thrown.getMessage());
    }

    @Test
    @Description("Test for when Order with orderID does not exist in database - OrderDoesNotExist Exception should be thrown")
    @DisplayName("When Order with ID doesn't exist")
    void UnitTest_Order_doesnt_exist(){

        ScanItemRequest request=new ScanItemRequest("123456", UUID.randomUUID());
        //when(orderRepo.findById(Mockito.any())).thenReturn(null);
        Throwable thrown = Assertions.assertThrows(OrderDoesNotExist.class, ()-> userService.scanItem(request));
        assertEquals("Order with ID does not exist in repository - could not get Order entity", thrown.getMessage());
    }

    @Test
    @Description("Test for when item does exist")
    @DisplayName("When Item with barcode does exist")
    void UnitTest_Item_does_exist() throws OrderDoesNotExist, cs.superleague.user.exceptions.InvalidRequestException, URISyntaxException {

        ScanItemRequest request= new ScanItemRequest("123456", o1UUID);
        //when(orderRepo.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(o));
        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("orderID", request.getOrderID());
        String uriString = "http://"+paymentHost+":"+paymentPort+"/payment/getOrder";
        URI uri = new URI(uriString);
        GetOrderResponse getOrderResponse = new GetOrderResponse(o, true, new Date(), "");
        ResponseEntity<GetOrderResponse> useCaseResponseEntity = new ResponseEntity<>(getOrderResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(uri, parts, GetOrderResponse.class)).thenReturn(useCaseResponseEntity);

        ScanItemResponse response= userService.scanItem(request);
        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Item successfully scanned",response.getMessage());
    }
}
