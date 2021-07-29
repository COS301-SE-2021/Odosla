package cs.superleague.user.dataclass;

import cs.superleague.payment.dataclass.GeoPoint;

import javax.persistence.*;
import java.util.Calendar;
import java.util.UUID;

@Entity
@Table
public class Driver extends User {

    /* Attributes */
    @Id
    private UUID driverID;
    private double rating;

    @OneToOne(cascade={CascadeType.ALL})
    private GeoPoint currentAddress;


    public Driver(String name, String surname, String username, String email, String phoneNumber, String password, Calendar activationDate, String activationCode, String resetCode, String resetExpiration, boolean isActive, UserType accountType, UUID driverID, double rating, GeoPoint currentAddress) {
        super(name, surname, username, email, phoneNumber, password, activationDate, activationCode, resetCode, resetExpiration, isActive, accountType);
        this.driverID = driverID;
        this.rating = rating;
        this.currentAddress = currentAddress;
    }

    public Driver(UUID driverID, double rating, GeoPoint currentAddress) {
        this.driverID = driverID;
        this.rating = rating;
        this.currentAddress = currentAddress;
    }

    public Driver() {

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