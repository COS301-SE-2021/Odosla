package cs.superleague.user.repos;

import cs.superleague.user.dataclass.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdminRepo extends JpaRepository<Admin, UUID> {
    boolean findByEmail(String email);
    Admin findAdminByEmail(String email);
}
