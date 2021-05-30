package user.responses;

public class FinalizePasswordResetResponse {
    /* Attributes */
    private String message;

    /* Constructor */
    public FinalizePasswordResetResponse(String message) {
        this.message = message;
    }

    /* Getters and Setters */
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}