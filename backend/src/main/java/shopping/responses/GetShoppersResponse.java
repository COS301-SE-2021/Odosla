package shopping.responses;

import user.dataclass.Shopper;

import java.util.List;

public class GetShoppersResponse {

    /** attributes */
    private final List<Shopper> listOfShoppers;

    /** constructor
     *
     * @param listOfShoppers (list of all shoppers currently in store)
     */
    public GetShoppersResponse(List<Shopper> listOfShoppers) {
        this.listOfShoppers = listOfShoppers;
    }

    /** getter */
    public List<Shopper> getListOfShoppers() {
        return listOfShoppers;
    }
}
