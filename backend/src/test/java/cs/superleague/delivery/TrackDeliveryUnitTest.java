package cs.superleague.delivery;

import cs.superleague.delivery.repos.DeliveryDetailRepo;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.user.repos.AdminRepo;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.DriverRepo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

@ExtendWith(MockitoExtension.class)
public class TrackDeliveryUnitTest {
    @InjectMocks
    private DeliveryServiceImpl deliveryService;

    @Mock
    DeliveryRepo deliveryRepo;
    @Mock
    DeliveryDetailRepo deliveryDetailRepo;
    @Mock
    AdminRepo adminRepo;
    @Mock
    DriverRepo driverRepo;
    @Mock
    CustomerRepo customerRepo;
    @Mock
    StoreRepo storeRepo;

    @BeforeEach
    void setUp(){

    }

    @AfterEach
    void tearDown(){

    }

    @Test
    @Description("Tests that the request object is created successfully.")
    @DisplayName("Request object creation.")
    void checkRequestObjectCreation_UnitTest(){

    }
}
