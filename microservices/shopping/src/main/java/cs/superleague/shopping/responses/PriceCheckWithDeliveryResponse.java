package cs.superleague.shopping.responses;

import cs.superleague.payment.dataclass.CartItem;

import java.util.List;
import java.util.UUID;

public class PriceCheckWithDeliveryResponse {
    private List<CartItem> cheaperItems;
    private UUID storeOneID;
    private UUID storeTwoID;
    private UUID storeThreeID;
    private boolean newOrder;

    public PriceCheckWithDeliveryResponse(List<CartItem> cheaperItems, UUID storeOneID, UUID storeTwoID, UUID storeThreeID, boolean newOrder) {
        this.cheaperItems = cheaperItems;
        this.storeOneID = storeOneID;
        this.storeTwoID = storeTwoID;
        this.storeThreeID = storeThreeID;
        this.newOrder = newOrder;
    }

    public boolean isNewOrder() {
        return newOrder;
    }

    public void setNewOrder(boolean newOrder) {
        this.newOrder = newOrder;
    }

    public List<CartItem> getCheaperItems() {
        return cheaperItems;
    }

    public void setCheaperItems(List<CartItem> cheaperItems) {
        this.cheaperItems = cheaperItems;
    }

    public UUID getStoreOneID() {
        return storeOneID;
    }

    public void setStoreOneID(UUID storeOneID) {
        this.storeOneID = storeOneID;
    }

    public UUID getStoreTwoID() {
        return storeTwoID;
    }

    public void setStoreTwoID(UUID storeTwoID) {
        this.storeTwoID = storeTwoID;
    }

    public UUID getStoreThreeID() {
        return storeThreeID;
    }

    public void setStoreThreeID(UUID storeThreeID) {
        this.storeThreeID = storeThreeID;
    }
}
