package payment.dataclass;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class Invoice {
    private UUID invoiceID;
    private Calendar date;
    private String details;
    private float totalCost;
    private List<Item> item;
    //private DeliveryDetail deliveryDetail;


    public Invoice() {
    }

    public Invoice(UUID invoiceID, Calendar date, String details, float totalCost, List<Item> item) {
        this.invoiceID = invoiceID;
        this.date = date;
        this.details = details;
        this.totalCost = totalCost;
        this.item = item;
    }

    public UUID getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(UUID invoiceID) {
        this.invoiceID = invoiceID;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public float getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(float totalCost) {
        this.totalCost = totalCost;
    }

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }
}
