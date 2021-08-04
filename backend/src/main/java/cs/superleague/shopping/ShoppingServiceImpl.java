package cs.superleague.shopping;

import cs.superleague.shopping.exceptions.StoreClosedException;
import cs.superleague.shopping.requests.*;
import cs.superleague.shopping.responses.*;
import cs.superleague.user.UserService;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.exceptions.UserDoesNotExistException;
import cs.superleague.user.exceptions.UserException;
import cs.superleague.user.requests.GetShopperByUUIDRequest;
import cs.superleague.user.responses.GetShopperByUUIDResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.StoreRepo;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service("shoppingServiceImpl")
public class ShoppingServiceImpl implements ShoppingService {

    private final StoreRepo storeRepo;
    private final OrderRepo orderRepo;
    private final UserService userService;

    @Autowired
    public ShoppingServiceImpl(StoreRepo storeRepo, OrderRepo orderRepo, UserService userService) {
        this.storeRepo = storeRepo;
        this.orderRepo = orderRepo;
        this.userService = userService;
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

            if(storeEntity==null)
            {
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not get Catalog entity");
            }
            response=new GetCatalogueResponse(request.getStoreID(),storeEntity.getStock(),Calendar.getInstance().getTime(), "Catalogue entity from store was correctly returned");

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
        GetStoreByUUIDRequest getStoreByUUIDRequest = new GetStoreByUUIDRequest(updatedOrder.getStoreID());
        GetStoreByUUIDResponse getStoreByUUIDResponse = null;
        try {
            getStoreByUUIDResponse = getStoreByUUID(getStoreByUUIDRequest);
        }catch(Exception e){
            throw new InvalidRequestException(e.getMessage());
        }

        getStoreByUUIDResponse.getStore().getCurrentOrders().add(updatedOrder);

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
                throw new InvalidRequestException("Store ID parameter in request can't be null - can't get next queued");
            }
            Store store;

            try {
                store = storeRepo.findById(request.getStoreID()).orElse(null);
            }
            catch(Exception e){
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not get next queued entity");
            }

            if(store == null){
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not get next queued entity");
            }

            List<Order> orderQueue= store.getOrderQueue();

            if(orderQueue.size()==0){
                response=new GetNextQueuedResponse(Calendar.getInstance().getTime(),false,"The order queue of shop is empty",orderQueue,null);
                return response;
            }

            Date oldestProcessedDate=orderQueue.get(0).getProcessDate().getTime();
            Order correspondingOrder=orderQueue.get(0);

            for (Order o: orderQueue){
                if(oldestProcessedDate.after(o.getProcessDate().getTime())){
                    oldestProcessedDate=o.getProcessDate().getTime();
                    correspondingOrder=o;
                }
            }

            orderQueue.remove(correspondingOrder);
            store.setOrderQueue(orderQueue);

            response=new GetNextQueuedResponse(Calendar.getInstance().getTime(),true,"Queue was successfully updated for store", orderQueue,correspondingOrder);

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
            }catch (NullPointerException e){
                //Catching nullPointerException from mockito unit test, when(storeRepo.findById(mockito.any())) return null - which will return null pointer exception

            }


            if(storeEntity==null) {
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
    public GetStoreOpenResponse getStoreOpen(GetStoreOpenRequest request) throws InvalidRequestException, StoreDoesNotExistException, StoreClosedException {
        GetStoreOpenResponse response=null;

        if(request!=null){

            if (request.getStoreID()==null) {
                throw new InvalidRequestException("The Store ID in GetStoreOpenRequest parameter is null - Could not set store to open");
            }
            Store storeEntity=null;
            try {
                storeEntity = storeRepo.findById(request.getStoreID()).orElse(null);
            }
            catch (Exception e){
                throw new StoreDoesNotExistException("Store with ID does not exist in repository");
            }

            if(storeEntity==null)
            {
                throw new StoreDoesNotExistException("Store with ID does not exist in repository");
            }

            Calendar calendar= Calendar.getInstance();
            if(calendar.get(Calendar.HOUR_OF_DAY) >= storeEntity.getOpeningTime() && calendar.get(Calendar.HOUR_OF_DAY) < storeEntity.getClosingTime())
            {
                storeEntity.setOpen(true);
                response=new GetStoreOpenResponse(request.getStoreID(),storeEntity.getOpen(),Calendar.getInstance().getTime(), "Store is now open for business");
            }
            else
            {
                storeEntity.setOpen(false);
                response=new GetStoreOpenResponse(request.getStoreID(),storeEntity.getOpen(),Calendar.getInstance().getTime(), "Store is closed for business");
            }
            response.setOpeningTime(storeEntity.getOpeningTime());
            response.setClosingTime(storeEntity.getClosingTime());

        }
        else{
            throw new InvalidRequestException("The GetStoreOpenRequest parameter is null - Could not set store to open");
        }
        return response;
    }

    /**
     *
     * @param request is used to bring in:
     *                private storeID
     *
     * getItems should:
     *               1. Check that the request object is not null, if so then throw an InvalidRequestException
     *               2. Check if the request's storeID is not null, else throw an InvalidRequestException
     *               3. Use the request's storeID to find the corresponding Store object in the repo. If
     *               it doesn't exist then throw a StoreDoesNotExistException.
     *               4. Initialize the response object's constructor to the store entity's getStock().getItems()
     *               5. Return the response object.
     *
     * Request Object (GetItemsRequest):
     * {
     *                "storeID":"7fa06899-98e5-43a0-b4d0-9dbc8e29f74a"
     * }
     *
     * Response Object (GetItemsResponse):
     * {
     *                "items":storeEntity.getStock().getItems()
     *                "timestamp":"2021-01-05T11:50:55"
     *                "message":"Store items have been retrieved"
     * }
     *
     * @return
     * @throws InvalidRequestException
     * @throws StoreDoesNotExistException
     * */

    @Override
    public GetItemsResponse getItems(GetItemsRequest request) throws InvalidRequestException, StoreDoesNotExistException {
        GetItemsResponse response=null;

        if(request!=null){

            if (request.getStoreID()==null) {
                throw new InvalidRequestException("The Store ID in GetItemsRequest parameter is null - Could not get items from store");
            }
            Store storeEntity=null;
            try {
                storeEntity = storeRepo.findById(request.getStoreID()).orElse(null);
            }
            catch (Exception e) {
                throw new StoreDoesNotExistException("Store with ID does not exist in repository");
            }
            if(storeEntity==null)
            {
                throw new StoreDoesNotExistException("Store with ID does not exist in repository");
            }

            response = new GetItemsResponse(request.getStoreID(), storeEntity.getStock().getItems(), Calendar.getInstance().getTime(), "Store items have been retrieved");

        }
        else{
            throw new InvalidRequestException("The GetItemsRequest parameter is null - Could not retrieve items");
        }
        return response;
    }

    /**
     *
     * @param request is used to bring in:
     *                private storeID
     *                private catalogue
     *
     * updateCatalogue should:
     *               1. Check that the request object is not null, if so then throw an InvalidRequestException
     *               2. Check if the request's storeID is not null, else throw an InvalidRequestException
     *               3. Check if the request's catalogue is not null, else throw an InvalidRequestException
     *               4. Use the request's storeID to find the corresponding Store object in the repo. If
     *               it doesn't exist then throw a StoreDoesNotExistException.
     *               5. Initialize the response object's constructor to the storeID and response message
     *               6. Return the response object.
     *
     * Request Object (UpdateCatalogueRequest):
     * {
     *                "storeID":"7fa06899-98e5-43a0-b4d0-9dbc8e29f74a"
     *                "catalogue":{
     *                          "item1":{
     *                                  "productID":"123456"
     *                                  ...
     *                          }
     *                          ...
     *                }
     * }
     *
     * Response Object (UpdateCatalogueResponse):
     * {
     *                "response":true
     *                "message":"Catalogue updated for the store"
     *                "storeID":"7fa06899-98e5-43a0-b4d0-9dbc8e29f74a"
     * }
     *
     * @return
     * @throws InvalidRequestException
     * @throws StoreDoesNotExistException
     * */
    @Override

    public UpdateCatalogueResponse updateCatalogue(UpdateCatalogueRequest request) throws InvalidRequestException, StoreDoesNotExistException {
        UpdateCatalogueResponse response = null;
        if (request != null) {
            if (request.getCatalogue() == null) {
                throw new InvalidRequestException("The Catalogue in UpdateCatalogueRequest parameter is null - Could not update catalogue for the shop");
            }
            Store storeEntity = null;
            try {
                storeEntity = storeRepo.findById(request.getCatalogue().getStoreID()).orElse(null);
            } catch (Exception e) {
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not update Catalog entity");
            }
            if(storeEntity==null){
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not update Catalog entity");
            }
            storeEntity.setStock(request.getCatalogue());
            storeRepo.save(storeEntity);
            response = new UpdateCatalogueResponse(true, "Catalogue updated for the store", storeEntity.getStoreID());
        } else {
            throw new InvalidRequestException("The request object for GetCatalogueRequest is null - Could not update catalogue for the shop");
        }
        return response;
    }

    /**
     *
     * @param request is used to bring in:
     *                private orderID
     *                private storeID
     *
     * removeQueuedOrder should:
     *               1. Check that the request object is not null, if so then throw an InvalidRequestException
     *               2. Check if the request's storeID or OrderID is not null, else throw an InvalidRequestException
     *               3. Use the request's storeID to find the corresponding Store object in the repo. If
     *               it doesn't exist then throw a StoreDoesNotExistException.
     *               4. Find the corresponding order in the Store object, if it is empty or not there return false
     *               6. Remove the corresponding item from the Store object.
     *               5. Return the response object with the corresponding orderID that has been removed.
     *
     * Request Object (RemoveQueuedOrderRequest):
     * {
     *                "orderID":"d30e7a98-c918-11eb-b8bc-0242ac130003"
     *                "storeID":"7fa06899-98e5-43a0-b4d0-9dbc8e29f74a"
     * }
     *
     * Response Object (RemoveQueuedOrderResponse):
     * {
     *                "isRemoved":true
     *                "message":"Order successfully removed from the queue"
     *                "orderID":"d30e7a98-c918-11eb-b8bc-0242ac130003"
     * }
     *
     * @return
     * @throws InvalidRequestException
     * @throws StoreDoesNotExistException
     * */


    public RemoveQueuedOrderResponse removeQueuedOrder(RemoveQueuedOrderRequest request) throws InvalidRequestException, StoreDoesNotExistException {
        if(request != null){
            if(request.getOrderID() == null) {
                throw new InvalidRequestException("Order ID parameter in request can't be null - can't remove from queue");
            }else if(request.getStoreID() == null){
                throw new InvalidRequestException("Store ID parameter in request can't be null - can't remove from queue");
            }else{
                Store store;
                try {
                    store = storeRepo.findById(request.getStoreID()).orElse(null);
                }
                catch(Exception e){
                    throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not get next queued entity");
                }
                if(store==null){
                    throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not get next queued entity");
                }
                List<Order> orderQueue=store.getOrderQueue();
                if(orderQueue.size()==0) {
                    return new RemoveQueuedOrderResponse(false, "The order queue of shop is empty", null);
                }
                Order correspondingOrder=null;

                for (Order o: orderQueue){
                    if(o.getOrderID().equals(request.getOrderID())){
                        correspondingOrder=o;
                    }
                }
                if(correspondingOrder == null){
                    return new RemoveQueuedOrderResponse(false, "Order not found in shop queue", null);
                }
                orderQueue.remove(correspondingOrder);
                store.setOrderQueue(orderQueue);
                storeRepo.save(store);
                return new RemoveQueuedOrderResponse(true, "Order successfully removed from the queue", correspondingOrder.getOrderID());
            }
        }else{
            throw new InvalidRequestException("Request object for RemoveQueuedOrderRequest can't be null - can't get next queued");
        }
    }

    /**
     *
     * @param request is used to bring in:
     *                StoreID - store ID where want to get shoppers from
     *  getShoppers should:
     *                1. Check request object is correct - else throw InvalidRequestException
     *                2. Take in the store ID from request object
     *                3. Get corresponding store from databse with ID
     *                4. If it store doesn't exist - throw StoreDoesNotException
     *                5. Get list of shoppers at current store
     *                6. If list of shoppers is null return response with success being false
     *                7. If list of shoppers is not null return response with list of shoppers and success status being true
     * Request object (GetShoppersRequest)
     * {
     *               "storeID": "7fa06899-98e5-43a0-b4d0-9dbc8e29f74a"
     *
     * }
     *
     * Response object (GetShoppersResponse)
     * {
     *                "listOfShoppers": storeEntity.getShoppers()
     *                "success": "true"
     *                "timeStamp":"2021-01-05T11:50:55"
     *                "message":"List of Shoppers successfully returned"
     * }
     * @return
     * @throws InvalidRequestException
     * @throws StoreDoesNotExistException
     */
    @Override
    public GetShoppersResponse getShoppers(GetShoppersRequest request) throws InvalidRequestException, StoreDoesNotExistException {
        GetShoppersResponse response=null;

        if(request!=null){

            if(request.getStoreID()==null){
                throw new InvalidRequestException("Store ID in request object can't be null");
            }

            Store storeEntity=null;

            try {
                storeEntity = storeRepo.findById(request.getStoreID()).orElse(null);
            }catch (Exception e){}

            if(storeEntity==null){
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not get Shoppers");
            }

            List<Shopper> listOfShoppers=null;
            listOfShoppers=storeEntity.getShoppers();


            if(listOfShoppers!=null) {
                response = new GetShoppersResponse(listOfShoppers, true, Calendar.getInstance().getTime(), "List of Shoppers successfully returned");
            }
            else{
                response = new GetShoppersResponse(null, false, Calendar.getInstance().getTime(), "List of Shoppers is null");
            }
        }
        else{
            throw new InvalidRequestException("Request object for get Shoppers can't be null");
        }
        return response;
    }

    /**
     *
     * @param request used to bring in:
     *                ShopperID - Id of shopper that should be addes to list of shoppers in Store
     *                StoreID - StoreID of which store to add the shopper to
     * addShopper should:
     *                1. Check is request object is correct else throw InvalidRequestException
     *                2. Take in store Id and get corresponding store entity
     *                3. If Store doesn't exist -throw StoreDoesNotExistException
     *                4. Check if listOfshoppers in Store is null - if true return response with sucess being false
     *                5. Get ShopperEntity with corresponding shopperID
     *                6. If shopper doesn't exist throw return response object stating shopper could not be retrieved from databse
     *                7. If shopper does exist then check not currently in list of shoppers
     *                8. If shopper already exists in list of shoppers, return response with success being false
     *                9. Else add shopper to shopper list and return response with success being true
     * Request Object
     * {
     *                "shopperID":"d30e7a98-c918-11eb-b8bc-0242ac130003"
     *                "storeID":"7fa06899-98e5-43a0-b4d0-9dbc8e29f74a"
     *
     * }
     * Response Object
     * {
     *                "success":"true"
     *                "timeStamp":"2021-01-05T11:50:55"
     *                "message":"Shopper was successfully added"
     *
     * }
     * @return
     * @throws InvalidRequestException
     * @throws StoreDoesNotExistException
     * @throws cs.superleague.user.exceptions.InvalidRequestException
     * @throws UserDoesNotExistException
     */

    @Override
    public AddShopperResponse addShopper(AddShopperRequest request) throws InvalidRequestException, StoreDoesNotExistException, UserException {
        AddShopperResponse response=null;

        if(request!=null){

            boolean invalidReq = false;
            String invalidMessage = "";

            if(request.getShopperID()==null){
                invalidReq=true;
                invalidMessage="Shopper ID in request object for add shopper is null";

            }
            else if(request.getStoreID()==null){
                invalidReq=true;
                invalidMessage="Store ID in request object for add shopper is null";
            }

            if (invalidReq) throw new InvalidRequestException(invalidMessage);

            Store storeEntity=null;

            try {
                storeEntity = storeRepo.findById(request.getStoreID()).orElse(null);
            }catch (Exception e){}

            if(storeEntity==null){
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not add Shopper");
            }


            List<Shopper> listOfShoppers=storeEntity.getShoppers();
            /* Get Shopper by UUID- get Shopper Object */
            /* Shopper shopper */
            if(listOfShoppers!=null){
                /* Get Shopper by UUID- get Shopper Object */
                /* Shopper shopper */
                GetShopperByUUIDRequest shoppersRequest=new GetShopperByUUIDRequest(request.getShopperID());
                GetShopperByUUIDResponse shopperResponse;
                try {
                    shopperResponse = userService.getShopperByUUIDRequest(shoppersRequest);
                }catch(Exception e){
                    throw e;
                }


                Boolean notPresent=true;
                for(Shopper shopper:listOfShoppers){
                    if(shopper.getShopperID().equals(request.getShopperID())){
                        response=new AddShopperResponse(false,Calendar.getInstance().getTime(), "Shopper already is already in listOfShoppers");
                        notPresent=false;
                    }
                }
                if(notPresent){
                    listOfShoppers.add(shopperResponse.getShopper());
                    storeEntity.setShoppers(listOfShoppers);
                    storeRepo.save(storeEntity);
                    response=new AddShopperResponse(true,Calendar.getInstance().getTime(), "Shopper was successfully added");
                }
            }
            else{
                response=new AddShopperResponse(false,Calendar.getInstance().getTime(), "list of Shoppers is null");
            }

        }
        else{
            throw new InvalidRequestException("Request object can't be null for addShopper");
        }

        return response;

    }

    /**
     *
     * @param request is used to bring in:
     *                ShopperID - Id of shopper that should be addes to list of shoppers in Store
     *                StoreID - StoreID of which store to add the shopper to
     * removeShopper should:
     *                1.Check is request object is correct else throw InvalidRequestException
     *                2.Take in store Id and get corresponding store entity
     *                3.If Store doesn't exist -throw StoreDoesNotExistException
     *                4.Check if listOfshoppers in Store is null - if true return response with sucess being false
     *                5.Get ShopperEntity with corresponding shopperID
     *                6.If shopper doesn't exist throw return response object stating shopper could not be retrieved from databse
     *                7.If shopper does exist then check not currently in list of shoppers
     *                8. If not in list return response with success being false and that the shopper doesn't exist in the list
     *                9. If Shopper is in list then remove shopper from list
     * Request Object (RemoveShopperRequest)
     * {
     *                "shopperID":"d30e7a98-c918-11eb-b8bc-0242ac130003"
     *                "storeID":"7fa06899-98e5-43a0-b4d0-9dbc8e29f74a"
     *
     * }
     * Response Object
     * {
     *                "success":"true"
     *                "timeStamp":"2021-01-05T11:50:55"
     *                "message":"Shopper was successfully removed"
     *
     * }
     * @return
     * @throws InvalidRequestException
     * @throws StoreDoesNotExistException
     * @throws cs.superleague.user.exceptions.InvalidRequestException
     * @throws UserDoesNotExistException
     */
    @Override
    public RemoveShopperResponse removeShopper(RemoveShopperRequest request) throws InvalidRequestException, StoreDoesNotExistException, UserException {
        RemoveShopperResponse response=null;

        if(request!=null){

            boolean invalidReq = false;
            String invalidMessage = "";

            if(request.getShopperID()==null){
                invalidReq=true;
                invalidMessage="Shopper ID in request object for remove shopper is null";

            }
            else if(request.getStoreID()==null){
                invalidReq=true;
                invalidMessage="Store ID in request object for remove shopper is null";
            }

            if (invalidReq) throw new InvalidRequestException(invalidMessage);

            Store storeEntity=null;

            try {
                storeEntity = storeRepo.findById(request.getStoreID()).orElse(null);
            }catch(Exception e){}

            if(storeEntity==null){
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not remove Shopper");
            }


            List<Shopper> listOfShoppers=storeEntity.getShoppers();

            if(listOfShoppers!=null){
                GetShopperByUUIDRequest shoppersRequest=new GetShopperByUUIDRequest(request.getShopperID());
                GetShopperByUUIDResponse shopperResponse=userService.getShopperByUUIDRequest(shoppersRequest);

                 Boolean inList=false;
                 for(Shopper shopper:listOfShoppers){
                     if(shopper.getShopperID().equals(request.getShopperID())){
                         listOfShoppers.remove(shopper);
                         inList=true;
                     }
                 }
                 if(inList==true) {
                     storeEntity.setShoppers(listOfShoppers);
                     storeRepo.save(storeEntity);
                     response = new RemoveShopperResponse(true, Calendar.getInstance().getTime(), "Shopper was successfully removed");
                 }
                 else{
                     response = new RemoveShopperResponse(false, Calendar.getInstance().getTime(), "Shopper isn't in list of shoppers in store entity");
                 }

            }
            else{
                response=new RemoveShopperResponse(false,Calendar.getInstance().getTime(), "list of Shoppers is null");
            }

        }
        else{
            throw new InvalidRequestException("Request object can't be null for removeShopper");
        }

        return response;
    }

    /**
     *
     * @param request used to bring in:
     *                storeID - storeId where the shopper list should be cleared
     * clearShopper should:
     *                1. Check is request object is correct else throw InvalidRequestException
     *                2. Take in store Id and get corresponding store entity
     *                3. If Store doesn't exist -throw StoreDoesNotExistException
     *                4. Gets listofshoppers - if null return response with false success and stating list is null
     *                5. If list isn't null - clear list and return response with success being true
     * Request Object (ClearShoppersRequest)
     * {
     *                "storeID":"7fa06899-98e5-43a0-b4d0-9dbc8e29f74a"
     * }
     * Response Object (ClearShoppersResponse)
     * {
     *                "success":"true"
     *                "timeStamp":"2021-01-05T11:50:55"
     *                "message":"List of Shopper successfuly cleared"
     *
     * }
     * @return
     * @throws InvalidRequestException
     * @throws StoreDoesNotExistException
     */
    @Override
    public ClearShoppersResponse clearShoppers(ClearShoppersRequest request) throws InvalidRequestException, StoreDoesNotExistException {
        ClearShoppersResponse response=null;
        if(request!=null){

            if(request.getStoreID()==null){
                throw new InvalidRequestException("Store ID in request object for clearShoppers can't be null");
            }

            Store storeEntity=null;
            try {
                storeEntity = storeRepo.findById(request.getStoreID()).orElse(null);
            }
            catch (Exception e){ }

            if(storeEntity==null){
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not clear shoppers");
            }


            List<Shopper> listOfShoppers=storeEntity.getShoppers();
            if(listOfShoppers!=null) {
                listOfShoppers.clear();

                storeEntity.setShoppers(listOfShoppers);
                storeRepo.save(storeEntity);
                response = new ClearShoppersResponse(true, Calendar.getInstance().getTime(), "List of Shopper successfuly cleared");
            }
            else{
                response=new ClearShoppersResponse(false,Calendar.getInstance().getTime(), "List of shoppers is null");
            }
        }
        else{
            throw new InvalidRequestException("Request object can't be null for clearShoppers");
        }

        return response;
    }

    /**
     *
     * @param request is used to bring in:
     *                private storeID
     *                private String storeBrand;
                      private int maxShoppers;
                      private int maxOrders;
                      private Boolean isOpen;
                      private int openingTime;
                      private int closingTime;
     *
     * updateStore should:
     *               1. Check that the request object is not null, if so then throw an InvalidRequestException
     *               2. Check if the request's store object is not null, else throw an InvalidRequestException
     *               3. Check if the request's storeID is not null, else throw an StoreDoesNotExistException
     *               4. Use the request's storeID to find the corresponding Store object in the repo. If
     *               it doesn't exist then throw a StoreDoesNotExistException.
     *               5. Update the found store object's fields with the request object store's data.
     *               6. Initialize the response object's constructor to the storeID and response message
     *               7. Return the response object.
     *
     * Request Object (UpdateStoreRequest):
     * {
     *                "storeID":"7fa06899-98e5-43a0-b4d0-9dbc8e29f74a"
     *                "store":{
     *                          "storeBrand":"PnP"
     *                          ...
     *                }
     * }
     *
     * Response Object (UpdateStoreResponse):
     * {
     *                "response":true
     *                "message":"Store updated successfully"
     *                "storeID":"7fa06899-98e5-43a0-b4d0-9dbc8e29f74a"
     * }
     *
     * @return
     * @throws InvalidRequestException
     * @throws StoreDoesNotExistException
     * */
    @Override
    public UpdateStoreResponse updateStore(UpdateStoreRequest request) throws InvalidRequestException, StoreDoesNotExistException {

        UpdateStoreResponse response = null;
        if (request != null) {
            if (request.getStore() == null) {
                throw new InvalidRequestException("The Store object in UpdateStoreRequest parameter is null - Could not update store");
            }
            Store storeEntity = null;
            try {
                storeEntity = storeRepo.findById(request.getStore().getStoreID()).orElse(null);
            } catch (Exception e) {
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not update Store entity");
            }
            if(storeEntity==null){
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not update Store entity");
            }

            if(request.getStore().getOpen()!=null){
                storeEntity.setOpen(request.getStore().getOpen());
            }
            if(request.getStore().getOpeningTime()!=-1){
                storeEntity.setOpeningTime(request.getStore().getOpeningTime());
            }
            if(request.getStore().getClosingTime()!=-1){
                storeEntity.setClosingTime(request.getStore().getClosingTime());
            }
            if(request.getStore().getStoreBrand()!=null){
                storeEntity.setStoreBrand(request.getStore().getStoreBrand());
            }
            if(request.getStore().getMaxShoppers()!=-1){
                storeEntity.setMaxShoppers(request.getStore().getMaxShoppers());
            }
            if(request.getStore().getMaxOrders()!=-1){
                storeEntity.setMaxOrders(request.getStore().getMaxOrders());
            }
            storeRepo.save(storeEntity);

            response = new UpdateStoreResponse(true, "Store updated successfully", storeEntity.getStoreID());
        }
        else {
            throw new InvalidRequestException("The request object for UpdateStoreRequest is null - Could not update store");
        }
        return response;
    }

    /**
     *
     * @param request is used to bring in:
     *                private storeID
     *                private List<Shopper> newShoppers;
     *
     * updateShoppers should:
     *               1. Check that the request object is not null, if so then throw an InvalidRequestException
     *               2. Check if the request's store object is not null, else throw an InvalidRequestException
     *               3. Check if the request's newShoppers object is not null, else throw an InvalidRequestException
     *               4. Check if the request's storeID is not null, else throw an StoreDoesNotExistException
     *               5. Use the request's storeID to find the corresponding Store object in the repo. If
     *               it doesn't exist then throw a StoreDoesNotExistException.
     *               6. Update the found store object's list of shoppers with the request object store's shopper list.
     *               7. Initialize the response object's constructor to the storeID and response message
     *               8. Return the response object.
     *
     * Request Object (UpdateShoppersRequest):
     * {
     *
     *                "store":{
     *                          "storeID":"7fa06899-98e5-43a0-b4d0-9dbc8e29f74a"
     *                          ...
     *                }
     *                "newShoppers":{
     *                          "shopper":{
     *                                  "id":"1fa96449-98e5-43a0-b4d0-9dbc8e29f74a"
     *                                  ...
     *                          }
     *                          "shopper":{
     *                                  "id":"2fa96449-98e5-43a0-b4d0-9dbc8e29f74a"
     *                                  ...
     *                          }
     *                }
     * }
     *
     * Response Object (UpdateShoppersResponse):
     * {
     *                "response":true
     *                "message":"Shoppers updated successfully"
     *                "storeID":"7fa06899-98e5-43a0-b4d0-9dbc8e29f74a"
     * }
     *
     * @return
     * @throws InvalidRequestException
     * @throws StoreDoesNotExistException
     * */
    @Override
    public UpdateShoppersResponse updateShoppers(UpdateShoppersRequest request) throws InvalidRequestException, StoreDoesNotExistException {

        UpdateShoppersResponse response = null;
        if (request != null) {
            if (request.getStore() == null) {
                throw new InvalidRequestException("The Store object in UpdateShoppersRequest parameter is null - Could not update shoppers");
            }
            if (request.getNewShoppers() == null) {
                throw new InvalidRequestException("The newShoppers object in UpdateShoppersRequest parameter is null - Could not update shoppers");
            }
            Store storeEntity = null;
            try {
                storeEntity = storeRepo.findById(request.getStore().getStoreID()).orElse(null);
            } catch (Exception e) {
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not update Shoppers entity");
            }
            if(storeEntity==null){
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not update Shoppers entity");
            }

            storeEntity.setShoppers(request.getNewShoppers());
            storeRepo.save(storeEntity);

            response = new UpdateShoppersResponse(true, "Shoppers updated successfully", storeEntity.getStoreID());
        }
        else {
            throw new InvalidRequestException("The request object for UpdateShoppersRequest is null - Could not update shoppers");
        }
        return response;
    }

    /**
     *
     * @param request is used to bring in:
     *
     *  getStores should:
     *                1. Check if there are any stores in the store database
     *                2. If there are stores, store them in a list
     *                3. If there aren't any stores in the database, return response with success being false
     *                4. If list of shoppers is not null return response with list of shoppers and success status being true
     *
     * Request object (GetStoresRequest)
     * {
     *
     *
     * }
     *
     * Response object (GetStoresResponse)
     * {
     *                "stores": storeEntity.findAll()
     *                "success": "true"
     *                "message":"List of Stores successfully returned"
     * }
     * @return
     */
    @Override
    public GetStoresResponse getStores(GetStoresRequest request) throws InvalidRequestException{
        GetStoresResponse response=null;

        if(request!=null){

            List<Store> storeEntity=null;

            try {
                storeEntity = storeRepo.findAll();

            }catch (Exception e){}

            if(storeRepo.count()!=0) {

                response = new GetStoresResponse(true, "List of Stores successfully returned", storeEntity);
            }
            else{
                response = new GetStoresResponse(false,"List of Stores is null", null);
            }
        }
        else{
            throw new InvalidRequestException("Request object for getStores can't be null");
        }
        return response;

    }

    /**
     *
     * @param request object is used to bring in:
     *                private storeID
     *
     * getQueue should:
     *               1. Check that the request object is not null, if so then throw an InvalidRequestException
     *               2. Check if the appropriate order attributes from the request are not null, else throw an InvalidRequestException
     *               3. Check that the store exists, else throw a StoreDoesNotExistException
     *               4. Find and store the order list from that store in the response object
     *               5. Return the response object.
     *
     * Request Object (GetQueueRequest):
     * {
     *                "storeID": {"d09832893"}
     * }
     *
     * Response Object (GetQueueResponse):
     * {
     *                "success":true
     *                "message":"Order was correctly added to queue"
     *                "queueOfOrders": store.getOrderQueue
     * }
     *
     * @return
     * @throws InvalidRequestException
     * @throws StoreDoesNotExistException
     * */

    @Override
    public GetQueueResponse getQueue(GetQueueRequest request) throws InvalidRequestException, StoreDoesNotExistException {
        GetQueueResponse response=null;

        if(request!=null){

            if(request.getStoreID()==null){
                throw new InvalidRequestException("Store ID parameter in request can't be null - can't get queue of orders");
            }
            Store store=null;

            try {
                store = storeRepo.findById(request.getStoreID()).orElse(null);
            }
            catch(Exception e){
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - can't get queue of orders");
            }

            if(store==null){
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - can't get queue of orders");
            }
            List<Order> orderQueue= store.getOrderQueue();

            if(orderQueue.size()!=0){
                response=new GetQueueResponse(true,"The order queue was successfully returned", orderQueue);
            }
            else
            {
                response=new GetQueueResponse(false,"The order queue of shop is empty",null);

            }
            return response;

        }
        else{
            throw new InvalidRequestException("Request object for GetQueueRequest can't be null - can't get queue");
        }
    }
}

