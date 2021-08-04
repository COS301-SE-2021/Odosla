package cs.superleague.user;

import cs.superleague.shopping.ShoppingServiceImpl;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.AdminRepo;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.DriverRepo;
import cs.superleague.user.repos.ShopperRepo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class LoginUserUnitTest {
    @Mock
    ShopperRepo shopperRepo;
    @Mock
    DriverRepo driverRepo;
    @Mock
    CustomerRepo customerRepo;
    @Mock
    AdminRepo adminRepo;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setup(){}

    @AfterEach
    void teardown(){}

    @Test
    @Description("Tests for when login is submited with a null request object- exception should be thrown")
    @DisplayName("When request object is not specificed")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.loginUser(null));
        assertEquals("LoginRequest is null", thrown.getMessage());
    }


}
