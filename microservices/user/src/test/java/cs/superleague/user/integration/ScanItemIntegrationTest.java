package cs.superleague.user.integration;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.requests.SaveOrderToRepoRequest;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.requests.SaveItemToRepoRequest;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.ShopperRepo;
import cs.superleague.user.requests.CompletePackagingOrderRequest;
import cs.superleague.user.requests.ScanItemRequest;
import cs.superleague.user.responses.ScanItemResponse;
import org.junit.jupiter.api.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class ScanItemIntegrationTest {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    ShopperRepo shopperRepo;

    @Autowired
    private UserServiceImpl userService;

    Shopper shopper;
    Order o;
    UUID o1UUID=UUID.fromString("9fcd3c49-bca7-4125-aabe-714d79165e0b");
    UUID storeUUID1= UUID.fromString("8b31c6fc-d65b-41dd-925e-d10b96292d84");
    UUID expectedU1=UUID.fromString("bb6dabfc-c07f-4525-8850-a94118b4916d");
    UUID expectedS1=UUID.fromString("1dac460b-8430-46cd-8fc8-d63205959306");
    UUID expectedShopper1=UUID.fromString("7945cd42-023b-4ca0-a126-928bd57d53a6");
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

    ScanItemRequest request;
    ScanItemResponse response;

    @BeforeEach
    void setUp()
    {
        i1=new Item("Heinz Tomato Sauce","123456","123456",storeUUID1,36.99,1,"description","img/");
        i2=new Item("Bar one","012345","012345",storeUUID1,14.99,3,"description","img/");
        SaveItemToRepoRequest saveItemToRepo = new SaveItemToRepoRequest(i1);
        rabbitTemplate.convertAndSend("ShoppingEXCHANGE", "RK_SaveItemToRepo", saveItemToRepo);
        saveItemToRepo = new SaveItemToRepoRequest(i2);
        rabbitTemplate.convertAndSend("ShoppingEXCHANGE", "RK_SaveItemToRepo", saveItemToRepo);
        listOfItems.add(i1);
        listOfItems.add(i2);

        totalC=133.99;
        expectedDiscount=0.0;
        Calendar c1=Calendar.getInstance();
        Date d1=new Date(2021,06,1,14,30);
        c1.setTime(d1);

        o=new Order(o1UUID, expectedU1, expectedS1, expectedShopper1, new Date(), new Date(), totalC, OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedDiscount, deliveryAddress, storeAddress, false);
        SaveOrderToRepoRequest saveOrderRequest = new SaveOrderToRepoRequest(o);
        rabbitTemplate.convertAndSend("PaymentEXCHANGE", "RK_SaveOrderToRepo", saveOrderRequest);

        shopper = new Shopper();
        shopper.setShopperID(expectedShopper1);
        shopper.setName("JJ");

        shopperRepo.save(shopper);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests for when ScanItem is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void IntegrationTest_ScanItemNullRequestObject(){
        request = null;
        assertThrows(InvalidRequestException.class, ()-> {
            response = userService.scanItem(request);
        });
    }

    @Test
    @Description("Tests for when scanItem is submitted with a null request parameter- exception should be thrown")
    @DisplayName("When orderID parameter is not specified")
    void IntegrationTest_testingNullRequestOrderIDParameter(){
        request = new ScanItemRequest("123456", null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.scanItem(request));
        assertEquals("Order ID is null in ScanItemRequest request - could not retrieve order entity", thrown.getMessage());
    }

    @Test
    @Description("Tests for when scanItem is submitted with a null request parameter- exception should be thrown")
    @DisplayName("When barcode parameter is not specified")
    void IntegrationTest_testingNullRequestBarcodeParameter(){
        request = new ScanItemRequest(null, o1UUID);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.scanItem(request));
        assertEquals("Barcode is null in ScanItemRequest request - could not scan item", thrown.getMessage());
    }

    @Test
    @Description("Tests for when scanItem orderID doesn't exist - exception should be thrown")
    @DisplayName("When orderID does not exist")
    void IntegrationTest_testingInvalidOrder(){
        request = new ScanItemRequest("123456", UUID.randomUUID());
        Throwable thrown = Assertions.assertThrows(OrderDoesNotExist.class, ()-> userService.scanItem(request));
        assertEquals("Order with ID does not exist in repository - could not get Order entity", thrown.getMessage());
    }

    @Test
    @Description("Tests for when barcode doesn't match any items in the order - exception should be thrown")
    @DisplayName("When barcode does not exist")
    void IntegrationTest_testingInvalidItem(){
        request = new ScanItemRequest("123459", o1UUID);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.scanItem(request));
        assertEquals("Item barcode doesn't match any of the items in the order", thrown.getMessage());
    }



    /** Checking request object is created correctly */
    @Test
    @Description("Tests whether the ScanItem request object was created correctly")
    @DisplayName("ScanItem correctly constructed")
    void IntegrationTest_ScanItemRequestConstruction() {
        request = new ScanItemRequest("123456", o1UUID);
        assertNotNull(request);
    }
}
