package cs.superleague.analytics.CreateAnalyticsDataHelpers;

import cs.superleague.integration.ServiceSelector;
import cs.superleague.user.UserService;
import cs.superleague.user.dataclass.*;
import cs.superleague.user.requests.GetUsersRequest;
import cs.superleague.user.responses.GetUsersResponse;

import java.util.*;

public class CreateUserAnalyticsData {

    private List<User> users;
    private List<Driver> drivers;
    private int totalUsers;
    private int totalCustomers;
    private int totalAdmins;
    private int totalShoppers;
    private double totalDrivers;
    private int driversOnShift;
    private int totalOrderCompleted;
    private double ratingSum;
    private Driver [] topDrivers;

    private GetUsersResponse response;
    private UserType userType;

    private Calendar startDate;
    private Calendar endDate;
    private UUID adminID;

    public CreateUserAnalyticsData(Calendar startDate, Calendar endDate, UUID adminID, UserService userService){

        users = new ArrayList<>();
        drivers = new ArrayList<>();

        totalUsers = 0;
        totalCustomers = 0;
        totalAdmins = 0;
        totalShoppers = 0;
        totalDrivers = 0;
        driversOnShift = 0;
        totalOrderCompleted = 0;
        ratingSum = 0;
        topDrivers = new Driver[10];

        this.adminID = adminID;
        this.startDate = startDate;
        this.endDate = endDate;

        try{
            response = userService.getUsers(new GetUsersRequest(adminID.toString()));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public HashMap<String, Object> getUserStatisticsData(){

        HashMap<String, Object> data = new HashMap<String, Object>();

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
//        data.put("startDate", this.startDate);
//        data.put("endDate", this.endDate);
        }

        return data;
    }

    private boolean generateUserStatisticsData(){

        if(response != null) {
            users.addAll(response.getUsers());
        }else{
            return false;
        }

        for (User user : this.users) {
            userType = user.getAccountType();

            if(userType == UserType.CUSTOMER){
                totalCustomers += 1;
            }else if(userType == UserType.SHOPPER){

                if(user instanceof Shopper){
                    totalOrderCompleted += ((Shopper) user).getOrdersCompleted();
                }

                totalShoppers += 1;
            }else if(userType == UserType.ADMIN){
                totalAdmins += 1;
            }else if(userType == UserType.DRIVER){

                if(user instanceof Driver){
                    if(((Driver) user).getOnShift()){
                        driversOnShift += 1;
                    }

                    drivers.add((Driver) user);
                    ratingSum += ((Driver) user).getRating();
                }
                totalDrivers += 1;
            }
        }

        ratingSum = ratingSum / totalDrivers;
        getTopDrivers(drivers);

        return true;
    }

    private void getTopDrivers(List<Driver> drivers){

        for (Driver driver: drivers) {
            if(driver.getRating() > topDrivers[0].getRating()){
                Driver temp;
                topDrivers[0] = driver;

                for (int i = 0; i < 10; i++) {
                    for (int j = i+1; j < 10; j++) {
                        if(topDrivers[i].getRating() > topDrivers[j].getRating()) {
                            temp = topDrivers[i];
                            topDrivers[i] = topDrivers[j];
                            topDrivers[j] = temp;
                        }
                    }
                }
            }
        }
    }
}