package cs.superleague.recommendation;

import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.recommendation.dataclass.Recommendation;
import cs.superleague.recommendation.exceptions.InvalidRequestException;
import cs.superleague.recommendation.exceptions.RecommendationRepoException;
import cs.superleague.recommendation.repos.RecommendationRepo;
import cs.superleague.recommendation.requests.GetCartRecommendationRequest;
import cs.superleague.recommendation.requests.GetOrderRecommendationRequest;
import cs.superleague.recommendation.responses.GetCartRecommendationResponse;
import cs.superleague.recommendation.responses.GetOrderRecommendationResponse;
import cs.superleague.shopping.dataclass.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Service("recommendationServiceImpl")
public class RecommendationServiceImpl implements RecommendationService{

    private final RecommendationRepo recommendationRepo;
    private final OrderRepo orderRepo;
    private Calendar lastUpdate;

    @Autowired
    public RecommendationServiceImpl(RecommendationRepo recommendationRepo, OrderRepo orderRepo) {
        this.recommendationRepo = recommendationRepo;
        this.orderRepo = orderRepo;
    }


    @Override
    public GetCartRecommendationResponse getCartRecommendation(GetCartRecommendationRequest request) throws InvalidRequestException, RecommendationRepoException {
        if (request == null){
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getItemIDs() == null){
            throw new InvalidRequestException("Null item list.");
        }
        if (recommendationRepo != null){
            populateRecommendations();
            List<UUID> orderIDs = new ArrayList<>();
            List<Integer> frequencyOfOrders = new ArrayList<>();
            for (String productID : request.getItemIDs()){
                List<Recommendation> recommendation = recommendationRepo.findRecommendationByProductyID(productID);
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
                    Order order = orderRepo.findById(orderIDs.get(frequencyOfOrders.indexOf(frequency))).orElse(null);
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
                }
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

    //Helper functions
    public void populateRecommendations(){
        List<Order> orders;
        if (lastUpdate == null){
            //Recommendation recommendation = recommendationRepo.findTopByOrdersOrderByRecommendationAddedDateDesc();
            Recommendation recommendation = null;
            if (recommendation != null){
                lastUpdate = recommendation.getRecommendationAddedDate();
                orders = orderRepo.findAll();
                //orders = orderRepo.findAllByCreateDateAfter(lastUpdate);
                lastUpdate = Calendar.getInstance();
            }else{
                orders = orderRepo.findAll();
                lastUpdate = Calendar.getInstance();
            }
        }else {
            orders = orderRepo.findAll();
            //orders = orderRepo.findAllByCreateDateAfter(lastUpdate);
            lastUpdate = Calendar.getInstance();
        }
        for (Order o : orders){
            for (Item i : o.getItems()){
                Recommendation recommendation = new Recommendation();
                recommendation.setRecommendationAddedDate(Calendar.getInstance());
                recommendation.setOrderID(o.getOrderID());
                recommendation.setProductID(i.getProductID());
                UUID recommendationID = UUID.randomUUID();
                while(recommendationRepo.findRecommendationByRecommendationID(recommendationID) != null){
                    recommendationID = UUID.randomUUID();
                }
                recommendation.setRecommendationID(recommendationID);
                recommendationRepo.save(recommendation);
            }
        }
    }
}
