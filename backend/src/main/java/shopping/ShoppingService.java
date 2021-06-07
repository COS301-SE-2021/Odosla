package shopping;
import shopping.exceptions.InvalidRequestException;
import shopping.exceptions.StoreDoesNotExistException;
import shopping.requests.*;
import shopping.responses.*;

public interface ShoppingService {

    GetCatalogueResponse getCatalogue(GetCatalogueRequest request) throws InvalidRequestException, StoreDoesNotExistException;

    AddToQueueResponse addToQueue(AddToQueueRequest request) throws InvalidRequestException;

    GetNextQueuedResponse getNextQueued(GetNextQueuedRequest request) throws InvalidRequestException, StoreDoesNotExistException;

    GetStoreByUUIDResponse getStoreByUUID(GetStoreByUUIDRequest request) throws InvalidRequestException, StoreDoesNotExistException;

    ToggleStoreOpenResponse toggleStoreOpen(ToggleStoreOpenRequest request) throws InvalidRequestException, StoreDoesNotExistException;
}