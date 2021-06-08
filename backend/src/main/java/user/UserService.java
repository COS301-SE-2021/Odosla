package user;

import user.requests.*;
import user.responses.*;

public interface UserService {
//    public RegisterResponse registerUser(RegisterUserRequest registerRequest);
//    public RegisterResponse registerAdminUser(RegisterUserRequest registerRequest);

    public ScanItemResponse scanItem(ScanItemRequest request);
    public CompletePackagingOrderResponse completePackagingOrder(CompletePackagingOrderRequest request);

}