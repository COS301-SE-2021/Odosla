package user.dataclass;

import user.dataclass.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table
public class Shopper extends User {
    /* Attributes */
    @Id
    private UUID userID;
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