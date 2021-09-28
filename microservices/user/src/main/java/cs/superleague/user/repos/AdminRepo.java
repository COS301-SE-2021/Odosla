package cs.superleague.user.repos;

import cs.superleague.user.dataclass.Admin;
import cs.superleague.user.dataclass.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AdminRepo extends JpaRepository<Admin, UUID> {
    Admin findAdminByEmail(String email);

    Optional<Admin> findById(UUID adminID);
}
