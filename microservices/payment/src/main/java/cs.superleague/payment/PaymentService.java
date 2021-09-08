package cs.superleague.payment;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.exceptions.NotAuthorisedException;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.exceptions.PaymentException;
import cs.superleague.payment.requests.*;
import cs.superleague.payment.responses.*;
import cs.superleague.shopping.exceptions.StoreClosedException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.user.requests.GetUsersRequest;
import cs.superleague.user.responses.GetUsersResponse;
import cs.superleague.user.exceptions.UserDoesNotExistException;

public interface PaymentService {

    // ORDER

    SubmitOrderResponse submitOrder(SubmitOrderRequest request) throws PaymentException, cs.superleague.shopping.exceptions.InvalidRequestException, StoreDoesNotExistException, StoreClosedException, InterruptedException, cs.superleague.user.exceptions.InvalidRequestException;

    CancelOrderResponse cancelOrder(CancelOrderRequest req) throws InvalidRequestException, OrderDoesNotExist, NotAuthorisedException;

    UpdateOrderResponse updateOrder(UpdateOrderRequest request) throws PaymentException;

    GetOrderResponse getOrder(GetOrderRequest req) throws PaymentException;

    SetStatusResponse setStatus(SetStatusRequest request) throws PaymentException;

    GetStatusResponse getStatus(GetStatusRequest request) throws PaymentException;

    GetItemsResponse getItems(GetItemsRequest request) throws PaymentException;

    // TRANSACTION

    CreateTransactionResponse createTransaction(CreateTransactionRequest request) throws PaymentException, InterruptedException;

    VerifyPaymentResponse verifyPayment(VerifyPaymentRequest request) throws PaymentException, InterruptedException;

    ReverseTransactionResponse  reverseTransaction(ReverseTransactionRequest request);


    // INVOICE

    GenerateInvoiceResponse generateInvoice(GenerateInvoiceRequest request) throws InvalidRequestException;

    GetInvoiceResponse getInvoice(GetInvoiceRequest request) throws InvalidRequestException, NotAuthorisedException;

    GetOrdersResponse getOrders(GetOrdersRequest request) throws PaymentException;

    //CUSTOMER REQUESTS

    GetCustomersActiveOrdersResponse getCustomersActiveOrders(GetCustomersActiveOrdersRequest request) throws InvalidRequestException, OrderDoesNotExist, cs.superleague.user.exceptions.InvalidRequestException, UserDoesNotExistException;

}