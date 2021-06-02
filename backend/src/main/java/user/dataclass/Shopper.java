package user.dataclass;

import user.dataclass.User;

public class Shopper extends User {
    /* Attributes */
    private double rating;
    //private Shop shop;

    public Shopper(double rating) {
        super();
        this.rating = rating;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}