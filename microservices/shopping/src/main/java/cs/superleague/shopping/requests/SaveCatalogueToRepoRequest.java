package cs.superleague.shopping.requests;

import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Item;

public class SaveCatalogueToRepoRequest {

    private Catalogue catalogue;

    public SaveCatalogueToRepoRequest(Catalogue catalogue){
        this.catalogue = catalogue;
    }

    public Catalogue getCatalogue() {
        return catalogue;
    }

    public void setCatalogue(Catalogue catalogue) {
        this.catalogue = catalogue;
    }

}
