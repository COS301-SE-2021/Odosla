package cs.superleague.delivery.responses;

import cs.superleague.delivery.dataclass.Delivery;


public class GetNextOrderForDriverResponse {
    private String message;
    private Delivery delivery;

    public GetNextOrderForDriverResponse(String message, Delivery delivery) {
        this.message = message;
        this.delivery = delivery;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDeliveryID(Delivery delivery) {
        this.delivery = delivery;
    }
}
