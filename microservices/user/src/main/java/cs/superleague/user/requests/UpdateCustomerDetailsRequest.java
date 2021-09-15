package cs.superleague.user.requests;
import cs.superleague.payment.dataclass.GeoPoint;

public class UpdateCustomerDetailsRequest {

    private final String name;
    private final String surname;
    private final String email;
    private final String phoneNumber;
    private final String password;
    private final GeoPoint address;
    private final String currentPassword;

    public UpdateCustomerDetailsRequest(String name, String surname, String email, String phoneNumber, String password, GeoPoint address, String currentPassword) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.password = password;
        this.address = address;
        this.currentPassword = currentPassword;
    }

    public String getCurrentPassword() {
        return currentPassword;
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

    public GeoPoint getAddress() {
        return address;
    }
}
