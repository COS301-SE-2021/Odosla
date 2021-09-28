package cs.superleague.analytics.CreateAnalyticsDataHelpers;

import cs.superleague.analytics.exceptions.InvalidRequestException;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.dataclass.User;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.responses.GetAdminsResponse;
import cs.superleague.user.responses.GetCustomersResponse;
import cs.superleague.user.responses.GetDriversResponse;
import cs.superleague.user.responses.GetShoppersResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

public class CreateUserAnalyticsData {

    private final String userHost;
    private final String userPort;

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
    private Driver[] topDrivers;

    private final Date startDate;
    private final Date endDate;

    private ResponseEntity<GetAdminsResponse> adminResponseEntity;
    private ResponseEntity<GetCustomersResponse> customerResponseEntity;
    private ResponseEntity<GetDriversResponse> driverResponseEntity;
    private ResponseEntity<GetShoppersResponse> shopperResponseEntity;

    public CreateUserAnalyticsData(Date startDate, Date endDate,
                                   RestTemplate restTemplate, String userHost,
                                   String userPort) throws URISyntaxException {

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

        this.userHost = userHost;
        this.userPort = userPort;

        String stringUri = "http://" + userHost + ":" + userPort + "/user/getAdmins";
        URI uri = new URI(stringUri);

        try {

            Map<String, Object> parts = new HashMap<>();

            adminResponseEntity = restTemplate.postForEntity(uri, parts,
                    GetAdminsResponse.class);

            stringUri = "http://" + userHost + ":" + userPort + "/user/getCustomers";
            uri = new URI(stringUri);

            customerResponseEntity = restTemplate.postForEntity(uri, parts,
                    GetCustomersResponse.class);

            stringUri = "http://" + userHost + ":" + userPort + "/user/getDrivers";
            uri = new URI(stringUri);

            driverResponseEntity = restTemplate.postForEntity(uri, parts,
                    GetDriversResponse.class);

            stringUri = "http://" + userHost + ":" + userPort + "/user/getShoppers";
            uri = new URI(stringUri);

            shopperResponseEntity = restTemplate.postForEntity(uri, parts,
                    GetShoppersResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, Object> getUserStatisticsData() throws InvalidRequestException {

        HashMap<String, Object> data = new HashMap<>();

        if (generateUserStatisticsData()) {
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

        if (adminResponseEntity == null || customerResponseEntity == null
                || driverResponseEntity == null || shopperResponseEntity == null) {
            return false;
        }

        GetAdminsResponse adminsResponse = adminResponseEntity.getBody();
        GetCustomersResponse customersResponse = customerResponseEntity.getBody();
        GetDriversResponse driversResponse = driverResponseEntity.getBody();
        GetShoppersResponse shoppersResponse = shopperResponseEntity.getBody();

        if (adminsResponse == null || customersResponse == null || driversResponse == null
                || shoppersResponse == null) {
            return false;
        }

        if (adminsResponse.getUsers() != null)
            users.addAll(adminsResponse.getUsers());

        if (customersResponse.getUsers() != null)
            users.addAll(customersResponse.getUsers());

        if (driversResponse.getUsers() != null)
            users.addAll(driversResponse.getUsers());

        if (shoppersResponse.getUsers() != null)
            users.addAll(shoppersResponse.getUsers());

        UserType userType;
        for (User user : this.users) {
            userType = user.getAccountType();

            if (startDate == null || endDate == null) {
                throw new InvalidRequestException("Start Date and End Date cannot be null");
            }

            if (user.getActivationDate() != null)
                if (startDate.getTime() <= user.getActivationDate().getTime()
                        && endDate.getTime() >= user.getActivationDate().getTime()) {
                    if (userType == UserType.CUSTOMER) {
                        totalCustomers += 1;
                    } else if (userType == UserType.SHOPPER) {

                        if (user instanceof Shopper) {
                            totalOrderCompleted += ((Shopper) user).getOrdersCompleted();
                            System.out.println("Shopper:" + totalOrderCompleted);
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

        if (totalDrivers > 0) {
            ratingSum = ratingSum / totalDrivers;
        }

        getTopDrivers(drivers);

        return true;
    }

    private void getTopDrivers(List<Driver> drivers) {

        int size = Math.min(drivers.size(), 10);

        topDrivers = new Driver[size];

        drivers.sort(Comparator.comparing(Driver::getRating).reversed());

        for (int i = 0; i < size; i++) {
            topDrivers[i] = drivers.get(i);
        }
    }
}