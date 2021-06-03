package payment;
import payment.exceptions.InvalidRequestException;
import payment.exceptions.OrderDoesNotExist;
import payment.exceptions.PaymentException;
import payment.requests.*;
import payment.responses.*;
public interface PaymentService {

    // ORDER



    SubmitOrderResponse submitOrder(SubmitOrderRequest request) throws PaymentException;
    CancelOrderResponse cancelOrder(CancelOrderRequest req) throws InvalidRequestException, OrderDoesNotExist;
    // TRANSACTION

    CreateTransactionResponse createTransaction(CreateTransactionRequest request);

    VerifyPaymentResponse verifyPayment(VerifyPaymentRequest request);

    ReverseTransactionResponse  reverseTransaction(ReverseTransactionRequest request);

    // INVOICE

    GenerateInvoiceResponse generateInvoice(GenerateInvoiceRequest request);

    GetInvoiceResponse getInvoice(GetInvoiceRequest request);


}