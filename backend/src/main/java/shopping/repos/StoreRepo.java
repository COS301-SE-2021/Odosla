package shopping.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import payment.dataclass.Order;
import shopping.dataclass.Store;

import java.util.UUID;

@Repository
public interface StoreRepo extends JpaRepository<Store, UUID> {
}
