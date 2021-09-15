package cs.superleague.user.dataclass;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Category {

    @Id
    private final UUID categoryId;

//    private final List<Tuple<Item, Integer>> listOfItems;
    private final String name;
    private final String imgUrl;

    public Category() {
        categoryId = null;
        name = null;
        imgUrl = null;
    }

    public Category(UUID categoryId, String name, String imgUrl) {
        this.categoryId = categoryId;
        this.name = name;
        this.imgUrl = imgUrl;
    }

    public UUID getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public String getImgUrl() {
        return imgUrl;
    }
}
