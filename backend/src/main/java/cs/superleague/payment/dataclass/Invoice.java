package cs.superleague.payment.dataclass;

import cs.superleague.shopping.dataclass.Item;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class Invoice {
    private UUID invoiceID;
    private UUID customerID;
    private Calendar date;
    private String details;
    private Double totalCost;
    private List<Item> item;
    //private DeliveryDetail deliveryDetail;



    public Invoice(UUID invoiceID, UUID customerID, Calendar date, String details, Double totalCost, List<Item> item) {
        this.invoiceID = invoiceID;
        this.customerID = customerID;
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

    public Double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(Double totalCost) {
        this.totalCost = totalCost;
    }

    public List<Item> getItem() {
        return item;
    }

    public void setItem(List<Item> item) {
        this.item = item;
    }

    public UUID getCustomerID() {
        return customerID;
    }
}
