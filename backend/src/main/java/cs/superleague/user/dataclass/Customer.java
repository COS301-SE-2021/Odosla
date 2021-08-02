package cs.superleague.user.dataclass;

import cs.superleague.payment.dataclass.GeoPoint;

import javax.persistence.*;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Entity
@Table
public class Customer extends User {

    @OneToOne(cascade={CascadeType.ALL})
    private GeoPoint address;

    @OneToMany
    private List<GroceryList> groceryLists;

    public Customer(){

    }

    public Customer(GeoPoint address, List<GroceryList> groceryLists) {
        this.address = address;
        this.groceryLists = groceryLists;
    }

    public Customer(String name, String surname, String username, UUID id, String email, String phoneNumber, String password, Calendar activationDate, String activationCode, String resetCode, String resetExpiration, boolean isActive, UserType accountType, GeoPoint address, List<GroceryList> groceryLists) {
        super(name, surname, username, id, email, phoneNumber, password, activationDate, activationCode, resetCode, resetExpiration, isActive, accountType);
        this.address = address;
        this.groceryLists = groceryLists;
    }

    public GeoPoint getAddress() {
        return address;
    }

    public void setAddress(GeoPoint address) {
        this.address = address;
    }

    public List<GroceryList> getGroceryLists() {
        return groceryLists;
    }
}