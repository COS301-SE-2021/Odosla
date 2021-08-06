package cs.superleague.user.repos;

import cs.superleague.user.dataclass.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DriverRepo extends JpaRepository<Driver, UUID> {

    Boolean findByEmail(String email);
    Driver findDriverByEmail(String email);
    Optional<Driver> findById(UUID driverID);
}
