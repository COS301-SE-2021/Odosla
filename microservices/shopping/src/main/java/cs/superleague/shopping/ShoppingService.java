package cs.superleague.shopping;

import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.ItemDoesNotExistException;
import cs.superleague.shopping.exceptions.StoreClosedException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.requests.*;
import cs.superleague.shopping.responses.*;
import cs.superleague.user.exceptions.UserException;

import java.net.URISyntaxException;


public interface ShoppingService {

    GetCatalogueResponse getCatalogue(GetCatalogueRequest request) throws InvalidRequestException, StoreDoesNotExistException;

    AddToQueueResponse addToQueue(AddToQueueRequest request) throws InvalidRequestException;

    GetNextQueuedResponse getNextQueued(GetNextQueuedRequest request) throws InvalidRequestException, StoreDoesNotExistException, URISyntaxException;

    GetStoreByUUIDResponse getStoreByUUID(GetStoreByUUIDRequest request) throws InvalidRequestException, StoreDoesNotExistException;

    GetStoreOpenResponse getStoreOpen(GetStoreOpenRequest request) throws InvalidRequestException, StoreDoesNotExistException, StoreClosedException;

    GetItemsResponse getItems(GetItemsRequest request) throws InvalidRequestException, StoreDoesNotExistException;

    UpdateCatalogueResponse updateCatalogue(UpdateCatalogueRequest request) throws InvalidRequestException, StoreDoesNotExistException;

    RemoveQueuedOrderResponse removeQueuedOrder(RemoveQueuedOrderRequest request) throws InvalidRequestException, StoreDoesNotExistException;

    GetShoppersResponse getShoppers(GetShoppersRequest request) throws InvalidRequestException, StoreDoesNotExistException;

    AddShopperResponse addShopper(AddShopperRequest request) throws StoreDoesNotExistException, UserException, URISyntaxException;

    RemoveShopperResponse removeShopper(RemoveShopperRequest request) throws InvalidRequestException, StoreDoesNotExistException, UserException, URISyntaxException;

    ClearShoppersResponse clearShoppers(ClearShoppersRequest request) throws InvalidRequestException, StoreDoesNotExistException;

    UpdateStoreResponse updateStore(UpdateStoreRequest request) throws InvalidRequestException, StoreDoesNotExistException;

    UpdateShoppersResponse updateShoppers(UpdateShoppersRequest request) throws InvalidRequestException, StoreDoesNotExistException;

    GetStoresResponse getStores(GetStoresRequest request) throws InvalidRequestException;

    GetQueueResponse getQueue(GetQueueRequest request) throws InvalidRequestException, StoreDoesNotExistException;

    SaveStoreToRepoResponse saveStoreToRepo(SaveStoreToRepoRequest request) throws InvalidRequestException, StoreDoesNotExistException;

    GetAllItemsResponse getAllItems(GetAllItemsRequest request) throws InvalidRequestException;

    SaveItemToRepoResponse saveItemToRepo(SaveItemToRepoRequest request) throws InvalidRequestException;

    GetItemsByIDResponse getItemsByID(GetItemsByIDRequest request) throws InvalidRequestException;

    void addToFrontOfQueue(AddToFrontOfQueueRequest request) throws InvalidRequestException;

    SaveCatalogueToRepoResponse saveCatalogueToRepo(SaveCatalogueToRepoRequest request) throws InvalidRequestException;

    GetProductByBarcodeResponse getProductByBarcode(GetProductByBarcodeRequest request) throws InvalidRequestException, ItemDoesNotExistException;

    PriceCheckResponse priceCheck(PriceCheckRequest request) throws InvalidRequestException;

    PriceCheckAllAvailableStoresResponse priceCheckAllAvailableStores(PriceCheckAllAvailableStoresRequest request) throws InvalidRequestException;

    GetCloseEnoughStoresResponse getCloseEnoughStores(GetCloseEnoughStoresRequest request) throws InvalidRequestException, StoreDoesNotExistException;

    ItemIsInStockResponse itemIsInStock(ItemIsInStockRequest request) throws InvalidRequestException;

    CalculateOverallDistanceResponse calculateOverallDistance(CalculateOverallDistanceRequest request) throws InvalidRequestException;

    PriceCheckWithDeliveryResponse priceCheckWithDelivery(PriceCheckWithDeliveryRequest request) throws InvalidRequestException, StoreDoesNotExistException;
}