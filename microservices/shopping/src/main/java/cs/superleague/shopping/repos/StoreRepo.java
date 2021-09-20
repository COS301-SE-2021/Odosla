package cs.superleague.shopping.repos;

import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cs.superleague.shopping.dataclass.Store;

import java.util.UUID;

@Repository
public interface StoreRepo extends JpaRepository<Store, UUID> {
}
