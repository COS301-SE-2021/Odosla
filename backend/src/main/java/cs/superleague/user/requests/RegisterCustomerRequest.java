package cs.superleague.user.requests;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.user.dataclass.UserType;

import java.util.Date;
import java.util.UUID;

public class RegisterCustomerRequest {

    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private String password;
    private GeoPoint address;

    public RegisterCustomerRequest(String name, String surname, String email, String phoneNumber, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public RegisterCustomerRequest(String name, String surname, String username, String email, String phoneNumber, String password, Date activationDate, GeoPoint address) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.address = address;
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

    public GeoPoint getAddress() {
        return address;
    }

    public void setAddress(GeoPoint address) {
        this.address = address;
    }
}
