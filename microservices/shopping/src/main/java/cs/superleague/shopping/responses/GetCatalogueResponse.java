package cs.superleague.shopping.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import cs.superleague.shopping.dataclass.Catalogue;

import java.util.Date;
import java.util.UUID;

public class GetCatalogueResponse {

    Catalogue catalogue;
    String message;
    @JsonFormat(pattern = "E MMM dd HH:mm:ss z yyyy")
    Date timestamp;
    UUID storeID;

    public GetCatalogueResponse(UUID storeID, Catalogue catalogue, Date timestamp, String message) {
        this.catalogue = catalogue;
        this.message = message;
        this.timestamp = timestamp;
        this.storeID = storeID;
    }

    /*
     * getCatalogue returns the catalogue object
     * */
    public Catalogue getCatalogue() {
        return catalogue;
    }

    /*
     * getMessage returns the message variable
     * */
    public String getMessage() {
        return message;
    }

    /*
     * getTimestamp returns the timestamp object
     * */
    public Date getTimestamp() {
        return timestamp;
    }

    public UUID getStoreID() {
        return storeID;
    }

    public void setStoreID(UUID storeID) {
        this.storeID = storeID;
    }

}
