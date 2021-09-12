package cs.superleague.delivery.stub.responses;

import cs.superleague.delivery.stub.dataclass.Driver;

public class FindDriverByIdResponse {

    private final Driver driver;

    public FindDriverByIdResponse() {
        this.driver = null;
    }

    public FindDriverByIdResponse(Driver driver) {
        this.driver = driver;
    }

    public Driver getDriver() {
        return driver;
    }
}
