package cs.superleague.user.requests;

import java.io.Serializable;
import java.util.UUID;

public class RemoveProblemFromRepoRequest implements Serializable {

    private UUID orderID;
    private String bardcode;

    public RemoveProblemFromRepoRequest() {
    }

    public RemoveProblemFromRepoRequest(UUID orderID, String bardcode) {
        this.orderID = orderID;
        this.bardcode = bardcode;
    }

    public UUID getOrderID() {
        return orderID;
    }

    public String getBardcode() {
        return bardcode;
    }
}
