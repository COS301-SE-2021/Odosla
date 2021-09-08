package cs.superleague.shopping.responses;

import cs.superleague.shopping.dataclass.Store;

import java.util.List;
import java.util.UUID;

public class GetStoresResponse {

    List<Store> stores;
    private boolean response;
    private String message;

    public GetStoresResponse(boolean response, String message,List<Store> stores)
    {
        this.response = response;
        this.message = message;
        this.stores = stores;
    }

    public boolean getResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Store> getStores() {
        return stores;
    }

    public void setStores(List<Store> stores) {
        this.stores = stores;
    }
}
