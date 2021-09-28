package cs.superleague.recommendation;

import cs.superleague.payment.dataclass.CartItem;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.responses.GetAllCartItemsResponse;
import cs.superleague.payment.responses.GetOrderByUUIDResponse;
import cs.superleague.payment.responses.GetOrderResponse;
import cs.superleague.recommendation.dataclass.Recommendation;
import cs.superleague.recommendation.exceptions.InvalidRequestException;
import cs.superleague.recommendation.exceptions.RecommendationRepoException;
import cs.superleague.recommendation.repos.RecommendationRepo;
import cs.superleague.recommendation.requests.AddRecommendationRequest;
import cs.superleague.recommendation.requests.GetCartRecommendationRequest;
import cs.superleague.recommendation.requests.GetOrderRecommendationRequest;
import cs.superleague.recommendation.requests.RemoveRecommendationRequest;
import cs.superleague.shopping.responses.GetAllItemsResponse;
import cs.superleague.recommendation.responses.GetCartRecommendationResponse;
import cs.superleague.recommendation.responses.GetOrderRecommendationResponse;
import cs.superleague.shopping.dataclass.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Service("recommendationServiceImpl")
public class RecommendationServiceImpl implements RecommendationService {
    @Value("${paymentHost}")
    private String paymentHost;
    @Value("${paymentPort}")
    private String paymentPort;

    private final RecommendationRepo recommendationRepo;
    private final RestTemplate restTemplate;

    @Autowired
    public RecommendationServiceImpl(RecommendationRepo recommendationRepo, RestTemplate restTemplate) {
        this.recommendationRepo = recommendationRepo;
        this.restTemplate = restTemplate;
    }


    @Override
    public GetCartRecommendationResponse getCartRecommendation(GetCartRecommendationRequest request) throws InvalidRequestException, RecommendationRepoException, URISyntaxException {
        System.out.println("IMPLEMENTATION IDS REQUEST: ");
        System.out.println(request.getItemIDs());
        if (request == null) {
            return getRandomRecommendations("Null request object.");
        }
        if (request.getItemIDs() == null) {
            return getRandomRecommendations("Null item list.");
        }
        if (request.getItemIDs().size() == 0) {
            return getRandomRecommendations("No items in item list.");
        }
        if (recommendationRepo != null) {
            List<UUID> orderIDs = new ArrayList<>();
            List<Integer> frequencyOfOrders = new ArrayList<>();
            for (String productID : request.getItemIDs()) {
                System.out.println("FORLOOP:"+productID);
                List<Recommendation> recommendation = recommendationRepo.findRecommendationByProductID(productID);
                if (recommendation != null) {
                    for (Recommendation orderID : recommendation) {
                        if (!orderIDs.contains(orderID.getOrderID())) {
                            orderIDs.add(orderID.getOrderID());
                            frequencyOfOrders.add(1);
                        } else {
                            frequencyOfOrders.set(orderIDs.indexOf(orderID.getOrderID()), frequencyOfOrders.get(orderIDs.indexOf(orderID.getOrderID())) + 1);
                        }
                    }
                }
            }
            if (orderIDs.size() == 0) {
                return getRandomRecommendations("None of these items have been bought before.");
            }
            List<Order> finalRecommendation = new ArrayList<>();
            for (Integer frequency : frequencyOfOrders) {
                if (frequency >= request.getItemIDs().size()) {
                    System.out.println("WE ARE HERE 1");
                    System.out.println(orderIDs.get(frequencyOfOrders.indexOf(frequency)));

                    Map<String, Object> parts = new HashMap<>();
                    parts.put("orderID", orderIDs.get(frequencyOfOrders.indexOf(frequency)));
                    String stringUri = "http://" + paymentHost + ":" + paymentPort + "/payment/getOrderByUUID";
                    URI uri = new URI(stringUri);
                    ResponseEntity<GetOrderByUUIDResponse> responseEntity = restTemplate.postForEntity(
                            uri, parts, GetOrderByUUIDResponse.class);

                    Order order = null;
                    if (responseEntity.getBody() != null) {
                        order = responseEntity.getBody().getOrder();
                    }

                    if (responseEntity == null || !responseEntity.hasBody() ||
                            responseEntity.getBody() == null || responseEntity.getBody().getOrder() == null) {
                        return getRandomRecommendations("Could not Retrieve Orders");
                    }

                    if (order != null) {
                        System.out.println("WE GOT THE ORDER:"+order.getOrderID());
                        for(int i = 0; i < order.getCartItems().size();i++){
                            System.out.println("ITEM "+i+" "+order.getCartItems().get(i).getProductID());
                        }
                        finalRecommendation.add(order);
                    }
                }
            }
            List<CartItem> finalItemsRecommendation = new ArrayList<>();
            for (Order orders : finalRecommendation) {
                for (CartItem item : orders.getCartItems()) {
                    if (request.getItemIDs().contains(item.getProductID())) {
                        continue;
                    }
                    if (finalItemsRecommendation.contains(item)) {
                        continue;
                    }
                    finalItemsRecommendation.add(item);
                    if (finalItemsRecommendation.size() == 3) {
                        break;
                    }
                }
            }
            if (finalItemsRecommendation.size() == 0) {
                return getRandomRecommendations("There are no orders that have all the requested items in them.");
            }
            GetCartRecommendationResponse response = new GetCartRecommendationResponse(finalItemsRecommendation, true, "The following items are recommended to go with the cart.");
            return response;
        } else {
            return getRandomRecommendations("No recommendation repository found.");
        }
    }

    @Override
    public GetOrderRecommendationResponse getOrderRecommendation(GetOrderRecommendationRequest request) {
        return null;
    }

    @Override
    public void addRecommendation(AddRecommendationRequest request) throws InvalidRequestException {
        if (request == null) {
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getOrderID() == null || request.getProductID() == null) {
            throw new InvalidRequestException("Null parameters.");
        }
        for (String productID : request.getProductID()) {
            UUID recommendationID = UUID.randomUUID();
            while (recommendationRepo.findRecommendationByRecommendationID(recommendationID) != null) {
                recommendationID = UUID.randomUUID();
            }
            Recommendation recommendation = new Recommendation(recommendationID, productID, request.getOrderID());
            recommendationRepo.save(recommendation);
        }
    }

    @Override
    public void removeRecommendation(RemoveRecommendationRequest request) throws InvalidRequestException {
        if (request == null) {
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getOrderID() == null) {
            throw new InvalidRequestException("Null parameters.");
        }
        List<Recommendation> recommendations = recommendationRepo.findRecommendationByOrderID(request.getOrderID());
        for (Recommendation recommendation : recommendations) {
            recommendationRepo.delete(recommendation);
        }
    }

    // Helper/s

    private GetCartRecommendationResponse getRandomRecommendations(String errorMessage) throws URISyntaxException {
        System.out.println("ERROR MESSAGE COMING INTO RANDOM: "+errorMessage);
        int count = 0;
        int randomInt = 0;
        List<CartItem> allItems;
        List<CartItem> randomItems = new ArrayList<>();
        Random random = new Random();
        Map<String, Object> parts = new HashMap<>();

        String stringUri = "http://" + paymentHost + ":" + paymentPort + "/payment/getAllCartItems";
        URI uri = new URI(stringUri);

        ResponseEntity<GetAllCartItemsResponse> responseEntity = restTemplate.postForEntity(
                uri,
                parts, GetAllCartItemsResponse.class);

        if (responseEntity == null || !responseEntity.hasBody() || responseEntity.getBody() == null
                || responseEntity.getBody().getCartItems() == null) {
            return new GetCartRecommendationResponse(new ArrayList<>(),
                    false, "Could not retrieve Items");
        }

        allItems = responseEntity.getBody().getCartItems();

        if (allItems.size() < 3) {
            count = allItems.size();
        } else {
            count = 3;
        }

        System.out.println(count);
        for (int i = 0; i < count; i++) {
            randomInt = random.nextInt(allItems.size());
            randomItems.add(allItems.get(randomInt));
        }

        errorMessage = "Random recommendations returned because " + errorMessage;
        return new GetCartRecommendationResponse(randomItems, false, errorMessage);
    }

}
