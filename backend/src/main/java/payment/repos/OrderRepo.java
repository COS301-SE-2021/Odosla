package order.repos;

import order.dataclass.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

 @Repository
 public interface OrderRepo extends JpaRepository<Order, UUID> {
 }

