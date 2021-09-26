package cs.superleague.user.responses;

import cs.superleague.user.dataclass.Driver;

import java.io.Serializable;

public class GetDriverByEmailResponse implements Serializable {
    final Driver driver;
    private final boolean success;

    public GetDriverByEmailResponse(Driver driver, boolean success) {
        this.driver = driver;
        this.success = success;
    }

    public Driver getDriver() {
        return driver;
    }

    public boolean isSuccess() {
        return success;
    }
}
