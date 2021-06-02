package shopping;
import shopping.exceptions.InvalidRequestException;
import shopping.requests.*;
import shopping.responses.*;

public interface ShoppingService {

    GetCatalogueResponse getCatalogue();

    AddToQueueResponse addToQueue(AddToQueueRequest request) throws InvalidRequestException;

    ScanItemResponse scanItem(ScanItemRequest request);

}