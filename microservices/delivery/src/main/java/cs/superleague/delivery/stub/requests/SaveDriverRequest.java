package cs.superleague.delivery.stub.requests;

import cs.superleague.delivery.stub.dataclass.Driver;

import java.io.Serializable;

public class SaveDriverRequest implements Serializable {

    private final Driver driver;

    public SaveDriverRequest() {
        this.driver = null;
    }

    public SaveDriverRequest(Driver driver) {
        this.driver = driver;
    }

    public Driver getDriver() {
        return driver;
    }
}
