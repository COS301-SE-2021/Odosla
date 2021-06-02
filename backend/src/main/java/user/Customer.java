package user;

import payment.dataclass.GeoPoint;
import user.dataclass.User;

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