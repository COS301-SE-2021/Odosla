package cs.superleague.shopping;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreClosedException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.requests.*;
import cs.superleague.shopping.responses.*;
import cs.superleague.user.exceptions.UserDoesNotExistException;


public interface ShoppingService {

    GetCatalogueResponse getCatalogue(GetCatalogueRequest request) throws InvalidRequestException, StoreDoesNotExistException;

    AddToQueueResponse addToQueue(AddToQueueRequest request) throws InvalidRequestException;

    GetNextQueuedResponse getNextQueued(GetNextQueuedRequest request) throws InvalidRequestException, StoreDoesNotExistException;

    GetStoreByUUIDResponse getStoreByUUID(GetStoreByUUIDRequest request) throws InvalidRequestException, StoreDoesNotExistException;

    GetStoreOpenResponse getStoreOpen(GetStoreOpenRequest request) throws InvalidRequestException, StoreDoesNotExistException, StoreClosedException, StoreClosedException;

    GetItemsResponse getItems(GetItemsRequest request) throws InvalidRequestException, StoreDoesNotExistException;

    UpdateCatalogueResponse updateCatalogue(UpdateCatalogueRequest request) throws InvalidRequestException, StoreDoesNotExistException;

    RemoveQueuedOrderResponse removeQueuedOrder(RemoveQueuedOrderRequest request) throws InvalidRequestException, StoreDoesNotExistException;

    GetShoppersResponse getShoppers(GetShoppersRequest request) throws InvalidRequestException, StoreDoesNotExistException;

    AddShopperResponse addShoper(AddShopperRequest request) throws InvalidRequestException, StoreDoesNotExistException, cs.superleague.user.exceptions.InvalidRequestException, UserDoesNotExistException;

    RemoveShopperResponse removeShopper(RemoveShopperRequest request) throws InvalidRequestException, StoreDoesNotExistException, cs.superleague.user.exceptions.InvalidRequestException, UserDoesNotExistException;

    ClearShoppersResponse clearShoppers(ClearShoppersRequest request) throws InvalidRequestException, StoreDoesNotExistException;
}