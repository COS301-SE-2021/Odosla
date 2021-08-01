package cs.superleague.user.dataclass;

import java.util.Date;
import java.util.UUID;

public class User {

    /* Attributes */
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private String password;
    private Date activationDate;
    private String activationCode;
    private String resetCode;
    private String resetExpiration;
    private boolean isActive;
    private UserType accountType;

    /* Constructor  */

    public User(String name, String surname,String email, String phoneNumber, String password, Date activationDate, String activationCode, String resetCode, String resetExpiration, boolean isActive, UserType accountType) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.activationDate = activationDate;
        this.activationCode = activationCode;
        this.resetCode = resetCode;
        this.resetExpiration = resetExpiration;
        this.isActive = isActive;
        this.accountType = accountType;
    }

    public User(String name, String surname, String email, String phoneNumber, String password, String activationCode,UserType accountType) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.activationCode=activationCode;
        this.accountType=accountType;
    }

    public User(){

    }

    /* Getters and Setters */

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

    public UserType getAccountType() {
        return accountType;
    }

    public void setAccountType(UserType accountType) {
        this.accountType = accountType;
    }
}