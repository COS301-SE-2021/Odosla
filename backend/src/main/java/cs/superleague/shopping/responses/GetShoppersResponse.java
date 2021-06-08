package cs.superleague.shopping.responses;

import cs.superleague.user.dataclass.Shopper;

import java.util.List;

public class GetShoppersResponse {

    private final List<Shopper> listOfShoppers;
    private boolean success;
    private String message;

    public GetShoppersResponse(List<Shopper> listOfShoppers) {

        this.listOfShoppers = listOfShoppers;
    }

    public List<Shopper> getListOfShoppers() {
        return listOfShoppers;
    }
}
