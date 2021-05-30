package user;

public class Customer extends User{
    private GeoPoint address;

    public Customer(GeoPoint address) {
        this.address = address;
    }

    public GeoPoint getAddress() {
        return address;
    }

    public void setAddress(GeoPoint address) {
        this.address = address;
    }
}