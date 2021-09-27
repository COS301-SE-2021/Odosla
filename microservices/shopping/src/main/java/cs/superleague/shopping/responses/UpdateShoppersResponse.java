package cs.superleague.shopping.responses;

import java.util.UUID;

public class UpdateShoppersResponse {

    private boolean response;
    private String message;
    private final UUID storeID;

    public UpdateShoppersResponse(boolean response, String message, UUID storeID) {
        this.response = response;
        this.message = message;
        this.storeID = storeID;
    }

    public boolean getResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UUID getStoreID() {
        return storeID;
    }
}
