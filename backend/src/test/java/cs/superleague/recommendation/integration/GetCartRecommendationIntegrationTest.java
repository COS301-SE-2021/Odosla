package cs.superleague.recommendation.integration;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.recommendation.RecommendationServiceImpl;
import cs.superleague.recommendation.exceptions.InvalidRequestException;
import cs.superleague.recommendation.exceptions.RecommendationRepoException;
import cs.superleague.recommendation.repos.RecommendationRepo;
import cs.superleague.recommendation.requests.GetCartRecommendationRequest;
import cs.superleague.recommendation.responses.GetCartRecommendationResponse;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.repos.ItemRepo;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.Description;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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
    List<String> itemsInCart;
    @BeforeEach
    void setUp() {
        Item item1 = new Item();
        item1.setProductID("p123");
        Item item2 = new Item();
        item2.setProductID("p124");
        Item item3 = new Item();
        item3.setProductID("p999");
        itemRepo.save(item1);
        itemRepo.save(item2);
        itemRepo.save(item3);
        GeoPoint geoPoint = new GeoPoint(1.0,1.0,"address");
        Order order = new Order();
        List<Item> itemList = new ArrayList<>();
        itemList.add(item1);
        itemList.add(item2);
        itemList.add(item3);
        order.setItems(itemList);
        order.setOrderID(UUID.randomUUID());
        order.setDeliveryAddress(geoPoint);
        order.setStoreAddress(geoPoint);
        orderRepo.save(order);
        itemsInCart = new ArrayList<>();
        itemsInCart.add("p123");
        itemsInCart.add("p124");
    }

    @AfterEach
    void tearDown() {

    }

    @Test
    @Description("")
    @DisplayName("hi")
    void checksTheAddingOfOrdersToTheRecommendationRepo_IntegrationTest() throws InvalidRequestException, RecommendationRepoException {
        GetCartRecommendationRequest request = new GetCartRecommendationRequest(itemsInCart);
        GetCartRecommendationResponse response = recommendationService.getCartRecommendation(request);
        System.out.println(response.getRecommendations().get(0).getProductID());
    }
}
