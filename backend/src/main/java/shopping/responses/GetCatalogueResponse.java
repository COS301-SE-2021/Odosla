package shopping.responses;

import shopping.dataclass.Catalogue;

public class GetCatalogueResponse {

    Catalogue catalogue;

    public GetCatalogueResponse(Catalogue catalogue)
    {
        this.catalogue= catalogue;
    }

    public Catalogue getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(Catalogue catalogue) {
        this.catalogue = catalogue;
    }


}
