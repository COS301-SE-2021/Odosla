package shopping.responses;

import user.dataclass.Shopper;

import java.util.Date;
import java.util.List;

public class GetShoppersResponse {

    /** attributes */
    private final List<Shopper> listOfShoppers;
    private final boolean success;
    private final Date timestamp;
    private final String message;

    /** constructor
     *
     * @param listOfShoppers - list of shoppers from the store
     * @param success - whether the response was successfully created or not
     * @param timestamp - the time the response was created
     * @param message - message corresponding to the response object
     */
    public GetShoppersResponse(List<Shopper> listOfShoppers, boolean success, Date timestamp, String message) {
        this.listOfShoppers = listOfShoppers;
        this.success = success;
        this.timestamp = timestamp;
        this.message = message;
    }


    /** getter */
    public List<Shopper> getListOfShoppers() {
        return listOfShoppers;
    }

    public boolean isSuccess() {
        return success;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }
}
