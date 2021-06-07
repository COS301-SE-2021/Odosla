package shopping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import payment.dataclass.Order;
import payment.dataclass.OrderStatus;
import payment.repos.OrderRepo;
import shopping.dataclass.Store;
import shopping.exceptions.InvalidRequestException;
import shopping.exceptions.StoreDoesNotExistException;
import shopping.repos.StoreRepo;
import shopping.requests.*;
import shopping.responses.*;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("shippingServiceImpl")
public class ShoppingServiceImpl implements ShoppingService {

    private final StoreRepo storeRepo;
    private final OrderRepo orderRepo;
    private final ShoppingServiceImpl shoppingService;

    @Autowired
    public ShoppingServiceImpl(StoreRepo storeRepo, OrderRepo orderRepo, ShoppingServiceImpl shoppingService) {
        this.storeRepo = storeRepo;
        this.orderRepo = orderRepo;
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

    /**
     *
     * @param request object is used to bring in:
     *                private order
     *
     * addToQueue should:
     *               1. Check that the request object is not null, if so then throw an InvalidRequestException
     *               2. Check if the appropriate order attributes from the request are not null, else throw an InvalidRequestException
     *               3. Add the order to the correct stores queue
     *               4. Return the response object.
     *
     * Request Object (AddToQueueRequest):
     * {
     *                "Order": {"orderID":"903420394i", "storeID":"d09832893", "items":....., ...}
     * }
     *
     * Response Object (AddToQueueResponse):
     * {
     *                "success":true
     *                "timestamp":"2021-01-05T11:50:55"
     *                "message":"Order successfuly created"
     * }
     *
     * @return
     * @throws InvalidRequestException
     * */

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
                invalidReq = true;
            } else if (order.getUserID() == null){
                invalidMessage = "Invalid request: missing user ID";
                invalidReq = true;
            } else if (order.getStoreID() == null){
                invalidMessage = "Invalid request: missing store ID";
                invalidReq = true;
            } else if (order.getTotalCost() == null){
                invalidMessage = "Invalid request: missing order cost";
                invalidReq = true;
            } else if (order.getStatus() != OrderStatus.PURCHASED){
                invalidMessage = "Invalid request: order has incompatible status";
                invalidReq = true;
            } else if (order.getItems() == null || order.getItems().isEmpty()){
                invalidMessage = "Invalid request: item list is empty or null";
                invalidReq = true;
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

    /**
     *
     * @param request object is used to bring in:
     *                private storeID
     *
     * getNextQueued should:
     *               1. Check that the request object is not null, if so then throw an InvalidRequestException
     *               2. Check if the appropriate order attributes from the request are not null, else throw an InvalidRequestException
     *               3. Check that the store exists, else throw a StoreDoesNotExistException
     *               4. Find the next order to be processed in the queue
     *               5. Return the response object.
     *
     * Request Object (GetNextQueuedRequest):
     * {
     *                "storeID": {"d09832893"}
     * }
     *
     * Response Object (GetNextQueuedResponse):
     * {
     *                "success":true
     *                "timestamp":"2021-01-05T11:50:55"
     *                "message":"Order was correctly added to queue"
     * }
     *
     * @return
     * @throws InvalidRequestException
     * @throws StoreDoesNotExistException
     * */

    @Override
    public GetNextQueuedResponse getNextQueued(GetNextQueuedRequest request) throws InvalidRequestException, StoreDoesNotExistException {
        GetNextQueuedResponse response=null;

        if(request!=null){

            if(request.getStoreID()==null){
                throw new InvalidRequestException("Store ID paramter in request can't be null - can't get next queued");
            }
            Store store;

            try {
                store = storeRepo.findById(request.getStoreID()).orElse(null);
            }
            catch(Exception e){
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not get next queued entity");
            }

            List<Order> orderqueue=store.getOrderQueue();

            if(orderqueue.size()==0){
                response=new GetNextQueuedResponse(Calendar.getInstance().getTime(),false,"The order queue of shop is empty",orderqueue,null);
                return response;
            }

            Date oldestProcssedDate=orderqueue.get(0).getProcessDate().getTime();
            Order correspondingOrder=orderqueue.get(0);

            for (Order o: orderqueue){
                if(oldestProcssedDate.after(o.getProcessDate().getTime())){
                    oldestProcssedDate=o.getProcessDate().getTime();
                    correspondingOrder=o;
                }
            }

            orderqueue.remove(correspondingOrder);
            store.setOrderQueue(orderqueue);

            oldestProcssedDate=orderqueue.get(0).getProcessDate().getTime();
            correspondingOrder=orderqueue.get(0);

            for (Order o: orderqueue){
                if(oldestProcssedDate.after(o.getProcessDate().getTime())){
                    oldestProcssedDate=o.getProcessDate().getTime();
                    correspondingOrder=o;
                }
            }

            response=new GetNextQueuedResponse(Calendar.getInstance().getTime(),true,"Queue was successfully updated for store", orderqueue,correspondingOrder);

        }
        else{
            throw new InvalidRequestException("Request object for GetNextQueuedRequest can't be null - can't get next queued");
        }
        return response;
    }

    /**
     *
     * @param request object is used to bring in:
     *                private storeID
     *
     * getStoreByUUID should:
     *               1. Check that the request object is not null, if so then throw an InvalidRequestException
     *               2. Check if the appropriate order attributes from the request are not null, else throw an InvalidRequestException
     *               3. Check that the store exists, else throw a StoreDoesNotExistException
     *               4. Return the response object
     *
     * Request Object (GetStoreByUUIDRequest):
     * {
     *                "storeID": {"d09832893"}
     * }
     *
     * Response Object (GetStoreByUUIDResponse):
     * {
     *                "storeEntity": {"storeID":"903420394i", "storeBrand":"Woolworths Food", "maxShoppers":....., ...}
     *                "timestamp":"2021-01-05T11:50:55"
     *                "message":"Store entity with corresponding id was returned"
     * }
     *
     * @return
     * @throws InvalidRequestException
     * @throws StoreDoesNotExistException
     * */

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

    @Override
    public ToggleStoreOpenResponse toggleStoreOpen(ToggleStoreOpenRequest request) throws InvalidRequestException, StoreDoesNotExistException {
        ToggleStoreOpenResponse response=null;

        if(request!=null){

            if (request.getStoreID()==null) {
                throw new InvalidRequestException("The Store ID in ToggleStoreOpenRequest parameter is null - Could not set store to open");
            }
            Store storeEntity=null;
            try {
                storeEntity = storeRepo.findById(request.getStoreID()).orElse(null);
            }
            catch (Exception e){
                throw new StoreDoesNotExistException("Store with ID does not exist in repository");
            }
            response=new ToggleStoreOpenResponse(Calendar.getInstance().getTime(), "Store is now set to open");

        }
        else{
            throw new InvalidRequestException("The ToggleStoreOpenRequest parameter is null - Could not set store to open");
        }
        return response;
    }
}
