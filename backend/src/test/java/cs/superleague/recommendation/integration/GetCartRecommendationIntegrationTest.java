package cs.superleague.recommendation.integration;

import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.recommendation.RecommendationServiceImpl;
import cs.superleague.recommendation.exceptions.InvalidRequestException;
import cs.superleague.recommendation.exceptions.RecommendationRepoException;
import cs.superleague.recommendation.repos.RecommendationRepo;
import cs.superleague.recommendation.requests.GetCartRecommendationRequest;
import cs.superleague.recommendation.responses.GetCartRecommendationResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.context.annotation.Description;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@Transactional
public class GetCartRecommendationIntegrationTest {

    @Autowired
    RecommendationRepo recommendationRepo;
    @Autowired
    RecommendationServiceImpl recommendationService;
    @Autowired
    OrderRepo orderRepo;

    List<String> itemsInCart;
    @BeforeEach
    void setUp() {
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
    }
}
