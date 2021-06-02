package payment;
import payment.requests.*;
import payment.responses.*;
public interface PaymentService {

    // ORDER

    SubmitOrderResponse submitOrder();

    CancelOrderResponse cancelOrder();

    // TRANSACTION

    CreateTransactionResponse createTransaction(CreateTransactionRequest request);

    VerifyPaymentResponse verifyPayment(VerifyPaymentRequest request);

    ReverseTransactionResponse  reverseTransaction(ReverseTransactionRequest request);

    // INVOICE

    GenerateInvoiceResponse generateInvoice(GenerateInvoiceRequest request);

    GetInvoiceResponse getInvoice(GetInvoiceRequest request);


}