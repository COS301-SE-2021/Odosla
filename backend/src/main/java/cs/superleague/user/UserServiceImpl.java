package cs.superleague.user;

import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.responses.GetStoreByUUIDResponse;
import cs.superleague.user.dataclass.Shopper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cs.superleague.user.exceptions.*;
import cs.superleague.user.repos.ShopperRepo;
import cs.superleague.user.responses.*;
import cs.superleague.user.requests.*;

import java.util.Calendar;

@Service("userServiceImpl")
public class UserServiceImpl implements UserService{

    private final ShopperRepo shopperRepo;
    //private final UserService userService;

    @Autowired
    public UserServiceImpl(ShopperRepo shopperRepo){//, UserService userService) {
        this.shopperRepo = shopperRepo;
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
            shopperEntity = shopperRepo.findById(request.getUserID()).orElse(null);
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


}