package cs.superleague.delivery.responses;

import cs.superleague.delivery.stub.user.dataclass.Driver;

import java.util.UUID;

public class GetDeliveryDriverByOrderIDResponse {

    private Driver driver;
    private UUID deliveryID;
    private String message;

    public GetDeliveryDriverByOrderIDResponse(Driver driver, String message, UUID deliveryID)
    {
        this.driver=driver;
        this.message=message;
        this.deliveryID=deliveryID;
    }

    public Driver getDriver() {
        return driver;
    }

    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    public UUID getDeliveryID() {
        return deliveryID;
    }

    public void setDeliveryID(UUID deliveryID) {
        this.deliveryID = deliveryID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
