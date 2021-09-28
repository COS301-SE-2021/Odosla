package cs.superleague.delivery.repos;

import cs.superleague.delivery.dataclass.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeliveryRepo extends JpaRepository<Delivery, UUID> {
    List<Delivery> findAllByDriverIdIsNull();
}
