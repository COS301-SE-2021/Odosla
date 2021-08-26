package cs.superleague.recommendation;

import cs.superleague.recommendation.requests.GetCartRecommendationRequest;
import cs.superleague.recommendation.requests.GetOrderRecommendationRequest;
import cs.superleague.recommendation.responses.GetCartRecommendationResponse;
import cs.superleague.recommendation.responses.GetOrderRecommendationResponse;

public interface RecommendationService {
    GetCartRecommendationResponse getCartRecommendation(GetCartRecommendationRequest request);

    GetOrderRecommendationResponse getOrderRecommendation(GetOrderRecommendationRequest request);
}
