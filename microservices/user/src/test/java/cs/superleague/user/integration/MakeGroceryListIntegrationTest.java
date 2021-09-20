//package cs.superleague.user.integration;
//
//import cs.superleague.integration.security.JwtUtil;
//import cs.superleague.shopping.dataclass.Catalogue;
//import cs.superleague.shopping.dataclass.Item;
//import cs.superleague.shopping.dataclass.Store;
//import cs.superleague.user.UserServiceImpl;
//import cs.superleague.user.dataclass.Customer;
//import cs.superleague.user.dataclass.GroceryList;
//import cs.superleague.user.dataclass.Shopper;
//import cs.superleague.user.dataclass.UserType;
//import cs.superleague.user.exceptions.InvalidRequestException;
//import cs.superleague.user.repos.CustomerRepo;
//import cs.superleague.user.repos.GroceryListRepo;
//import cs.superleague.user.repos.ShopperRepo;
//import cs.superleague.user.requests.MakeGroceryListRequest;
//import cs.superleague.user.responses.MakeGroceryListResponse;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
//public class MakeGroceryListIntegrationTest {
//
//    @Autowired
//    CustomerRepo customerRepo;
//
//    @Autowired
//    GroceryListRepo groceryListRepo;
//
//    @Autowired
//    ShopperRepo shopperRepo;
//
//    @Autowired
//    private UserServiceImpl userService;
//
//    @Autowired
//    JwtUtil jwtUtil;
//
//    GroceryList groceryList;
//    Customer customer;
//    Shopper shopper;
//    Item I1;
//    Item I2;
//    Item item;
//    Catalogue catalogue;
//    Catalogue notExistCatalogue;
//    Store store;
//
//    UUID customerID;
//    UUID shopperID;
//    UUID groceryListID;
//    UUID expectedS1;
//
//    String jwtTokenCustomer;
//    String jwtTokenShopper;
//
//    List<Item> listOfItems = new ArrayList<>();
//    List<Item> notExistItems = new ArrayList<>();
//    List<String> listOfBarcodes = new ArrayList<>();
//    List<Store> listOfStores = new ArrayList<>();
//    List<Store> notExistStores = new ArrayList<>();
//    List<GroceryList> groceryLists = new ArrayList<>();
//
//    @Value("${env.SECRET}")
//    private String SECRET = "stub";
//
//    @Value("${env.HEADER}")
//    private String HEADER = "stub";
//
//    @BeforeEach
//    void setUp() {
//
//        customerID = UUID.randomUUID();
//        shopperID = UUID.randomUUID();
//        groceryListID = UUID.randomUUID();
//        expectedS1 = UUID.randomUUID();
//
//        shopper = new Shopper();
//        shopper.setShopperID(shopperID);
//        shopper.setName("JJ");
//        shopper.setAccountType(UserType.SHOPPER);
//        shopper.setEmail("customer@gmailp.com");
//        shopperRepo.save(shopper);
//
//        I1=new Item("Heinz Tamatoe Sauce","123456","123456",expectedS1,36.99,1,"description","img/");
//        I2=new Item("Bar one","012345","012345",expectedS1,14.99,3,"description","img/");
//        item = null;
//
//        listOfItems.add(I1);
//        notExistItems.add(I2);
//
//        itemRepo.save(I1);
//
//
//        groceryList = new GroceryList(groceryListID, "Seamus' party", listOfItems);
//        groceryLists.add(groceryList);
//        groceryListRepo.save(groceryList);
//
//        customer = new Customer();
//        customer.setCustomerID(customerID);
//        customer.setName("Pep");
//        customer.setEmail("customer@gmaill.com");
//        customer.setGroceryLists(groceryLists);
//        customer.setAccountType(UserType.CUSTOMER);
//        customer.setGroceryLists(groceryLists);
//        customerRepo.save(customer);
//
//        jwtTokenCustomer = jwtUtil.generateJWTTokenCustomer(customer);
//        jwtTokenShopper = jwtUtil.generateJWTTokenShopper(shopper);
//
//        listOfBarcodes.add("123456");
//
//        catalogue = new Catalogue(UUID.randomUUID(),listOfItems);
//        notExistCatalogue = new Catalogue(UUID.randomUUID(), notExistItems);
//        store = new Store(expectedS1,"Checkers",catalogue,2,null,null,4,true);
//        listOfStores.add(store);
//        notExistStores.add(new Store(expectedS1,"Checkers",notExistCatalogue,2,null,null,4,true));
//
//        catalogueRepo.save(catalogue);
//        storeRepo.save(store);
//
//        String jwt = jwtUtil.generateJWTTokenCustomer(customer);
//        jwt = jwt.replace(HEADER,"");
//        Claims claims= Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwt).getBody();
//        List<String> authorities = (List) claims.get("authorities");
//        String userType= (String) claims.get("userType");
//        String email = (String) claims.get("email");
//        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
//                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
//        HashMap<String, Object> info=new HashMap<String, Object>();
//        info.put("userType",userType);
//        info.put("email",email);
//        auth.setDetails(info);
//        SecurityContextHolder.getContext().setAuthentication(auth);
//    }
//
//    @AfterEach
//    void tearDown(){
//        SecurityContextHolder.clearContext();
//    }
//
//
//    @Test
//    @DisplayName("When request object is not specified")
//    void IntegrationTest_testingNullRequestObject(){
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.makeGroceryList(null));
//        assertEquals("MakeGroceryList Request is null - could not make grocery list", thrown.getMessage());
//    }
//
//    @Test
//    @DisplayName("When barcodes parameter is not specified")
//    void IntegrationTest_testingNullRequestBarcodesParameter(){
//        MakeGroceryListRequest request  = new MakeGroceryListRequest(null, "Seamus' Party");
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.makeGroceryList(request));
//        assertEquals("Barcodes list empty - could not make the grocery list", thrown.getMessage());
//    }
//
//    @Test
//    @DisplayName("When name parameter is not specified")
//    void IntegrationTest_testingNullRequestNameParameter(){
//        MakeGroceryListRequest request  = new MakeGroceryListRequest( listOfBarcodes, null);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.makeGroceryList(request));
//        assertEquals("Grocery List Name is Null - could not make the grocery list", thrown.getMessage());
//    }
//
//
//
//    @Test
//    @DisplayName("When the barcodes do not exist")
//    void IntegrationTest_testing_Barcodes_DoneExist(){
//        List<String> list= new ArrayList<>();
//        list.add("6543fskh");
//        MakeGroceryListRequest request  = new MakeGroceryListRequest( list, "Seamus' bachelor party");
//
//        try{
//            MakeGroceryListResponse response = userService.makeGroceryList(request);
//            assertEquals("Cannot find item with given productID - could not make the grocery list", response.getMessage());
//            assertFalse(response.isSuccess());
//        }catch(Exception e){
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    @Test
//    @DisplayName("When the groceryList Creation is successful")
//    void IntegrationTest_testingSuccessfulGroceryListCreation(){
//        MakeGroceryListRequest request  = new MakeGroceryListRequest(listOfBarcodes, "Seamus' bachelor party");
//
//        try{
//            MakeGroceryListResponse response = userService.makeGroceryList(request);
//            assertEquals("Grocery List successfully created", response.getMessage());
//            assertTrue(response.isSuccess());
//        }catch(Exception e){
//            e.printStackTrace();
//            fail();
//        }
//    }
//}