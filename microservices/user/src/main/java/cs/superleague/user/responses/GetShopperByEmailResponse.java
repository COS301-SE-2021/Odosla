package cs.superleague.user.responses;

import cs.superleague.user.dataclass.Shopper;

public class GetShopperByEmailResponse {

    final Shopper shopper;
    private final boolean success;

    public GetShopperByEmailResponse(Shopper shopper, boolean success) {
        this.shopper = shopper;
        this.success = success;
    }

    public Shopper getShopper() {
        return shopper;
    }

    public boolean isSuccess() {
        return success;
    }
}
