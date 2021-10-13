package cs.superleague.user.dataclass;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.UUID;

@Entity
public class Wallet implements Serializable {

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
