package shopping;
import shopping.requests.*;
import shopping.responses.*;

public interface ShoppingService {

    GetCatalogueResponse getStockList();

    AssignOrderResponse assignOrder(AssignOrderRequest request);

    ScanItemResponse scanItem(ScanItemRequest request);

}