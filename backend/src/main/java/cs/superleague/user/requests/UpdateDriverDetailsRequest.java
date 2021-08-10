package cs.superleague.user.requests;

import java.util.UUID;

public class UpdateDriverDetailsRequest {

    private UUID driverID;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phoneNumber;

    public UpdateDriverDetailsRequest(UUID driverID, String name, String surname, String email, String password, String phoneNumber) {
        this.driverID = driverID;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public UUID getDriverID() {
        return driverID;
    }

    public void setDriverID(UUID driverID) {
        this.driverID = driverID;
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
}
