package cs.superleague.user.dataclass;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.shopping.dataclass.Item;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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

    public Customer(String name, String surname, String username, UUID id, String email, String phoneNumber, String password, Calendar activationDate, String activationCode, String resetCode, String resetExpiration, boolean isActive, UserType accountType, GeoPoint address, List<GroceryList> groceryLists, List<Item> shoppingCart, Preference preference, Wallet wallet) {
        super(name, surname, username, id, email, phoneNumber, password, activationDate, activationCode, resetCode, resetExpiration, isActive, accountType);
        this.address = address;
        this.groceryLists = groceryLists;
        this.shoppingCart = shoppingCart;
        this.preference = preference;
        this.wallet = wallet;
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