package cs.superleague.delivery.stub.user.dataclass;

import cs.superleague.delivery.stub.payment.dataclass.GeoPoint;

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

    @Column(name = "deliveryid")
    private UUID deliveryID;

    @Column(name = "total_ratings")
    private double totalRatings=0;

    @Column(name = "rating")
    private double rating;

    private Boolean onShift=false;
    private Boolean isCurrentlyDelivering=false;

    @Column(name = "deliveries_completed")
    private int deliveriesCompleted=0;

    @Column(name = "number_of_ratings")
    private int numberOfRatings=0;

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

    public Boolean getCurrentlyDelivering() {
        return isCurrentlyDelivering;
    }

    public void setCurrentlyDelivering(Boolean currentlyDelivering) {
        isCurrentlyDelivering = currentlyDelivering;
    }

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

    public UUID getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(UUID deliveryID) {
        this.deliveryID = deliveryID;
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

    public int getDeliveriesCompleted() {
        return deliveriesCompleted;
    }

    public void setDeliveriesCompleted(int deliveriesCompleted) {
        this.deliveriesCompleted = deliveriesCompleted;
    }

    public int getNumberOfRatings() {
        return numberOfRatings;
    }

    public void setNumberOfRatings(int numberOfRatings) {
        this.numberOfRatings = numberOfRatings;
    }

    public double getTotalRatings() {
        return totalRatings;
    }

    public void setTotalRatings(double totalRatings) {
        this.totalRatings = totalRatings;
    }

}