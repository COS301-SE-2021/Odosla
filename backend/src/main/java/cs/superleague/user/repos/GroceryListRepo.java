package cs.superleague.user.repos;

import cs.superleague.user.dataclass.GroceryList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GroceryListRepo extends JpaRepository<GroceryList, UUID> {
    public GroceryList findGroceryListByNameAndUserID(String name, UUID groceryListID);
}

