package cs.superleague.shopping.requests;

import java.util.UUID;

public class ClearShoppersRequest {

    /** attributes */
    private UUID storeID;

    /** constructor
     *
     * @param storeID the store which the shoppers need to be cleared from
     */
    public ClearShoppersRequest(UUID storeID) {
        this.storeID = storeID;
    }

    /** getter */
    public UUID getStoreID() {
        return storeID;
    }
}
