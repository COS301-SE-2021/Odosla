package cs.superleague.user.dataclass;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Table;
import java.util.Calendar;
import java.util.UUID;

@MappedSuperclass
public class User {

    /* Attributes */
    @Id
    private UUID id;
    private String name;
    private String surname;
    private String username;

    private String email;
    private String phoneNumber;
    private String password;
    private Calendar activationDate;
    private String activationCode;
    private String resetCode;
    private String resetExpiration;
    private boolean isActive;
    private UserType accountType;

    /* Constructor  */

    public User(String name, String surname, String username, UUID id, String email, String phoneNumber, String password, Calendar activationDate, String activationCode, String resetCode, String resetExpiration, boolean isActive, UserType accountType) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.id = id;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public Calendar getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Calendar activationDate) {
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

    /* Functions */

    public boolean login(){
        return true;
    }

    public boolean logout(){
        return true;
    }

    public boolean registerUser(){
        return true;
    }

    public boolean resetPassword(){
        return true;
    }

    public boolean updateAccountDetails(){
        return true;
    }
}