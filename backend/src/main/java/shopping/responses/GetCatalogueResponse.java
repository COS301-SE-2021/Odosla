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

    public Catalogue getCatalogue() {
        return catalogue;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
