package cs.superleague.shopping.requests;

import cs.superleague.shopping.dataclass.Catalogue;

import java.util.UUID;

public class UpdateCatalogueRequest {
    private UUID storeID;
    private Catalogue catalogue;

    public UpdateCatalogueRequest(UUID storeID, Catalogue catalogue) {
        this.storeID = storeID;
        this.catalogue = catalogue;
    }

    public UUID getStoreID() {
        return storeID;
    }

    public void setStoreID(UUID storeID) {
        this.storeID = storeID;
    }

    public Catalogue getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(Catalogue catalogue) {
        this.catalogue = catalogue;
    }
}
