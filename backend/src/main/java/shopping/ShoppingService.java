package shopping;
import shopping.requests.*;
import shopping.responses.*;

public interface ShoppingService {

    GetCatalogueResponse getCatalogue();

    AssignOrderResponse assignOrder(AssignOrderRequest request);

    ScanItemResponse scanItem(ScanItemRequest request);

}