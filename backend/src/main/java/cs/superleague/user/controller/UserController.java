package cs.superleague.user.controller;

import cs.superleague.api.UserApi;
import cs.superleague.integration.ServiceSelector;
import cs.superleague.models.*;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Driver;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.repos.AdminRepo;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.DriverRepo;
import cs.superleague.user.repos.ShopperRepo;
import cs.superleague.user.requests.*;
import cs.superleague.user.responses.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.UUID;

@CrossOrigin
@RestController
public class UserController implements UserApi {

    @Autowired
    UserServiceImpl userService;
    @Autowired
    DriverRepo driverRepo;
    @Autowired
    AdminRepo adminRepo;
    @Autowired
    CustomerRepo customerRepo;
    @Autowired
    ShopperRepo shopperRepo;

    @Override
    public ResponseEntity<UserLoginResponse> loginUser(UserLoginRequest body){

        Driver driver=new Driver();
        UUID driverId= UUID.randomUUID();
        String driverEmail="driverEmail@gmail.com";
        String password="Kelcat@01";
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(15);
        String passwordHashed=passwordEncoder.encode(password);

        driver.setDriverID(driverId);
        driver.setEmail(driverEmail);
        driver.setPassword(passwordHashed);
        UserType userType = null;
        driverRepo.save(driver);
        UserLoginResponse response=new UserLoginResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            if (body.getUserType().equals("DRIVER")) {
                userType = UserType.DRIVER;
            } else if (body.getUserType().equals("CUSTOMER")) {
                userType = UserType.CUSTOMER;
            } else if (body.getUserType().equals("SHOPPER")) {
                userType = UserType.SHOPPER;
            } else if (body.getUserType().equals("ADMIN")) {
                userType = UserType.ADMIN;
            }

            String emailRequest=body.getEmail();
            String passwordRequest=body.getPassword();


            LoginRequest request = new LoginRequest(body.getEmail(), body.getPassword(), userType);

            LoginResponse loginResponse= ServiceSelector.getUserService().loginUser(request);

            try {
                response.setMessage(loginResponse.getMessage());
                response.setToken(loginResponse.getToken());
                response.setSuccess(loginResponse.isSuccess());
                response.setDate(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(loginResponse.getTimestamp()));

            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        driverRepo.deleteAll();
        shopperRepo.deleteAll();
        customerRepo.deleteAll();
        adminRepo.deleteAll();
        return new ResponseEntity<>(response, httpStatus);

    }

    @Override
    public ResponseEntity<UserRegisterDriverResponse> registerDriver(UserRegisterDriverRequest body) {
        UserRegisterDriverResponse response=new UserRegisterDriverResponse();
        HttpStatus httpStatus=HttpStatus.OK;
        try{

            RegisterDriverRequest request=new RegisterDriverRequest(body.getName(), body.getSurname(), body.getEmail(), body.getPhoneNumber(), body.getPassword());
            RegisterDriverResponse driverResponse=ServiceSelector.getUserService().registerDriver(request);

            try{
                response.setMessage(driverResponse.getMessage());
                response.setDate(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(driverResponse.getTimestamp()));
                response.setSuccess(driverResponse.isSuccess());
            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(response,httpStatus);
    }

    @Override
    public ResponseEntity<UserRegisterCustomerResponse> registerCustomer(UserRegisterCustomerRequest body) {
        UserRegisterCustomerResponse response=new UserRegisterCustomerResponse();
        HttpStatus httpStatus=HttpStatus.OK;
        try{

            RegisterCustomerRequest request=new RegisterCustomerRequest(body.getName(), body.getSurname(), body.getEmail(), body.getPhoneNumber(), body.getPassword());
            RegisterCustomerResponse customerResponse=ServiceSelector.getUserService().registerCustomer(request);

            try{
                response.setMessage(customerResponse.getMessage());
                response.setDate(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(customerResponse.getTimestamp()));
                response.setSuccess(customerResponse.isSuccess());
            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(response,httpStatus);
    }

    @Override
    public ResponseEntity<UserRegisterAdminResponse> registerAdmin(UserRegisterAdminRequest body) {
        UserRegisterAdminResponse response=new UserRegisterAdminResponse();
        HttpStatus httpStatus=HttpStatus.OK;
        try{

            RegisterAdminRequest request=new RegisterAdminRequest(body.getName(), body.getSurname(), body.getEmail(), body.getPhoneNumber(), body.getPassword());
            RegisterAdminResponse adminResponse=ServiceSelector.getUserService().registerAdmin(request);

            try{
                response.setMessage(adminResponse.getMessage());
                response.setDate(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(adminResponse.getTimestamp()));
                response.setSuccess(adminResponse.isSuccess());
            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(response,httpStatus);
    }

    @Override
    public ResponseEntity<UserRegisterShopperResponse> registerShopper(UserRegisterShopperRequest body) {
        UserRegisterShopperResponse response=new UserRegisterShopperResponse();
        HttpStatus httpStatus=HttpStatus.OK;
        try{

            RegisterShopperRequest request=new RegisterShopperRequest(body.getName(), body.getSurname(), body.getEmail(), body.getPhoneNumber(), body.getPassword());
            RegisterShopperResponse shopperResponse=ServiceSelector.getUserService().registerShopper(request);

            try{
                response.setMessage(shopperResponse.getMessage());
                response.setDate(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(shopperResponse.getTimestamp()));
                response.setSuccess(shopperResponse.isSuccess());
            }catch(Exception e){
                e.printStackTrace();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(response,httpStatus);
    }


    @Override
    public ResponseEntity<UserSetCartResponse> setCart(UserSetCartRequest body) {
        return null;
    }
}
