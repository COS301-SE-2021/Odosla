package shopping;

import shopping.requests.AssignOrderRequest;
import shopping.requests.GetNextQueuedRequest;
import shopping.responses.AssignOrderResponse;
import shopping.responses.GetNextQueuedResponse;
import shopping.responses.GetStockListResponse;

import java.util.UUID;

public class ShoppingServiceImpl implements ShoppingService {

    @Override
    public GetStockListResponse getStockList() {
        return null;
    }

    @Override
    public AssignOrderResponse assignOrder(AssignOrderRequest request) {
        return null;
    }

    @Override
    public GetNextQueuedResponse getNextQueued(GetNextQueuedRequest request) {
        return null;
    }
}
