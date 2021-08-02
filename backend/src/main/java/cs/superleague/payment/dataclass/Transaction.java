package cs.superleague.payment.dataclass;

import javax.persistence.*;
import java.util.Calendar;
import java.util.UUID;

@Entity
@Table(name = "transactionTable")
public class Transaction {
    @Id
    private UUID transactionID;
    private Calendar date;

    @OneToOne(cascade={CascadeType.ALL})
    private Order order;

    private double amount;
    private String transactionAddress;

    //private PayOption card;


    public Transaction() {
    }

    public Transaction(UUID transactionID, Calendar date, Order order, double amount, String transactionAddress) {
        this.transactionID = transactionID;
        this.date = date;
        this.order = order;
        this.amount = amount;
        this.transactionAddress = transactionAddress;
    }

    public UUID getTransactionID() {
        return transactionID;
    }

    public void setTransactionID(UUID transactionID) {
        this.transactionID = transactionID;
    }

    public Calendar getDate() {
        return date;
    }

    public void setDate(Calendar date) {
        this.date = date;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransactionAddress() {
        return transactionAddress;
    }

    public void setTransactionAddress(String transactionAddress) {
        this.transactionAddress = transactionAddress;
    }
}
