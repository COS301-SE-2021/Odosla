package cs.superleague.delivery.responses;

public class AssignDriverToDeliveryResponse {
    private boolean isAssigned;
    private String message;

    public AssignDriverToDeliveryResponse(boolean isAssigned, String message) {
        this.isAssigned = isAssigned;
        this.message = message;
    }

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setAssigned(boolean assigned) {
        isAssigned = assigned;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
