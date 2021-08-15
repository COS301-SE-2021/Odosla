package cs.superleague.user.requests;

import java.util.List;

public class MakeGroceryListRequest {

    private final String JWTToken;
    private final List<String> productIds;
    private final String name;


    public MakeGroceryListRequest(String JWTToken, List<String> productIds, String name) { //if multiple items are parsed in
        this.JWTToken = JWTToken;
        this.productIds = productIds;
        this.name = name;
    }

    public String getJWTToken() {
        return JWTToken;
    }

    public List<String> getProductIds() {
        return productIds;
    }

    public String getName() {
        return name;
    }
}
