package cs.superleague.user.requests;

import cs.superleague.user.dataclass.Shopper;

import java.io.Serializable;

public class SaveShopperToRepoRequest implements Serializable {
    private Shopper shopper;

    public SaveShopperToRepoRequest(Shopper shopper) {
        this.shopper = shopper;
    }

    public Shopper getShopper() {
        return shopper;
    }

    public void setShopper(Shopper shopper) {
        this.shopper = shopper;
    }
}
