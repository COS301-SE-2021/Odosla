package cs.superleague.shopping.repos;

import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemRepo extends JpaRepository<Item, UUID> {
    Item findAllByBarcodeAndStoreID(String barcode, UUID storeID);
}

