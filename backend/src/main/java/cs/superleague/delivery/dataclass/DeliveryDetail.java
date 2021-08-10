package cs.superleague.delivery.dataclass;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Calendar;

@Entity
public class DeliveryDetail {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private Calendar time;
    private DeliveryStatus status;
}
