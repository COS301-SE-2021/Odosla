package shopping.requests;

import java.util.UUID;

public class AddShopperRequest {

    /* attributes */
    private UUID shopperID;
    private UUID storeID;

    /** constructor
     *
     * @param shopperID the shoppersID to add the the Store
     * @param storeID the store ID of the store the shopper should be added to
     */
    public AddShopperRequest(UUID shopperID,UUID storeID) {
        this.shopperID = shopperID;
        this.storeID=storeID;
    }

    /** getters */
    public UUID getShopperID() {
        return shopperID;
    }

    public UUID getStoreID() {
        return storeID;
    }
}
