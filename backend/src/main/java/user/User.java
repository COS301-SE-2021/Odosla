package user;

import java.util.Calendar;
import java.util.UUID;

//import java.util.UUID;
//import java.util.LocalDateTime;
public class User {
    /* Attributes */
    private UUID id;
    private String name;
    private String surname;
    private String contact;
    private String username;
    private String email;
    private String password;
    private String activationCode;
    private String resetCode;
    private Calendar resetExpiration;
    private Boolean isAdmin;
    private boolean isActive;
    private String phoneNumber;
    /* Constructor  */

    public User(UUID id, String name, String surname, String contact, String username, String email, String password, String activationCode, String resetCode, Calendar resetExpiration, Boolean isAdmin, boolean isActive, String phoneNumber) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.contact = contact;
        this.username = username;
        this.email = email;
        this.password = password;
        this.activationCode = activationCode;
        this.resetCode = resetCode;
        this.resetExpiration = resetExpiration;
        this.isAdmin = isAdmin;
        this.isActive = isActive;
        this.phoneNumber = phoneNumber;
    }

    public User() {

    }

    /* Getters and Setters */
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Calendar getResetExpiration() {
        return resetExpiration;
    }

    public void setResetExpiration(Calendar resetExpiration) {
        this.resetExpiration = resetExpiration;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}