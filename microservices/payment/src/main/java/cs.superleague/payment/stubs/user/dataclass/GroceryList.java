package cs.superleague.payment.stubs.user.dataclass;

import cs.superleague.payment.stubs.shopping.dataclass.Item;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table
public class GroceryList {

    @Id
    private final UUID groceryListID;
    private final String name;

    @ManyToMany
    @JoinTable
    @LazyCollection(LazyCollectionOption.FALSE)
    private final List<Item> items;

    public GroceryList(UUID groceryListID, String name, List<Item> items) {
        this.groceryListID = groceryListID;
        this.name = name;
        this.items = items;
    }

    public GroceryList() {
        this.groceryListID = null;
        this.items = null;
        this.name = null;
    }

    public UUID getGroceryListID() {
        return groceryListID;
    }

    public String getName() {
        return name;
    }

    public List<Item> getItems() {
        return items;
    }
}
