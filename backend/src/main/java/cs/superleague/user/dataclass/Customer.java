package cs.superleague.user.dataclass;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.shopping.dataclass.Item;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.List;

@Entity
@Table
public class Customer extends User {

    @Id
    private UUID customerID;

    @OneToOne(cascade={CascadeType.ALL})
    private GeoPoint address;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<GroceryList> groceryLists;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Item> shoppingCart;

    @OneToOne (cascade={CascadeType.ALL})
    private Preference preference;

    @OneToOne (cascade={CascadeType.ALL})
    private Wallet wallet;

    public Customer(){

    }

    public Customer(String name, String surname, String email, String phoneNumber, String password, Date activationDate, String activationCode, String resetCode, String resetExpiration, boolean isActive, UserType accountType, UUID customerID, GeoPoint address, List<GroceryList> groceryLists, List<Item> shoppingCart, Preference preference, Wallet wallet) {
        super(name, surname, email, phoneNumber, password, activationDate, activationCode, resetCode, resetExpiration, isActive, accountType);
        this.customerID = customerID;
        this.address = address;
        this.groceryLists = groceryLists;
        this.shoppingCart = shoppingCart;
        this.preference = preference;
        this.wallet = wallet;
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

    public GeoPoint getAddress() {
        return address;
    }

    public void setAddress(GeoPoint address) {
        this.address = address;
    }

    public List<GroceryList> getGroceryLists() {
        return groceryLists;
    }

    public List<Item> getShoppingCart() {
        return shoppingCart;
    }
}