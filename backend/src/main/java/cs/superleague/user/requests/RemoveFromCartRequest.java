package cs.superleague.user.requests;

import cs.superleague.shopping.dataclass.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RemoveFromCartRequest {

    private final UUID customerID;
    private final List<Item> items;

    public RemoveFromCartRequest(UUID customerID, List<Item> items) {
        this.customerID = customerID;
        this.items = items;
    }

    public RemoveFromCartRequest(UUID customerID, Item item) {

        List<Item> items = new ArrayList<>();
        items.add(item);

        this.customerID = customerID;
        this.items = items;
    }

    public UUID getCustomerID() {
        return customerID;
    }

    public List<Item> getItems() {
        return items;
    }
}
