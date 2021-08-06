package cs.superleague.user.repos;

import cs.superleague.user.dataclass.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import cs.superleague.user.dataclass.Shopper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ShopperRepo extends JpaRepository<Shopper, UUID> {

  List<Shopper> findAll();

  Shopper findShopperByEmail(String email);
  Optional<Shopper> findById(UUID driverID);
}

