package cs.superleague.user;

import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.requests.*;
import cs.superleague.user.responses.*;

public interface UserService {
//    public RegisterResponse registerUser(RegisterUserRequest registerRequest);
//    public RegisterResponse registerAdminUser(RegisterUserRequest registerRequest);

    public CompletePackagingOrderResponse completePackagingOrder(CompletePackagingOrderRequest request) throws InvalidRequestException;
    public ScanItemResponse scanItem(ScanItemRequest request);
    public RegisterCustomerResponse registerCustomer (RegisterCustomerRequest request);
    public RegisterDriverResponse registerDriver(RegisterDriverRequest request) throws InvalidRequestException;
    public RegisterShopperResponse registerShopper(RegisterShopperRequest request);
    public RegisterAdminResponse registerAdmin(RegisterAdminRequest request);
    public LoginResponse loginUser(LoginRequest request);



}