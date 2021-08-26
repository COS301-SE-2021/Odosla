package cs.superleague.recommendation.dataclass;

import cs.superleague.shopping.dataclass.Item;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "recommendationTable")
public class Recommendation {
    @Id
    private UUID orderID;
    @ManyToMany
    @JoinTable
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Item> items;
}
