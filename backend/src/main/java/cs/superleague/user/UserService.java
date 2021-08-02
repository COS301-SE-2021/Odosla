package cs.superleague.user;

import cs.superleague.user.exceptions.*;
import cs.superleague.user.requests.*;
import cs.superleague.user.responses.*;


public interface UserService {
//    public RegisterResponse registerUser(RegisterUserRequest registerRequest);
//    public RegisterResponse registerAdminUser(RegisterUserRequest registerRequest);

    public CompletePackagingOrderResponse completePackagingOrder(CompletePackagingOrderRequest request) throws InvalidRequestException;
    public ScanItemResponse scanItem(ScanItemRequest request);
    public GetShopperByUUIDResponse getShopperByUUIDRequest(GetShopperByUUIDRequest request) throws InvalidRequestException, UserDoesNotExistException;

    /* Customer  */
    MakeGroceryListResponse MakeGroceryList(MakeGroceryListRequest request) throws InvalidRequestException, UserDoesNotExistException;
    GetShoppingCartResponse getShoppingCart(GetShoppingCartRequest request) throws InvalidRequestException, UserDoesNotExistException;
}