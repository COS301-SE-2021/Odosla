package cs.superleague.payment.dataclass;

public enum OrderStatus {
    AWAITING_PAYMENT,
    VERIFYING,
    PURCHASED,
    IN_QUEUE,
    PACKING,
    PROBLEM,
    AWAITING_COLLECTION,
    ASSIGNED_DRIVER,
    DELIVERY_COLLECTED,
    CUSTOMER_COLLECTED,
    DELIVERED,
    CANCELLED
}
