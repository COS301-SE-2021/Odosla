package cs.superleague.user;

import cs.superleague.user.exceptions.*;
import cs.superleague.user.requests.*;
import cs.superleague.user.responses.*;

public interface UserService {
//    public RegisterResponse registerUser(RegisterUserRequest registerRequest);
//    public RegisterResponse registerAdminUser(RegisterUserRequest registerRequest);

    GetShopperByUUIDResponse getShopperByUUIDRequest(GetShopperByUUIDRequest request) throws InvalidRequestException, UserDoesNotExistException;

    public CompletePackagingOrderResponse completePackagingOrder(CompletePackagingOrderRequest request) throws InvalidRequestException;
    public ScanItemResponse scanItem(ScanItemRequest request);
    public RegisterCustomerResponse registerCustomer (RegisterCustomerRequest request) throws InvalidRequestException;
    public RegisterDriverResponse registerDriver(RegisterDriverRequest request) throws InvalidRequestException;
    public RegisterShopperResponse registerShopper(RegisterShopperRequest request) throws InvalidRequestException;
    public RegisterAdminResponse registerAdmin(RegisterAdminRequest request) throws InvalidRequestException;
    public LoginResponse loginUser(LoginRequest request) throws UserException;
    public AccountVerifyResponse verifyAccount(AccountVerifyRequest request) throws Exception;

    /* Customer  */
    MakeGroceryListResponse makeGroceryList(MakeGroceryListRequest request) throws InvalidRequestException, UserDoesNotExistException;
    GetShoppingCartResponse getShoppingCart(GetShoppingCartRequest request) throws InvalidRequestException, UserDoesNotExistException;
    //RegisterCustomerResponse registerCustomer(RegisterCustomerRequest request) throws InvalidRequestException, AlreadyExistsException;
    UpdateCustomerDetailsResponse updateCustomerDetails(UpdateCustomerDetailsRequest request) throws InvalidRequestException, UserDoesNotExistException;
    AddToCartResponse addToCart(AddToCartRequest request) throws InvalidRequestException, UserDoesNotExistException;
}