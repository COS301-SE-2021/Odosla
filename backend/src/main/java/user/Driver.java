package user;

public class Driver extends User{
    /* Attributes */
    private double rating;
    private GeoPoint currentAddress;

    public Driver(double rating, GeoPoint currentAddress) {
        this.rating = rating;
        this.currentAddress = currentAddress;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public GeoPoint getCurrentAddress() {
        return currentAddress;
    }

    public void setCurrentAddress(GeoPoint currentAddress) {
        this.currentAddress = currentAddress;
    }
}