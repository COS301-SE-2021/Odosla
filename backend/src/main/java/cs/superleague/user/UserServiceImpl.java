package cs.superleague.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.ShopperRepo;
import cs.superleague.user.responses.*;
import cs.superleague.user.requests.*;

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


}