package cs.superleague.payment.stubs;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
public class Wallet {

    @Id
    private final UUID walletID;
    // private List<PayOption>
    // private PayOption ??

    public Wallet(UUID walletID) {
        this.walletID = walletID;
    }

    public Wallet() {
        walletID = null;
    }
}
