package cs.superleague.shopping.requests;

import cs.superleague.shopping.dataclass.Store;
import cs.superleague.user.dataclass.Shopper;

import java.util.List;

public class UpdateShoppersRequest {

    Store store;
    List<Shopper> newShoppers;

    public void UpdateShoppersRequest(Store store, List<Shopper> newShoppers)
    {
        this.store=store;
        this.newShoppers= newShoppers;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public List<Shopper> getNewShoppers() {
        return newShoppers;
    }

    public void setNewShoppers(List<Shopper> newShoppers) {
        this.newShoppers = newShoppers;
    }

}
