package cs.superleague.user.dataclass;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@Entity
@Table
public class Admin extends User {

    @Id
    private UUID adminID;
}
