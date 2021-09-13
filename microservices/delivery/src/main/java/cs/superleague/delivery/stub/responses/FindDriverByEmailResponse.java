package cs.superleague.delivery.stub.responses;

import cs.superleague.delivery.stub.user.dataclass.Driver;

public class FindDriverByEmailResponse {

    private final Driver driver;

    public FindDriverByEmailResponse() {
        this.driver = null;
    }

    public FindDriverByEmailResponse(Driver driver) {
        this.driver = driver;
    }

    public Driver getDriver() {
        return driver;
    }
}
