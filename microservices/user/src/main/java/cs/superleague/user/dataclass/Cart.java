package cs.superleague.user.dataclass;
import cs.superleague.shopping.dataclass.Item;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table
public class Cart {

    @Id
    private final UUID cartID;
    private final String name;

    @ManyToMany
    @JoinTable
    @LazyCollection(LazyCollectionOption.FALSE)
    private final List<Item> items;

    public Cart(UUID cartID, String name, List<Item> items) {
        this.cartID = cartID;
        this.name = name;
        this.items = items;
    }

    public Cart() {
        this.cartID = null;
        this.items = null;
        this.name = null;
    }

    public UUID getCartID() {
        return cartID;
    }

    public String getName() {
        return name;
    }

    public List<Item> getItems() {
        return items;
    }
}
