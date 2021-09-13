package cs.superleague.shopping.stubs.user.requests;

import cs.superleague.shopping.stubs.user.dataclass.Shopper;

import java.io.Serializable;

public class SaveShopperRequest implements Serializable {
    private Shopper shopper;

    public SaveShopperRequest(Shopper shopper) {
        this.shopper = shopper;
    }

    public Shopper getShopper() {
        return shopper;
    }

    public void setShopper(Shopper shopper) {
        this.shopper = shopper;
    }
}
