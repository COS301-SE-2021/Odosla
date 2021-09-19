package cs.superleague.notifications.repos;

import cs.superleague.notifications.dataclass.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, UUID> {
}
