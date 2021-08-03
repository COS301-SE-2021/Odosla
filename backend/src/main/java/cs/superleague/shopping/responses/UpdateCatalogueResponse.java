package cs.superleague.shopping.responses;

import java.util.UUID;

public class UpdateCatalogueResponse {
    private boolean response;
    private String message;
    private UUID storeID;

    public UpdateCatalogueResponse(boolean response, String message, UUID storeID) {
        this.response = response;
        this.message = message;
        this.storeID = storeID;
    }

    public boolean isResponse() {
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
