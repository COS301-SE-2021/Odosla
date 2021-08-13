package cs.superleague.integration;

import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.integration.security.JwtUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class JwtTokenTest {
    @Mock
    CustomerRepo customerRepo;

    @InjectMocks
    private UserServiceImpl userService;

    UUID uuid=UUID.randomUUID();
    @BeforeEach
    void setUp() {}

    @AfterEach
    void tearDown(){}

    @Test
    @DisplayName("Test 1")
    void test(){
        Customer customer=new Customer();
        customer.setAccountType(UserType.CUSTOMER);
        customer.setCustomerID(uuid);
        customer.setEmail("testEmail@gmail.com");

        JwtUtil c=new JwtUtil();
        String token=c.generateJWTTokenCustomer(customer);
        System.out.println(token);
        Date hi=c.extractExpiration(token);
        System.out.println(hi);
        System.out.println(c.extractUserType(token));

        System.out.println( c.validateToken(token,customer))
       ;
    }
}
