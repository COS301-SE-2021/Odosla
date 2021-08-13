package cs.superleague.user.dataclass;

import cs.superleague.payment.dataclass.GeoPoint;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table
public class Driver extends User {

    /* Attributes */

    @Column(unique = true)
    @Id
    private UUID driverID;
    private double rating;
    private Boolean onShift=false;

    @OneToOne(cascade={CascadeType.ALL})
    private GeoPoint currentAddress;


    public Driver(String name, String surname, String email, String phoneNumber, String password, Date activationDate, String activationCode, String resetCode, String resetExpiration, boolean isActive, UserType accountType, UUID driverID) {
        super(name, surname, email, phoneNumber, password, activationDate, activationCode, resetCode, resetExpiration,accountType);
        this.driverID = driverID;
    }

    public Driver(String name, String surname, String email, String phoneNumber, String password, String activationCode, UserType accountType, UUID driverID) {
        super(name, surname, email, phoneNumber, password, activationCode, accountType);
        this.driverID = driverID;
    }

    public Driver() {}

    public UUID getDriverID() {
        return driverID;
    }

    public void setDriverID(UUID driverID) {
        this.driverID = driverID;
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

    public Boolean getOnShift() {
        return onShift;
    }

    public void setOnShift(Boolean onShift) {
        this.onShift = onShift;
    }
}