package user.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import user.dataclass.User;


import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
}