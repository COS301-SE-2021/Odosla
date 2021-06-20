package cs.superleague.shopping.integration;

import cs.superleague.shopping.ShoppingServiceImpl;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Shopper;
import cs.superleague.user.repos.ShopperRepo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
public class GetShoppersIntegrationTest {


    @Autowired
    ShoppingServiceImpl shoppingService;

    //OPTIONAL SERVICES
    @Autowired
    UserServiceImpl userService;

    @Autowired
    StoreRepo storeRepo;

    @Autowired
    ShopperRepo shopperRepo;


    UUID storeUUID1= UUID.randomUUID();
    Store store;
    Shopper shopper;
    Shopper shopper1;
    Shopper shopper2;
    UUID shopperID=UUID.randomUUID();
    UUID shopperID2=UUID.randomUUID();
    UUID shopperID3=UUID.randomUUID();
    UUID storeID=UUID.randomUUID();
    List<Shopper> shopperList=new ArrayList<>();

    @BeforeEach
    void setUp() {
        store=new Store();
        shopper=new Shopper();
        shopper.setId(shopperID);
        shopper1=new Shopper();
        shopper1.setId(shopperID2);
        shopper2=new Shopper();
        shopper2.setId(shopperID3);
        shopperList.add(shopper1);
        shopperList.add(shopper2);
        shopperRepo.save(shopper1);
        shopperRepo.save(shopper2);
    }

    @AfterEach
    void tearDown() {

        storeRepo.deleteAll();
        shopperRepo.deleteAll();
    }


}
