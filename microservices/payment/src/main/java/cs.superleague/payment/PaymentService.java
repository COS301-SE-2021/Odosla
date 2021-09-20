package cs.superleague.payment;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.exceptions.NotAuthorisedException;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.exceptions.PaymentException;
import cs.superleague.payment.requests.*;
import cs.superleague.payment.responses.*;

import java.net.URISyntaxException;

public interface PaymentService {

    // ORDER

    SubmitOrderResponse submitOrder(SubmitOrderRequest request) throws PaymentException, InterruptedException, URISyntaxException;

    CancelOrderResponse cancelOrder(CancelOrderRequest req) throws InvalidRequestException, OrderDoesNotExist, NotAuthorisedException, URISyntaxException;

    UpdateOrderResponse updateOrder(UpdateOrderRequest request) throws PaymentException, URISyntaxException;

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

    GetCustomersActiveOrdersResponse getCustomersActiveOrders(GetCustomersActiveOrdersRequest request) throws InvalidRequestException, OrderDoesNotExist, URISyntaxException;

    void saveOrderToRepo(SaveOrderToRepoRequest request) throws InvalidRequestException;

    GetAllCartItemsResponse getAllCartItems(GetAllCartItemsRequest request) throws InvalidRequestException;

    GetOrderByUUIDResponse getOrderByUUID(GetOrderByUUIDRequest request) throws InvalidRequestException;
}