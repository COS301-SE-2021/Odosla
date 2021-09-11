package cs.superleague.recommendation;

import cs.superleague.recommendation.exceptions.InvalidRequestException;
import cs.superleague.recommendation.exceptions.RecommendationRepoException;
import cs.superleague.recommendation.requests.AddRecommendationRequest;
import cs.superleague.recommendation.requests.GetCartRecommendationRequest;
import cs.superleague.recommendation.requests.GetOrderRecommendationRequest;
import cs.superleague.recommendation.responses.AddRecommendationResponse;
import cs.superleague.recommendation.responses.GetCartRecommendationResponse;
import cs.superleague.recommendation.responses.GetOrderRecommendationResponse;

public interface RecommendationService {
    GetCartRecommendationResponse getCartRecommendation(GetCartRecommendationRequest request) throws InvalidRequestException, RecommendationRepoException;

    GetOrderRecommendationResponse getOrderRecommendation(GetOrderRecommendationRequest request);

    AddRecommendationResponse addRecommendation(AddRecommendationRequest request) throws InvalidRequestException;
}
