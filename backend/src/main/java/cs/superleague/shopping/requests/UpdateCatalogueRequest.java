package cs.superleague.shopping.requests;

import cs.superleague.shopping.dataclass.Catalogue;

public class UpdateCatalogueRequest {
    private Catalogue catalogue;

    public UpdateCatalogueRequest(Catalogue catalogue) {
        this.catalogue = catalogue;
    }

    public Catalogue getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(Catalogue catalogue) {
        this.catalogue = catalogue;
    }
}
