package cs.superleague.user.responses;

import cs.superleague.user.dataclass.Driver;

public class GetDriverByEmailResponse {
    final Driver driver;

    public GetDriverByEmailResponse(Driver driver) {
        this.driver = driver;
    }

    public Driver getDriver() {
        return driver;
    }
}
