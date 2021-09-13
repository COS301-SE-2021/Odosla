package cs.superleague.user.requests;

import cs.superleague.user.dataclass.Driver;

import java.io.Serializable;

public class SaveDriverRequest implements Serializable {
    private Driver driver;

    public SaveDriverRequest(Driver driver) {
        this.driver = driver;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
