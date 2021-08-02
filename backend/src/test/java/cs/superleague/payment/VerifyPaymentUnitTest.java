package cs.superleague.payment;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.exceptions.NotAuthorisedException;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.requests.UpdateOrderRequest;
import cs.superleague.payment.responses.UpdateOrderResponse;
import cs.superleague.shopping.dataclass.Item;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Mockito;
import org.springframework.context.annotation.Description;
import org.mockito.InjectMocks;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class VerifyPaymentUnitTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;
    
}
