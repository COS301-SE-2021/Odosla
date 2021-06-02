package payment;
import payment.exceptions.InvalidRequestException;
import payment.requests.*;
import payment.responses.*;
public interface PaymentService {

    // ORDER



    SubmitOrderResponse submitOrder(SubmitOrderRequest request) throws InvalidRequestException;
    CancelOrderResponse cancelOrder(CancelOrderRequest req);
    // TRANSACTION

    CreateTransactionResponse createTransaction(CreateTransactionRequest request);

    VerifyPaymentResponse verifyPayment(VerifyPaymentRequest request);

    ReverseTransactionResponse  reverseTransaction(ReverseTransactionRequest request);

    // INVOICE

    GenerateInvoiceResponse generateInvoice(GenerateInvoiceRequest request);

    GetInvoiceResponse getInvoice(GetInvoiceRequest request);


}