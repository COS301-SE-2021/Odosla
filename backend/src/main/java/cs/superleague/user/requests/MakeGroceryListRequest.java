package cs.superleague.user.requests;

import java.util.List;

public class MakeGroceryListRequest {

    private final List<String> productIds;
    private final String name;


    public MakeGroceryListRequest(List<String> productIds, String name) { //if multiple items are parsed in
        this.productIds = productIds;
        this.name = name;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public String getName() {
        return name;
    }
}
