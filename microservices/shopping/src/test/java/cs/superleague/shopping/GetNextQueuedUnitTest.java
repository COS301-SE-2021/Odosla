package cs.superleague.shopping;

import cs.superleague.integration.security.CurrentUser;
import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.dataclass.*;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.responses.GetOrderResponse;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.responses.GetShopperByEmailResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Description;
import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.shopping.requests.GetNextQueuedRequest;
import cs.superleague.shopping.responses.GetNextQueuedResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetNextQueuedUnitTest {

    @Value("${paymentHost}")
    private String paymentHost;
    @Value("${paymentPort}")
    private String paymentPort;
    @Value("${userHost}")
    private String userHost;
    @Value("${userPort}")
    private String userPort;

    @Mock
    private StoreRepo storeRepo;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private ShoppingServiceImpl shoppingService;

    private String SECRET = "uQmMa86HgOi6uweJ1JSftIN7TBHFDa3KVJh6kCyoJ9bwnLBqA0YoCAhMMk";

    UUID storeUUID1= UUID.randomUUID();
    Shopper shopper;
    Store s;
    Catalogue c;
    Order o;
    Order o2;
    UUID o1UUID=UUID.randomUUID();
    UUID o2UUID=UUID.randomUUID();
    UUID expectedU1=UUID.randomUUID();
    UUID expectedS1=UUID.randomUUID();
    UUID expectedShopper1=UUID.randomUUID();
    Double expectedDiscount;
    Double totalC;
    Item i1;
    Item i2;
    CartItem c1;
    CartItem c2;
    List<Item> listOfItems=new ArrayList<>();
    List<CartItem> cartItems=new ArrayList<>();
    List<Order> listOfOrders=new ArrayList<>();
    String expectedMessage;
    OrderStatus expectedStatus;
    OrderType expectedType;
    GeoPoint deliveryAddress=new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");
    GeoPoint storeAddress=new GeoPoint(3.0, 3.0, "Woolworthes, Hillcrest Boulevard");
    List<Item> expectedListOfItems=new ArrayList<>();

    @BeforeEach
    void setUp() {
        i1=new Item("Heinz Tamatoe Sauce","123456","123456",storeUUID1,36.99,1,"description","img/");
        c1=new CartItem("Heinz Tamatoe Sauce","123456","123456",storeUUID1,36.99,1,"description","img/");
        i2=new Item("Bar one","012345","012345",storeUUID1,14.99,3,"description","img/");
        c2=new CartItem("Bar one","012345","012345",storeUUID1,14.99,3,"description","img/");
        listOfItems.add(i1);
        listOfItems.add(i2);
        cartItems.add(c1);
        cartItems.add(c2);
        c=new Catalogue(storeUUID1,listOfItems);
        s=new Store(storeUUID1,"Woolworthes",c,2,null,null,4,true);
        Date d1=new Date(2021,06,1,14,30);
        Date d2=new Date(2021,06,1,14,23);
        totalC=133.99;
        expectedDiscount=0.0;

        shopper = new Shopper();
        shopper.setShopperID(UUID.randomUUID());
        shopper.setAccountType(UserType.SHOPPER);
        shopper.setEmail("u14254922@tuks.co.za");
        shopper.setOnShift(true);

        o=new Order(o1UUID, expectedU1, expectedS1, expectedShopper1, new Date(), d1, totalC,
                OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedDiscount, deliveryAddress, storeAddress, false);
        o2=new Order(o2UUID,expectedU1, expectedS1, expectedShopper1, new Date(), d2, totalC,
                OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedDiscount, deliveryAddress, storeAddress, false);

        o.setCartItems(cartItems);
        o2.setCartItems(cartItems);

        listOfOrders.add(o);
        listOfOrders.add(o2);

        JwtUtil jwtUtil = new JwtUtil();
        String jwt = jwtUtil.generateJWTTokenShopper(shopper);
        jwt = jwt.replace("Bearer ","");
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
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests for when getNextQueued is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.getNextQueued(null));
        assertEquals("Request object for GetNextQueuedRequest can't be null - can't get next queued", thrown.getMessage());
    }

    @Test
    @Description("Tests for when storeID in request object is null- exception should be thrown")
    @DisplayName("When request object parameter -storeID - is not specificed")
    void UnitTest_testingNull_storeID_Parameter_RequestObject(){
        GetNextQueuedRequest request=new GetNextQueuedRequest(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.getNextQueued(request));
        assertEquals("Store ID parameter in request can't be null - can't get next queued", thrown.getMessage());
    }

    @Test
    @Description("Test for when Store with storeID does not exist in database - StoreDoesNotExist Exception should be thrown")
    @DisplayName("When Store with ID doesn't exist")
    void UnitTest_Store_doesnt_exist(){
        GetNextQueuedRequest request=new GetNextQueuedRequest(storeUUID1);
        when(storeRepo.findById(Mockito.any())).thenReturn(null);
        Throwable thrown = Assertions.assertThrows(StoreDoesNotExistException.class, ()-> shoppingService.getNextQueued(request));
        assertEquals("Store with ID does not exist in repository - could not get next queued entity", thrown.getMessage());
    }

    @Test
    @Description("Test for when Store with storeID does not have any current orders in order queue")
    @DisplayName("Order queue is empty")
    void UnitTest_Store_no_orders_in_orderQueue() throws InvalidRequestException, StoreDoesNotExistException, cs.superleague.user.exceptions.InvalidRequestException, URISyntaxException {
        GetNextQueuedRequest request=new GetNextQueuedRequest(storeUUID1);
        List<Order> listOfOrders2=new ArrayList<>();
        s.setOrderQueue(null);
        when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(s));
        GetNextQueuedResponse response=shoppingService.getNextQueued(request);
        assertNotNull(response);
        assertEquals("The order queue of shop is empty", response.getMessage());
        assertEquals(false,response.isResponse());
    }

    @Test
    @Description("Test for when order and removes previous order from order queue")
    @DisplayName("Removes and returns correct order")
    void UnitTest_order_is_correctly_removed() throws InvalidRequestException, StoreDoesNotExistException, cs.superleague.user.exceptions.InvalidRequestException, URISyntaxException {

        GetNextQueuedRequest request=new GetNextQueuedRequest(storeUUID1);
        s.setOrderQueue(listOfOrders);
        when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(s));

        List<Order> orderQueue= s.getOrderQueue();

        String stringUri = "http://"+paymentHost+":"+paymentPort+"/payment/getOrder";
        URI uri = new URI(stringUri);

        Map<String, Object> parts = new HashMap<String, Object>();
        parts.put("orderID", orderQueue.get(1).getOrderID().toString());

        GetOrderResponse getOrderResponse = new GetOrderResponse(o, true, new Date(),
                "Order successfully retrieved");

        ResponseEntity<GetOrderResponse> responseEntity = new ResponseEntity<>(getOrderResponse, HttpStatus.OK);

        Mockito.when(restTemplate.postForEntity(uri, parts, GetOrderResponse.class)).thenReturn(responseEntity);

        parts = new HashMap<>();
        parts.put("email", shopper.getEmail());

        stringUri = "http://"+userHost+":"+userPort+"/user/getShopperByEmail";
        uri = new URI(stringUri);


        GetShopperByEmailResponse getShopperByEmailResponse = new GetShopperByEmailResponse(shopper, true);

        ResponseEntity<GetShopperByEmailResponse> getShopperByEmailResponseResponseEntity =
                new ResponseEntity<>(getShopperByEmailResponse, HttpStatus.OK);

        Mockito.when(restTemplate.postForEntity(uri, parts, GetShopperByEmailResponse.class))
                .thenReturn(getShopperByEmailResponseResponseEntity);

        CurrentUser currentUser = new CurrentUser();

        currentUser.setEmail(shopper.getEmail());

        Mockito.when(new CurrentUser()).thenReturn(currentUser);

        GetNextQueuedResponse response=shoppingService.getNextQueued(request);
        listOfOrders.remove(o2);
        assertNotNull(response);
        assertEquals(true,response.isResponse());
        assertEquals(listOfOrders.size(),response.getQueueOfOrders().size());
        assertEquals("Queue was successfully updated for store",response.getMessage());
        assertEquals(listOfOrders,response.getQueueOfOrders());
        assertEquals(o2,response.getNewCurrentOrder());

    }

    /** Checking request object is created correctly */
    @Test
    @Description("Tests whether the GetNextQueued request object was created correctly")
    @DisplayName("GetNextQueueRequest correctly constructed")
    void UnitTest_AddToQueueRequestConstruction() {

        GetNextQueuedRequest request = new GetNextQueuedRequest(storeUUID1);

        assertNotNull(request);
        assertEquals(storeUUID1, request.getStoreID());
    }

}
