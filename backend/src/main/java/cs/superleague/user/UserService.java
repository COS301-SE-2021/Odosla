package cs.superleague.user;

import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.user.exceptions.*;
import cs.superleague.user.requests.*;
import cs.superleague.user.responses.*;

public interface UserService {
//    public RegisterResponse registerUser(RegisterUserRequest registerRequest);
//    public RegisterResponse registerAdminUser(RegisterUserRequest registerRequest);

    CompletePackagingOrderResponse completePackagingOrder(CompletePackagingOrderRequest request) throws InvalidRequestException, OrderDoesNotExist;
    ScanItemResponse scanItem(ScanItemRequest request) throws InvalidRequestException, OrderDoesNotExist;
    RegisterCustomerResponse registerCustomer (RegisterCustomerRequest request) throws InvalidRequestException;
    RegisterDriverResponse registerDriver(RegisterDriverRequest request) throws InvalidRequestException;
    RegisterShopperResponse registerShopper(RegisterShopperRequest request) throws InvalidRequestException;
    RegisterAdminResponse registerAdmin(RegisterAdminRequest request) throws InvalidRequestException;
    LoginResponse loginUser(LoginRequest request) throws UserException;
    AccountVerifyResponse verifyAccount(AccountVerifyRequest request) throws Exception;
    GetShopperByUUIDResponse getShopperByUUIDRequest(GetShopperByUUIDRequest request) throws UserException;
    UpdateShopperDetailsResponse updateShopperDetails(UpdateShopperDetailsRequest request) throws UserException;
    UpdateAdminDetailsResponse updateAdminDetails(UpdateAdminDetailsRequest request) throws UserException;
    UpdateDriverDetailsResponse updateDriverDetails(UpdateDriverDetailsRequest request) throws UserException;
    ResetPasswordResponse resetPassword(ResetPasswordRequest request) throws UserException;

    /* Customer  */
    MakeGroceryListResponse makeGroceryList(MakeGroceryListRequest request) throws InvalidRequestException, CustomerDoesNotExistException;
    GetShoppingCartResponse getShoppingCart(GetShoppingCartRequest request) throws InvalidRequestException, CustomerDoesNotExistException;
    //RegisterCustomerResponse registerCustomer(RegisterCustomerRequest request) throws InvalidRequestException, AlreadyExistsException;
    UpdateCustomerDetailsResponse updateCustomerDetails(UpdateCustomerDetailsRequest request) throws InvalidRequestException, CustomerDoesNotExistException;
    SetCartResponse setCart(SetCartRequest request) throws InvalidRequestException, CustomerDoesNotExistException;
    ClearShoppingCartResponse clearShoppingCart(ClearShoppingCartRequest request) throws InvalidRequestException, UserDoesNotExistException;
    CollectOrderResponse collectOrder(CollectOrderRequest request) throws OrderDoesNotExist, InvalidRequestException;
    CompleteDeliveryResponse completeDelivery(CompleteDeliveryRequest request) throws OrderDoesNotExist, InvalidRequestException;
    UpdateDriverShiftResponse updateDriverShift(UpdateDriverShiftRequest request) throws InvalidRequestException, DriverDoesNotExistException;
    RemoveFromCartResponse removeFromCart(RemoveFromCartRequest request) throws InvalidRequestException, CustomerDoesNotExistException;
}