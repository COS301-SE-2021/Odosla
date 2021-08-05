package cs.superleague.user.requests;

import cs.superleague.payment.dataclass.Order;

import java.util.UUID;

public class ScanItemRequest {
    private Order order;
    private String barcode;

    public ScanItemRequest(String barcode, Order order) {
        this.order = order;
        this.barcode = barcode;
    }

    public Order getOrder() {
        return order;
    }

    public String getBarcode() {
        return barcode;
    }
}
