package cs.superleague.recommendation;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    @Description("H")
    @DisplayName("hi")
    void testsGetCartRecommendationRequestObject_UnitTest() throws InvalidRequestException, RecommendationRepoException {
        GetCartRecommendationRequest request = new GetCartRecommendationRequest(itemsInCart);
        when(recommendationRepo.findRecommendationByProductyID("p123")).thenReturn(list1OfRecommendations);
        when(recommendationRepo.findRecommendationByProductyID("p124")).thenReturn(list2OfRecommendations);
        when(orderRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(order));
        GetCartRecommendationResponse response = recommendationService.getCartRecommendation(request);
        System.out.println(response.getRecommendations().get(0).getProductID());
    }
}
