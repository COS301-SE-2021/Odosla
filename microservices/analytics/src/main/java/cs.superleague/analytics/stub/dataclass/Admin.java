package cs.superleague.analytics.stub.dataclass;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;

@Entity
@Table
public class Admin extends User {

    @Id
    private UUID adminID;


    public Admin(String name, String surname, String email, String phoneNumber, String password, Date activationDate, String activationCode, String resetCode, String resetExpiration, boolean isActive, UserType accountType, UUID adminID) {
        super(name, surname, email, phoneNumber, password, activationDate, activationCode, resetCode, resetExpiration,accountType);
        this.adminID = adminID;
    }

    public Admin(String name, String surname, String email, String phoneNumber, String password, String activationCode, UserType accountType, UUID adminID) {
        super(name, surname, email, phoneNumber, password, activationCode, accountType);
        this.adminID = adminID;
    }

    public Admin() {

    }

    public UUID getAdminID() {
        return adminID;
    }

    public void setAdminID(UUID adminID) {
        this.adminID = adminID;
    }
}
