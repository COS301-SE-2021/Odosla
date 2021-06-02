package payment.responses;

import payment.dataclass.OrderStatus;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class SubmitOrderResponse {
    /** attributes */
    private final UUID orderId;
    private final OrderStatus status;
    private final Date timestamp;
    private final String message;

    /** CONSTRUCTOR
     * @param orderId - unique order identifier
     * @param status - status of order throughout it's lifetime
     * @param timestamp - time that the system sends the response object
     * @param message - message returned after response
     */
    public SubmitOrderResponse(UUID orderId, OrderStatus status, Date timestamp, String message) {
        this.orderId = orderId;
        this.status = status;
        this.timestamp = timestamp;
        this.message = message;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
