//package cs.superleague.recommendation;
//
//import cs.superleague.payment.dataclass.CartItem;
//import cs.superleague.payment.dataclass.Order;
//import cs.superleague.payment.responses.GetAllCartItemsResponse;
//import cs.superleague.recommendation.dataclass.Recommendation;
//import cs.superleague.recommendation.exceptions.InvalidRequestException;
//import cs.superleague.recommendation.exceptions.RecommendationRepoException;
//import cs.superleague.recommendation.repos.RecommendationRepo;
//import cs.superleague.recommendation.requests.GetCartRecommendationRequest;
//import cs.superleague.recommendation.responses.GetCartRecommendationResponse;
//import cs.superleague.shopping.dataclass.Item;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Description;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.web.client.RestTemplate;
//
//import java.net.URISyntaxException;
//import java.util.*;
//
//import static org.junit.Assert.assertEquals;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class GetCartRecommendationUnitTest {
//    @Value("${paymentHost}")
//    private String paymentHost;
//    @Value("${paymentPort}")
//    private String paymentPort;
//    @Mock
//    private RecommendationRepo recommendationRepo;
//    @InjectMocks
//    private RecommendationServiceImpl recommendationService;
//    @Mock
//    RabbitTemplate rabbitTemplate;
//    @Mock
//    RestTemplate restTemplate;
//    List<String> itemsInCart;
//    Recommendation recommendation;
//    Recommendation recommendation1;
//    List<Recommendation> list1OfRecommendations = new ArrayList<>();
//    List<Recommendation> list2OfRecommendations = new ArrayList<>();
//    List<CartItem> cartItems = new ArrayList<>();
//    Order order;
//
//    @BeforeEach
//    void setUp() {
//        itemsInCart = new ArrayList<>();
//        itemsInCart.add("p123");
//        itemsInCart.add("p124");
//        order = new Order();
//        UUID orderID = UUID.randomUUID();
//        order.setOrderID(orderID);
//        Item item = new Item();
//        item.setProductID("p123");
//        List<Item> items = new ArrayList<>();
//        items.add(item);
//        Item item1 = new Item();
//        item1.setProductID("p124");
//        items.add(item1);
//        Item item2 = new Item();
//        item2.setProductID("p999");
//        items.add(item2);
//        recommendation = new Recommendation();
//        recommendation.setProductID("p123");
//        List<Order> orders = new ArrayList<>();
//        orders.add(order);
//        recommendation.setOrderID(orderID);
//        recommendation1 = new Recommendation();
//        recommendation1.setProductID("p124");
//        recommendation1.setOrderID(orderID);
//        list1OfRecommendations.add(recommendation);
//        list2OfRecommendations.add(recommendation1);
//    }
//
//    @AfterEach
//    void tearDown() {
//    }
//
//    @Test
//    @Description("Tests the request objects creation.")
//    @DisplayName("Request object creation")
//    void testsTheRequestObjectIsCreatedSuccessfully_UnitTest(){
//        GetCartRecommendationRequest request = new GetCartRecommendationRequest(itemsInCart);
//        for (int i = 0; i < itemsInCart.size(); i++){
//            assertEquals(itemsInCart.get(i), request.getItemIDs().get(i));
//        }
//    }
//
//    @Test
//    @Description("Null request object being passed in.")
//    @DisplayName("Null request object and no items returned")
//    void nullRequestObjectPassedIntoFunction_UnitTest() throws InvalidRequestException, URISyntaxException, RecommendationRepoException {
//        cartItems = new ArrayList<>();
//        Map<String, Object> parts = new HashMap<>();
//        GetAllCartItemsResponse getAllCartItemsResponse = new GetAllCartItemsResponse(cartItems, new Date(), "");
//        ResponseEntity<GetAllCartItemsResponse> responseEntity = new ResponseEntity<>(getAllCartItemsResponse, HttpStatus.OK);
//        when(restTemplate.postForEntity("http://" + paymentHost + ":" + paymentPort + "/payment/getAllCartItems",
//                parts, GetAllCartItemsResponse.class)).thenReturn(responseEntity);
//        GetCartRecommendationResponse response = recommendationService.getCartRecommendation(null);
//        Assertions.assertEquals("Could not retrieve Items", response.getMessage());
//        Assertions.assertEquals(false, response.isSuccess());
//    }
//
//    @Test
//    @Description("Null request object being passed in.")
//    @DisplayName("Null request object and items returned")
//    void nullRequestObjectPassedIntoFunctionItemsReturned_UnitTest() throws InvalidRequestException, URISyntaxException, RecommendationRepoException {
//        CartItem cartItem = new CartItem();
//        cartItem.setProductID("123");
//        cartItems.add(cartItem);
//        Map<String, Object> parts = new HashMap<>();
//        GetAllCartItemsResponse getAllCartItemsResponse = new GetAllCartItemsResponse(cartItems, new Date(), "");
//        ResponseEntity<GetAllCartItemsResponse> responseEntity = new ResponseEntity<>(getAllCartItemsResponse, HttpStatus.OK);
//        when(restTemplate.postForEntity("http://" + paymentHost + ":" + paymentPort + "/payment/getAllCartItems",
//                parts, GetAllCartItemsResponse.class)).thenReturn(responseEntity);
//        GetCartRecommendationResponse response = recommendationService.getCartRecommendation(null);
//        Assertions.assertEquals("Random recommendations returned because Null request object.", response.getMessage());
//        Assertions.assertEquals(false, response.isSuccess());
//    }
//
//    @Test
//    @Description("Tests for when the list of items to get recommendation for is null.")
//    @DisplayName("Null parameter and items returned")
//    void nullListOfItemsInRequestObject_UnitTest() throws InvalidRequestException, URISyntaxException, RecommendationRepoException {
//        GetCartRecommendationRequest request = new GetCartRecommendationRequest(null);
//        CartItem cartItem = new CartItem();
//        cartItem.setProductID("123");
//        cartItems.add(cartItem);
//        Map<String, Object> parts = new HashMap<>();
//        GetAllCartItemsResponse getAllCartItemsResponse = new GetAllCartItemsResponse(cartItems, new Date(), "");
//        ResponseEntity<GetAllCartItemsResponse> responseEntity = new ResponseEntity<>(getAllCartItemsResponse, HttpStatus.OK);
//        when(restTemplate.postForEntity("http://" + paymentHost + ":" + paymentPort + "/payment/getAllCartItems",
//                parts, GetAllCartItemsResponse.class)).thenReturn(responseEntity);
//        GetCartRecommendationResponse response = recommendationService.getCartRecommendation(request);
//        Assertions.assertEquals("Random recommendations returned because Null item list.", response.getMessage());
//        Assertions.assertEquals(false, response.isSuccess());
//    }
//
//    @Test
//    @Description("Tests for when the item list passed in has no items in it, size of 0.")
//    @DisplayName("No items in list and random items returned")
//    void noItemsInListPassedInThroughRequestObject_UnitTest() throws InvalidRequestException, URISyntaxException, RecommendationRepoException {
//        List<String> noItems = new ArrayList<>();
//        GetCartRecommendationRequest request = new GetCartRecommendationRequest(noItems);
//        CartItem cartItem = new CartItem();
//        cartItem.setProductID("123");
//        cartItems.add(cartItem);
//        Map<String, Object> parts = new HashMap<>();
//        GetAllCartItemsResponse getAllCartItemsResponse = new GetAllCartItemsResponse(cartItems, new Date(), "");
//        ResponseEntity<GetAllCartItemsResponse> responseEntity = new ResponseEntity<>(getAllCartItemsResponse, HttpStatus.OK);
//        when(restTemplate.postForEntity("http://" + paymentHost + ":" + paymentPort + "/payment/getAllCartItems",
//                parts, GetAllCartItemsResponse.class)).thenReturn(responseEntity);
//        GetCartRecommendationResponse response = recommendationService.getCartRecommendation(request);
//        Assertions.assertEquals("Random recommendations returned because No items in item list.", response.getMessage());
//        Assertions.assertEquals(false, response.isSuccess());
//    }
//
//    @Test
//    @Description("Tests for when none of the items in the list have been bought before so therefore not found on the recommendation table.")
//    @DisplayName("New items")
//    void testsForWhenNoneOfTheItemsInTheListHaveBeenBoughtBefore_UnitTest() throws InvalidRequestException, RecommendationRepoException, URISyntaxException {
//        List<String> newItems = new ArrayList<>();
//        newItems.add("p000");
//        newItems.add("p111");
//        GetCartRecommendationRequest request = new GetCartRecommendationRequest(newItems);
//        when(recommendationRepo.findRecommendationByProductID("p111")).thenReturn(null);
//        when(recommendationRepo.findRecommendationByProductID("p000")).thenReturn(null);
//        CartItem cartItem = new CartItem();
//        cartItem.setProductID("123");
//        cartItems.add(cartItem);
//        Map<String, Object> parts = new HashMap<>();
//        GetAllCartItemsResponse getAllCartItemsResponse = new GetAllCartItemsResponse(cartItems, new Date(), "");
//        ResponseEntity<GetAllCartItemsResponse> responseEntity = new ResponseEntity<>(getAllCartItemsResponse, HttpStatus.OK);
//        when(restTemplate.postForEntity("http://" + paymentHost + ":" + paymentPort + "/payment/getAllCartItems",
//                parts, GetAllCartItemsResponse.class)).thenReturn(responseEntity);
//        GetCartRecommendationResponse response = recommendationService.getCartRecommendation(request);
//        Assertions.assertEquals("Random recommendations returned because No items in item list.", response.getMessage());
//        Assertions.assertEquals(false, response.isSuccess());
//    }
//
//    @Test
//    @Description("Tests for when some of the items are not bought before and some are.")
//    @DisplayName("No common order")
//    void noCommonOrderAcrossItems_UnitTest() throws InvalidRequestException, RecommendationRepoException, URISyntaxException {
//        List<String> halfNewHalfOld = new ArrayList<>();
//        halfNewHalfOld.add("p123");
//        halfNewHalfOld.add("p111");
//        GetCartRecommendationRequest request = new GetCartRecommendationRequest(halfNewHalfOld);
//        when(recommendationRepo.findRecommendationByProductID("p123")).thenReturn(list1OfRecommendations);
//        when(recommendationRepo.findRecommendationByProductID("p111")).thenReturn(null);
//        GetCartRecommendationResponse response = recommendationService.getCartRecommendation(request);
//        assertEquals("There are no orders that have all the requested items in them.", response.getMessage());
//        assertEquals(false, response.isSuccess());
//        assertEquals(null, response.getRecommendations());
//    }
//
//    @Test
//    @Description("Tests for the successful recommendation of one item")
//    @DisplayName("One item recommendation")
//    void testsGetCartRecommendationRequestObject_UnitTest() throws InvalidRequestException, RecommendationRepoException, URISyntaxException {
//        GetCartRecommendationRequest request = new GetCartRecommendationRequest(itemsInCart);
//        when(recommendationRepo.findRecommendationByProductID("p123")).thenReturn(list1OfRecommendations);
//        when(recommendationRepo.findRecommendationByProductID("p124")).thenReturn(list2OfRecommendations);
//        //when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(order));
//        GetCartRecommendationResponse response = recommendationService.getCartRecommendation(request);
//        assertEquals("p999", response.getRecommendations().get(0).getProductID());
//        assertEquals("The following items are recommended to go with the cart.", response.getMessage());
//        assertEquals(true, response.isSuccess());
//    }
//
//    @Test
//    @Description("Tests for when there are too many items to recommend so it cuts to the first 3.")
//    @DisplayName("Three item recommendation")
//    void testsForWhenThereAreTooManyItemsToRecommend_UnitTest() throws InvalidRequestException, RecommendationRepoException, URISyntaxException {
//        Item item3 = new Item();
//        item3.setProductID("p679");
//        Item item4 = new Item();
//        item4.setProductID("p830");
//        Item item5 = new Item();
//        item5.setProductID("p029");
//        //List<Item> items = order.getItems();
////        items.add(item3);
////        items.add(item4);
////        items.add(item5);
////        order.setItems(items);
//        GetCartRecommendationRequest request = new GetCartRecommendationRequest(itemsInCart);
//        when(recommendationRepo.findRecommendationByProductID("p123")).thenReturn(list1OfRecommendations);
//        when(recommendationRepo.findRecommendationByProductID("p124")).thenReturn(list2OfRecommendations);
//        //when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(order));
//        GetCartRecommendationResponse response = recommendationService.getCartRecommendation(request);
//        assertEquals("p999", response.getRecommendations().get(0).getProductID());
//        assertEquals("p679", response.getRecommendations().get(1).getProductID());
//        assertEquals("p830", response.getRecommendations().get(2).getProductID());
//        assertEquals("The following items are recommended to go with the cart.", response.getMessage());
//        assertEquals(true, response.isSuccess());
//    }
//}
