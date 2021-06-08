package user.dataclass;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Calendar;
import java.util.UUID;

@Entity
@Table
public class Shopper extends User {
    /* Attributes */
    @Id
    private UUID shopperID;
    private UUID storeID;
    private int ordersCompleted;
    //payOption..

    public Shopper() {
    }

    public Shopper(String name, String surname, String username, UUID id, String email, String phoneNumber, String password, Calendar activationDate, String activationCode, String resetCode, String resetExpiration, boolean isActive, UserType accountType, UUID shopperID, UUID storeID, int ordersCompleted) {
        super(name, surname, username, id, email, phoneNumber, password, activationDate, activationCode, resetCode, resetExpiration, isActive, accountType);
        this.shopperID = shopperID;
        this.storeID = storeID;
        this.ordersCompleted = ordersCompleted;
    }



}