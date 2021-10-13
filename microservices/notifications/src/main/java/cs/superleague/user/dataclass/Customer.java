package cs.superleague.user.dataclass;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.shopping.dataclass.Item;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table
public class Customer extends User {

    @Id
    private UUID customerID;

    @OneToOne(cascade = {CascadeType.ALL})
    private GeoPoint address;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<GroceryList> groceryLists;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Item> shoppingCart;

    @OneToOne(cascade = {CascadeType.ALL})
    private Preference preference;

    @OneToOne(cascade = {CascadeType.ALL})
    private Wallet wallet;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;

    public Customer() {

    }

    public Customer(String name, String surname, String email, String phoneNumber, String password, Date activationDate, String activationCode, String resetCode, String resetExpiration, boolean isActive, UserType accountType, UUID customerID, GeoPoint address, List<GroceryList> groceryLists, List<Item> shoppingCart, Preference preference, Wallet wallet) {
        super(name, surname, email, phoneNumber, password, activationDate, activationCode, resetCode, resetExpiration, accountType);
        this.customerID = customerID;
        this.address = address;
        this.groceryLists = groceryLists;
        this.shoppingCart = shoppingCart;
        this.preference = preference;
        this.wallet = wallet;
        this.email = email;

    }

    public Customer(String name, String surname, String email, String phoneNumber, String password, Date activationDate, String activationCode, String resetCode, String resetExpiration, boolean isActive, UserType accountType, UUID customerID) {
        super(name, surname, email, phoneNumber, password, activationDate, activationCode, resetCode, resetExpiration, accountType);
        this.customerID = customerID;
        this.email = email;
    }

    public Customer(String name, String surname, String email, String phoneNumber, String password, String activationCode, UserType accountType, UUID customerID) {
        super(name, surname, email, phoneNumber, password, activationCode, accountType);
        this.customerID = customerID;
        this.email = email;
    }

    public Customer(String name, String surname, String email, String phoneNumber, String password, String activationCode, UserType accountType, UUID customerID, GeoPoint address) {
        super(name, surname, email, phoneNumber, password, activationCode, accountType);
        this.customerID = customerID;
        this.address = address;
        this.email = email;
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

    public UUID getCustomerID() {
        return customerID;
    }

    public void setCustomerID(UUID customerID) {
        this.customerID = customerID;
    }

    public void setGroceryLists(List<GroceryList> groceryLists) {
        this.groceryLists = groceryLists;
    }

    public void setShoppingCart(List<Item> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }

    public void setPreference(Preference preference) {
        this.preference = preference;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Preference getPreference() {
        return preference;
    }

    public Wallet getWallet() {
        return wallet;
    }

}