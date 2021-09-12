package cs.superleague.importer.stub.shopping.requests;

import cs.superleague.importer.stub.shopping.dataclass.Store;

public class SaveStoreToRepoRequest {

    private final Store store;

    public SaveStoreToRepoRequest(){
        store = null;
    }

    public SaveStoreToRepoRequest(Store store) {
        this.store = store;
    }

    public Store getStore() {
        return store;
    }
}
