package order.dataclass;

public enum OrderStatus {
    AWAITING_PAYMENT,
    PURCHASED,
    IN_QUEUE,
    PACKING,
    AWAITING_COLLECTION,
    DELIVERY_COLLECTED,
    CUSTOMER_COLLECTED,
    DELIVERED
}
