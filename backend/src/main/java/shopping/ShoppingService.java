package shopping;
import shopping.requests.*;
import shopping.responses.*;

public interface ShoppingService {

    GetCatalogueResponse getCatalogue(GetCatalogueRequest request);

    AssignOrderResponse assignOrder(AssignOrderRequest request);

    ScanItemResponse scanItem(ScanItemRequest request);

}