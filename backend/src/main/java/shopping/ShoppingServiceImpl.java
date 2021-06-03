package shopping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import payment.dataclass.Order;
import payment.dataclass.OrderStatus;
import shopping.dataclass.Store;
import shopping.exceptions.InvalidRequestException;
import shopping.exceptions.StoreDoesNotExistException;
import shopping.repos.StoreRepo;
import shopping.requests.AddToQueueRequest;
import shopping.requests.GetCatalogueRequest;
import shopping.requests.GetStoreByUUIDRequest;
import shopping.requests.ScanItemRequest;
import shopping.responses.AddToQueueResponse;
import shopping.responses.GetCatalogueResponse;
import shopping.responses.GetStoreByUUIDResponse;
import shopping.responses.ScanItemResponse;

import java.util.Calendar;

@Service("shippingServiceImpl")
public class ShoppingServiceImpl implements ShoppingService {

    private final StoreRepo storeRepo;
    private final ShoppingServiceImpl shoppingService;

    @Autowired
    public ShoppingServiceImpl(StoreRepo storeRepo, ShoppingServiceImpl shoppingService) {
        this.storeRepo = storeRepo;
        this.shoppingService = shoppingService;
    }

    /**
     *
     * @param request is used to bring in:
     *                private storeID
     *
     * getCatalogue should:
     *               1. Check that the request object is not null, if so then throw an InvalidRequestException
     *               2. Check if the request's storeID is not null, else throw an InvalidRequestException
     *               3. Use the request's storeID to find the corresponding Store object in the repo. If
     *               it doesn't exist then throw a StoreDoesNotExistException.
     *               4. Use the found Store object's getters to initialize the response object's constructor.
     *               5. Return the response object.
     *
     * Request Object (GetCatalogueRequest):
     * {
     *                "storeID":"7fa06899-98e5-43a0-b4d0-9dbc8e29f74a"
     * }
     *
     * Response Object (GetCatalogueResponse):
     * {
     *                "catalogue":storeEntity.getStock()
     *                "timestamp":"2021-01-05T11:50:55"
     *                "message":"Catalogue entity from store was correctly returned"
     * }
     *
     * @return
     * @throws InvalidRequestException
     * @throws StoreDoesNotExistException
     * */
    @Override
    public GetCatalogueResponse getCatalogue(GetCatalogueRequest request) throws InvalidRequestException, StoreDoesNotExistException {
        GetCatalogueResponse response=null;

        if(request!=null){

            if (request.getStoreID()==null) {
                throw new InvalidRequestException("The Store ID in GetCatalogueRequest parameter is null - Could not get catalogue from shop");
            }
            Store storeEntity=null;
            try {
                storeEntity = storeRepo.findById(request.getStoreID()).orElse(null);
            }
            catch (Exception e){
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not get Catalog entity");
            }
            response=new GetCatalogueResponse(storeEntity.getStock(),Calendar.getInstance().getTime(), "Catalogue entity from store was correctly returned");

        }
        else{
            throw new InvalidRequestException("The request object for GetCatalaogueRequest is null - Could not get catalogue from shop");
        }
        return response;
    }

    @Override
    public AddToQueueResponse addToQueue(AddToQueueRequest request) throws InvalidRequestException {

        AddToQueueResponse response = null;

        boolean invalidReq = false;
        String invalidMessage = "";

        if (request == null || request.getOrder() == null){
            invalidReq = true;
            invalidMessage = "Invalid request: null value received";
        } else {
            Order order = request.getOrder();
            if (order.getOrderID() == null){
                invalidMessage = "Invalid request: Missing order ID";
            } else if (order.getUserID() == null){
                invalidMessage = "Invalid request: missing user ID";
            } else if (order.getStoreID() == null){
                invalidMessage = "Invalid request: missing store ID";
            } else if (order.getTotalCost() == null){
                invalidMessage = "Invalid request: missing order cost";
            } else if (order.getStatus() != OrderStatus.PURCHASED){
                invalidMessage = "Invalid request: order has incompatible status";
            } else if (order.getItems() == null || order.getItems().isEmpty()){
                invalidMessage = "Invalid request: item list is empty or null";
            }
        }

        if (invalidReq) throw new InvalidRequestException(invalidMessage);

        Order updatedOrder = request.getOrder();

        // // Update the order status and create time // //
        updatedOrder.setStatus(OrderStatus.IN_QUEUE);
        updatedOrder.setProcessDate(Calendar.getInstance());

        // <paymentService>.updateOrder(updatedOrder);


        // // Add order to respective store order queue in db // //
        // ...

        response = new AddToQueueResponse(true, "Order successfuly created", Calendar.getInstance().getTime());

        return response;
    }

    @Override
    public ScanItemResponse scanItem(ScanItemRequest request) {
        return null;
    }

    @Override
    public GetStoreByUUIDResponse getStoreByUUID(GetStoreByUUIDRequest request) throws InvalidRequestException, StoreDoesNotExistException {
        GetStoreByUUIDResponse response = null;
        if(request != null){

            if(request.getStoreID()==null){
                throw new InvalidRequestException("StoreID is null in GetStoreByUUIDRequest request - could not return store entity");
            }

            Store storeEntity=null;
            try {
               storeEntity = storeRepo.findById(request.getStoreID()).orElse(null);
            }
            catch (Exception e){
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not get Store entity");
            }
            response=new GetStoreByUUIDResponse(storeEntity,Calendar.getInstance().getTime(),"Store entity with corresponding id was returned");
        }
        else{
            throw new InvalidRequestException("GetStoreByUUID request is null - could not return store entity");
        }
        return response;
    }
}
