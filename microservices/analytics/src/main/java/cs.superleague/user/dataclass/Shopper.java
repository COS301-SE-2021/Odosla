package cs.superleague.user.dataclass;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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

    private Boolean onShift=false;

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Column(name="is_active")
    private boolean isActive;

    public Shopper() {
    }

    public Shopper(String name, String surname, String email, String phoneNumber, String password, Date activationDate, String activationCode, String resetCode, String resetExpiration, boolean isActive, UserType accountType, UUID shopperID) {
        super(name, surname, email, phoneNumber, password, activationDate, activationCode, resetCode, resetExpiration,accountType);
        this.shopperID = shopperID;
        this.isActive=isActive;
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

    public Boolean getOnShift() {
        return onShift;
    }

    public void setOnShift(Boolean onShift) {
        this.onShift = onShift;
    }
}