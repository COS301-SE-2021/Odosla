package cs.superleague.payment.responses;

public class ReviewPaymentResponse {
    private double costOfDelivery;
    private double costOfOrderOne;
    private double costOfOrderTwo;
    private double costOfOrderThree;
    private double packingCostOfOrderOne;
    private double packingCostOfOrderTwo;
    private double packingCostOfOrderThree;
    private double totalCost;

    public ReviewPaymentResponse(double costOfDelivery, double costOfOrderOne, double costOfOrderTwo, double costOfOrderThree, double packingCostOfOrderOne, double packingCostOfOrderTwo, double packingCostOfOrderThree, double totalCost) {
        this.costOfDelivery = costOfDelivery;
        this.costOfOrderOne = costOfOrderOne;
        this.costOfOrderTwo = costOfOrderTwo;
        this.costOfOrderThree = costOfOrderThree;
        this.packingCostOfOrderOne = packingCostOfOrderOne;
        this.packingCostOfOrderTwo = packingCostOfOrderTwo;
        this.packingCostOfOrderThree = packingCostOfOrderThree;
        this.totalCost = totalCost;
    }

    public double getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(double totalCost) {
        this.totalCost = totalCost;
    }

    public double getCostOfDelivery() {
        return costOfDelivery;
    }

    public void setCostOfDelivery(double costOfDelivery) {
        this.costOfDelivery = costOfDelivery;
    }

    public double getCostOfOrderOne() {
        return costOfOrderOne;
    }

    public void setCostOfOrderOne(double costOfOrderOne) {
        this.costOfOrderOne = costOfOrderOne;
    }

    public double getCostOfOrderTwo() {
        return costOfOrderTwo;
    }

    public void setCostOfOrderTwo(double costOfOrderTwo) {
        this.costOfOrderTwo = costOfOrderTwo;
    }

    public double getCostOfOrderThree() {
        return costOfOrderThree;
    }

    public void setCostOfOrderThree(double costOfOrderThree) {
        this.costOfOrderThree = costOfOrderThree;
    }

    public double getPackingCostOfOrderOne() {
        return packingCostOfOrderOne;
    }

    public void setPackingCostOfOrderOne(double packingCostOfOrderOne) {
        this.packingCostOfOrderOne = packingCostOfOrderOne;
    }

    public double getPackingCostOfOrderTwo() {
        return packingCostOfOrderTwo;
    }

    public void setPackingCostOfOrderTwo(double packingCostOfOrderTwo) {
        this.packingCostOfOrderTwo = packingCostOfOrderTwo;
    }

    public double getPackingCostOfOrderThree() {
        return packingCostOfOrderThree;
    }

    public void setPackingCostOfOrderThree(double packingCostOfOrderThree) {
        this.packingCostOfOrderThree = packingCostOfOrderThree;
    }

}
