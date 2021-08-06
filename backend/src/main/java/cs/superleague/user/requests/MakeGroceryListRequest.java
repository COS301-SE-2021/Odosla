package cs.superleague.user.requests;

import java.util.List;
import java.util.UUID;

public class MakeGroceryListRequest {

    private final UUID userID;
    private final List<String> barcodes;
    private final String name;


    public MakeGroceryListRequest(UUID userID, List<String> barcodes, String name) { //if multiple items are parsed in
        this.userID = userID;
        this.barcodes = barcodes;
        this.name = name;
    }

    public UUID getUserID() {
        return userID;
    }

    public List<String> getBarcodes() {
        return barcodes;
    }

    public String getName() {
        return name;
    }
}
