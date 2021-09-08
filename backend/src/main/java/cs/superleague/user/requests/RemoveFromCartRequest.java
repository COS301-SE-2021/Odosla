package cs.superleague.user.requests;


public class RemoveFromCartRequest {

    private final String customerID;
    private final String barcode;

    public String getCustomerID() {
        return customerID;
    }

    public String getBarcode() {
        return barcode;
    }

    public RemoveFromCartRequest(String customerID, String barcode) {
        this.customerID = customerID;
        this.barcode = barcode;
    }
}
