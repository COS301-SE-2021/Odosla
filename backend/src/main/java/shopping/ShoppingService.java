package shopping;
import shopping.requests.*;
import shopping.responses.*;

public interface ShoppingService {

    GetStockListResponse getStockList();

    AssignOrderResponse assignOrder(AssignOrderRequest request);

    GetNextQueuedResponse getNextQueued(GetNextQueuedRequest request);

}