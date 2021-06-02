package shopping.dataclass;

import java.util.List;

public class Catalogue {
    private List<Item> items;

    public Catalogue() {
    }

    public Catalogue(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
