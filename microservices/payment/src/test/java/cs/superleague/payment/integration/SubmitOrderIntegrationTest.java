package cs.superleague.payment.integration;

import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.PaymentServiceImpl;
import cs.superleague.payment.dataclass.*;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.exceptions.PaymentException;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.shopping.requests.SaveStoreToRepoRequest;
import cs.superleague.payment.requests.SubmitOrderRequest;
import cs.superleague.payment.responses.SubmitOrderResponse;
import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.requests.SaveItemToRepoRequest;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.requests.SaveCustomerToRepoRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.mail.StoreClosedException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class SubmitOrderIntegrationTest {
    @Autowired
    PaymentServiceImpl paymentService;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    RestTemplate restTemplate;

    @Value("${env.SECRET}")
    private String SECRET = "stub";

    @Value("${env.HEADER}")
    private String HEADER= "stub";

    /* Requests */
    SubmitOrderRequest submitOrderRequest;

    /* User Ids */
    UUID userID = UUID.fromString("ffd9af91-7573-4a10-a103-85971cea4f6b");

    /* Store Ids */
    UUID storeID = UUID.fromString("4a0d95d8-8ca3-4224-a6ca-744932b3abba");

    /*Items */
    Item item1,item2,item3,item4,item5;
    CartItem cItem1,cItem2,cItem3,cItem4,cItem5;
    List<Item> itemList = new ArrayList<>();
    List<CartItem> cartItems = new ArrayList<>();

    /*GeoPoints*/
    GeoPoint geoPoint1;
    GeoPoint geoPoint2;

    Catalogue stock;

    /*Store */
    Store store;

    /*Order */
    List<Order> currentOrders=new ArrayList<>();
    List<Order> orderQueue=new ArrayList<>();

    Customer customer;

    String jwtToken;

    @BeforeEach
    void setup(){

        customer=new Customer();
        customer.setCustomerID(userID);
        customer.setEmail("hello@gmail.com");
        customer.setAccountType(UserType.CUSTOMER);
        jwtToken=jwtUtil.generateJWTTokenCustomer(customer);



        item1 = new Item("name1","productID1","barcode1",storeID,10.0,2,"Description1","imageURL1");
        item2 = new Item("name2","productID2","barcode2",storeID,30.0,1,"Description2","imageURL2");
        item3 = new Item("name3","productID3","barcode3",storeID,27.0,1,"Description3","imageURL3");
        item4 = new Item("name4","productID4","barcode4",storeID,22.0,1,"Description4","imageURL4");
        item5 = new Item("name5","productID5","barcode5",storeID,22.0,1,"Description5","imageURL5");

        cItem1 = new CartItem("name1","productID1","barcode1",storeID,10.0,2,"Description1","imageURL1");
        cItem2 = new CartItem("name2","productID2","barcode2",storeID,30.0,1,"Description2","imageURL2");
        cItem3 = new CartItem("name3","productID3","barcode3",storeID,27.0,1,"Description3","imageURL3");
        cItem4 = new CartItem("name4","productID4","barcode4",storeID,22.0,1,"Description4","imageURL4");
        cItem5 = new CartItem("name5","productID5","barcode5",storeID,22.0,1,"Description5","imageURL5");

        itemList.add(item1);
        itemList.add(item2);
        itemList.add(item3);
        itemList.add(item4);
        itemList.add(item5);

        cartItems.add(cItem1);
        cartItems.add(cItem2);
        cartItems.add(cItem3);
        cartItems.add(cItem4);
        cartItems.add(cItem5);

        geoPoint1=new GeoPoint(4.0,5.0,"address1");
        geoPoint2=new GeoPoint(3.0,3.5,"address2");
        stock=new Catalogue(storeID,itemList);

        store = new Store(UUID.fromString("4a0d95d8-8ca3-4224-a6ca-744932b3abba"),"StoreBrand",stock,3,currentOrders,orderQueue,6,true);
        store.setStoreLocation(geoPoint2);

        String jwt = jwtUtil.generateJWTTokenCustomer(customer);
        jwt = jwt.replace(HEADER,"");
        Claims claims= Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwt).getBody();
        List<String> authorities = (List) claims.get("authorities");
        String userType= (String) claims.get("userType");
        String email = (String) claims.get("email");
        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        HashMap<String, Object> info=new HashMap<String, Object>();
        info.put("userType",userType);
        info.put("email",email);
        auth.setDetails(info);
        SecurityContextHolder.getContext().setAuthentication(auth);

        SaveCustomerToRepoRequest saveCustomerToRepoRequest = new SaveCustomerToRepoRequest(customer);
        rabbitTemplate.convertAndSend("ShoppingEXCHANGE", "RK_SaveItemToRepo",
                saveCustomerToRepoRequest);

        for (Item item: itemList) {
            SaveItemToRepoRequest saveItemToRepo = new SaveItemToRepoRequest(item);
            rabbitTemplate.convertAndSend("ShoppingEXCHANGE", "RK_SaveItemToRepo", saveItemToRepo);
        }

//        catalogueRepo.save(stock);

        SaveStoreToRepoRequest saveStoreToRepoRequest = new SaveStoreToRepoRequest(store);
        rabbitTemplate.convertAndSend("ShoppingEXCHANGE", "RK_SaveItemToRepo", saveStoreToRepoRequest);

    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();

    }

    @Test
    @Description("Tests for if the given order request on order creation is null - exception should be thrown")
    @DisplayName("When order request object null")
    void IntegrationTest_CreateOrderNullOrderRequest() throws InvalidRequestException {
        submitOrderRequest = null;

        assertThrows(cs.superleague.payment.exceptions.InvalidRequestException.class, ()-> {
            SubmitOrderResponse submitOrderResponse = paymentService.submitOrder(submitOrderRequest);
        });
    }

    @Test
    @Description("Tests for if the given order request on order creation has a null parameter - exception should be thrown")
    @DisplayName("When order parameter of request object null")
    void IntegrationTest_CreateOrderWithNullParamterRequest() throws InvalidRequestException {
        submitOrderRequest=new SubmitOrderRequest(null,3.0,storeID,OrderType.DELIVERY, 3.3, 3.5, "Homer Street");
        assertThrows(cs.superleague.payment.exceptions.InvalidRequestException.class, ()-> {
            SubmitOrderResponse submitOrderResponse = paymentService.submitOrder(submitOrderRequest);
        });

        submitOrderRequest=new SubmitOrderRequest(cartItems,null,storeID,OrderType.DELIVERY, 3.3, 3.5, "Homer Street");
        assertThrows(cs.superleague.payment.exceptions.InvalidRequestException.class, ()-> {
            SubmitOrderResponse submitOrderResponse = paymentService.submitOrder(submitOrderRequest);
        });

        submitOrderRequest=new SubmitOrderRequest(cartItems,3.0,null,OrderType.DELIVERY, 3.3, 3.5, "Homer Street");
        assertThrows(cs.superleague.payment.exceptions.InvalidRequestException.class, ()-> {
            SubmitOrderResponse submitOrderResponse = paymentService.submitOrder(submitOrderRequest);
        });

        submitOrderRequest=new SubmitOrderRequest(cartItems,3.0,storeID,null, 3.3, 3.5, "Homer Street");
        assertThrows(cs.superleague.payment.exceptions.InvalidRequestException.class, ()-> {
            SubmitOrderResponse submitOrderResponse = paymentService.submitOrder(submitOrderRequest);
        });
    }

    @Test
    @Description("Tests for if the store does not exist")
    @DisplayName("When order parameter gives id of a store that does not exist")
    void IntegrationTest_StoreDoesNotExist() throws InvalidRequestException {
//        UUID storeID2=UUID.randomUUID();
//        submitOrderRequest=new SubmitOrderRequest(cartItems,3.0,storeID2,OrderType.DELIVERY, 3.3, 3.5, "Homer Street");
//        assertThrows(InvalidRequestException.class, ()-> {
//            SubmitOrderResponse submitOrderResponse = paymentService.submitOrder(submitOrderRequest);
//        });
    }

    @Test
    @Description("Tests for if the store does is closed")
    @DisplayName("When store with store ID is closed")
    void IntegrationTest_StoreDoesisClosed() throws InvalidRequestException {
//        storeRepo.deleteAll();
//        Store updateStore = storeRepo.findById(storeID).orElse(null);
//        store.setOpen(false);
        //store.setOpen(false);
        //storeRepo.save(store);
//        storeRepo.save(updateStore);

//        SaveStoreToRepoRequest saveStoreToRepoRequest = new SaveStoreToRepoRequest(store);
//        rabbitTemplate.convertAndSend("ShoppingEXCHANGE", "RK_SaveItemToRepo", saveStoreToRepoRequest);
//
//        submitOrderRequest=new SubmitOrderRequest(cartItems,3.0,storeID,OrderType.DELIVERY, 3.3, 3.5, "Homer Street");
//        assertThrows(cs.superleague.payment.exceptions.InvalidRequestException.class, ()-> {
//            SubmitOrderResponse submitOrderResponse = paymentService.submitOrder(submitOrderRequest);
//        });
    }




    @Test
    @Description("Tests whether the SubmitOrderRequest object is constructed correctly")
    @DisplayName("SubmitOrderRequest correct construction")
    void IntegrationTest_SubmitOrderRequestConstruction() {

        submitOrderRequest=new SubmitOrderRequest(cartItems,3.0,storeID,OrderType.DELIVERY, 3.3, 3.5, "Homer Street");

        assertNotNull(submitOrderRequest);
        assertEquals(cartItems,submitOrderRequest.getListOfItems());
        assertEquals(3.0,submitOrderRequest.getDiscount());
        assertEquals(storeID,submitOrderRequest.getStoreID());
        assertEquals(OrderType.DELIVERY,submitOrderRequest.getOrderType());
    }

    @Test
    @Description("This test tests whether an order is created correctly - should return valid data stored in order entity")
    @DisplayName("When Order is created correctly")
    void IntegrationTest_CreatOrderConstruction() throws PaymentException, InterruptedException, URISyntaxException {
//        Order order=null;
//        submitOrderRequest=new SubmitOrderRequest(cartItems,3.0,
//                UUID.fromString("4a0d95d8-8ca3-4224-a6ca-744932b3abba"),OrderType.DELIVERY,
//                3.3, 3.5, "Homer Street");
//        SubmitOrderResponse submitOrderResponse= paymentService.submitOrder(submitOrderRequest);
//
//        if(orderRepo.findById(submitOrderResponse.getOrder().getOrderID()).isPresent()){
//            order=orderRepo.findById(submitOrderResponse.getOrder().getOrderID()).orElse(null);
//        }
//        assertNotNull(order);
//        assertEquals(userID, order.getUserID());
//        assertEquals(itemList.size(),order.getCartItems().size());
//        Iterator<Item> itemIterator_it = itemList.iterator();
//        Iterator<CartItem> orderItemIterator_it = order.getCartItems().iterator();
//        while(itemIterator_it.hasNext() && orderItemIterator_it.hasNext()){
//                Item item=itemIterator_it.next();
//                CartItem orderItem=orderItemIterator_it.next();
//                assertEquals(item.getPrice(),orderItem.getPrice());
//                assertEquals(item.getQuantity(),orderItem.getQuantity());
//                assertEquals(item.getBarcode(),orderItem.getBarcode());
//                assertEquals(item.getStoreID(),orderItem.getStoreID());
//                assertEquals(item.getDescription(),orderItem.getDescription());
//                assertEquals(item.getProductID(),orderItem.getProductID());
//                assertEquals(item.getImageUrl(),orderItem.getImageUrl());
//        }
//
//        assertEquals(3.0,order.getDiscount());
//        assertEquals(UUID.fromString("4a0d95d8-8ca3-4224-a6ca-744932b3abba"),order.getStoreID());
//        //assertEquals(geoPoint2.getGeoID(),order.getStoreAddress().getGeoID());
//        assertEquals(geoPoint2.getAddress(),order.getStoreAddress().getAddress());
//        assertEquals(geoPoint2.getLatitude(),order.getStoreAddress().getLatitude());
//        assertEquals(geoPoint2.getLongitude(),order.getStoreAddress().getLongitude());
//        //assertEquals(geoPoint1.getGeoID(),order.getDeliveryAddress().getGeoID());
//        assertEquals(OrderType.DELIVERY,order.getType());
    }


}
