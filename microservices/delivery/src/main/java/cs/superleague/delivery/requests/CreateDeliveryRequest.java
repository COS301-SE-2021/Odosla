package cs.superleague.delivery.requests;

import cs.superleague.payment.dataclass.GeoPoint;

import java.util.Calendar;
import java.util.UUID;

public class CreateDeliveryRequest {
    private UUID orderID;
    private UUID customerID;
    private UUID storeID;
    private Calendar timeOfDelivery;
    private GeoPoint placeOfDelivery;

    public CreateDeliveryRequest(UUID orderID, UUID customerID, UUID storeID, Calendar timeOfDelivery, GeoPoint placeOfDelivery) {
        this.orderID = orderID;
        this.customerID = customerID;
        this.storeID = storeID;
        this.timeOfDelivery = timeOfDelivery;
        this.placeOfDelivery = placeOfDelivery;
    }

    public GeoPoint getPlaceOfDelivery() {
        return placeOfDelivery;
    }

    public void setPlaceOfDelivery(GeoPoint placeOfDelivery) {
        this.placeOfDelivery = placeOfDelivery;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }

    public UUID getCustomerID() {
        return customerID;
    }

    public void setCustomerID(UUID customerID) {
        this.customerID = customerID;
    }

    public UUID getStoreID() {
        return storeID;
    }

    public void setStoreID(UUID storeID) {
        this.storeID = storeID;
    }

    public Calendar getTimeOfDelivery() {
        return timeOfDelivery;
    }

    public void setTimeOfDelivery(Calendar timeOfDelivery) {
        this.timeOfDelivery = timeOfDelivery;
    }
}
