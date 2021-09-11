package cs.superleague.shopping.requests;

import cs.superleague.shopping.dataclass.Store;

public class SaveStoreToRepoRequest {

    private Store store;

    public SaveStoreToRepoRequest(Store store)
    {
        this.store=store;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

}
