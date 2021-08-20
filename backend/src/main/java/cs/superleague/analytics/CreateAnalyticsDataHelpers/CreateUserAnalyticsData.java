package cs.superleague.analytics.CreateAnalyticsDataHelpers;

import cs.superleague.user.UserService;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.dataclass.User;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.requests.GetUsersRequest;
import cs.superleague.user.responses.GetUsersResponse;

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
    private UserService userService;

    private GetUsersResponse response;
    private UserType userType;

    private final Calendar startDate;
    private final Calendar endDate;
    private UUID adminID;

    public CreateUserAnalyticsData(Calendar startDate, Calendar endDate, UUID adminID, UserService userService){

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

        this.adminID = adminID;
        this.startDate = startDate;
        this.endDate = endDate;
        this.userService = userService;

        GetUsersRequest request = new GetUsersRequest(adminID.toString());
        try{
            response = this.userService.getUsers(request);
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
            data.put("startDate", this.startDate.getTime());
            data.put("endDate", this.endDate.getTime());
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

            totalUsers += 1;

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