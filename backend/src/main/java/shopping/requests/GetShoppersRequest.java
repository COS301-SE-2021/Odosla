package shopping.requests;

import java.util.UUID;

public class GetShoppersRequest {

    /**attributes */
    private UUID storeID;

    /** constructor
     *
     * @param storeID the store ID of the relevant store to fetch shoppers from
     */
    public GetShoppersRequest(UUID storeID) {
        this.storeID = storeID;
    }

    /*getter */
    public UUID getStoreID() {
        return storeID;
    }

}
