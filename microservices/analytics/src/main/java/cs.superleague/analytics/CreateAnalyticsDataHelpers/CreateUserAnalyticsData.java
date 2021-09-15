package cs.superleague.analytics.CreateAnalyticsDataHelpers;

import cs.superleague.analytics.exceptions.InvalidRequestException;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.dataclass.User;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.responses.GetUsersResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;

public class CreateUserAnalyticsData {

    private final List<User> users;
    private final List<Driver> drivers;
    private int totalUsers;
    private int totalCustomers;
    private int totalAdmins;
    private int totalShoppers;
    private int totalDrivers;
    private int driversOnShift;
    private int totalOrderCompleted;
    private double ratingSum;
    private Driver [] topDrivers;

    private final Date startDate;
    private final Date endDate;

    private ResponseEntity<GetUsersResponse> responseEntity;

    public CreateUserAnalyticsData(Date startDate, Date endDate,
                               RestTemplate restTemplate){

        this.users = new ArrayList<>();
        this.drivers = new ArrayList<>();

        this.totalUsers = 0;
        this.totalCustomers = 0;
        this.totalAdmins = 0;
        this.totalShoppers = 0;
        this.totalDrivers = 0;
        this.driversOnShift = 0;
        this.totalOrderCompleted = 0;
        this.ratingSum = 0;
        this.topDrivers = new Driver[10];

        this.startDate = startDate;
        this.endDate = endDate;

        String uri = "http://localhost:8089/user/getUsers";

        try{

            Map<String, Object> parts = new HashMap<>();

            responseEntity = restTemplate.postForEntity(uri, parts,
                    GetUsersResponse.class);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public HashMap<String, Object> getUserStatisticsData() throws InvalidRequestException {

        HashMap<String, Object> data = new HashMap<>();

        if(generateUserStatisticsData()) {
            data.put("totalNum_Users", totalUsers);
            data.put("totalNum_Admins", totalAdmins);
            data.put("totalNum_Customers", totalCustomers);
            data.put("totalNum_Shoppers", totalShoppers);
            data.put("totalNum_OrdersCompletedByShoppers", totalOrderCompleted);
            data.put("totalNum_Drivers", totalDrivers);
            data.put("totalNum_DriversOnShift", driversOnShift);
            data.put("averageRating_Drivers", ratingSum);
            data.put("top10Drivers", topDrivers);
            data.put("startDate", this.startDate);
            data.put("endDate", this.endDate);
        }

        return data;
    }

    private boolean generateUserStatisticsData() throws InvalidRequestException {

        if(responseEntity == null){
            return false;
        }

        GetUsersResponse response = responseEntity.getBody();

        if(response != null) {
            users.addAll(response.getUsers());
        }else{
            return false;
        }

        UserType userType;
        for (User user : this.users) {
            userType = user.getAccountType();

            if(startDate == null || endDate == null){
                throw new InvalidRequestException("Start Date and End Date cannot be null");
            }

            if (startDate.getTime() <= user.getActivationDate().getTime()
                    && endDate.getTime() >= user.getActivationDate().getTime()) {
                if (userType == UserType.CUSTOMER) {
                    totalCustomers += 1;
                } else if (userType == UserType.SHOPPER) {

                    if (user instanceof Shopper) {
                        totalOrderCompleted += ((Shopper) user).getOrdersCompleted();
                    }

                    totalShoppers += 1;
                } else if (userType == UserType.ADMIN) {
                    totalAdmins += 1;
                } else if (userType == UserType.DRIVER) {

                    if (user instanceof Driver) {
                        if (((Driver) user).getOnShift()) {
                            driversOnShift += 1;
                        }

                        drivers.add((Driver) user);
                        ratingSum += ((Driver) user).getRating();
                    }
                    totalDrivers += 1;
                }

                totalUsers += 1;

            }
        }

        if(totalDrivers > 0) {
            ratingSum = ratingSum / totalDrivers;
        }

        getTopDrivers(drivers);

        return true;
    }

    private void getTopDrivers(List<Driver> drivers){

        int size = Math.min(drivers.size(), 10);

        topDrivers = new Driver[size];

        drivers.sort(Comparator.comparing(Driver::getRating).reversed());

        for(int i = 0; i < size; i++) {
            topDrivers[i] = drivers.get(i);
        }
    }
}