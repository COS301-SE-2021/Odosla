package cs.superleague.user.repos;

import cs.superleague.user.dataclass.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {
}