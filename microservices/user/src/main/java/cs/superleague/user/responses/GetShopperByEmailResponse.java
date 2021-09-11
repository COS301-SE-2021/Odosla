package cs.superleague.user.responses;

import cs.superleague.user.dataclass.Shopper;

public class GetShopperByEmailResponse {
    final Shopper shopper;

    public GetShopperByEmailResponse(Shopper shopper) {
        this.shopper = shopper;
    }

    public Shopper getShopper() {
        return shopper;
    }
}
