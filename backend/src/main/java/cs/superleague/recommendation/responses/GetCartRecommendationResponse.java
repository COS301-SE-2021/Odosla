package cs.superleague.recommendation.responses;

import cs.superleague.shopping.dataclass.Item;

import java.util.List;

public class GetCartRecommendationResponse {
    private List<Item> recommendations;
    private boolean isSuccess;
    private String message;
}
