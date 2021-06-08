package user;

import user.exceptions.InvalidRequestException;
import user.requests.*;
import user.responses.*;

public interface UserService {
//    public RegisterResponse registerUser(RegisterUserRequest registerRequest);
//    public RegisterResponse registerAdminUser(RegisterUserRequest registerRequest);

    public CompletePackagingOrderResponse completePackagingOrder(CompletePackagingOrderRequest request) throws InvalidRequestException;
    public ScanItemResponse scanItem(ScanItemRequest request);


}