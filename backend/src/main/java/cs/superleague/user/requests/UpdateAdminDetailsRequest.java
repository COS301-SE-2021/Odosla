package cs.superleague.user.requests;

import java.util.UUID;

public class UpdateAdminDetailsRequest {

    private UUID adminID;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private String password;

    public UpdateAdminDetailsRequest(UUID adminID, String name, String surname, String email, String phoneNumber, String password) {
        this.adminID = adminID;
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public UUID getAdminID() {
        return adminID;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
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

    public void setAdminID(UUID adminID) {
        this.adminID = adminID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
