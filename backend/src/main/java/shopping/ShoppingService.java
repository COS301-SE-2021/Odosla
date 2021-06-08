package shopping;

import shopping.exceptions.*;
import shopping.requests.*;
import shopping.responses.*;
import shopping.requests.GetNextQueuedRequest;

public interface ShoppingService {

    GetCatalogueResponse getCatalogue(GetCatalogueRequest request) throws InvalidRequestException, StoreDoesNotExistException;

    AddToQueueResponse addToQueue(AddToQueueRequest request) throws InvalidRequestException;

    GetNextQueuedResponse getNextQueued(GetNextQueuedRequest request) throws InvalidRequestException, StoreDoesNotExistException;

    GetStoreByUUIDResponse getStoreByUUID(GetStoreByUUIDRequest request) throws InvalidRequestException, StoreDoesNotExistException;

    GetStoreOpenResponse getStoreOpen(GetStoreOpenRequest request) throws InvalidRequestException, StoreDoesNotExistException, StoreClosedException;
}