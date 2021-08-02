package cs.superleague.user.repos;

import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.Shopper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, UUID> {
    boolean findByEmail(String email);
    Customer findCustomerByEmail(String email);
}
