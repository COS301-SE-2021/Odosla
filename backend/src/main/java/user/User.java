package user;

public class User {
    /* Attributes */
    private UUID id;
    private String name;
    private String surname;
    private String contact;
    /* Constructor  */
    public User(UUID id, String name, String surname,String contact) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.contact=contact;
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
}