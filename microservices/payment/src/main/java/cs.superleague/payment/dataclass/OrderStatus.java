package cs.superleague.payment.dataclass;

import java.io.Serializable;

public enum OrderStatus implements Serializable {
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
