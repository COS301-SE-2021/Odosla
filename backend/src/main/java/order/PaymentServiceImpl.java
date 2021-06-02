package order;

import order.dataclass.Item;
import order.requests.*;
import order.responses.*;
import java.util.ArrayList;

import java.util.List;

public class PaymentServiceImpl implements PaymentService {

    // ORDER IMPLEMENTATION

    @Override
    public SubmitOrderResponse submitOrder() {
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