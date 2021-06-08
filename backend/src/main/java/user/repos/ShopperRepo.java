package user.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import payment.dataclass.Order;
import user.dataclass.Shopper;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShopperRepo extends JpaRepository<Shopper, UUID> {

  List<Shopper> findAll();

}

