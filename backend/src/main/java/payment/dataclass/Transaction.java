package payment.dataclass;

import java.util.Calendar;

public class Transaction {
    private Calendar date;
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
}
