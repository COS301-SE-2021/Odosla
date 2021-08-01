package cs.superleague.user.dataclass;

import cs.superleague.payment.dataclass.GeoPoint;

import javax.persistence.*;
import java.util.List;

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