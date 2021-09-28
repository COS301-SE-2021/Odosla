package cs.superleague.user.repos;

import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, UUID> {

    Optional<Customer> findByEmail(String email);

    Optional<Customer> findById(UUID customerID);
}
