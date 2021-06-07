package user.dataclass;

import user.UserType;
import user.dataclass.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Calendar;
import java.util.UUID;

@Entity
@Table
public class Shopper extends User {
    /* Attributes */
    @Id
    private UUID userID;
    private UUID shopID;
    private double rating;
    //private Shop shop;


    public Shopper() {

    }

    public Shopper(String name, String surname, String username, UUID id, String email, String phoneNumber, String password, Calendar activationDate, String activationCode, String resetCode, String resetExpiration, boolean isActive, UserType accountType, UUID userID, UUID shopID, double rating) {
        super(name, surname, username, id, email, phoneNumber, password, activationDate, activationCode, resetCode, resetExpiration, isActive, accountType);
        this.userID = userID;
        this.shopID = shopID;
        this.rating = rating;
    }

    public Shopper(UUID userID) {
        this.userID = userID;
    }

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

    public UUID getUserID() {
        return userID;
    }

    public void setUserID(UUID userID) {
        this.userID = userID;
    }

    public UUID getShopID() {
        return shopID;
    }

    public void setShopID(UUID shopID) {
        this.shopID = shopID;
    }
}