package cs.superleague.recommendation;

import cs.superleague.integration.ServiceSelector;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.recommendation.dataclass.Recommendation;
import cs.superleague.recommendation.exceptions.InvalidRequestException;
import cs.superleague.recommendation.exceptions.RecommendationRepoException;
import cs.superleague.recommendation.repos.RecommendationRepo;
import cs.superleague.recommendation.requests.GetCartRecommendationRequest;
import cs.superleague.recommendation.responses.GetCartRecommendationResponse;
import cs.superleague.shopping.dataclass.Item;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetCartRecommendationUnitTest {
    @Mock
    private RecommendationRepo recommendationRepo;
    @Mock
    private OrderRepo orderRepo;
    @InjectMocks
    private RecommendationServiceImpl recommendationService;

    List<String> itemsInCart;
    Recommendation recommendation;
    Recommendation recommendation1;
    List<Recommendation> list1OfRecommendations = new ArrayList<>();
    List<Recommendation> list2OfRecommendations = new ArrayList<>();
    Order order;

    @BeforeEach
    void setUp() {
        itemsInCart = new ArrayList<>();
        itemsInCart.add("p123");
        itemsInCart.add("p124");
        order = new Order();
        UUID orderID = UUID.randomUUID();
        order.setOrderID(orderID);
        Item item = new Item();
        item.setProductID("p123");
        List<Item> items = new ArrayList<>();
        items.add(item);
        Item item1 = new Item();
        item1.setProductID("p124");
        items.add(item1);
        Item item2 = new Item();
        item2.setProductID("p999");
        items.add(item2);
        order.setItems(items);
        recommendation = new Recommendation();
        recommendation.setProductID("p123");
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        recommendation.setOrderID(orderID);
        recommendation1 = new Recommendation();
        recommendation1.setProductID("p124");
        recommendation1.setOrderID(orderID);
        list1OfRecommendations.add(recommendation);
        list2OfRecommendations.add(recommendation1);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests the request objects creation.")
    @DisplayName("Request object")
    void testsTheRequestObjectIsCreatedSuccessfully_UnitTest(){
        GetCartRecommendationRequest request = new GetCartRecommendationRequest(itemsInCart);
        for (int i = 0; i < itemsInCart.size(); i++){
            assertEquals(itemsInCart.get(i), request.getItemIDs().get(i));
        }
    }

    @Test
    @Description("Null request object being passed in.")
    @DisplayName("Null request object")
    void nullRequestObjectPassedIntoFunction_UnitTest(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> recommendationService.getCartRecommendation(null));
        Assertions.assertEquals("Null request object.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the list of items to get recommendation for is null.")
    @DisplayName("Null parameter")
    void nullListOfItemsInRequestObject_UnitTest(){
        GetCartRecommendationRequest request = new GetCartRecommendationRequest(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> recommendationService.getCartRecommendation(request));
        Assertions.assertEquals("Null item list.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when the item list passed in has no items in it, size of 0.")
    @DisplayName("No items in list")
    void noItemsInListPassedInThroughRequestObject_UnitTest(){
        List<String> noItems = new ArrayList<>();
        GetCartRecommendationRequest request = new GetCartRecommendationRequest(noItems);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> recommendationService.getCartRecommendation(request));
        Assertions.assertEquals("No items in item list.", thrown.getMessage());
    }

    @Test
    @Description("Tests for when none of the items in the list have been bought before so therefore not found on the recommendation table.")
    @DisplayName("New items")
    void testsForWhenNoneOfTheItemsInTheListHaveBeenBoughtBefore_UnitTest() throws InvalidRequestException, RecommendationRepoException {
        List<String> newItems = new ArrayList<>();
        newItems.add("p000");
        newItems.add("p111");
        GetCartRecommendationRequest request = new GetCartRecommendationRequest(newItems);
        when(recommendationRepo.findRecommendationByProductID("p111")).thenReturn(null);
        when(recommendationRepo.findRecommendationByProductID("p000")).thenReturn(null);
        GetCartRecommendationResponse response = recommendationService.getCartRecommendation(request);
        assertEquals("None of these items have been bought before.", response.getMessage());
        assertEquals(false, response.isSuccess());
        assertEquals(null, response.getRecommendations());
    }

    @Test
    @Description("Tests for when some of the items are not bought before and some are.")
    @DisplayName("No common order")
    void noCommonOrderAcrossItems_UnitTest() throws InvalidRequestException, RecommendationRepoException {
        List<String> halfNewHalfOld = new ArrayList<>();
        halfNewHalfOld.add("p123");
        halfNewHalfOld.add("p111");
        GetCartRecommendationRequest request = new GetCartRecommendationRequest(halfNewHalfOld);
        when(recommendationRepo.findRecommendationByProductID("p123")).thenReturn(list1OfRecommendations);
        when(recommendationRepo.findRecommendationByProductID("p111")).thenReturn(null);
        GetCartRecommendationResponse response = recommendationService.getCartRecommendation(request);
        assertEquals("There are no orders that have all the requested items in them.", response.getMessage());
        assertEquals(false, response.isSuccess());
        assertEquals(null, response.getRecommendations());
    }

    @Test
    @Description("Tests for the successful recommendation of one item")
    @DisplayName("One item recommendation")
    void testsGetCartRecommendationRequestObject_UnitTest() throws InvalidRequestException, RecommendationRepoException {
        GetCartRecommendationRequest request = new GetCartRecommendationRequest(itemsInCart);
        when(recommendationRepo.findRecommendationByProductID("p123")).thenReturn(list1OfRecommendations);
        when(recommendationRepo.findRecommendationByProductID("p124")).thenReturn(list2OfRecommendations);
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(order));
        GetCartRecommendationResponse response = recommendationService.getCartRecommendation(request);
        assertEquals("p999", response.getRecommendations().get(0).getProductID());
        assertEquals("The following items are recommended to go with the cart.", response.getMessage());
        assertEquals(true, response.isSuccess());
    }
}
