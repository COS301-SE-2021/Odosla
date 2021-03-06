package cs.superleague.shopping.responses;

import java.util.UUID;

public class UpdateStoreResponse {

    private boolean response;
    private String message;
    private UUID storeID;

    public UpdateStoreResponse(boolean response, String message, UUID storeID) {
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

    public void setStoreID(UUID storeID) {
        this.storeID = storeID;
    }
}
