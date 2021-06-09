package cs.superleague.user.dataclass;

import cs.superleague.payment.dataclass.GeoPoint;

public class Customer extends User {

    private GeoPoint address;

    public Customer(GeoPoint address) {
        super();
        this.address = address;
    }

    public GeoPoint getAddress() {
        return address;
    }

    public void setAddress(GeoPoint address) {
        this.address = address;
    }
}