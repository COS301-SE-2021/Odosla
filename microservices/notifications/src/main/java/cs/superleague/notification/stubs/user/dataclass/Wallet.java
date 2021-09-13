package cs.superleague.notification.stubs.user.dataclass;

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
