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

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "email")
    private String email;

    private Boolean onShift=false;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public String getResetCode() {
        return resetCode;
    }

    public void setResetCode(String resetCode) {
        this.resetCode = resetCode;
    }

    public String getResetExpiration() {
        return resetExpiration;
    }

    public void setResetExpiration(String resetExpiration) {
        this.resetExpiration = resetExpiration;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Column(name="phone_number")
    private String phoneNumber;

    @Column(name = "password")
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
    //@Enumerated(EnumType.STRING)
    private UserType accountType;

    //payOption..

    public Shopper() {
    }

    public Shopper(String name, String surname, String email, String phoneNumber, String password, Date activationDate, String activationCode, String resetCode, String resetExpiration, boolean isActive, UserType accountType, UUID shopperID) {
        super(name, surname, email, phoneNumber, password, activationDate, activationCode, resetCode, resetExpiration,accountType);
        this.name=name;
        this.surname=surname;
        this.shopperID = shopperID;
        this.email= email;
        this.phoneNumber= phoneNumber;
        this.password= password;
        this.activationDate= activationDate;
        this.activationCode=activationCode;
        this.resetCode=resetCode;
        this.resetExpiration=resetExpiration;
        this.accountType=accountType;
        this.isActive=isActive;
    }

    public Shopper(String name, String surname, String email, String phoneNumber, String password, String activationCode, UserType accountType, UUID shopperID) {
        super(name, surname, email, phoneNumber, password, activationCode, accountType);
        this.name=name;
        this.surname=surname;
        this.shopperID = shopperID;
        this.email= email;
        this.phoneNumber= phoneNumber;
        this.password= password;
        this.activationCode=activationCode;
        this.accountType=accountType;
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

    public UserType getAccountType() {
        return accountType;
    }

    public void setAccountType(UserType accountType) {
        this.accountType = accountType;
    }

    public Boolean getOnShift() {
        return onShift;
    }

    public void setOnShift(Boolean onShift) {
        this.onShift = onShift;
    }
}