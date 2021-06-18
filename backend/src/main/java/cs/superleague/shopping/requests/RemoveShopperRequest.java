package cs.superleague.shopping.requests;

import java.util.UUID;

public class RemoveShopperRequest {

    /* attributes */
    private UUID shopperID;
    private UUID storeID;

    /** constructor
     *
     * @param shopperID the shopper that needs to be removed from Store
     * @param storeID the store which the shopper need to be removed from
     */
    public RemoveShopperRequest(UUID shopperID, UUID storeID) {
        this.shopperID = shopperID;
        this.storeID = storeID;
    }

    /* getters */
    public UUID getShopperID() {
        return shopperID;
    }

    public UUID getStoreID() {
        return storeID;
    }
}
