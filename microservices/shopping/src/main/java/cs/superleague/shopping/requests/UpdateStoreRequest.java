package cs.superleague.shopping.requests;

import cs.superleague.shopping.dataclass.Store;

public class UpdateStoreRequest {

    private Store store;

    public UpdateStoreRequest(Store store) {
        this.store = store;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

}
