package cs.superleague.shopping;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreClosedException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.requests.*;
import cs.superleague.shopping.responses.*;

public interface ShoppingService {

    GetCatalogueResponse getCatalogue(GetCatalogueRequest request) throws InvalidRequestException, StoreDoesNotExistException;

    AddToQueueResponse addToQueue(AddToQueueRequest request) throws InvalidRequestException;

    GetNextQueuedResponse getNextQueued(GetNextQueuedRequest request) throws InvalidRequestException, StoreDoesNotExistException;

    GetStoreByUUIDResponse getStoreByUUID(GetStoreByUUIDRequest request) throws InvalidRequestException, StoreDoesNotExistException;

    GetStoreOpenResponse getStoreOpen(GetStoreOpenRequest request) throws InvalidRequestException, StoreDoesNotExistException, StoreClosedException, StoreClosedException;

    GetItemsResponse getItems(GetItemsRequest request) throws InvalidRequestException, StoreDoesNotExistException;
}