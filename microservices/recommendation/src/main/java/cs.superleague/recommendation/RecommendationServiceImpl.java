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
import cs.superleague.shopping.exceptions.ItemDoesNotExistException;
import cs.superleague.recommendation.requests.*;
import cs.superleague.recommendation.responses.GenerateRecommendationTableResponse;
import cs.superleague.shopping.responses.GetAllItemsResponse;
import cs.superleague.recommendation.responses.GetCartRecommendationResponse;
import cs.superleague.recommendation.responses.GetOrderRecommendationResponse;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.responses.GetItemsByIDResponse;
import cs.superleague.shopping.responses.GetItemsResponse;
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
    @Value("${shoppingHost}")
    private String shoppingHost;
    @Value("${shoppingPort}")
    private String shoppingPort;

    private final RecommendationRepo recommendationRepo;
    private final RestTemplate restTemplate;

    @Autowired
    public RecommendationServiceImpl(RecommendationRepo recommendationRepo, RestTemplate restTemplate) {
        this.recommendationRepo = recommendationRepo;
        this.restTemplate = restTemplate;
    }


    @Override
    public GetCartRecommendationResponse getCartRecommendation(GetCartRecommendationRequest request) throws InvalidRequestException, RecommendationRepoException, URISyntaxException, ItemDoesNotExistException {
        if (request == null) {
            throw new InvalidRequestException("No request object specified, need storeID to make recommendations.");
        }
        if (request.getStoreID() == null){
            throw new InvalidRequestException("Need StoreID to make recommendations.");
        }
        if (request.getItemIDs() == null) {
            return getRandomRecommendations("Null item list.", request.getStoreID(), new ArrayList<>());
        }
        if (request.getItemIDs().size() == 0) {
            return getRandomRecommendations("No items in item list.", request.getStoreID(), new ArrayList<>());
        }
        if (recommendationRepo != null) {
            List<UUID> orderIDs = new ArrayList<>();
            List<Integer> frequencyOfOrders = new ArrayList<>();
            for (String productID : request.getItemIDs()) {
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
                return getRandomRecommendations("None of these items have been bought before.", request.getStoreID(), request.getItemIDs());
            }
            List<Order> finalRecommendation = new ArrayList<>();
            List<String> productsToRecommend = new ArrayList<>();
            List<Integer> frequencyOfItems = new ArrayList<>();
            for (Integer frequency : frequencyOfOrders) {
                if (frequency >= request.getItemIDs().size() / 2) {
//
//                    Map<String, Object> parts = new HashMap<>();
//                    parts.put("orderID", orderIDs.get(frequencyOfOrders.indexOf(frequency)));
//                    String stringUri = "http://" + paymentHost + ":" + paymentPort + "/payment/getOrderByUUID";
//                    URI uri = new URI(stringUri);
//                    ResponseEntity<GetOrderByUUIDResponse> responseEntity = restTemplate.postForEntity(
//                            uri, parts, GetOrderByUUIDResponse.class);
//
//                    Order order = null;
//                    if (responseEntity.getBody() != null) {
//                        order = responseEntity.getBody().getOrder();
//                    }
//
//                    if (responseEntity == null || !responseEntity.hasBody() ||
//                            responseEntity.getBody() == null || responseEntity.getBody().getOrder() == null) {
//                        return getRandomRecommendations("Could not Retrieve Orders", request.getStoreID(), request.getItemIDs());
//                    }
//
//                    if (order != null) {
//                        finalRecommendation.add(order);
//                    }
                    List<Recommendation> recommendationList = recommendationRepo.findRecommendationsByOrderID(orderIDs.get(frequencyOfOrders.indexOf(frequency)));
                    for (Recommendation recommendation : recommendationList){
                        if (productsToRecommend.contains(recommendation.getProductID())){
                            frequencyOfItems.set(productsToRecommend.indexOf(recommendation.getProductID()), (frequencyOfItems.get(productsToRecommend.indexOf(recommendation.getProductID())) + 1));
                        } else{
                            frequencyOfItems.add(1);
                            productsToRecommend.add(recommendation.getProductID());
                        }
                    }
                }
            }
            List<CartItem> finalItemsRecommendation = new ArrayList<>();
//            for (Order orders : finalRecommendation) {
//                for (CartItem item : orders.getCartItems()) {
//                    if (request.getItemIDs().contains(item.getProductID())) {
//                        continue;
//                    }
//                    if (finalItemsRecommendation.contains(item)) {
//                        continue;
//                    }
//                    if (item.getStoreID().compareTo(request.getStoreID()) != 0){
//                        continue;
//                    }
//                    finalItemsRecommendation.add(item);
//                    if (finalItemsRecommendation.size() == 3) {
//                        break;
//                    }
//                }
//            }
            int highestFrequency = Collections.max(frequencyOfItems);
            List<String> bestRecommendations = new ArrayList<>();
            for (Integer frequency : frequencyOfItems){
                if (frequency == null){
                    continue;
                }
                if (frequency == highestFrequency){
                    bestRecommendations.add(productsToRecommend.get(frequencyOfItems.indexOf(frequency)));
                    frequencyOfItems.set(frequencyOfItems.indexOf(frequency), null);
                }
            }
            Map<String, Object> parts = new HashMap<>();
            parts.put("itemIDs", bestRecommendations);
            String stringUri = "http://" + shoppingHost + ":" + shoppingPort + "/shopping/getItemsByID";
            URI uri = new URI(stringUri);
            ResponseEntity<GetItemsByIDResponse> itemResponse = restTemplate.postForEntity(uri, parts, GetItemsByIDResponse.class);
            if (itemResponse == null || itemResponse.getBody() == null){
                throw new ItemDoesNotExistException("Item not found in database.");
            }
            List<Item> finalItems = itemResponse.getBody().getItems();
            for (Item item : finalItems){
                if (item.getStoreID().compareTo(request.getStoreID()) == 0){
                    CartItem cartItem = new CartItem(item.getName(), item.getProductID(), item.getBarcode(), null, item.getPrice(), 1, item.getDescription(), item.getImageUrl(), item.getBrand(), item.getSize(), item.getItemType(), item.getPrice(), item.getStoreID());
                    finalItemsRecommendation.add(cartItem);
                }
            }
            if (finalItemsRecommendation.size() == 0) {
                return getRandomRecommendations("There are no orders that have all the requested items in them.", request.getStoreID(), request.getItemIDs());
            }
            while (finalItemsRecommendation.size() < 3){
                GetCartRecommendationResponse getCartRecommendationResponse = getRandomRecommendations("", request.getStoreID(), request.getItemIDs());
                for (CartItem item : getCartRecommendationResponse.getRecommendations()){
                    if (finalItemsRecommendation.size() == 3){
                        break;
                    }
                    if (finalItemsRecommendation.contains(item)){
                        continue;
                    }
                    finalItemsRecommendation.add(item);
                }
            }
            GetCartRecommendationResponse response = new GetCartRecommendationResponse(finalItemsRecommendation, true, "The following items are recommended to go with the cart.");
            return response;
        } else {
            return getRandomRecommendations("No recommendation repository found.", request.getStoreID(), request.getItemIDs());
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
        List<Recommendation> recommendations = recommendationRepo.findRecommendationsByOrderID(request.getOrderID());
        for (Recommendation recommendation : recommendations) {
            recommendationRepo.delete(recommendation);
        }
    }

    // Helper/s

    private GetCartRecommendationResponse getRandomRecommendations(String errorMessage, UUID storeID, List<String> productIDs) throws URISyntaxException {

        int count = 0;
        int randomInt = 0;
        List<CartItem> allItems;
        List<CartItem> randomItems = new ArrayList<>();
        Random random = new Random();
        String stringUri = "http://" + shoppingHost + ":" + shoppingPort + "/shopping/getItems";
        URI uri = new URI(stringUri);
        Map<String, Object> parts = new HashMap<>();
        parts.put("storeID", storeID.toString());

        ResponseEntity<GetItemsResponse> responseEntity = restTemplate.postForEntity(
                uri,
                parts, GetItemsResponse.class);

        if (responseEntity == null || !responseEntity.hasBody() || responseEntity.getBody() == null
                || responseEntity.getBody().getItems() == null) {
            return new GetCartRecommendationResponse(new ArrayList<>(),
                    false, "Could not retrieve Items");
        }

        allItems = new ArrayList<>();
        for (Item item : responseEntity.getBody().getItems()){
            CartItem cartItem = new CartItem(item.getName(), item.getProductID(), item.getBarcode(), null, item.getPrice(), 1, item.getDescription(), item.getImageUrl(), item.getBrand(), item.getSize(), item.getItemType(), item.getPrice(), item.getStoreID());
            allItems.add(cartItem);
        }
        if (allItems.size() < 3) {
            count = allItems.size();
        } else {
            count = 3;
        }

        System.out.println(count);
        for (int i = 0; i < count; i++) {
            randomInt = random.nextInt(allItems.size());
            if (randomItems.contains(allItems.get(randomInt)) || productIDs.contains((allItems.get(randomInt).getProductID())) || !allItems.get(randomInt).getStoreID().equals(storeID)){
                i--;
                continue;
            }
            randomItems.add(allItems.get(randomInt));
        }

        errorMessage = "Random recommendations returned because " + errorMessage;
        return new GetCartRecommendationResponse(randomItems, false, errorMessage);
    }

    @Override
    public GenerateRecommendationTableResponse generateRecommendationTable(GenerateRecommendationTableRequest request) throws InvalidRequestException, URISyntaxException {
        if (request == null){
            throw new InvalidRequestException("Null request object.");
        }
        else{
            List<Recommendation> recommendations = recommendationRepo.findAll();
            List<UUID> orderIDs = new ArrayList<>();
            List<List<String>> recommendationTable = new ArrayList<List<String>>();
            int sizeOfTable = 0;
            int index=0;

            if(recommendations!=null){

                for(int k = 0; k < recommendations.size(); k++)
                {
                    if(k == 0){
                        sizeOfTable++;
                        orderIDs.add(recommendations.get(k).getOrderID());
                    }
                    else{
                        boolean found = false;
                        for (UUID orderID : orderIDs) {
                            if (orderID.equals(recommendations.get(k).getOrderID())) {
                                found = true;
                                break;
                            }
                        }

                        if(!found){
                            sizeOfTable++;
                            orderIDs.add(recommendations.get(k).getOrderID());
                        }
                    }
                }

                for(int i = 0; i < sizeOfTable; i++)  {
                    recommendationTable.add(new ArrayList<String>());
                }

                for(int k = 0; k < sizeOfTable; k++){
                    List<Recommendation> orderIDRecommendations = recommendationRepo.findRecommendationsByOrderID(orderIDs.get(k));
                    List<String> productIDs = new ArrayList<>();

                    for (Recommendation orderIDRecommendation : orderIDRecommendations) {
                        productIDs.add(orderIDRecommendation.getProductID());
                    }

                    Map<String, Object> parts = new HashMap<>();
                    parts.put("itemIDs", productIDs);
                    String stringUri = "http://" + shoppingHost + ":" + shoppingPort + "/shopping/getItemsByID";
                    URI uri = new URI(stringUri);
                    ResponseEntity<GetItemsByIDResponse> responseEntity = restTemplate.postForEntity(
                            uri, parts, GetItemsByIDResponse.class);

                    List<Item> items = null;
                    if (responseEntity.getBody() != null) {
                        items = responseEntity.getBody().getItems();
                    }

                    if (items != null){
                        for (Item item : items) {
                            recommendationTable.get(k).add(item.getName());
                        }
                    }

                }

                return new GenerateRecommendationTableResponse(true, "Table created successfully", recommendationTable);

            }else{
                throw new InvalidRequestException("No recommendations in the table");
            }

        }
    }

}
