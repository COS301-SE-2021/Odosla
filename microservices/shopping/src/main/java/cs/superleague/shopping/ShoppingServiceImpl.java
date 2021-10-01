package cs.superleague.shopping;

import cs.superleague.integration.security.CurrentUser;
import cs.superleague.payment.dataclass.CartItem;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.requests.SaveOrderToRepoRequest;
import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.ItemDoesNotExistException;
import cs.superleague.shopping.exceptions.StoreClosedException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.CatalogueRepo;
import cs.superleague.shopping.repos.ItemRepo;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.shopping.requests.*;
import cs.superleague.shopping.responses.*;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.exceptions.UserException;
import cs.superleague.user.requests.SaveShopperToRepoRequest;
import cs.superleague.user.responses.GetShopperByEmailResponse;
import cs.superleague.user.responses.GetShopperByUUIDResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Service("shoppingServiceImpl")
public class ShoppingServiceImpl implements ShoppingService {
    @Value("${paymentHost}")
    private String paymentHost;
    @Value("${paymentPort}")
    private String paymentPort;
    @Value("${userHost}")
    private String userHost;
    @Value("${userPort}")
    private String userPort;

    private final StoreRepo storeRepo;
    private final ItemRepo itemRepo;
    private final CatalogueRepo catalogueRepo;

    @Autowired
    private RabbitTemplate rabbit;

    private final RestTemplate restTemplate;


    @Autowired
    public ShoppingServiceImpl(StoreRepo storeRepo, ItemRepo itemRepo, RestTemplate restTemplate, CatalogueRepo catalogueRepo) {
        this.storeRepo = storeRepo;
        this.itemRepo = itemRepo;
        this.restTemplate = restTemplate;
        this.catalogueRepo = catalogueRepo;
    }

    /**
     * @param request is used to bring in:
     *                private storeID
     *                <p>
     *                getCatalogue should:
     *                1. Check that the request object is not null, if so then throw an InvalidRequestException
     *                2. Check if the request's storeID is not null, else throw an InvalidRequestException
     *                3. Use the request's storeID to find the corresponding Store object in the repo. If
     *                it doesn't exist then throw a StoreDoesNotExistException.
     *                4. Use the found Store object's getters to initialize the response object's constructor.
     *                5. Return the response object.
     *                <p>
     *                Request Object (GetCatalogueRequest):
     *                {
     *                "storeID":"7fa06899-98e5-43a0-b4d0-9dbc8e29f74a"
     *                }
     *                <p>
     *                Response Object (GetCatalogueResponse):
     *                {
     *                "catalogue":storeEntity.getStock()
     *                "timestamp":"2021-01-05T11:50:55"
     *                "message":"Catalogue entity from store was correctly returned"
     *                }
     * @return
     * @throws InvalidRequestException
     * @throws StoreDoesNotExistException
     */
    @Override
    public GetCatalogueResponse getCatalogue(GetCatalogueRequest request) throws InvalidRequestException, StoreDoesNotExistException {
        GetCatalogueResponse response = null;

        if (request != null) {

            if (request.getStoreID() == null) {
                throw new InvalidRequestException("The Store ID in GetCatalogueRequest parameter is null - Could not get catalogue from shop");
            }
            Store storeEntity = null;
            try {
                storeEntity = storeRepo.findById(request.getStoreID()).orElse(null);
            } catch (Exception e) {
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not get Catalog entity");
            }

            if (storeEntity == null) {
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not get Catalog entity");
            }
            response = new GetCatalogueResponse(request.getStoreID(), storeEntity.getStock(), Calendar.getInstance().getTime(), "Catalogue entity from store was correctly returned");

        } else {
            throw new InvalidRequestException("The request object for GetCatalogueRequest is null - Could not get catalogue from shop");
        }
        return response;
    }

    /**
     * @param request object is used to bring in:
     *                private order
     *                <p>
     *                addToQueue should:
     *                1. Check that the request object is not null, if so then throw an InvalidRequestException
     *                2. Check if the appropriate order attributes from the request are not null, else throw an InvalidRequestException
     *                3. Add the order to the correct stores queue
     *                4. Return the response object.
     *                <p>
     *                Request Object (AddToQueueRequest):
     *                {
     *                "Order": {"orderID":"903420394i", "storeID":"d09832893", "items":....., ...}
     *                }
     *                <p>
     *                Response Object (AddToQueueResponse):
     *                {
     *                "success":true
     *                "timestamp":"2021-01-05T11:50:55"
     *                "message":"Order successfuly created"
     *                }
     * @return
     * @throws InvalidRequestException
     */

    @Override
    public AddToQueueResponse addToQueue(AddToQueueRequest request) throws InvalidRequestException {

        AddToQueueResponse response = null;

        boolean invalidReq = false;
        String invalidMessage = "";

        if (request == null || request.getOrder() == null) {
            invalidReq = true;
            invalidMessage = "Invalid request: null value received";
        } else {
            Order order = request.getOrder();
            if (order.getOrderID() == null) {
                invalidMessage = "Invalid request: Missing order ID";
                invalidReq = true;
            } else if (order.getUserID() == null) {
                invalidMessage = "Invalid request: missing user ID";
                invalidReq = true;
            } else if (order.getStoreID() == null) {
                invalidMessage = "Invalid request: missing store ID";
                invalidReq = true;
            } else if (order.getTotalCost() == null) {
                invalidMessage = "Invalid request: missing order cost";
                invalidReq = true;
            } else if (order.getCartItems() == null || order.getCartItems().isEmpty()) {
                invalidMessage = "Invalid request: item list is empty or null";
                invalidReq = true;
            }
        }

        if (invalidReq) throw new InvalidRequestException(invalidMessage);

        Order updatedOrder = request.getOrder();

        updatedOrder.setStatus(OrderStatus.IN_QUEUE);
        updatedOrder.setProcessDate(new Date());

        SaveOrderToRepoRequest saveOrderToRepoRequest = new SaveOrderToRepoRequest(updatedOrder);
        rabbit.convertAndSend("PaymentEXCHANGE", "RK_SaveOrderToRepo", saveOrderToRepoRequest);

        Store store = null;
        try {
            store = storeRepo.findById(request.getOrder().getStoreID()).orElse(null);
        } catch (Exception e) {
            throw new InvalidRequestException(e.getMessage());
        }

        if (store.getOrderQueue() == null) {
            store.setOrderQueue(new ArrayList<>());
        }
        store.getOrderQueue().add(updatedOrder);

        if (storeRepo != null)
            storeRepo.save(store);

        response = new AddToQueueResponse(true, "Order successfully created", Calendar.getInstance().getTime());

        return response;
    }

    /**
     * @param request object is used to bring in:
     *                private storeID
     *                <p>
     *                getNextQueued should:
     *                1. Check that the request object is not null, if so then throw an InvalidRequestException
     *                2. Check if the appropriate order attributes from the request are not null, else throw an InvalidRequestException
     *                3. Check that the store exists, else throw a StoreDoesNotExistException
     *                4. Find the next order to be processed in the queue
     *                5. Return the response object.
     *                <p>
     *                Request Object (GetNextQueuedRequest):
     *                {
     *                "storeID": {"d09832893"}
     *                }
     *                <p>
     *                Response Object (GetNextQueuedResponse):
     *                {
     *                "success":true
     *                "timestamp":"2021-01-05T11:50:55"
     *                "message":"Order was correctly added to queue"
     *                }
     * @return
     * @throws InvalidRequestException
     * @throws StoreDoesNotExistException
     */

    @Override
    public GetNextQueuedResponse getNextQueued(GetNextQueuedRequest request) throws InvalidRequestException, StoreDoesNotExistException, URISyntaxException {
        GetNextQueuedResponse response = null;

        if (request != null) {

            if (request.getStoreID() == null) {
                throw new InvalidRequestException("Store ID parameter in request can't be null - can't get next queued");
            }

            Store store;

            try {
                store = storeRepo.findById(request.getStoreID()).orElse(null);
            } catch (Exception e) {
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not get next queued entity");
            }

            if (store == null) {
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not get next queued entity");
            }

            List<Order> orderQueue = store.getOrderQueue();

            if (orderQueue == null || orderQueue.isEmpty()) {
                response = new GetNextQueuedResponse(Calendar.getInstance().getTime(), false, "The order queue of shop is empty", orderQueue, null);
                return response;
            }

            Date oldestProcessedDate = orderQueue.get(0).getProcessDate();
            Order correspondingOrder = orderQueue.get(0);

            for (Order o : orderQueue) {
                if (oldestProcessedDate.after(o.getProcessDate())) {
                    oldestProcessedDate = o.getProcessDate();
                    correspondingOrder = o;
                }
            }

            Order updateOrder = correspondingOrder;

            if (updateOrder != null) {
                CurrentUser currentUser = new CurrentUser();
                Map<String, Object> parts = new HashMap<String, Object>();
                parts.put("email", currentUser.getEmail());

                String stringUri = "http://" + userHost + ":" + userPort + "/user/getShopperByEmail";
                URI uri = new URI(stringUri);

                ResponseEntity<GetShopperByEmailResponse> getShopperByEmailResponseEntity =
                        restTemplate.postForEntity(uri, parts, GetShopperByEmailResponse.class);

                GetShopperByEmailResponse getShopperByEmailResponse = getShopperByEmailResponseEntity.getBody();
                Shopper shopper = getShopperByEmailResponse.getShopper();
                assert shopper != null;

                updateOrder.setShopperID(shopper.getShopperID());
                updateOrder.setStatus(OrderStatus.PACKING);

                SaveOrderToRepoRequest saveOrderToRepoRequest = new SaveOrderToRepoRequest(updateOrder);
                rabbit.convertAndSend("PaymentEXCHANGE", "RK_SaveOrderToRepo", saveOrderToRepoRequest);

            }

            if (storeRepo != null) {
                store.getOrderQueue().remove(correspondingOrder);
                storeRepo.save(store);
            }

            response = new GetNextQueuedResponse(Calendar.getInstance().getTime(), true, "Queue was successfully updated for store", orderQueue, correspondingOrder);

        } else {
            throw new InvalidRequestException("Request object for GetNextQueuedRequest can't be null - can't get next queued");
        }
        return response;

    }

    /**
     * @param request object is used to bring in:
     *                private storeID
     *                <p>
     *                getStoreByUUID should:
     *                1. Check that the request object is not null, if so then throw an InvalidRequestException
     *                2. Check if the appropriate order attributes from the request are not null, else throw an InvalidRequestException
     *                3. Check that the store exists, else throw a StoreDoesNotExistException
     *                4. Return the response object
     *                <p>
     *                Request Object (GetStoreByUUIDRequest):
     *                {
     *                "storeID": {"d09832893"}
     *                }
     *                <p>
     *                Response Object (GetStoreByUUIDResponse):
     *                {
     *                "storeEntity": {"storeID":"903420394i", "storeBrand":"Woolworths Food", "maxShoppers":....., ...}
     *                "timestamp":"2021-01-05T11:50:55"
     *                "message":"Store entity with corresponding id was returned"
     *                }
     * @return
     * @throws InvalidRequestException
     * @throws StoreDoesNotExistException
     */

    @Override
    public GetStoreByUUIDResponse getStoreByUUID(GetStoreByUUIDRequest request) throws InvalidRequestException, StoreDoesNotExistException {
        GetStoreByUUIDResponse response = null;
        if (request != null) {

            if (request.getStoreID() == null) {
                throw new InvalidRequestException("StoreID is null in GetStoreByUUIDRequest request - could not return store entity");
            }

            Store storeEntity = null;
            try {
                storeEntity = storeRepo.findById(request.getStoreID()).orElse(null);
            } catch (NullPointerException e) {
                //Catching nullPointerException from mockito unit test, when(storeRepo.findById(mockito.any())) return null - which will return null pointer exception
            }

            if (storeEntity == null) {
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not get Store entity");
            }
            response = new GetStoreByUUIDResponse(storeEntity, Calendar.getInstance().getTime(), "Store entity with corresponding id was returned");
        } else {
            throw new InvalidRequestException("GetStoreByUUID request is null - could not return store entity");
        }
        return response;
    }

    @Override
    public GetStoreOpenResponse getStoreOpen(GetStoreOpenRequest request) throws InvalidRequestException, StoreDoesNotExistException, StoreClosedException {
        GetStoreOpenResponse response = null;

        if (request != null) {

            if (request.getStoreID() == null) {
                throw new InvalidRequestException("The Store ID in GetStoreOpenRequest parameter is null - Could not set store to open");
            }
            Store storeEntity = null;
            try {
                storeEntity = storeRepo.findById(request.getStoreID()).orElse(null);
            } catch (Exception e) {
                throw new StoreDoesNotExistException("Store with ID does not exist in repository");
            }

            if (storeEntity == null) {
                throw new StoreDoesNotExistException("Store with ID does not exist in repository");
            }

            Calendar calendar = Calendar.getInstance();
            if (calendar.get(Calendar.HOUR_OF_DAY) >= storeEntity.getOpeningTime() && calendar.get(Calendar.HOUR_OF_DAY) < storeEntity.getClosingTime()) {
                storeEntity.setOpen(true);
                storeRepo.save(storeEntity);
                response = new GetStoreOpenResponse(request.getStoreID(), storeEntity.getOpen(), Calendar.getInstance().getTime(), "Store is now open for business");
            } else {
                storeEntity.setOpen(false);
                storeRepo.save(storeEntity);
                response = new GetStoreOpenResponse(request.getStoreID(), storeEntity.getOpen(), Calendar.getInstance().getTime(), "Store is closed for business");
            }
            response.setOpeningTime(storeEntity.getOpeningTime());
            response.setClosingTime(storeEntity.getClosingTime());

        } else {
            throw new InvalidRequestException("The GetStoreOpenRequest parameter is null - Could not set store to open");
        }
        return response;
    }

    /**
     * @param request is used to bring in:
     *                private storeID
     *                <p>
     *                getItems should:
     *                1. Check that the request object is not null, if so then throw an InvalidRequestException
     *                2. Check if the request's storeID is not null, else throw an InvalidRequestException
     *                3. Use the request's storeID to find the corresponding Store object in the repo. If
     *                it doesn't exist then throw a StoreDoesNotExistException.
     *                4. Initialize the response object's constructor to the store entity's getStock().getItems()
     *                5. Return the response object.
     *                <p>
     *                Request Object (GetItemsRequest):
     *                {
     *                "storeID":"7fa06899-98e5-43a0-b4d0-9dbc8e29f74a"
     *                }
     *                <p>
     *                Response Object (GetItemsResponse):
     *                {
     *                "items":storeEntity.getStock().getItems()
     *                "timestamp":"2021-01-05T11:50:55"
     *                "message":"Store items have been retrieved"
     *                }
     * @return
     * @throws InvalidRequestException
     * @throws StoreDoesNotExistException
     */

    @Override
    public GetItemsResponse getItems(GetItemsRequest request) throws InvalidRequestException, StoreDoesNotExistException {
        GetItemsResponse response = null;

        if (request != null) {

            if (request.getStoreID() == null) {
                throw new InvalidRequestException("The Store ID in GetItemsRequest parameter is null - Could not get items from store");
            }
            Store storeEntity = null;
            try {
                storeEntity = storeRepo.findById(request.getStoreID()).orElse(null);
            } catch (Exception e) {
                throw new StoreDoesNotExistException("Store with ID does not exist in repository");
            }
            if (storeEntity == null) {
                throw new StoreDoesNotExistException("Store with ID does not exist in repository");
            }

            response = new GetItemsResponse(request.getStoreID(), storeEntity.getStock().getItems(), Calendar.getInstance().getTime(), "Store items have been retrieved");

        } else {
            throw new InvalidRequestException("The GetItemsRequest parameter is null - Could not retrieve items");
        }
        return response;
    }

    /**
     * @param request is used to bring in:
     *                private storeID
     *                private catalogue
     *                <p>
     *                updateCatalogue should:
     *                1. Check that the request object is not null, if so then throw an InvalidRequestException
     *                2. Check if the request's storeID is not null, else throw an InvalidRequestException
     *                3. Check if the request's catalogue is not null, else throw an InvalidRequestException
     *                4. Use the request's storeID to find the corresponding Store object in the repo. If
     *                it doesn't exist then throw a StoreDoesNotExistException.
     *                5. Initialize the response object's constructor to the storeID and response message
     *                6. Return the response object.
     *                <p>
     *                Request Object (UpdateCatalogueRequest):
     *                {
     *                "storeID":"7fa06899-98e5-43a0-b4d0-9dbc8e29f74a"
     *                "catalogue":{
     *                "item1":{
     *                "productID":"123456"
     *                ...
     *                }
     *                ...
     *                }
     *                }
     *                <p>
     *                Response Object (UpdateCatalogueResponse):
     *                {
     *                "response":true
     *                "message":"Catalogue updated for the store"
     *                "storeID":"7fa06899-98e5-43a0-b4d0-9dbc8e29f74a"
     *                }
     * @return
     * @throws InvalidRequestException
     * @throws StoreDoesNotExistException
     */
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
            if (storeEntity == null) {
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
     * @param request is used to bring in:
     *                private orderID
     *                private storeID
     *                <p>
     *                removeQueuedOrder should:
     *                1. Check that the request object is not null, if so then throw an InvalidRequestException
     *                2. Check if the request's storeID or OrderID is not null, else throw an InvalidRequestException
     *                3. Use the request's storeID to find the corresponding Store object in the repo. If
     *                it doesn't exist then throw a StoreDoesNotExistException.
     *                4. Find the corresponding order in the Store object, if it is empty or not there return false
     *                6. Remove the corresponding item from the Store object.
     *                5. Return the response object with the corresponding orderID that has been removed.
     *                <p>
     *                Request Object (RemoveQueuedOrderRequest):
     *                {
     *                "orderID":"d30e7a98-c918-11eb-b8bc-0242ac130003"
     *                "storeID":"7fa06899-98e5-43a0-b4d0-9dbc8e29f74a"
     *                }
     *                <p>
     *                Response Object (RemoveQueuedOrderResponse):
     *                {
     *                "isRemoved":true
     *                "message":"Order successfully removed from the queue"
     *                "orderID":"d30e7a98-c918-11eb-b8bc-0242ac130003"
     *                }
     * @return
     * @throws InvalidRequestException
     * @throws StoreDoesNotExistException
     */


    public RemoveQueuedOrderResponse removeQueuedOrder(RemoveQueuedOrderRequest request) throws InvalidRequestException, StoreDoesNotExistException {
        if (request != null) {
            if (request.getOrderID() == null) {
                throw new InvalidRequestException("Order ID parameter in request can't be null - can't remove from queue");
            } else if (request.getStoreID() == null) {
                throw new InvalidRequestException("Store ID parameter in request can't be null - can't remove from queue");
            } else {
                Store store;
                try {
                    store = storeRepo.findById(request.getStoreID()).orElse(null);
                } catch (Exception e) {
                    throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not get next queued entity");
                }
                if (store == null) {
                    throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not get next queued entity");
                }
                List<Order> orderQueue = store.getOrderQueue();
                if (orderQueue.size() == 0) {
                    return new RemoveQueuedOrderResponse(false, "The order queue of shop is empty", null);
                }
                Order correspondingOrder = null;

                for (Order o : orderQueue) {
                    if (o.getOrderID().equals(request.getOrderID())) {
                        correspondingOrder = o;
                    }
                }
                if (correspondingOrder == null) {
                    return new RemoveQueuedOrderResponse(false, "Order not found in shop queue", null);
                }
                orderQueue.remove(correspondingOrder);
                store.setOrderQueue(orderQueue);
                storeRepo.save(store);
                return new RemoveQueuedOrderResponse(true, "Order successfully removed from the queue", correspondingOrder.getOrderID());
            }
        } else {
            throw new InvalidRequestException("Request object for RemoveQueuedOrderRequest can't be null - can't get next queued");
        }
    }

    /**
     * @param request is used to bring in:
     *                StoreID - store ID where want to get shoppers from
     *                getShoppers should:
     *                1. Check request object is correct - else throw InvalidRequestException
     *                2. Take in the store ID from request object
     *                3. Get corresponding store from databse with ID
     *                4. If it store doesn't exist - throw StoreDoesNotException
     *                5. Get list of shoppers at current store
     *                6. If list of shoppers is null return response with success being false
     *                7. If list of shoppers is not null return response with list of shoppers and success status being true
     *                Request object (GetShoppersRequest)
     *                {
     *                "storeID": "7fa06899-98e5-43a0-b4d0-9dbc8e29f74a"
     *                <p>
     *                }
     *                <p>
     *                Response object (GetShoppersResponse)
     *                {
     *                "listOfShoppers": storeEntity.getShoppers()
     *                "success": "true"
     *                "timeStamp":"2021-01-05T11:50:55"
     *                "message":"List of Shoppers successfully returned"
     *                }
     * @return
     * @throws InvalidRequestException
     * @throws StoreDoesNotExistException
     */
    @Override
    public GetShoppersResponse getShoppers(GetShoppersRequest request) throws InvalidRequestException, StoreDoesNotExistException {
        GetShoppersResponse response = null;

        if (request != null) {

            if (request.getStoreID() == null) {
                throw new InvalidRequestException("Store ID in request object can't be null");
            }

            Store storeEntity = null;

            try {
                storeEntity = storeRepo.findById(request.getStoreID()).orElse(null);
            } catch (Exception e) {
            }

            if (storeEntity == null) {
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not get Shoppers");
            }

            List<Shopper> listOfShoppers = null;
            listOfShoppers = storeEntity.getShoppers();


            if (listOfShoppers != null) {
                response = new GetShoppersResponse(listOfShoppers, true, Calendar.getInstance().getTime(), "List of Shoppers successfully returned");
            } else {
                response = new GetShoppersResponse(null, false, Calendar.getInstance().getTime(), "List of Shoppers is null");
            }
        } else {
            throw new InvalidRequestException("Request object for get Shoppers can't be null");
        }
        return response;
    }

    /**
     * @param request used to bring in:
     *                ShopperID - Id of shopper that should be added to list of shoppers in Store
     *                StoreID - StoreID of which store to add the shopper to
     *                addShopper should:
     *                1. Check is request object is correct else throw InvalidRequestException
     *                2. Take in store Id and get corresponding store entity
     *                3. If Store doesn't exist -throw StoreDoesNotExistException
     *                4. Check if listOfshoppers in Store is null - if true return response with sucess being false
     *                5. Get ShopperEntity with corresponding shopperID
     *                6. If shopper doesn't exist throw return response object stating shopper could not be retrieved from databse
     *                7. If shopper does exist then check not currently in list of shoppers
     *                8. If shopper already exists in list of shoppers, return response with success being false
     *                9. Else add shopper to shopper list and return response with success being true
     *                Request Object
     *                {
     *                "shopperID":"d30e7a98-c918-11eb-b8bc-0242ac130003"
     *                "storeID":"7fa06899-98e5-43a0-b4d0-9dbc8e29f74a"
     *                <p>
     *                }
     *                Response Object
     *                {
     *                "success":"true"
     *                "timeStamp":"2021-01-05T11:50:55"
     *                "message":"Shopper was successfully added"
     *                <p>
     *                }
     * @return
     * @throws InvalidRequestException
     * @throws StoreDoesNotExistException
     * @throws cs.superleague.user.exceptions.InvalidRequestException
     */

    @Override
    public AddShopperResponse addShopper(AddShopperRequest request) throws StoreDoesNotExistException, UserException, URISyntaxException {
        AddShopperResponse response = null;

        if (request != null) {

            boolean invalidReq = false;
            String invalidMessage = "";

            if (request.getShopperID() == null) {
                invalidReq = true;
                invalidMessage = "Shopper ID in request object for add shopper is null";

            } else if (request.getStoreID() == null) {
                invalidReq = true;
                invalidMessage = "Store ID in request object for add shopper is null";
            }

            if (invalidReq) throw new cs.superleague.user.exceptions.InvalidRequestException(invalidMessage);

            Store storeEntity = null;

            try {
                storeEntity = storeRepo.findById(request.getStoreID()).orElse(null);
            } catch (Exception e) {
            }

            if (storeEntity == null) {
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not add Shopper");
            }

            List<Shopper> listOfShoppers = storeEntity.getShoppers();

            Map<String, Object> parts = new HashMap<String, Object>();
            parts.put("userID", request.getShopperID().toString());
            String stringUri = "http://" + userHost + ":" + userPort + "/user/getShopperByUUID";
            URI uri = new URI(stringUri);
            ResponseEntity<GetShopperByUUIDResponse> getShopperByUUIDResponseEntity = restTemplate.postForEntity(uri, parts, GetShopperByUUIDResponse.class);

            GetShopperByUUIDResponse shopperResponse = getShopperByUUIDResponseEntity.getBody();

            Boolean notPresent = true;

            if (listOfShoppers != null) {

                for (Shopper shopper : listOfShoppers) {
                    if (shopper.getShopperID().equals(request.getShopperID())) {
                        response = new AddShopperResponse(false, Calendar.getInstance().getTime(), "Shopper already is in listOfShoppers");
                        notPresent = false;
                    }
                }
                if (notPresent) {
                    Shopper updateShopper = shopperResponse.getShopper();
                    if (updateShopper != null) {
                        updateShopper.setStoreID(request.getStoreID());
                        SaveShopperToRepoRequest saveShopperToRepoRequest = new SaveShopperToRepoRequest(updateShopper);
                        rabbit.convertAndSend("UserEXCHANGE", "RK_SaveShopperToRepo", saveShopperToRepoRequest);
                    }

                    listOfShoppers.add(shopperResponse.getShopper());
                    storeEntity.setShoppers(listOfShoppers);
                    storeRepo.save(storeEntity);
                    response = new AddShopperResponse(true, Calendar.getInstance().getTime(), "Shopper was successfully added");
                }
            } else {
                Shopper updateShopper = shopperResponse.getShopper();
                if (updateShopper != null) {
                    updateShopper.setStoreID(request.getStoreID());
                    SaveShopperToRepoRequest saveShopperToRepoRequest = new SaveShopperToRepoRequest(updateShopper);
                    rabbit.convertAndSend("UserEXCHANGE", "RK_SaveShopperToRepo", saveShopperToRepoRequest);

                }

                List<Shopper> newList = new ArrayList<>();
                newList.add(shopperResponse.getShopper());
                storeEntity.setShoppers(newList);
                storeRepo.save(storeEntity);
                response = new AddShopperResponse(true, Calendar.getInstance().getTime(), "Shopper was successfully added");

            }

        } else {
            throw new cs.superleague.user.exceptions.InvalidRequestException("Request object can't be null for addShopper");
        }

        return response;

    }

    /**
     * @param request is used to bring in:
     *                ShopperID - Id of shopper that should be addes to list of shoppers in Store
     *                StoreID - StoreID of which store to add the shopper to
     *                removeShopper should:
     *                1.Check is request object is correct else throw InvalidRequestException
     *                2.Take in store Id and get corresponding store entity
     *                3.If Store doesn't exist -throw StoreDoesNotExistException
     *                4.Check if listOfshoppers in Store is null - if true return response with sucess being false
     *                5.Get ShopperEntity with corresponding shopperID
     *                6.If shopper doesn't exist throw return response object stating shopper could not be retrieved from databse
     *                7.If shopper does exist then check not currently in list of shoppers
     *                8. If not in list return response with success being false and that the shopper doesn't exist in the list
     *                9. If Shopper is in list then remove shopper from list
     *                Request Object (RemoveShopperRequest)
     *                {
     *                "shopperID":"d30e7a98-c918-11eb-b8bc-0242ac130003"
     *                "storeID":"7fa06899-98e5-43a0-b4d0-9dbc8e29f74a"
     *                <p>
     *                }
     *                Response Object
     *                {
     *                "success":"true"
     *                "timeStamp":"2021-01-05T11:50:55"
     *                "message":"Shopper was successfully removed"
     *                <p>
     *                }
     * @return
     * @throws InvalidRequestException
     * @throws StoreDoesNotExistException
     * @throws cs.superleague.user.exceptions.InvalidRequestException
     * @throws UserException
     */
    @Override
    public RemoveShopperResponse removeShopper(RemoveShopperRequest request) throws InvalidRequestException, StoreDoesNotExistException, UserException, URISyntaxException {
        RemoveShopperResponse response = null;

        if (request != null) {

            boolean invalidReq = false;
            String invalidMessage = "";

            if (request.getShopperID() == null) {
                invalidReq = true;
                invalidMessage = "Shopper ID in request object for remove shopper is null";

            } else if (request.getStoreID() == null) {
                invalidReq = true;
                invalidMessage = "Store ID in request object for remove shopper is null";
            }

            if (invalidReq) throw new InvalidRequestException(invalidMessage);

            Store storeEntity = null;

            try {
                storeEntity = storeRepo.findById(request.getStoreID()).orElse(null);
            } catch (Exception e) {
            }

            if (storeEntity == null) {
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not remove Shopper");
            }


            List<Shopper> listOfShoppers = storeEntity.getShoppers();

            if (listOfShoppers != null) {

                Map<String, Object> parts = new HashMap<String, Object>();
                parts.put("userID", request.getShopperID().toString());
                String stringUri = "http://" + userHost + ":" + userPort + "/user/getShopperByUUID";
                URI uri = new URI(stringUri);
                ResponseEntity<GetShopperByUUIDResponse> getShopperByUUIDResponseEntity = restTemplate.postForEntity(uri, parts, GetShopperByUUIDResponse.class);

                GetShopperByUUIDResponse shopperResponse = getShopperByUUIDResponseEntity.getBody();

                Boolean inList = false;
                for (Shopper shopper : listOfShoppers) {
                    if (shopper.getShopperID().equals(request.getShopperID())) {
                        listOfShoppers.remove(shopper);
                        inList = true;
                    }
                }
                if (inList == true) {
                    storeEntity.setShoppers(listOfShoppers);
                    storeRepo.save(storeEntity);
                    response = new RemoveShopperResponse(true, Calendar.getInstance().getTime(), "Shopper was successfully removed");
                } else {
                    response = new RemoveShopperResponse(false, Calendar.getInstance().getTime(), "Shopper isn't in list of shoppers in store entity");
                }

            } else {
                response = new RemoveShopperResponse(false, Calendar.getInstance().getTime(), "list of Shoppers is null");
            }

        } else {
            throw new InvalidRequestException("Request object can't be null for removeShopper");
        }

        return response;
    }

    /**
     * @param request used to bring in:
     *                storeID - storeId where the shopper list should be cleared
     *                clearShopper should:
     *                1. Check is request object is correct else throw InvalidRequestException
     *                2. Take in store Id and get corresponding store entity
     *                3. If Store doesn't exist -throw StoreDoesNotExistException
     *                4. Gets listofshoppers - if null return response with false success and stating list is null
     *                5. If list isn't null - clear list and return response with success being true
     *                Request Object (ClearShoppersRequest)
     *                {
     *                "storeID":"7fa06899-98e5-43a0-b4d0-9dbc8e29f74a"
     *                }
     *                Response Object (ClearShoppersResponse)
     *                {
     *                "success":"true"
     *                "timeStamp":"2021-01-05T11:50:55"
     *                "message":"List of Shopper successfuly cleared"
     *                <p>
     *                }
     * @return
     * @throws InvalidRequestException
     * @throws StoreDoesNotExistException
     */
    @Override
    public ClearShoppersResponse clearShoppers(ClearShoppersRequest request) throws InvalidRequestException, StoreDoesNotExistException {
        ClearShoppersResponse response = null;
        if (request != null) {

            if (request.getStoreID() == null) {
                throw new InvalidRequestException("Store ID in request object for clearShoppers can't be null");
            }

            Store storeEntity = null;
            try {
                storeEntity = storeRepo.findById(request.getStoreID()).orElse(null);
            } catch (Exception e) {
            }

            if (storeEntity == null) {
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not clear shoppers");
            }


            List<Shopper> listOfShoppers = storeEntity.getShoppers();
            if (listOfShoppers != null) {
                listOfShoppers.clear();

                storeEntity.setShoppers(listOfShoppers);
                storeRepo.save(storeEntity);
                response = new ClearShoppersResponse(true, Calendar.getInstance().getTime(), "List of Shopper successfuly cleared");
            } else {
                response = new ClearShoppersResponse(false, Calendar.getInstance().getTime(), "List of shoppers is null");
            }
        } else {
            throw new InvalidRequestException("Request object can't be null for clearShoppers");
        }

        return response;
    }

    /**
     * @param request is used to bring in:
     *                private storeID
     *                private String storeBrand;
     *                private int maxShoppers;
     *                private int maxOrders;
     *                private Boolean isOpen;
     *                private int openingTime;
     *                private int closingTime;
     *                <p>
     *                updateStore should:
     *                1. Check that the request object is not null, if so then throw an InvalidRequestException
     *                2. Check if the request's store object is not null, else throw an InvalidRequestException
     *                3. Check if the request's storeID is not null, else throw an StoreDoesNotExistException
     *                4. Use the request's storeID to find the corresponding Store object in the repo. If
     *                it doesn't exist then throw a StoreDoesNotExistException.
     *                5. Update the found store object's fields with the request object store's data.
     *                6. Initialize the response object's constructor to the storeID and response message
     *                7. Return the response object.
     *                <p>
     *                Request Object (UpdateStoreRequest):
     *                {
     *                "storeID":"7fa06899-98e5-43a0-b4d0-9dbc8e29f74a"
     *                "store":{
     *                "storeBrand":"PnP"
     *                ...
     *                }
     *                }
     *                <p>
     *                Response Object (UpdateStoreResponse):
     *                {
     *                "response":true
     *                "message":"Store updated successfully"
     *                "storeID":"7fa06899-98e5-43a0-b4d0-9dbc8e29f74a"
     *                }
     * @return
     * @throws InvalidRequestException
     * @throws StoreDoesNotExistException
     */
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
            if (storeEntity == null) {
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not update Store entity");
            }

            if (request.getStore().getOpen() != null) {
                storeEntity.setOpen(request.getStore().getOpen());
            }
            if (request.getStore().getOpeningTime() != -1) {
                storeEntity.setOpeningTime(request.getStore().getOpeningTime());
            }
            if (request.getStore().getClosingTime() != -1) {
                storeEntity.setClosingTime(request.getStore().getClosingTime());
            }
            if (request.getStore().getStoreBrand() != null) {
                storeEntity.setStoreBrand(request.getStore().getStoreBrand());
            }
            if (request.getStore().getMaxShoppers() != -1) {
                storeEntity.setMaxShoppers(request.getStore().getMaxShoppers());
            }
            if (request.getStore().getMaxOrders() != -1) {
                storeEntity.setMaxOrders(request.getStore().getMaxOrders());
            }
            storeRepo.save(storeEntity);

            response = new UpdateStoreResponse(true, "Store updated successfully", storeEntity.getStoreID());
        } else {
            throw new InvalidRequestException("The request object for UpdateStoreRequest is null - Could not update store");
        }
        return response;
    }

    /**
     * @param request is used to bring in:
     *                private storeID
     *                private List<Shopper> newShoppers;
     *                <p>
     *                updateShoppers should:
     *                1. Check that the request object is not null, if so then throw an InvalidRequestException
     *                2. Check if the request's store object is not null, else throw an InvalidRequestException
     *                3. Check if the request's newShoppers object is not null, else throw an InvalidRequestException
     *                4. Check if the request's storeID is not null, else throw an StoreDoesNotExistException
     *                5. Use the request's storeID to find the corresponding Store object in the repo. If
     *                it doesn't exist then throw a StoreDoesNotExistException.
     *                6. Update the found store object's list of shoppers with the request object store's shopper list.
     *                7. Initialize the response object's constructor to the storeID and response message
     *                8. Return the response object.
     *                <p>
     *                Request Object (UpdateShoppersRequest):
     *                {
     *                <p>
     *                "store":{
     *                "storeID":"7fa06899-98e5-43a0-b4d0-9dbc8e29f74a"
     *                ...
     *                }
     *                "newShoppers":{
     *                "shopper":{
     *                "id":"1fa96449-98e5-43a0-b4d0-9dbc8e29f74a"
     *                ...
     *                }
     *                "shopper":{
     *                "id":"2fa96449-98e5-43a0-b4d0-9dbc8e29f74a"
     *                ...
     *                }
     *                }
     *                }
     *                <p>
     *                Response Object (UpdateShoppersResponse):
     *                {
     *                "response":true
     *                "message":"Shoppers updated successfully"
     *                "storeID":"7fa06899-98e5-43a0-b4d0-9dbc8e29f74a"
     *                }
     * @return
     * @throws InvalidRequestException
     * @throws StoreDoesNotExistException
     */
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
            if (storeEntity == null) {
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - could not update Shoppers entity");
            }

            storeEntity.setShoppers(request.getNewShoppers());
            storeRepo.save(storeEntity);

            response = new UpdateShoppersResponse(true, "Shoppers updated successfully", storeEntity.getStoreID());
        } else {
            throw new InvalidRequestException("The request object for UpdateShoppersRequest is null - Could not update shoppers");
        }
        return response;
    }

    /**
     * @param request is used to bring in:
     *                <p>
     *                getStores should:
     *                1. Check if there are any stores in the store database
     *                2. If there are stores, store them in a list
     *                3. If there aren't any stores in the database, return response with success being false
     *                4. If list of shoppers is not null return response with list of shoppers and success status being true
     *                <p>
     *                Request object (GetStoresRequest)
     *                {
     *                <p>
     *                <p>
     *                }
     *                <p>
     *                Response object (GetStoresResponse)
     *                {
     *                "stores": storeEntity.findAll()
     *                "success": "true"
     *                "message":"List of Stores successfully returned"
     *                }
     * @return
     */
    @Override
    public GetStoresResponse getStores(GetStoresRequest request) throws InvalidRequestException {
        GetStoresResponse response = null;

        if (request != null) {

            List<Store> storeEntity = null;

            try {
                storeEntity = storeRepo.findAll();

            } catch (Exception e) {
            }

            if (storeEntity != null && !storeEntity.isEmpty()) {

                response = new GetStoresResponse(true, "List of Stores successfully returned", storeEntity);
            } else {
                response = new GetStoresResponse(false, "List of Stores is null", null);
            }
        } else {
            throw new InvalidRequestException("Request object for getStores can't be null");
        }
        return response;

    }

    /**
     * @param request object is used to bring in:
     *                private storeID
     *                <p>
     *                getQueue should:
     *                1. Check that the request object is not null, if so then throw an InvalidRequestException
     *                2. Check if the appropriate order attributes from the request are not null, else throw an InvalidRequestException
     *                3. Check that the store exists, else throw a StoreDoesNotExistException
     *                4. Find and store the order list from that store in the response object
     *                5. Return the response object.
     *                <p>
     *                Request Object (GetQueueRequest):
     *                {
     *                "storeID": {"d09832893"}
     *                }
     *                <p>
     *                Response Object (GetQueueResponse):
     *                {
     *                "success":true
     *                "message":"Order was correctly added to queue"
     *                "queueOfOrders": store.getOrderQueue
     *                }
     * @return
     * @throws InvalidRequestException
     * @throws StoreDoesNotExistException
     */

    @Override
    public GetQueueResponse getQueue(GetQueueRequest request) throws InvalidRequestException, StoreDoesNotExistException {
        GetQueueResponse response = null;

        if (request != null) {

            if (request.getStoreID() == null) {
                throw new InvalidRequestException("Store ID parameter in request can't be null - can't get queue of orders");
            }
            Store store = null;

            try {
                store = storeRepo.findById(request.getStoreID()).orElse(null);
            } catch (Exception e) {
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - can't get queue of orders");
            }

            if (store == null) {
                throw new StoreDoesNotExistException("Store with ID does not exist in repository - can't get queue of orders");
            }
            List<Order> orderQueue = store.getOrderQueue();

            if (orderQueue != null) {
                response = new GetQueueResponse(true, "The order queue was successfully returned", orderQueue);
            } else {
                response = new GetQueueResponse(false, "The order queue of shop is empty", null);

            }
            return response;

        } else {
            throw new InvalidRequestException("Request object for GetQueueRequest can't be null - can't get queue");
        }
    }

    /**
     * @param request object is used to bring in:
     *                private store
     *                <p>
     *                SaveStoreToRepo should:
     *                1. Check that the request object is not null, if so then throw an InvalidRequestException
     *                2. Check if the appropriate request attributes from the request are not null, else throw an InvalidRequestException
     *                3. Check that the StoreRepo is not null and if not, save the store
     *                5. Return the response object.
     *                <p>
     *                Request Object (SaveStoreToRepoRequest):
     *                {
     *                "store": request.getStore();
     *                }
     *                <p>
     *                Response Object (SaveStoreToRepoResponse):
     *                {
     *                "success":true
     *                "message":"Store was successfully saved"
     *                "timestamp": "2021-01-05T11:50:55"
     *                }
     * @return
     * @throws InvalidRequestException
     */

    @Override
    public SaveStoreToRepoResponse saveStoreToRepo(SaveStoreToRepoRequest request) throws InvalidRequestException {
        SaveStoreToRepoResponse response = null;

        if (request != null) {

            if (request.getStore() == null) {
                throw new InvalidRequestException("Store in parameter in request can't be null - can't save store");
            }

            Store store = request.getStore();

            if (storeRepo != null) {

                storeRepo.save(store);
                return new SaveStoreToRepoResponse(true, Calendar.getInstance().getTime(), "Store successfully saved.");

            } else {
                return new SaveStoreToRepoResponse(false, Calendar.getInstance().getTime(), "Store can't be saved.");

            }


        } else {
            throw new InvalidRequestException("Request object can't be null - can't save store");
        }
    }

    /**
     * @param request is used to bring in:
     *                <p>
     *                <p>
     *                getAllItems should:
     *                1. Check if the ItemRepo is not null, else throw an InvalidRequestException
     *                2. Return all items in response object.
     *                <p>
     *                Request Object (GetAllItemsRequest):
     *                {
     *                <p>
     *                }
     *                <p>
     *                Response Object (GetAllItemsResponse):
     *                {
     *                "items":itemsRepo.findAll()
     *                "timestamp":"2021-01-05T11:50:55"
     *                "message":"All items have been retrieved"
     *                }
     * @return
     * @throws InvalidRequestException
     */

    @Override
    public GetAllItemsResponse getAllItems(GetAllItemsRequest request) throws InvalidRequestException {
        GetAllItemsResponse response = null;

        if (request != null) {

            List<Item> items;
            try {
                items = itemRepo.findAll();
            } catch (Exception e) {
                throw new InvalidRequestException("No items exist in repository");
            }

            if (items == null) {
                response = new GetAllItemsResponse(null, Calendar.getInstance().getTime(), "All items can't be retrieved");
            } else {
                response = new GetAllItemsResponse(items, Calendar.getInstance().getTime(), "All items have been retrieved");
            }
        } else {
            throw new InvalidRequestException("The GetAllItemsRequest parameter is null - Could not retrieve items");
        }
        return response;
    }

    /**
     * @param request object is used to bring in:
     *                private item
     *                <p>
     *                SaveItemToRepo should:
     *                1. Check that the request object is not null, if so then throw an InvalidRequestException
     *                2. Check if the appropriate request attributes from the request are not null, else throw an InvalidRequestException
     *                3. Check that the ItemRepo is not null and if not, save the item
     *                5. Return the response object.
     *                <p>
     *                Request Object (SaveItemToRepoRequest):
     *                {
     *                "item": request.getItem();
     *                }
     *                <p>
     *                Response Object (SaveItemToRepoResponse):
     *                {
     *                "success":true
     *                "message":"Item was successfully saved"
     *                "timestamp": "2021-01-05T11:50:55"
     *                }
     * @return
     * @throws InvalidRequestException
     */

    @Override
    public SaveItemToRepoResponse saveItemToRepo(SaveItemToRepoRequest request) throws InvalidRequestException {
        SaveItemToRepoResponse response = null;

        if (request != null) {

            if (request.getItem() == null) {
                throw new InvalidRequestException("Item in parameter in request can't be null - can't save item");
            }

            Item item = request.getItem();

            if (itemRepo != null) {
                itemRepo.save(item);
                return new SaveItemToRepoResponse(true, Calendar.getInstance().getTime(), "Item successfully saved.");

            } else {
                return new SaveItemToRepoResponse(false, Calendar.getInstance().getTime(), "Item can't be saved.");

            }

        } else {
            throw new InvalidRequestException("Request object can't be null - can't save item");
        }
    }

    /**
     * @param request is used to bring in:
     *                <p>
     *                <p>
     *                getItemsByUUIDS should:
     *                1. Check if the ItemRepo is not null, else throw an InvalidRequestException
     *                2. Return all items in response object.
     *                <p>
     *                Request Object (GetItemsByUUIDSRequest):
     *                {
     *                <p>
     *                }
     *                <p>
     *                Response Object (GetItemsByUUIDSResponse):
     *                {
     *                "items":itemsRepo.findAll()
     *                "timestamp":"2021-01-05T11:50:55"
     *                "message":"All items have been retrieved"
     *                }
     * @return
     * @throws InvalidRequestException
     */

    @Override
    public GetItemsByIDResponse getItemsByID(GetItemsByIDRequest request) throws InvalidRequestException {
        GetItemsByIDResponse response = null;

        if (request != null) {

            if (request.getItemIDs() == null) {
                throw new InvalidRequestException("Null parameter in request object, can't get Items");
            }
            List<Item> items;
            try {
                items = itemRepo.findAll();
            } catch (Exception e) {
                throw new InvalidRequestException("No items exist in repository");
            }

            if (items == null) {
                response = new GetItemsByIDResponse(null, Calendar.getInstance().getTime(), "Items can't be retrieved");
            } else {
                List<String> reqItems = request.getItemIDs();
                List<Item> itemsFound = new ArrayList<>();

                for (int k = 0; k < reqItems.size(); k++) {
                    for (int j = 0; j < items.size(); j++) {
                        if (reqItems.get(k).equals(items.get(j).getProductID())) {
                            itemsFound.add(items.get(j));
                        }
                    }

                }
                response = new GetItemsByIDResponse(itemsFound, Calendar.getInstance().getTime(), "All items have been retrieved");
            }
        } else {
            throw new InvalidRequestException("The GetAllItemsRequest parameter is null - Could not retrieve items");
        }
        return response;
    }

    @Override
    public void addToFrontOfQueue(AddToFrontOfQueueRequest request) throws InvalidRequestException {

        boolean invalidReq = false;
        String invalidMessage = "";

        if (request == null || request.getOrder() == null) {
            invalidReq = true;
            invalidMessage = "Invalid request: null value received";
        } else {
            Order order = request.getOrder();
            if (order.getOrderID() == null) {
                invalidMessage = "Invalid request: Missing order ID";
                invalidReq = true;
            } else if (order.getUserID() == null) {
                invalidMessage = "Invalid request: missing user ID";
                invalidReq = true;
            } else if (order.getStoreID() == null) {
                invalidMessage = "Invalid request: missing store ID";
                invalidReq = true;
            } else if (order.getTotalCost() == null) {
                invalidMessage = "Invalid request: missing order cost";
                invalidReq = true;
            } else if (order.getCartItems() == null || order.getCartItems().isEmpty()) {
                invalidMessage = "Invalid request: item list is empty or null";
                invalidReq = true;
            }
        }

        if (invalidReq) throw new InvalidRequestException(invalidMessage);

        Order updatedOrder = request.getOrder();

        updatedOrder.setStatus(OrderStatus.IN_QUEUE);

        SaveOrderToRepoRequest saveOrderToRepoRequest = new SaveOrderToRepoRequest(updatedOrder);
        rabbit.convertAndSend("PaymentEXCHANGE", "RK_SaveOrderToRepo", saveOrderToRepoRequest);

        Store store;
        try {
            store = storeRepo.findById(request.getOrder().getStoreID()).orElse(null);
        } catch (Exception e) {
            throw new InvalidRequestException(e.getMessage());
        }

        if (store.getOrderQueue() == null) {
            store.setOrderQueue(new ArrayList<>());
        }
        store.getOrderQueue().add(0, updatedOrder);

        if (storeRepo != null)
            storeRepo.save(store);
    }

    @Override
    public SaveCatalogueToRepoResponse saveCatalogueToRepo(SaveCatalogueToRepoRequest request) throws InvalidRequestException {

        if (request != null) {

            if (request.getCatalogue() == null) {
                throw new InvalidRequestException("Catalogue in parameter in request can't be null - can't save catalogue");
            }

            Catalogue catalogue = request.getCatalogue();

            if (catalogueRepo != null) {
                catalogueRepo.save(catalogue);
                return new SaveCatalogueToRepoResponse(true, Calendar.getInstance().getTime(), "Catalogue successfully saved.");

            } else {
                return new SaveCatalogueToRepoResponse(false, Calendar.getInstance().getTime(), "Catalogue can't be saved.");

            }

        } else {
            throw new InvalidRequestException("Request object can't be null - can't save catalogue");
        }
    }

    @Override
    public GetProductByBarcodeResponse getProductByBarcode(GetProductByBarcodeRequest request) throws InvalidRequestException, ItemDoesNotExistException {
        if (request == null) {
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getProductBarcode() == null || request.getStoreID() == null) {
            throw new InvalidRequestException("Null parameters in request object.");
        }
        Item product = itemRepo.findAllByBarcodeAndStoreID(request.getProductBarcode(), request.getStoreID());
        if (product == null) {
            throw new ItemDoesNotExistException("The item requested does not exist in the database.");
        }
        return new GetProductByBarcodeResponse(true, "The item related to the barcode and store has been returned.", product);
    }

    @Override
    public PriceCheckResponse priceCheck(PriceCheckRequest request) throws InvalidRequestException {

        List<Item> items;
        List<Item> cheaperItems = new ArrayList<>();
        List<CartItem> requestCartItems = new ArrayList<>();
        String message = "Price check successfully completed";

        if (request == null) {
            throw new InvalidRequestException("Request object can't be null - can't perform a price check");
        }

        if (request.getCartItems() == null) {
            throw new InvalidRequestException("CartItem list in parameter request can't be null - can't perform" +
                    " a price check");
        }

        if (request.getCartItems().size() == 0) {
            message = "No items in CartItem List, could not perform a price check.";
            return new PriceCheckResponse(requestCartItems, message, false);
        }

        items = itemRepo.findAll();

        // stores all the cheaper items in a new list
        for (Item item : items) {
            for (CartItem cartItem : request.getCartItems()) {
                if (item.getBarcode().equals(cartItem.getBarcode()) &&
                        item.getPrice() < cartItem.getPrice()) {
                    cheaperItems.add(item);
                }
            }
        }

        // removes duplicate items with higher prices
        List<Item> cheaperItemsCopy = new ArrayList<>(cheaperItems);
        for (Item item : cheaperItemsCopy) {
            cheaperItems.removeIf(i -> item.getBarcode().equals(i.getBarcode()) &&
                    i.getPrice() >= item.getPrice());
        }

        requestCartItems = request.getCartItems();

        // removes expensive items from the cart items list
        for (Item item : cheaperItems) {
            for (CartItem cartItem : request.getCartItems()) {
                if (cartItem.getBarcode().equals(item.getBarcode())) {
                    requestCartItems.remove(cartItem);
                }
            }
        }

        // inserts cheaper cart items in cartItem List
        for (Item item : cheaperItems) {
            requestCartItems.add(createCartItem(item));
        }

        return new PriceCheckResponse(requestCartItems, message, true);
    }

    @Override
    public PriceCheckAllAvailableStoresResponse priceCheckAllAvailableStores(PriceCheckAllAvailableStoresRequest request) throws InvalidRequestException {

        Store store;
        UUID storeID;
        double distance;
        List<Item> items;
        GeoPoint storeLocation;
        List<Item> cheaperItems = new ArrayList<>();
        List<CartItem> requestCartItems = new ArrayList<>();
        String message = "Price check available stores successfully completed";

        if (request == null) {
            throw new InvalidRequestException("Request object can't be null - can't perform a price check");
        }

        if (request.getCartItems() == null) {
            throw new InvalidRequestException("CartItem list in parameter request can't be null - can't perform" +
                    " a price check");
        }

        if (request.getCartItems().size() == 0) {
            message = "No items in CartItem List, could not perform a price check.";
            return new PriceCheckAllAvailableStoresResponse(requestCartItems, message, false);
        }

        storeID = request.getCartItems().get(0).getStoreID();
        store = storeRepo.findById(storeID).orElse(null);

        if (store == null) {
            throw new InvalidRequestException("CartItems do not contain a valid storeID, can't perform a " +
                    "price Check");
        }

        storeLocation = store.getStoreLocation();

        items = itemRepo.findAll();

        // stores all the cheaper items from close by stores in a new list
        for (Item item : items) {
            storeID = item.getStoreID();
            store = storeRepo.findById(storeID).orElse(null);

            //disregards an item that may not have storeid
            if (store == null) {
                continue;
            }

            distance = getDistance(storeLocation, store.getStoreLocation());

            if (distance < 10)
                for (CartItem cartItem : request.getCartItems()) {
                    if (item.getBarcode().equals(cartItem.getBarcode()) &&
                            item.getPrice() < cartItem.getPrice()) {
                        cheaperItems.add(item);
                    }
                }
        }

        // removes duplicate items with higher prices
        List<Item> cheaperItemsCopy = new ArrayList<>(cheaperItems);
        for (Item item : cheaperItemsCopy) {
            cheaperItems.removeIf(i -> item.getBarcode().equals(i.getBarcode()) &&
                    i.getPrice() >= item.getPrice());
        }

        requestCartItems = request.getCartItems();

        // removes expensive items from the cart items list
        for (Item item : cheaperItems) {
            for (CartItem cartItem : request.getCartItems()) {
                if (cartItem.getBarcode().equals(item.getBarcode())) {
                    requestCartItems.remove(cartItem);
                }
            }
        }

        // inserts cheaper cart items in cartItem List
        for (Item item : cheaperItems) {
            requestCartItems.add(createCartItem(item));
        }

        return new PriceCheckAllAvailableStoresResponse(requestCartItems, message, true);
    }

    // Helper
    private double getDistance(GeoPoint point1, GeoPoint point2) {
        double theta = point1.getLongitude() - point2.getLongitude();
        double distance = Math.sin(Math.toRadians(point1.getLatitude())) * Math.sin(Math.toRadians(point2.getLatitude())) + Math.cos(Math.toRadians(point1.getLatitude())) * Math.cos(Math.toRadians(point2.getLatitude())) * Math.cos(Math.toRadians(theta));
        distance = Math.acos(distance);
        distance = Math.toDegrees(distance);
        distance = distance * 60 * 1.1515 * 1.609344;
        return distance;
    }

    private CartItem createCartItem(Item item){
        CartItem newCartItem = new CartItem();

        newCartItem.setCartItemNo(UUID.randomUUID());
        newCartItem.setBarcode(item.getBarcode());
        newCartItem.setBrand(item.getBrand());
        newCartItem.setItemType(item.getItemType());
        newCartItem.setName(item.getName());
        newCartItem.setDescription(item.getDescription());
        newCartItem.setImageUrl(item.getImageUrl());
        newCartItem.setPrice(item.getPrice());
        newCartItem.setProductID(item.getProductID());
        newCartItem.setQuantity(item.getQuantity());
        newCartItem.setSize(item.getSize());
        newCartItem.setStoreID(item.getStoreID());
        newCartItem.setTotalCost(item.getPrice() * item.getQuantity());

        return newCartItem;
    }

}

