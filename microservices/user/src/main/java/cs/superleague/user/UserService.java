package cs.superleague.user;

import cs.superleague.shopping.exceptions.ItemDoesNotExistException;
import cs.superleague.user.exceptions.*;
import cs.superleague.user.requests.*;
import cs.superleague.user.responses.*;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;

import java.net.URISyntaxException;

public interface UserService {
//    public RegisterResponse registerUser(RegisterUserRequest registerRequest);
//    public RegisterResponse registerAdminUser(RegisterUserRequest registerRequest);

    CompletePackagingOrderResponse completePackagingOrder(CompletePackagingOrderRequest request) throws InvalidRequestException, OrderDoesNotExist, cs.superleague.delivery.exceptions.InvalidRequestException, URISyntaxException;

    ScanItemResponse scanItem(ScanItemRequest request) throws InvalidRequestException, OrderDoesNotExist;

    RegisterCustomerResponse registerCustomer(RegisterCustomerRequest request) throws InvalidRequestException;

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
    MakeGroceryListResponse makeGroceryList(MakeGroceryListRequest request) throws InvalidRequestException, CustomerDoesNotExistException, URISyntaxException;

    GetShoppingCartResponse getShoppingCart(GetShoppingCartRequest request) throws InvalidRequestException, CustomerDoesNotExistException;

    //RegisterCustomerResponse registerCustomer(RegisterCustomerRequest request) throws InvalidRequestException, AlreadyExistsException;
    UpdateCustomerDetailsResponse updateCustomerDetails(UpdateCustomerDetailsRequest request) throws InvalidRequestException, CustomerDoesNotExistException;

    SetCartResponse setCart(SetCartRequest request) throws InvalidRequestException, CustomerDoesNotExistException;

    ClearShoppingCartResponse clearShoppingCart(ClearShoppingCartRequest request) throws InvalidRequestException, UserDoesNotExistException;

    CollectOrderResponse collectOrder(CollectOrderRequest request) throws OrderDoesNotExist, InvalidRequestException, URISyntaxException;

    CompleteDeliveryResponse completeDelivery(CompleteDeliveryRequest request) throws OrderDoesNotExist, InvalidRequestException, URISyntaxException;

    UpdateDriverShiftResponse updateDriverShift(UpdateDriverShiftRequest request) throws InvalidRequestException, DriverDoesNotExistException;

    RemoveFromCartResponse removeFromCart(RemoveFromCartRequest request) throws InvalidRequestException, CustomerDoesNotExistException;

    UpdateShopperShiftResponse updateShopperShift(UpdateShopperShiftRequest request) throws InvalidRequestException, ShopperDoesNotExistException, StoreDoesNotExistException, URISyntaxException;

    /* Analytics user data*/
    GetAdminsResponse getAdmins(GetAdminsRequest request) throws Exception;

    GetCustomersResponse getCustomers(GetCustomersRequest request) throws Exception;

    GetDriversResponse getDrivers(GetDriversRequest request) throws Exception;

    GetShoppersResponse getShoppers(GetShoppersRequest request) throws Exception;

    GetGroceryListsResponse getGroceryLists(GetGroceryListsRequest request) throws UserException;

    GetCustomerByUUIDResponse getCustomerByUUID(GetCustomerByUUIDRequest request) throws UserException;

    GetDriverByUUIDResponse getDriverByUUID(GetDriverByUUIDRequest request) throws UserException;

    GetAdminByUUIDResponse getAdminByUUID(GetAdminByUUIDRequest request) throws UserException;

    DriverSetRatingResponse driverSetRating(DriverSetRatingRequest request) throws InvalidRequestException, DriverDoesNotExistException;

    GetCustomerByEmailResponse getCustomerByEmail(GetCustomerByEmailRequest request) throws InvalidRequestException, CustomerDoesNotExistException;

    GetShopperByEmailResponse getShopperByEmail(GetShopperByEmailRequest request) throws InvalidRequestException, ShopperDoesNotExistException;

    GetDriverByEmailResponse getDriverByEmail(GetDriverByEmailRequest request) throws InvalidRequestException, DriverDoesNotExistException;

    GetAdminByEmailResponse getAdminByEmail(GetAdminByEmailRequest request) throws InvalidRequestException, AdminDoesNotExistException;

    void saveDriver(SaveDriverToRepoRequest request) throws InvalidRequestException;

    void saveShopper(SaveShopperToRepoRequest request) throws InvalidRequestException;

    ItemNotAvailableResponse itemNotAvailable(ItemNotAvailableRequest request) throws InvalidRequestException, URISyntaxException, OrderDoesNotExist, ItemDoesNotExistException;

    GetProblemsWithOrderResponse getProblemsWithOrder(GetProblemsWithOrderRequest request) throws InvalidRequestException, OrderDoesNotExist, URISyntaxException, ItemDoesNotExistException;

    void removeProblemFromRepo(RemoveProblemFromRepoRequest request) throws InvalidRequestException;
}