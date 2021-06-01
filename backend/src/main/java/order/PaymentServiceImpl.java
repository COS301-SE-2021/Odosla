package order;

import order.requests.*;
import order.responses.*;

public class PaymentServiceImpl implements PaymentService {

    // ORDER IMPLEMENTATION

    @Override
    public AddItemResponse addItem(AddItemRequest request) {
        return null;
    }

    @Override
    public RemoveItemResponse removeItem(RemoveItemRequest request) {
        return null;
    }

    @Override
    public SubmitOrderResponse submitOrder() {
        return null;
    }

    @Override
    public ResetOrderResponse resetOrder() {
        return null;
    }

    @Override
    public CancelOrderResponse cancelOrder() {
        return null;
    }

    // TRANSACTION IMPLEMENTATION

    @Override
    public CreateTransactionResponse createTransaction(CreateTransactionRequest request) {
        return null;
    }

    @Override
    public VerifyPaymentResponse verifyPayment(VerifyPaymentRequest request) {
        return null;
    }

    @Override
    public ReverseTransactionResponse reverseTransaction(ReverseTransactionRequest request) {
        return null;
    }

    // INVOICE IMPLEMENTATION

    @Override
    public GenerateInvoiceResponse generateInvoice(GenerateInvoiceRequest request) {
        return null;
    }

    @Override
    public GetInvoiceResponse getInvoice(GetInvoiceRequest request) {
        return null;
    }
}