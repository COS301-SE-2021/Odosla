package cs.superleague.delivery.repos;

import cs.superleague.delivery.dataclass.DeliveryDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeliveryDetailRepo extends JpaRepository<DeliveryDetail, Integer> {
    List<DeliveryDetail> findAllByDeliveryID(UUID deliveryID);
}
