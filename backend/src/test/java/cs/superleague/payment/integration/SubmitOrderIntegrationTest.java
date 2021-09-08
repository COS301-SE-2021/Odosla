package cs.superleague.payment.integration;

import cs.superleague.integration.ServiceSelector;
import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.PaymentServiceImpl;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.exceptions.PaymentException;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.SubmitOrderRequest;
import cs.superleague.payment.responses.SubmitOrderResponse;
import cs.superleague.shopping.ShoppingServiceImpl;
import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreClosedException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.CatalogueRepo;
import cs.superleague.shopping.repos.ItemRepo;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.repos.CustomerRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class SubmitOrderIntegrationTest {
    @Autowired
    PaymentServiceImpl paymentService;

    @Autowired
    ShoppingServiceImpl shoppingService;
    //OPTIONAL SERVICES

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    StoreRepo storeRepo;

    @Autowired
    CatalogueRepo catalogueRepo;

    @Autowired
    ItemRepo itemRepo;

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    JwtUtil jwtUtil;

    @Value("${env.SECRET}")
    private String SECRET = "stub";

    @Value("${env.HEADER}")
    private String HEADER= "stub";

    /* Requests */
    SubmitOrderRequest submitOrderRequest;

    /* User Ids */
    UUID userID=UUID.randomUUID();

    /* Store Ids */
    UUID storeID=UUID.randomUUID();

    /*Items */
    Item item1,item2,item3,item4,item5;
    List<Item> itemList=new ArrayList<>();

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
        customerRepo.save(customer);
        jwtToken=jwtUtil.generateJWTTokenCustomer(customer);

        item1=new Item("name1","productID1","barcode1",storeID,10.0,2,"Description1","imageURL1");
        item2=new Item("name2","productID2","barcode2",storeID,30.0,1,"Description2","imageURL2");
        item3=new Item("name3","productID3","barcode3",storeID,27.0,1,"Description3","imageURL3");
        item4=new Item("name4","productID4","barcode4",storeID,22.0,1,"Description4","imageURL4");
        item5=new Item("name5","productID5","barcode5",storeID,22.0,1,"Description5","imageURL5");
        itemList.add(item1);
        itemList.add(item2);
        itemList.add(item3);
        itemList.add(item4);
        itemList.add(item5);
        geoPoint1=new GeoPoint(4.0,5.0,"address1");
        geoPoint2=new GeoPoint(3.0,3.5,"address2");
        stock=new Catalogue(storeID,itemList);

        store=new Store(storeID,"StoreBrand",stock,3,currentOrders,orderQueue,6,true);
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

        itemRepo.save(item1);
        itemRepo.save(item2);
        itemRepo.save(item3);
        itemRepo.save(item4);
        itemRepo.save(item5);
        catalogueRepo.save(stock);
        storeRepo.save(store);
    }

    @AfterEach
    void tearDown() {
        storeRepo.deleteAll();
        orderRepo.deleteAll();
        catalogueRepo.deleteAll();
        itemRepo.deleteAll();
        SecurityContextHolder.clearContext();

    }

    @Test
    @Description("Tests for if the given order request on order creation is null - exception should be thrown")
    @DisplayName("When order request object null")
    void IntegrationTest_CreateOrderNullOrderRequest() throws InvalidRequestException {
        submitOrderRequest = null;

        assertThrows(cs.superleague.payment.exceptions.InvalidRequestException.class, ()-> {
            SubmitOrderResponse submitOrderResponse = ServiceSelector.getPaymentService().submitOrder(submitOrderRequest);
        });
    }

    @Test
    @Description("Tests for if the given order request on order creation has a null parameter - exception should be thrown")
    @DisplayName("When order parameter of request object null")
    void IntegrationTest_CreateOrderWithNullParamterRequest() throws InvalidRequestException {
        submitOrderRequest=new SubmitOrderRequest(null,3.0,storeID,OrderType.DELIVERY, 3.3, 3.5, "Homer Street");
        assertThrows(cs.superleague.payment.exceptions.InvalidRequestException.class, ()-> {
            SubmitOrderResponse submitOrderResponse = ServiceSelector.getPaymentService().submitOrder(submitOrderRequest);
        });

        submitOrderRequest=new SubmitOrderRequest(itemList,null,storeID,OrderType.DELIVERY, 3.3, 3.5, "Homer Street");
        assertThrows(cs.superleague.payment.exceptions.InvalidRequestException.class, ()-> {
            SubmitOrderResponse submitOrderResponse = ServiceSelector.getPaymentService().submitOrder(submitOrderRequest);
        });

        submitOrderRequest=new SubmitOrderRequest(itemList,3.0,null,OrderType.DELIVERY, 3.3, 3.5, "Homer Street");
        assertThrows(cs.superleague.payment.exceptions.InvalidRequestException.class, ()-> {
            SubmitOrderResponse submitOrderResponse = ServiceSelector.getPaymentService().submitOrder(submitOrderRequest);
        });

        submitOrderRequest=new SubmitOrderRequest(itemList,3.0,storeID,null, 3.3, 3.5, "Homer Street");
        assertThrows(cs.superleague.payment.exceptions.InvalidRequestException.class, ()-> {
            SubmitOrderResponse submitOrderResponse = ServiceSelector.getPaymentService().submitOrder(submitOrderRequest);
        });
    }

    @Test
    @Description("Tests for if the store does not exist")
    @DisplayName("When order parameter gives id of a store that does not exist")
    void IntegrationTest_StoreDoesNotExist() throws InvalidRequestException {
        UUID storeID2=UUID.randomUUID();
        submitOrderRequest=new SubmitOrderRequest(itemList,3.0,storeID2,OrderType.DELIVERY, 3.3, 3.5, "Homer Street");
        assertThrows(StoreDoesNotExistException.class, ()-> {
            SubmitOrderResponse submitOrderResponse = ServiceSelector.getPaymentService().submitOrder(submitOrderRequest);
        });
    }

    @Test
    @Description("Tests for if the store does is closed")
    @DisplayName("When store with store ID is closed")
    void IntegrationTest_StoreDoesisClosed() throws InvalidRequestException {
//        storeRepo.deleteAll();
        Store updateStore = storeRepo.findById(storeID).orElse(null);
        updateStore.setOpen(false);
        //store.setOpen(false);
        //storeRepo.save(store);
        storeRepo.save(updateStore);
        submitOrderRequest=new SubmitOrderRequest(itemList,3.0,storeID,OrderType.DELIVERY, 3.3, 3.5, "Homer Street");
        assertThrows(cs.superleague.payment.exceptions.InvalidRequestException.class, ()-> {
            SubmitOrderResponse submitOrderResponse = ServiceSelector.getPaymentService().submitOrder(submitOrderRequest);
        });
    }




    @Test
    @Description("Tests whether the SubmitOrderRequest object is constructed correctly")
    @DisplayName("SubmitOrderRequest correct construction")
    void IntegrationTest_SubmitOrderRequestConstruction() {

        submitOrderRequest=new SubmitOrderRequest(itemList,3.0,storeID,OrderType.DELIVERY, 3.3, 3.5, "Homer Street");

        assertNotNull(submitOrderRequest);
        assertEquals(itemList,submitOrderRequest.getListOfItems());
        assertEquals(3.0,submitOrderRequest.getDiscount());
        assertEquals(storeID,submitOrderRequest.getStoreID());
        assertEquals(OrderType.DELIVERY,submitOrderRequest.getOrderType());
    }

    @Test
    @Description("This test tests whether an order is created correctly - should return valid data stored in order entity")
    @DisplayName("When Order is created correctly")
    void IntegrationTest_CreatOrderConstruction() throws PaymentException, StoreClosedException, InvalidRequestException, StoreDoesNotExistException, InterruptedException, cs.superleague.user.exceptions.InvalidRequestException {
        Order order=null;
        submitOrderRequest=new SubmitOrderRequest(itemList,3.0,storeID,OrderType.DELIVERY, 3.3, 3.5, "Homer Street");
        SubmitOrderResponse submitOrderResponse= ServiceSelector.getPaymentService().submitOrder(submitOrderRequest);

        if(orderRepo.findById(submitOrderResponse.getOrder().getOrderID()).isPresent()){
            order=orderRepo.findById(submitOrderResponse.getOrder().getOrderID()).orElse(null);
        }
        assertNotNull(order);
        assertEquals(userID, order.getUserID());
        assertEquals(itemList.size(),order.getItems().size());
        Iterator<Item> itemIterator_it = itemList.iterator();
        Iterator<Item> orderItemIterator_it = order.  getItems().iterator();
        while(itemIterator_it.hasNext() && orderItemIterator_it.hasNext()){
                Item item=itemIterator_it.next();
                Item orderItem=orderItemIterator_it.next();
                assertEquals(item.getPrice(),orderItem.getPrice());
                assertEquals(item.getQuantity(),orderItem.getQuantity());
                assertEquals(item.getBarcode(),orderItem.getBarcode());
                assertEquals(item.getStoreID(),orderItem.getStoreID());
                assertEquals(item.getDescription(),orderItem.getDescription());
                assertEquals(item.getProductID(),orderItem.getProductID());
                assertEquals(item.getImageUrl(),orderItem.getImageUrl());
        }

        assertEquals(3.0,order.getDiscount());
        assertEquals(storeID,order.getStoreID());
        //assertEquals(geoPoint2.getGeoID(),order.getStoreAddress().getGeoID());
        assertEquals(geoPoint2.getAddress(),order.getStoreAddress().getAddress());
        assertEquals(geoPoint2.getLatitude(),order.getStoreAddress().getLatitude());
        assertEquals(geoPoint2.getLongitude(),order.getStoreAddress().getLongitude());
        //assertEquals(geoPoint1.getGeoID(),order.getDeliveryAddress().getGeoID());
        assertEquals(OrderType.DELIVERY,order.getType());
    }


}
