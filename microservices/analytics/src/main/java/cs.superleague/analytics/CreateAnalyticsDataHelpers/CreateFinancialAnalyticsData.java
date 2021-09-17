package cs.superleague.analytics.CreateAnalyticsDataHelpers;

import cs.superleague.analytics.exceptions.InvalidRequestException;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.responses.GetOrdersResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

//@Configuration
//@ConfigurationProperties
public class CreateFinancialAnalyticsData {
//    @Value("${paymentHost}")
    private String paymentHost;
//    @Value("${paymentPort}")
    private String paymentPort;

    private final List<Order> orders;
    private final List<UUID> userIds;
    private int totalOrders;
    private double totalPrice;
    private double averageOrderNumPerUser;
    private double averagePriceOfOrders;
    private Order [] topOrders;

    private final Date startDate;
    private final Date endDate;

    private ResponseEntity<GetOrdersResponse> responseEntity;

    public CreateFinancialAnalyticsData(Date startDate, Date endDate, RestTemplate restTemplate,
        String paymentPort, String paymentHost) throws URISyntaxException {

        this.orders = new ArrayList<>();
        this.userIds = new ArrayList<>();

        this.totalOrders = 0;
        this.totalPrice = 0;

        this.startDate = startDate;
        this.endDate = endDate;

        this.paymentHost = paymentHost;
        this.paymentHost = paymentHost;

        String stringUri = "http://"+paymentHost+":"+paymentPort+"/payment/getOrders";
        URI uri = new URI(stringUri);

        try{

            Map<String, Object> parts = new HashMap<>();

            responseEntity = restTemplate.postForEntity(uri, parts,
                    GetOrdersResponse.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public HashMap<String, Object> getFinancialStatisticsData() throws InvalidRequestException {

        HashMap<String, Object> data = new HashMap<>();

        if(generateFinancialStatisticsData()) {
            data.put("totalOrders", totalOrders);
            data.put("totalPrice", totalPrice);
            data.put("averageNumberOfOrderPerUser", averageOrderNumPerUser);
            data.put("topTenOrders", topOrders);
            data.put("averagePriceOfOrders", averagePriceOfOrders);
            data.put("startDate", this.startDate);
            data.put("endDate", this.endDate);
        }

        return data;
    }

    private boolean generateFinancialStatisticsData() throws InvalidRequestException {

        if(responseEntity == null){
            return false;
        }

        GetOrdersResponse getOrdersResponse = responseEntity.getBody();


        if(getOrdersResponse != null){
            orders.addAll(getOrdersResponse.getOrders());
        }else{
            return false;
        }

        for (Order order : this.orders) {

            if(startDate == null || endDate == null){
                throw new InvalidRequestException("Start Date and End Date cannot be null");
            }

            if(startDate.getTime() <= order.getCreateDate().getTime()
                && endDate.getTime() >= order.getCreateDate().getTime()) {

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