package cs.superleague.delivery.responses;

public class GetDeliveryCostResponse {
    private double cost;


    public GetDeliveryCostResponse(double cost) {
        this.cost = cost;

    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

}
