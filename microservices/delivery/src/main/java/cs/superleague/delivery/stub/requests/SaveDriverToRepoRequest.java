package cs.superleague.delivery.stub.requests;

import cs.superleague.delivery.stub.dataclass.Driver;

public class SaveDriverToRepoRequest {

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
