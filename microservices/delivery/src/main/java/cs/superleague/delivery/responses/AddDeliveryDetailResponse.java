package cs.superleague.delivery.responses;

public class AddDeliveryDetailResponse {
    private String message;
    private int id;

    public AddDeliveryDetailResponse(String message, int id) {
        this.message = message;
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
