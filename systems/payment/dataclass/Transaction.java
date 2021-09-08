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

    private float amount;
    //private PayOption card;


    public Transaction() {
    }

    public Transaction(Calendar date, Order order, float amount) {
        this.date = date;
        this.order = order;
        this.amount = amount;
    }

    public Transaction(UUID transactionID, Calendar date, Order order, float amount) {
        this.transactionID = transactionID;
        this.date = date;
        this.order = order;
        this.amount = amount;
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

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public UUID getTransactionID() {
        return transactionID;
    }
}
