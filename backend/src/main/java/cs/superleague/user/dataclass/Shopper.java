package cs.superleague.user.dataclass;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "shopper")
public class Shopper extends User {

    /* Attributes */
    @Id
    @Column(name = "shopperid")
    private UUID shopperID;

    @Column(name = "storeid")
    private UUID storeID;

    @Column(name = "orders_completed")
    private int ordersCompleted=0;

    private String name;
    private String surname;
    private String email;

    @Column(name="phone_number")
    private String phoneNumber;

    private String password;

    @Column(name="activation_date")
    private Date activationDate;

    @Column(name="activation_code")
    private String activationCode;

    @Column(name="reset_code")
    private String resetCode;

    @Column(name="reset_expiration")
    private String resetExpiration;

    @Column(name="is_active")
    private boolean isActive;

    @Column(name="account_type")
    @Enumerated(EnumType.STRING)
    private UserType accountType;

    //payOption..

    public Shopper() {
    }

    public Shopper(String name, String surname, String email, String phoneNumber, String password, Date activationDate, String activationCode, String resetCode, String resetExpiration, boolean isActive, UserType accountType, UUID shopperID) {
        super(name, surname, email, phoneNumber, password, activationDate, activationCode, resetCode, resetExpiration,accountType);
        this.shopperID = shopperID;
    }

    public Shopper(String name, String surname, String email, String phoneNumber, String password, String activationCode, UserType accountType, UUID shopperID) {
        super(name, surname, email, phoneNumber, password, activationCode, accountType);
        this.shopperID = shopperID;
    }

    public UUID getShopperID() {
        return shopperID;
    }

    public void setShopperID(UUID shopperID) {
        this.shopperID = shopperID;
    }

    public UUID getStoreID() {
        return storeID;
    }

    public void setStoreID(UUID storeID) {
        this.storeID = storeID;
    }

    public int getOrdersCompleted() {
        return ordersCompleted;
    }

    public void setOrdersCompleted(int ordersCompleted) {
        this.ordersCompleted = ordersCompleted;
    }



}