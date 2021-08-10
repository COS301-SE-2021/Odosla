package cs.superleague.delivery.repos;

import cs.superleague.delivery.dataclass.DeliveryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryDetailRepo extends JpaRepository<DeliveryDetail, Integer> {
}
