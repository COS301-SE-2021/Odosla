package cs.superleague.user.dataclass;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table
public class OrdersWithProblems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String problemID;
    private UUID orderID;
    private String currentProductBarcode;
    private String alternativeProductBarcode;

    public OrdersWithProblems(UUID orderID, String currentProductBarcode, String alternativeProductBarcode) {
        this.orderID = orderID;
        this.currentProductBarcode = currentProductBarcode;
        this.alternativeProductBarcode = alternativeProductBarcode;
    }

    public OrdersWithProblems() {
    }

    public String getProblemID() {
        return problemID;
    }

    public void setProblemID(String problemID) {
        this.problemID = problemID;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public void setOrderID(UUID orderID) {
        this.orderID = orderID;
    }

    public String getCurrentProductBarcode() {
        return currentProductBarcode;
    }

    public void setCurrentProductBarcode(String currentProductBarcode) {
        this.currentProductBarcode = currentProductBarcode;
    }

    public String getAlternativeProductBarcode() {
        return alternativeProductBarcode;
    }

    public void setAlternativeProductBarcode(String alternativeProductBarcode) {
        this.alternativeProductBarcode = alternativeProductBarcode;
    }
}
