package cs.superleague.user;

import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.user.exceptions.*;
import cs.superleague.user.requests.*;
import cs.superleague.user.responses.*;

public interface UserService {
//    public RegisterResponse registerUser(RegisterUserRequest registerRequest);
//    public RegisterResponse registerAdminUser(RegisterUserRequest registerRequest);

    public CompletePackagingOrderResponse completePackagingOrder(CompletePackagingOrderRequest request) throws InvalidRequestException, OrderDoesNotExist;
    public ScanItemResponse scanItem(ScanItemRequest request);
    public RegisterCustomerResponse registerCustomer (RegisterCustomerRequest request) throws InvalidRequestException;
    public RegisterDriverResponse registerDriver(RegisterDriverRequest request) throws InvalidRequestException;
    public RegisterShopperResponse registerShopper(RegisterShopperRequest request) throws InvalidRequestException;
    public RegisterAdminResponse registerAdmin(RegisterAdminRequest request) throws InvalidRequestException;
    public LoginResponse loginUser(LoginRequest request) throws UserException;
    public AccountVerifyResponse verifyAccount(AccountVerifyRequest request) throws Exception;
    public GetShopperByUUIDResponse getShopperByUUIDRequest(GetShopperByUUIDRequest request) throws UserException;

    /* Customer  */
    MakeGroceryListResponse makeGroceryList(MakeGroceryListRequest request) throws InvalidRequestException, UserDoesNotExistException;
    GetShoppingCartResponse getShoppingCart(GetShoppingCartRequest request) throws InvalidRequestException, UserDoesNotExistException;
    //RegisterCustomerResponse registerCustomer(RegisterCustomerRequest request) throws InvalidRequestException, AlreadyExistsException;
    UpdateCustomerDetailsResponse updateCustomerDetails(UpdateCustomerDetailsRequest request) throws InvalidRequestException, UserDoesNotExistException;
    AddToCartResponse addToCart(AddToCartRequest request) throws InvalidRequestException, UserDoesNotExistException;
    ClearShoppingCartResponse clearShoppingCart(ClearShoppingCartRequest request) throws InvalidRequestException, UserDoesNotExistException;
}