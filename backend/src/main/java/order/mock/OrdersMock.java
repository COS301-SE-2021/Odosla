package order.mock;

import order.dataclass.Item;
import order.dataclass.Order;

import java.util.List;

public class OrdersMock {

    private List<Item> items;
    private List<Order> orders;

    public List<Order> getOrders() {
        // will add mock orders here
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
