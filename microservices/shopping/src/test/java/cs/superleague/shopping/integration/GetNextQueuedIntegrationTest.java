package cs.superleague.shopping.integration;

import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.*;
import cs.superleague.payment.requests.SaveOrderToRepoRequest;
import cs.superleague.shopping.ShoppingServiceImpl;
import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.CatalogueRepo;
import cs.superleague.shopping.repos.ItemRepo;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.shopping.requests.GetNextQueuedRequest;
import cs.superleague.shopping.responses.GetNextQueuedResponse;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.requests.SaveShopperToRepoRequest;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.*;
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

import java.net.URISyntaxException;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class GetNextQueuedIntegrationTest {
    @Autowired
    StoreRepo storeRepo;

    @Autowired
    CatalogueRepo catalogueRepo;

    @Autowired
    ItemRepo itemRepo;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    RabbitTemplate rabbit;

    @Autowired
    ShoppingServiceImpl shoppingService;

    @Value("${env.SECRET}")
    private String SECRET = "stub";

    @Value("${env.HEADER}")
    private String HEADER = "stub";

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
    List<Item> listOfItems=new ArrayList<>();
    List<CartItem> cartItems = new ArrayList<>();
    List<Order> listOfOrders=new ArrayList<>();
    GeoPoint deliveryAddress=new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");
    GeoPoint storeAddress=new GeoPoint(3.0, 3.0, "Woolworthes, Hillcrest Boulevard");
    List<Item> expectedListOfItems=new ArrayList<>();

    @BeforeEach
    void setUp() {
        i1=new Item("Heinz Tamatoe Sauce","123456","123456",storeUUID1,36.99,1,"description","img/");
        CartItem ci1 = new CartItem("Heinz Tamatoe Sauce","123456","123456",storeUUID1,36.99,1,"description","img/");
        i2=new Item("Bar one","012345","012345",storeUUID1,14.99,3,"description","img/");
        CartItem ci2 = new CartItem("Bar one","012345","012345",storeUUID1,14.99,3,"description","img/");
        itemRepo.save(i1);
        itemRepo.save(i2);

        listOfItems.add(i1);
        listOfItems.add(i2);

        cartItems.add(ci1);
        cartItems.add(ci2);

        c=new Catalogue(storeUUID1,listOfItems);
        catalogueRepo.save(c);

        shopper = new Shopper();
        shopper.setShopperID(UUID.fromString("08d96d59-2e53-402a-92c4-0fb9a66fc2d4"));
        shopper.setName("Zivan");
        shopper.setEmail("zivanh7@gmail.com");
        shopper.setAccountType(UserType.SHOPPER);

        String jwt = jwtUtil.generateJWTTokenShopper(shopper);
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

        List<Shopper> shopperList= new ArrayList<>();
        shopperList.add(shopper);

        SaveShopperToRepoRequest saveShopperToRepoRequest = new SaveShopperToRepoRequest(shopper);
        rabbit.convertAndSend("UserEXCHANGE", "RK_SaveShopperToRepo", saveShopperToRepoRequest);
//        shopperRepo.save(shopper);

        s=new Store(storeUUID1,"Woolworths",c,2,null,null,4,true);
        s.setShoppers(shopperList);
        storeRepo.save(s);

        Date d1=new Date(2021,06,1,14,30);
        Date d2=new Date(2021,06,1,14,23);
        totalC=133.99;
        expectedDiscount=0.0;

        Calendar c1=Calendar.getInstance();
        Calendar c2=Calendar.getInstance();

        c1.setTime(d1);
        c2.setTime(d2);

        o=new Order(o1UUID, expectedU1, expectedS1, expectedShopper1, new Date(), d1, totalC,
                OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedDiscount, deliveryAddress, storeAddress, false);
        o2=new Order(o2UUID,expectedU1, expectedS1, expectedShopper1, new Date(), d2, totalC,
                OrderType.DELIVERY, OrderStatus.AWAITING_PAYMENT, expectedDiscount, deliveryAddress, storeAddress, false);


        o.setCartItems(cartItems);
        o2.setCartItems(cartItems);

        SaveOrderToRepoRequest saveOrderToRepoRequest = new SaveOrderToRepoRequest(o);
        rabbit.convertAndSend("PaymentEXCHANGE", "RK_saveOrderToRepo", saveOrderToRepoRequest);
        saveOrderToRepoRequest = new SaveOrderToRepoRequest(o);
        rabbit.convertAndSend("PaymentEXCHANGE", "RK_saveOrderToRepo", saveOrderToRepoRequest);

        listOfOrders.add(o);
        listOfOrders.add(o2);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @Description("Tests for when getNextQueued is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void IntegrationTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.getNextQueued(null));
        assertEquals("Request object for GetNextQueuedRequest can't be null - can't get next queued", thrown.getMessage());
    }

    @Test
    @Description("Tests for when storeID in request object is null- exception should be thrown")
    @DisplayName("When request object parameter -storeID - is not specificed")
    void IntegrationTest_testingNull_storeID_Parameter_RequestObject(){
        GetNextQueuedRequest request=new GetNextQueuedRequest(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.getNextQueued(request));
        assertEquals("Store ID parameter in request can't be null - can't get next queued", thrown.getMessage());
    }

    @Test
    @Description("Test for when Store with storeID does not exist in database - StoreDoesNotExist Exception should be thrown")
    @DisplayName("When Store with ID doesn't exist")
    void IntegrationTest_Store_doesnt_exist(){
        UUID invalidStoreID = UUID.randomUUID();
        while(invalidStoreID == storeUUID1){
            invalidStoreID = UUID.randomUUID();
        }
        GetNextQueuedRequest request=new GetNextQueuedRequest(invalidStoreID);
        Throwable thrown = Assertions.assertThrows(StoreDoesNotExistException.class, ()-> shoppingService.getNextQueued(request));
        assertEquals("Store with ID does not exist in repository - could not get next queued entity", thrown.getMessage());
    }

    @Test
    @Description("Test for when Store with storeID does not have any current orders in order queue")
    @DisplayName("Order queue is empty")
    void IntegrationTest_Store_no_orders_in_orderQueue() throws InvalidRequestException, StoreDoesNotExistException, cs.superleague.user.exceptions.InvalidRequestException, URISyntaxException {
        GetNextQueuedRequest request=new GetNextQueuedRequest(storeUUID1);
        Store updateStore = storeRepo.findById(storeUUID1).orElse(null);
        updateStore.setOrderQueue(null);
        storeRepo.save(updateStore);
        GetNextQueuedResponse response=shoppingService.getNextQueued(request);
        assertNotNull(response);
        assertEquals("The order queue of shop is empty", response.getMessage());
        assertEquals(false,response.isResponse());
    }

    @Test
    @Description("Tests whether the GetNextQueued request object was created correctly")
    @DisplayName("GetNextQueueRequest correctly constructed")
    void IntegrationTest_AddToQueueRequestConstruction() {
        GetNextQueuedRequest request = new GetNextQueuedRequest(storeUUID1);
        assertNotNull(request);
        assertEquals(storeUUID1, request.getStoreID());
    }
}
