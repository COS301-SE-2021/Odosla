package cs.superleague.shopping.requests;

import cs.superleague.shopping.dataclass.Store;

import java.io.Serializable;

public class SaveStoreToRepoRequest implements Serializable {

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
