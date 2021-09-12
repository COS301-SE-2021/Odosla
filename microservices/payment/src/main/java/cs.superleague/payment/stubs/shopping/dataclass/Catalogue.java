package cs.superleague.payment.stubs.shopping.dataclass;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "catalogueTable")
public class Catalogue {
    @Id
    private UUID storeID;

    @ManyToMany
    @JoinTable
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Item> items;

    public Catalogue() { }

    public Catalogue(UUID storeID, List<Item> items) {
        this.storeID=storeID;
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }


    public UUID getStoreID() {
        return storeID;
    }

    public void setStoreID(UUID storeID) {
        this.storeID = storeID;
    }

}
