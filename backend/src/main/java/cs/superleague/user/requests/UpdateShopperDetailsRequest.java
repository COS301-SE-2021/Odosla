package cs.superleague.user.requests;

import java.util.UUID;

public class UpdateShopperDetailsRequest {

    private UUID shopperID;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phoneNumber;
    private UUID storeID;

    public UpdateShopperDetailsRequest(UUID shopperID, String name, String surname, String email, String password, String phoneNumber) {
        this.shopperID = shopperID;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public UpdateShopperDetailsRequest(UUID shopperID, String name, String surname, String email, String password, String phoneNumber, UUID storeID) {
        this.shopperID = shopperID;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.storeID = storeID;
    }

    public UUID getShopperID() {
        return shopperID;
    }

    public void setShopperID(UUID shopperID) {
        this.shopperID = shopperID;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UUID getStoreID() {
        return storeID;
    }

    public void setStoreID(UUID storeID) {
        this.storeID = storeID;
    }
}

