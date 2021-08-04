package cs.superleague.user.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cs.superleague.user.dataclass.Shopper;

import java.util.List;
import java.util.UUID;

@Repository
public interface ShopperRepo extends JpaRepository<Shopper, UUID> {

  List<Shopper> findAll();

  boolean findByEmail(String email);

  Shopper findShopperByEmail(String email);
}

