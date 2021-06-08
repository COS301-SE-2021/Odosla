package cs.superleague.payment.requests;

public class AddItemRequest {
    private String barcode;
    private int quality;

    public AddItemRequest(String barcode, int quality) {
        this.barcode = barcode;
        this.quality = quality;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }
}
