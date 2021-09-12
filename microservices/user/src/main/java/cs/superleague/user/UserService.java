package cs.superleague.user;

import cs.superleague.user.exceptions.*;
import cs.superleague.user.requests.*;
import cs.superleague.user.responses.*;
import cs.superleague.user.stubs.payment.exceptions.OrderDoesNotExist;
import cs.superleague.user.stubs.shopping.exceptions.StoreDoesNotExistException;

public interface UserService {
//    public RegisterResponse registerUser(RegisterUserRequest registerRequest);
//    public RegisterResponse registerAdminUser(RegisterUserRequest registerRequest);

    CompletePackagingOrderResponse completePackagingOrder(CompletePackagingOrderRequest request) throws InvalidRequestException, OrderDoesNotExist, cs.superleague.user.stubs.delivery.exceptions.InvalidRequestException;
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
    FinalisePasswordResetResponse finalisePasswordReset(FinalisePasswordResetRequest request) throws UserException;
    ResendActivationCodeResponse resendActivationCode(ResendActivationCodeRequest request) throws UserException;
    SetCurrentLocationResponse setCurrentLocation(SetCurrentLocationRequest request) throws UserException;
    GetCurrentUserResponse getCurrentUser(GetCurrentUserRequest request) throws InvalidRequestException;

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
    UpdateShopperShiftResponse updateShopperShift(UpdateShopperShiftRequest request) throws InvalidRequestException, ShopperDoesNotExistException, StoreDoesNotExistException, StoreDoesNotExistException;

    /* Analytics user data*/
    GetUsersResponse getUsers(GetUsersRequest request) throws Exception;

    GetGroceryListsResponse getGroceryLists(GetGroceryListsRequest request) throws UserException;
    GetCustomerByUUIDResponse getCustomerByUUID(GetCustomerByUUIDRequest request) throws UserException;
    DriverSetRatingResponse driverSetRating(DriverSetRatingRequest request) throws InvalidRequestException, DriverDoesNotExistException;

    GetCustomerByEmailResponse getCustomerByEmail(GetCustomerByEmailRequest request) throws InvalidRequestException, CustomerDoesNotExistException;
    GetShopperByEmailResponse getShopperByEmail(GetShopperByEmailRequest request) throws InvalidRequestException, ShopperDoesNotExistException;
    GetDriverByEmailResponse getDriverByEmail(GetDriverByEmailRequest request) throws InvalidRequestException, DriverDoesNotExistException;

    void saveDriver(SaveDriverRequest request) throws InvalidRequestException;
    void saveShopper(SaveShopperRequest request) throws InvalidRequestException;
}