package cs.superleague.delivery.responses;

import cs.superleague.user.dataclass.Driver;

public class GetDeliveryDriverByOrderIDResponse {

    private Driver driver;
    private String message;

    public GetDeliveryDriverByOrderIDResponse(Driver driver, String message)
    {
        this.driver=driver;
        this.message=message;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }
}
