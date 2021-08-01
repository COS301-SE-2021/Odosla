package cs.superleague.user.dataclass;

import cs.superleague.shopping.dataclass.Item;
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
    private final UUID userID;

    @ManyToMany
    @JoinTable
    @LazyCollection(LazyCollectionOption.FALSE)
    private final List<Item> items;

    public GroceryList(UUID groceryListID, String name, List<Item> items, UUID userID) {
        this.groceryListID = groceryListID;
        this.name = name;
        this.items = items;
        this.userID = userID;
    }

    public GroceryList() {
        this.groceryListID = null;
        this.items = null;
        this.name = null;
        this.userID = null;
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
