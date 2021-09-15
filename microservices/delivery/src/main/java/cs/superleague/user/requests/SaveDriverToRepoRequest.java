package cs.superleague.user.requests;

import cs.superleague.user.dataclass.Driver;

import java.io.Serializable;

public class SaveDriverToRepoRequest implements Serializable {
    private final Driver driver;

    public SaveDriverToRepoRequest() {
        this.driver = null;
    }

    public SaveDriverToRepoRequest(Driver driver) {
        this.driver = driver;
    }

    public Driver getDriver() {
        return driver;
    }
}