package cs.superleague.user.requests;

import java.util.List;

public class MakeGroceryListRequest {

    private final String userID;
    private final List<String> productIds;
    private final String name;


    public MakeGroceryListRequest(String userID, List<String> barcodes, String name) { //if multiple items are parsed in
        this.userID = userID;
        this.productIds = barcodes;
        this.name = name;
    }

    public String getUserID() {
        return userID;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public String getName() {
        return name;
    }
}
