package cs.superleague.delivery.responses;

import cs.superleague.user.dataclass.Driver;

public class GetDeliveryDriverByOrderIDResponse {

    private Driver driver;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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
