package cs.superleague.recommendation;

import cs.superleague.recommendation.dataclass.Recommendation;
import cs.superleague.recommendation.exceptions.InvalidRequestException;
import cs.superleague.recommendation.exceptions.RecommendationRepoException;
import cs.superleague.recommendation.repos.RecommendationRepo;
import cs.superleague.recommendation.requests.AddRecommendationRequest;
import cs.superleague.recommendation.requests.GetCartRecommendationRequest;
import cs.superleague.recommendation.requests.GetOrderRecommendationRequest;
import cs.superleague.recommendation.requests.RemoveRecommendationRequest;
import cs.superleague.recommendation.responses.GetCartRecommendationResponse;
import cs.superleague.recommendation.responses.GetOrderRecommendationResponse;
import cs.superleague.recommendation.stubs.payment.dataclass.Order;
import cs.superleague.recommendation.stubs.shopping.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("recommendationServiceImpl")
public class RecommendationServiceImpl implements RecommendationService{

    private final RecommendationRepo recommendationRepo;

    @Autowired
    public RecommendationServiceImpl(RecommendationRepo recommendationRepo) {
        this.recommendationRepo = recommendationRepo;
    }


    @Override
    public GetCartRecommendationResponse getCartRecommendation(GetCartRecommendationRequest request) throws InvalidRequestException, RecommendationRepoException {
        if (request == null){
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getItemIDs() == null){
            throw new InvalidRequestException("Null item list.");
        }
        if (request.getItemIDs().size() == 0){
            throw new InvalidRequestException("No items in item list.");
        }
        if (recommendationRepo != null){
            List<UUID> orderIDs = new ArrayList<>();
            List<Integer> frequencyOfOrders = new ArrayList<>();
            for (String productID : request.getItemIDs()){
                List<Recommendation> recommendation = recommendationRepo.findRecommendationByProductID(productID);
                if (recommendation != null){
                    for (Recommendation orderID : recommendation){
                        if (!orderIDs.contains(orderID.getOrderID())){
                            orderIDs.add(orderID.getOrderID());
                            frequencyOfOrders.add(1);
                        }else{
                            frequencyOfOrders.set(orderIDs.indexOf(orderID.getOrderID()), frequencyOfOrders.get(orderIDs.indexOf(orderID.getOrderID())) + 1);
                        }
                    }
                }
            }
            if (orderIDs.size() == 0){
                GetCartRecommendationResponse response = new GetCartRecommendationResponse(null, false, "None of these items have been bought before.");
                return response;
            }
            List<Order> finalRecommendation = new ArrayList<>();
            for (Integer frequency : frequencyOfOrders){
                if (frequency >= request.getItemIDs().size()){
//                    Order order = orderRepo.findById(orderIDs.get(frequencyOfOrders.indexOf(frequency))).orElse(null);
                    Order order = null;
                    if (order != null){
                        finalRecommendation.add(order);
                    }
                }
            }
            List<Item> finalItemsRecommendation = new ArrayList<>();
            for (Order orders : finalRecommendation){
                for (Item item : orders.getItems()){
                    if (request.getItemIDs().contains(item.getProductID())){
                        continue;
                    }
                    if (finalItemsRecommendation.contains(item)){
                        continue;
                    }
                    finalItemsRecommendation.add(item);
                    if (finalItemsRecommendation.size() == 3){
                        break;
                    }
                }
            }
            if (finalItemsRecommendation.size() == 0){
                GetCartRecommendationResponse response = new GetCartRecommendationResponse(null, false, "There are no orders that have all the requested items in them.");
                return response;
            }
            GetCartRecommendationResponse response = new GetCartRecommendationResponse(finalItemsRecommendation, true, "The following items are recommended to go with the cart.");
            return response;
        }else{
            throw new RecommendationRepoException("No recommendation repository found.");
        }
    }

    @Override
    public GetOrderRecommendationResponse getOrderRecommendation(GetOrderRecommendationRequest request) {
        return null;
    }

    @Override
    public void addRecommendation(AddRecommendationRequest request) throws InvalidRequestException {
        if (request == null){
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getOrderID() == null || request.getProductID() == null){
            throw new InvalidRequestException("Null parameters.");
        }
        for (String productID : request.getProductID()){
            UUID recommendationID = UUID.randomUUID();
            while (recommendationRepo.findRecommendationByRecommendationID(recommendationID) != null){
                recommendationID = UUID.randomUUID();
            }
            Recommendation recommendation = new Recommendation(recommendationID, productID, request.getOrderID());
            recommendationRepo.save(recommendation);
        }
    }

    @Override
    public void removeRecommendation(RemoveRecommendationRequest request) throws InvalidRequestException {
        if (request == null){
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getOrderID() == null){
            throw new InvalidRequestException("Null parameters.");
        }
        List<Recommendation> recommendations = recommendationRepo.findRecommendationByOrderID(request.getOrderID());
        for (Recommendation recommendation : recommendations){
            recommendationRepo.delete(recommendation);
        }
    }

}
