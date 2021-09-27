package cs.superleague.user.repos;

import cs.superleague.user.dataclass.OrdersWithProblems;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrdersWithProblemsRepo extends JpaRepository<OrdersWithProblems, String> {
    List<OrdersWithProblems> findOrdersWithProblemsByOrderID(UUID orderID);
}
