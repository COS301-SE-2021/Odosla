package cs.superleague.delivery.responses;

import cs.superleague.shopping.dataclass.Store;

import java.util.List;

public class GetAdditionalStoresDeliveryCostResponse {
    private List<Store> stores;
    private List<Double> additionalCost;
    private String message;


    public GetAdditionalStoresDeliveryCostResponse(List<Store> stores, List<Double> additionalCost, String message) {
        this.stores = stores;
        this.additionalCost = additionalCost;
        this.message = message;
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

    public List<Double> getAdditionalCost() {
        return additionalCost;
    }

    public void setAdditionalCost(List<Double> additionalCost) {
        this.additionalCost = additionalCost;
    }
}
