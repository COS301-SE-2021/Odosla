package cs.superleague.user.stubs.payment.dataclass;

public enum OrderStatus {
    AWAITING_PAYMENT,
    VERIFYING,
    PURCHASED,
    IN_QUEUE,
    PACKING,
    AWAITING_COLLECTION,
    ASSIGNED_DRIVER,
    DELIVERY_COLLECTED,
    CUSTOMER_COLLECTED,
    DELIVERED
}
