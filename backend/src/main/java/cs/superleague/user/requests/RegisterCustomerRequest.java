package cs.superleague.user.requests;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.user.dataclass.GroceryList;
import cs.superleague.user.dataclass.UserType;

import java.util.Calendar;
import java.util.List;

public class RegisterCustomerRequest {//extends RegisterUserRequest {

    private final String name;
    private final String surname;
    private final String username;
    private final String email;
    private final String phoneNumber;
    private final String password;
    private final Calendar activationDate;
    private final String activationCode;
    private final String resetCode;
    private final String resetExpiration;
    private final boolean isActive;
    private final UserType accountType;

//    private final Wallet wallet;
    private final GeoPoint address;
//    private final Preference preference;

    public RegisterCustomerRequest(String name, String surname, String username, String email, String phoneNumber, String password, Calendar activationDate, String activationCode, String resetCode, String resetExpiration, boolean isActive, UserType accountType, GeoPoint address) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.activationDate = activationDate;
        this.activationCode = activationCode;
        this.resetCode = resetCode;
        this.resetExpiration = resetExpiration;
        this.isActive = isActive;
        this.accountType = accountType;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public Calendar getActivationDate() {
        return activationDate;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public String getResetCode() {
        return resetCode;
    }

    public String getResetExpiration() {
        return resetExpiration;
    }

    public boolean isActive() {
        return isActive;
    }

    public UserType getAccountType() {
        return accountType;
    }

    public GeoPoint getAddress() {
        return address;
    }

}
