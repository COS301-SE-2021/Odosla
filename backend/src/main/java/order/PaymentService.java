package order;
import order.requests.*;
import order.responses.*;
public interface PaymentService {

    // ORDER

    AddItemResponse addItem(AddItemRequest request);

    RemoveItemResponse removeItem(RemoveItemRequest request);

    SubmitOrderResponse submitOrder();

    ResetOrderResponse resetOrder();

    CancelOrderResponse cancelOrder(CancelOrderRequest req);

    // TRANSACTION

    CreateTransactionResponse createTransaction(CreateTransactionRequest request);

    VerifyPaymentResponse verifyPayment(VerifyPaymentRequest request);

    ReverseTransactionResponse  reverseTransaction(ReverseTransactionRequest request);

    // INVOICE

    GenerateInvoiceResponse generateInvoice(GenerateInvoiceRequest request);

    GetInvoiceResponse getInvoice(GetInvoiceRequest request);


}