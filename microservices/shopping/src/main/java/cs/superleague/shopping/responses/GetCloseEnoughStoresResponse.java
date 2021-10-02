package cs.superleague.shopping.responses;

import cs.superleague.shopping.dataclass.Store;

import java.util.List;

public class GetCloseEnoughStoresResponse {
    private List<Store> closeStores;
    private List<Double> additionalDeliveryCosts;
    private String message;

    public GetCloseEnoughStoresResponse(List<Store> closeStores, List<Double> additionalDeliveryCosts, String message) {
        this.closeStores = closeStores;
        this.additionalDeliveryCosts = additionalDeliveryCosts;
        this.message = message;
    }

    public List<Store> getCloseStores() {
        return closeStores;
    }

    public void setCloseStores(List<Store> closeStores) {
        this.closeStores = closeStores;
    }

    public List<Double> getAdditionalDeliveryCosts() {
        return additionalDeliveryCosts;
    }

    public void setAdditionalDeliveryCosts(List<Double> additionalDeliveryCosts) {
        this.additionalDeliveryCosts = additionalDeliveryCosts;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
