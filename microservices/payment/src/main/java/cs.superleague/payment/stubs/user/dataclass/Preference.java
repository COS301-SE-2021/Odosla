package cs.superleague.payment.stubs.user.dataclass;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.UUID;

@Entity
public class Preference {

    @Id
    private final UUID preferenceID;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private final List<Category> accept;

    @OneToMany
    @LazyCollection(LazyCollectionOption.FALSE)
    private final List<Category> reject;


    public List<Category> getAccept() {
        return accept;
    }

    public List<Category> getReject() {
        return reject;
    }

    public Preference() {
        preferenceID = null;
        accept = null;
        reject = null;
    }

    public Preference(UUID preferenceID, List<Category> accept, List<Category> reject) {
        this.preferenceID = preferenceID;
        this.accept = accept;
        this.reject = reject;
    }

    public UUID getPreferenceID() {
        return preferenceID;
    }
}
