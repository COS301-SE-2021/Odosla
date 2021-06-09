package cs.superleague.payment;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.exceptions.OrderDoesNotExist;
import cs.superleague.payment.exceptions.PaymentException;
import cs.superleague.payment.requests.*;
import cs.superleague.payment.responses.*;
import cs.superleague.shopping.exceptions.StoreClosedException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;

public interface PaymentService {

    // ORDER

    SubmitOrderResponse submitOrder(SubmitOrderRequest request) throws PaymentException, cs.superleague.shopping.exceptions.InvalidRequestException, StoreDoesNotExistException, StoreClosedException;

    CancelOrderResponse cancelOrder(CancelOrderRequest req) throws InvalidRequestException, OrderDoesNotExist;

    UpdateOrderResponse updateOrder(UpdateOrderRequest request) throws PaymentException;

    GetOrderResponse getOrder(GetOrderRequest req) throws PaymentException;
    // TRANSACTION

    CreateTransactionResponse createTransaction(CreateTransactionRequest request);

    VerifyPaymentResponse verifyPayment(VerifyPaymentRequest request);

    ReverseTransactionResponse  reverseTransaction(ReverseTransactionRequest request);


    // INVOICE

    GenerateInvoiceResponse generateInvoice(GenerateInvoiceRequest request);

    GetInvoiceResponse getInvoice(GetInvoiceRequest request);

}