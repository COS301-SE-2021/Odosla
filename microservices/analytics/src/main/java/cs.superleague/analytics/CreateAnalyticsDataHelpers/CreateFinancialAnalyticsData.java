package cs.superleague.analytics.CreateAnalyticsDataHelpers;

import cs.superleague.payment.PaymentService;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.requests.GetOrdersRequest;
import cs.superleague.payment.responses.GetOrdersResponse;

import java.util.*;

public class CreateFinancialAnalyticsData {

    private final List<Order> orders;
    private final List<UUID> userIds;
    private int totalOrders;
    private double totalPrice;
    private double averageOrderNumPerUser;
    private double averagePriceOfOrders;
    private Order [] topOrders;

    private GetOrdersResponse getOrdersResponse;

    private final Date startDate;
    private final Date endDate;

    public CreateFinancialAnalyticsData(Date startDate, Date endDate, PaymentService paymentService){

        this.orders = new ArrayList<>();
        this.userIds = new ArrayList<>();

        this.totalOrders = 0;
        this.totalPrice = 0;

        this.startDate = startDate;
        this.endDate = endDate;

        GetOrdersRequest getOrdersRequest = new GetOrdersRequest();
        try{
            getOrdersResponse = paymentService.getOrders(getOrdersRequest);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public HashMap<String, Object> getFinancialStatisticsData(){

        HashMap<String, Object> data = new HashMap<>();

        if(generateFinancialStatisticsData()) {
            data.put("totalOrders", totalOrders);
            data.put("totalPrice", totalPrice);
            data.put("averageNumberOfOrderPerUser", averageOrderNumPerUser);
            data.put("topTenOrders", topOrders);
            data.put("averagePriceOfOrders", averagePriceOfOrders);
            data.put("startDate", this.startDate.getTime());
            data.put("endDate", this.endDate.getTime());
        }

        return data;
    }

    private boolean generateFinancialStatisticsData(){

        if(getOrdersResponse != null){
            orders.addAll(getOrdersResponse.getOrders());
        }else{
            return false;
        }

        for (Order order : this.orders) {

            if(startDate.getTime() <= order.getCreateDate().getTimeInMillis()
                && endDate.getTime() >= order.getCreateDate().getTimeInMillis()) {

                if (!userIds.contains(order.getUserID())) {
                    userIds.add(order.getUserID());
                }

                totalPrice += order.getTotalCost();
                totalOrders += 1;
            }
        }

        if(totalOrders != 0) {
            averageOrderNumPerUser = userIds.size() / (double) totalOrders;
            averageOrderNumPerUser = (double)Math.round(averageOrderNumPerUser * 100d) / 100d;
        }else{
            averageOrderNumPerUser = 0;
        }

        if(orders.size() != 0) {
            averagePriceOfOrders = totalPrice / (double) orders.size();
            averagePriceOfOrders = (double)Math.round(averagePriceOfOrders * 100d) / 100d;
        }else{
            averagePriceOfOrders = 0;
        }

        totalPrice = (double)Math.round(totalPrice * 100d) / 100d;

        getTopOrdersByPrice(orders);

        return true;
    }

    private void getTopOrdersByPrice(List<Order> orders){

        int size = Math.min(orders.size(), 10);

        topOrders = new Order[size];

        orders.sort(Comparator.comparing(Order::getTotalCost).reversed());

        for(int i = 0; i < size; i++){
            topOrders[i] = orders.get(i);
        }
    }
}