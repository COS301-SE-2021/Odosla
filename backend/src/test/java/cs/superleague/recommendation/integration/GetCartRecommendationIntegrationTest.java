package cs.superleague.recommendation.integration;

import cs.superleague.integration.ServiceSelector;
import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.exceptions.PaymentException;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.SubmitOrderRequest;
import cs.superleague.payment.responses.SubmitOrderResponse;
import cs.superleague.recommendation.RecommendationServiceImpl;
import cs.superleague.recommendation.dataclass.Recommendation;
import cs.superleague.recommendation.exceptions.InvalidRequestException;
import cs.superleague.recommendation.exceptions.RecommendationRepoException;
import cs.superleague.recommendation.repos.RecommendationRepo;
import cs.superleague.recommendation.requests.GetCartRecommendationRequest;
import cs.superleague.recommendation.responses.GetCartRecommendationResponse;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.StoreClosedException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.ItemRepo;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.repos.CustomerRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.Assert;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.Description;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@SpringBootTest
@Transactional
public class GetCartRecommendationIntegrationTest {

    @Autowired
    RecommendationRepo recommendationRepo;
    @Autowired
    RecommendationServiceImpl recommendationService;
    @Autowired
    OrderRepo orderRepo;
    @Autowired
    ItemRepo itemRepo;
    @Autowired
    StoreRepo storeRepo;
    @Autowired
    CustomerRepo customerRepo;

    private final String SECRET = "uQmMa86HgOi6uweJ1JSftIN7TBHFDa3KVJh6kCyoJ9bwnLBqA0YoCAhMMk";


    List<String> itemsInCart;
    @BeforeEach
    void setUp() throws PaymentException, StoreClosedException, cs.superleague.shopping.exceptions.InvalidRequestException, cs.superleague.user.exceptions.InvalidRequestException, InterruptedException, StoreDoesNotExistException {
        Customer customer = new Customer();
        customer.setCustomerID(UUID.randomUUID());
        customer.setName("Zivan");
        customer.setEmail("zivanh7@gmail.com");
        customer.setAccountType(UserType.CUSTOMER);
        customerRepo.save(customer);
        JwtUtil jwtUtil = new JwtUtil();
        String jwt = jwtUtil.generateJWTTokenCustomer(customer);
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
        Item item1 = new Item();
        item1.setProductID("p123");
        Item item2 = new Item();
        item2.setProductID("p124");
        Item item3 = new Item();
        item3.setProductID("p999");
        itemRepo.save(item1);
        itemRepo.save(item2);
        itemRepo.save(item3);
        List<Item> itemList = new ArrayList<>();
        itemList.add(item1);
        itemList.add(item2);
        itemList.add(item3);
        itemsInCart = new ArrayList<>();
        itemsInCart.add("p123");
        itemsInCart.add("p124");
        Store store = new Store();
        UUID storeID = UUID.randomUUID();
        store.setStoreID(storeID);
        store.setOpen(true);
        store.setStoreLocation(new GeoPoint(0.0, 0.0, ""));
        storeRepo.save(store);
        SubmitOrderRequest request = new SubmitOrderRequest(itemList, 0.0, storeID, OrderType.DELIVERY, 0.0, 0.0, "address");
        ServiceSelector.getPaymentService().submitOrder(request);
        Item item4 = new Item();
        item4.setProductID("p568");
        itemRepo.save(item4);
        List<Item> itemList1 = new ArrayList<>();
        itemList1.add(item4);
        request = new SubmitOrderRequest(itemList1, 0.0, storeID, OrderType.DELIVERY, 0.0, 0.0, "address");
        ServiceSelector.getPaymentService().submitOrder(request);
    }

    @AfterEach
    void tearDown() {
        storeRepo.deleteAll();
        orderRepo.deleteAll();
        customerRepo.deleteAll();
        recommendationRepo.deleteAll();
        itemRepo.deleteAll();
    }

    @Test
    @Description("Tests for when there is a null request object.")
    @DisplayName("Null request")
    void nullRequestObject_IntegrationTest(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> recommendationService.getCartRecommendation(null));
        Assertions.assertEquals("Null request object.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the list of items passed in is null.")
    @DisplayName("Null items list")
    void testsForWhenTheListOfItemsIsNull_IntegrationTest(){
        GetCartRecommendationRequest request = new GetCartRecommendationRequest(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> recommendationService.getCartRecommendation(request));
        Assertions.assertEquals("Null item list.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when there are no items in the list.")
    @DisplayName("No items in list")
    void noItemsInListPassedIn_IntegrationTest(){
        List<String> noItems = new ArrayList<>();
        GetCartRecommendationRequest request = new GetCartRecommendationRequest(noItems);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> recommendationService.getCartRecommendation(request));
        Assertions.assertEquals("No items in item list.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when none of the items in the cart have any recommendation entries.")
    @DisplayName("No items in recommendation")
    void testsForWhenNoItemsHaveBeenBoughtBefore_IntegrationTest() throws InvalidRequestException, RecommendationRepoException {
        List<String> itemsNotInRecommendation = new ArrayList<>();
        itemsNotInRecommendation.add("p000");
        GetCartRecommendationRequest request = new GetCartRecommendationRequest(itemsNotInRecommendation);
        GetCartRecommendationResponse response = recommendationService.getCartRecommendation(request);
        Assert.assertEquals(null,response.getRecommendations());
        Assert.assertEquals(false, response.isSuccess());
        Assert.assertEquals("None of these items have been bought before.", response.getMessage());
    }

    @Test
    @Description("Tests for when there is no common order for the items passed in.")
    @DisplayName("No common orders")
    void noCommonOrderAcrossItemsPassedIn_IntegrationTest() throws InvalidRequestException, RecommendationRepoException {
        List<String> itemsWithNoOrderInCommon = new ArrayList<>();
        itemsWithNoOrderInCommon.add("p568");
        itemsWithNoOrderInCommon.add("p123");
        GetCartRecommendationRequest request = new GetCartRecommendationRequest(itemsWithNoOrderInCommon);
        GetCartRecommendationResponse response = recommendationService.getCartRecommendation(request);
        Assert.assertEquals(null, response.getRecommendations());
        Assert.assertEquals(false, response.isSuccess());
        Assert.assertEquals("There are no orders that have all the requested items in them.", response.getMessage());
    }

    @Test
    @Description("Tests for when there is an order with one item that is not in the cart.")
    @DisplayName("One item recommendation")
    void checksTheAddingOfOrdersToTheRecommendationRepo_IntegrationTest() throws InvalidRequestException, RecommendationRepoException {
        GetCartRecommendationRequest request = new GetCartRecommendationRequest(itemsInCart);
        GetCartRecommendationResponse response = recommendationService.getCartRecommendation(request);
        Assert.assertEquals("p999",response.getRecommendations().get(0).getProductID());
        Assert.assertEquals(true, response.isSuccess());
        Assert.assertEquals("The following items are recommended to go with the cart.", response.getMessage());
    }
}
