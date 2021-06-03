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
import shopping.requests.AddToQueueRequest;
import shopping.requests.GetCatalogueRequest;
import shopping.requests.GetNextQueuedRequest;
import shopping.requests.GetStoreByUUIDRequest;
import shopping.responses.AddToQueueResponse;
import shopping.responses.GetCatalogueResponse;
import shopping.responses.GetNextQueuedResponse;
import shopping.responses.GetStoreByUUIDResponse;

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
                response=new GetNextQueuedResponse(Calendar.getInstance().getTime(),false,"The order queue of shop is empty");
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

            response=new GetNextQueuedResponse(Calendar.getInstance().getTime(),true,"Queue was successfully updated for store");

        }
        else{
            throw new InvalidRequestException("Request object for GetNextQueuedRequest can't be null - can't get next queued");
        }
        return response;
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
