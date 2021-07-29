package cs.superleague.user.requests;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.user.dataclass.UserType;

import java.util.Date;
import java.util.UUID;

public class RegisterDriverRequest {

    private String name;
    private String surname;
    private String username;
    private String email;
    private String phoneNumber;
    private String password;
    private Date activationDate;

    public RegisterDriverRequest(String name, String surname, String username, String email, String phoneNumber, String password, Date activationDate) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.activationDate = activationDate;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
