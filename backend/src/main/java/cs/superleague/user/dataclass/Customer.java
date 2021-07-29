package cs.superleague.user.dataclass;

import cs.superleague.payment.dataclass.GeoPoint;

import javax.persistence.*;
import java.util.Calendar;
import java.util.UUID;


@Entity
@Table
public class Customer extends User {

    @Id
    private UUID customerID;

    @OneToOne(cascade={CascadeType.ALL})
    private GeoPoint address;

    public Customer(String name, String surname, String username, String email, String phoneNumber, String password, Calendar activationDate, String activationCode, String resetCode, String resetExpiration, boolean isActive, UserType accountType, UUID customerID, GeoPoint address) {
        super(name, surname, username, email, phoneNumber, password, activationDate, activationCode, resetCode, resetExpiration, isActive, accountType);
        this.customerID = customerID;
        this.address = address;
    }

    public Customer(UUID customerID, GeoPoint address) {
        this.customerID = customerID;
        this.address = address;
    }

    public Customer() {}

    public GeoPoint getAddress() {
        return address;
    }

    public void setAddress(GeoPoint address) {
        this.address = address;
    }
}