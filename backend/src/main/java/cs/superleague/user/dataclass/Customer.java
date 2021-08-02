package cs.superleague.user.dataclass;

import cs.superleague.payment.dataclass.GeoPoint;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


@Entity
@Table
public class Customer extends User {

    @Id
    private UUID customerID;

    @OneToOne(cascade={CascadeType.ALL})
    private GeoPoint address;


    public Customer(String name, String surname, String email, String phoneNumber, String password, Date activationDate, String activationCode, String resetCode, String resetExpiration, boolean isActive, UserType accountType, UUID customerID, GeoPoint address) {
        super(name, surname, email, phoneNumber, password, activationDate, activationCode, resetCode, resetExpiration, isActive, accountType);
        this.customerID = customerID;
        this.address = address;
    }

    public Customer(String name, String surname, String email, String phoneNumber, String password, Date activationDate, String activationCode, String resetCode, String resetExpiration, boolean isActive, UserType accountType, UUID customerID) {
        super(name, surname, email, phoneNumber, password, activationDate, activationCode, resetCode, resetExpiration, isActive, accountType);
        this.customerID = customerID;
    }

    public Customer(String name, String surname, String email, String phoneNumber, String password, String activationCode, UserType accountType, UUID customerID) {
        super(name, surname, email, phoneNumber, password, activationCode, accountType);
        this.customerID = customerID;
    }

    public Customer(String name, String surname, String email, String phoneNumber, String password, String activationCode, UserType accountType, UUID customerID, GeoPoint address) {
        super(name, surname, email, phoneNumber, password, activationCode, accountType);
        this.customerID = customerID;
        this.address = address;
    }

    public Customer() {}

    public UUID getCustomerID() {
        return customerID;
    }

    public void setCustomerID(UUID customerID) {
        this.customerID = customerID;
    }

    public GeoPoint getAddress() {
        return address;
    }

    public void setAddress(GeoPoint address) {
        this.address = address;
    }
}