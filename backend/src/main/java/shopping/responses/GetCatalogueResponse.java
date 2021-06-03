package shopping.responses;

import shopping.dataclass.Catalogue;

import java.util.Date;

public class GetCatalogueResponse {

    private final Catalogue catalogue;
    private final String message;
    private final Date timestamp;

    public GetCatalogueResponse(Catalogue catalogue, Date timestamp, String message)
    {
        this.catalogue= catalogue;
        this.message = message;
        this.timestamp = timestamp;
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
}
