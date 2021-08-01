package cs.superleague.user;

import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.GroceryList;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.exceptions.UserDoesNotExistException;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.GroceryListRepo;
import cs.superleague.user.repos.ShopperRepo;
import cs.superleague.user.requests.CompletePackagingOrderRequest;
import cs.superleague.user.requests.GetShopperByUUIDRequest;
import cs.superleague.user.requests.MakeGroceryListRequest;
import cs.superleague.user.requests.ScanItemRequest;
import cs.superleague.user.responses.CompletePackagingOrderResponse;
import cs.superleague.user.responses.GetShopperByUUIDResponse;
import cs.superleague.user.responses.MakeGroceryListResponse;
import cs.superleague.user.responses.ScanItemResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService{

    private final ShopperRepo shopperRepo;
    private final CustomerRepo customerRepo;
    private final GroceryListRepo groceryListRepo;
    //private final UserService userService;

    @Autowired
    public UserServiceImpl(ShopperRepo shopperRepo, CustomerRepo customerRepo,
                       GroceryListRepo groceryListRepo){//, UserService userService) {
        this.shopperRepo = shopperRepo;
        this.customerRepo = customerRepo;
        this.groceryListRepo = groceryListRepo;
        //this.userService = userService;
    }

    @Override //unfinished
    public CompletePackagingOrderResponse completePackagingOrder(CompletePackagingOrderRequest request) throws InvalidRequestException {

        // Checking for valid and appropriately populated request

        boolean invalidReq = false;
        String invalidMessage = "";

        if (request == null){
            invalidReq = true;
            invalidMessage = "Invalid request: null value received";
        } else if (request.getOrderID() == null){
            invalidReq = true;
            invalidMessage = "Invalid request: no orderID received";
        }

        // // Get order by ID // //
        //Order updatedOrder = <paymentService>.getOrder(request.getOrderID());

        // if (updatedOrder.getStatus != OrderStatus.PACKING){
        //  invalidReq = true;
        //  invalidMessage = "Invalid request: incorrect order status;
        //}

        if (invalidReq) throw new InvalidRequestException(invalidMessage);


        // // Update the order status and create time // //
        //.setStatus(OrderStatus.AWAITING_COLLECTION);

        // <paymentService>.updateOrder(updatedOrder);


        //unfinished
        CompletePackagingOrderResponse response = new CompletePackagingOrderResponse();
        return response;

    }

    @Override
    public ScanItemResponse scanItem(ScanItemRequest request) {
        return null;
    }

    @Override
    public GetShopperByUUIDResponse getShopperByUUIDRequest(GetShopperByUUIDRequest request) throws InvalidRequestException, UserDoesNotExistException {
        GetShopperByUUIDResponse response=null;
        if(request != null){

            if(request.getUserID()==null){
                throw new InvalidRequestException("UserID is null in GetShopperByUUIDRequest request - could not return shopper entity");
            }

            Shopper shopperEntity=null;
            try {
                shopperEntity = shopperRepo.findById(request.getUserID()).orElse(null);
            }catch(Exception e){}
            if(shopperEntity==null) {
                throw new UserDoesNotExistException("User with ID does not exist in repository - could not get Shopper entity");
            }
            response=new GetShopperByUUIDResponse(shopperEntity, Calendar.getInstance().getTime(),"Shopper entity with corresponding user id was returned");
        }
        else{
            throw new InvalidRequestException("GetShopperByUUID request is null - could not return Shopper entity");
        }
        return response;
    }

    @Override
    public MakeGroceryListResponse MakeGroceryList(MakeGroceryListRequest request) throws InvalidRequestException, UserDoesNotExistException{
        UUID userID;
        String name;
        String message;
        Optional<Customer> customerOptional;
        GroceryList groceryList;

        if(request == null){
            throw new InvalidRequestException("MakeGroceryList Request is null - could not make grocery list");
        }

        if(request.getUserID() == null){
            throw new InvalidRequestException("UserID is null - could not make grocery list");
        }

        if(request.getItems() == null || request.getItems().isEmpty()){
            throw new InvalidRequestException("Item list empty - could not make the grocery list");
        }

        if(request.getName() == null){
            throw new InvalidRequestException("Grocery List Name is Null - could not make the grocery list");
        }

        userID = request.getUserID();
        customerOptional = customerRepo.findById(userID);
        if(customerOptional == null || !customerOptional.isPresent()){
            throw new UserDoesNotExistException("User with given userID does not exist - could not make the grocery list");
        }

        name = request.getName();
        groceryList = groceryListRepo.findGroceryListByNameAndUserID(name, userID);

        if(groceryList == null){
            groceryList = new GroceryList(UUID.randomUUID(), name, request.getItems(), request.getUserID());
            message = "Grocery List successfully created";
        }else{ // or update the groceryList with new list?
            throw new InvalidRequestException("Grocery List Name exists - could not make the grocery list");
        }

        groceryList = groceryListRepo.save(groceryList);

        return new MakeGroceryListResponse(groceryList, true, message);
    }
}