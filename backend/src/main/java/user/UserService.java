package user;

import user.requests.*;
import user.responses.*;

public interface UserService {
//    public RegisterResponse registerUser(RegisterUserRequest registerRequest);
//    public RegisterResponse registerAdminUser(RegisterUserRequest registerRequest);

    public CompletePackagingOrderResponse completePackagingOrder(CompletePackagingOrderRequest request);
    public ScanItemResponse scanItem(ScanItemRequest request);


}