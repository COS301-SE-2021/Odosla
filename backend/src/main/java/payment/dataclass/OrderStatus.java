package payment.dataclass;

public enum OrderStatus {
    AWAITING_PAYMENT,
    PURCHASED,
    PACKING,
    AWAITING_COLLECTION,
    DELIVERY_COLLECTED,
    CUSTOMER_COLLECTED,
    DELIVERED
}
