package payment;
import payment.exceptions.InvalidRequestException;
import payment.exceptions.NotAuthorisedException;
import payment.exceptions.OrderDoesNotExist;
import payment.exceptions.PaymentException;
import payment.requests.*;
import payment.responses.*;
import shopping.exceptions.StoreClosedException;
import shopping.exceptions.StoreDoesNotExistException;

public interface PaymentService {

    // ORDER

    SubmitOrderResponse submitOrder(SubmitOrderRequest request) throws PaymentException, shopping.exceptions.InvalidRequestException, StoreDoesNotExistException, StoreClosedException;

    CancelOrderResponse cancelOrder(CancelOrderRequest req) throws InvalidRequestException, OrderDoesNotExist;

    UpdateOrderResponse updateOrder(UpdateOrderRequest request) throws InvalidRequestException, OrderDoesNotExist, NotAuthorisedException;
    // TRANSACTION

    CreateTransactionResponse createTransaction(CreateTransactionRequest request);

    VerifyPaymentResponse verifyPayment(VerifyPaymentRequest request);

    ReverseTransactionResponse  reverseTransaction(ReverseTransactionRequest request);


    // INVOICE

    GenerateInvoiceResponse generateInvoice(GenerateInvoiceRequest request);

    GetInvoiceResponse getInvoice(GetInvoiceRequest request);

}