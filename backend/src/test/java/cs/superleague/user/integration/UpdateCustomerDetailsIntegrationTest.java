package cs.superleague.user.integration;

import cs.superleague.integration.security.JwtUtil;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.repos.ItemRepo;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.Customer;
import cs.superleague.user.dataclass.GroceryList;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.exceptions.AdminDoesNotExistException;
import cs.superleague.user.exceptions.CustomerDoesNotExistException;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.exceptions.UserDoesNotExistException;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.GroceryListRepo;
import cs.superleague.user.requests.UpdateCustomerDetailsRequest;
import cs.superleague.user.responses.UpdateCustomerDetailsResponse;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UpdateCustomerDetailsIntegrationTest {

    @Autowired
    CustomerRepo customerRepo;

    @Autowired
    GroceryListRepo groceryListRepo;

    @Autowired
    ItemRepo itemRepo;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    JwtUtil jwtTokenUtil;

    GroceryList groceryList;
    Customer customer, existingCustomer;
    Item I1;
    Item I2;

    UUID userID;
    UUID groceryListID;
    UUID expectedS1;

    GeoPoint deliveryAddress;

    List<Item> listOfItems = new ArrayList<>();
    List<GroceryList> groceryLists = new ArrayList<>();
    List<Item> shoppingCart = new ArrayList<>();
    List<Item> shoppingCartNULL = new ArrayList<>();

    BCryptPasswordEncoder passwordEncoder;
    UpdateCustomerDetailsRequest request;
    UpdateCustomerDetailsResponse response;

    String jwtTokenCustomer;

    Claims claims;

    @Value("${env.SECRET}")
    private String SECRET = "stub";

    @Value("${env.HEADER}")
    private String HEADER = "stub";

    @BeforeEach
    void setUp() {

        passwordEncoder = new BCryptPasswordEncoder(15);
        userID = UUID.randomUUID();
        groceryListID = UUID.randomUUID();
        expectedS1 = UUID.randomUUID();

        I1=new Item("Heinz Tamatoe Sauce","TTT123456","123456",expectedS1,36.99,1,"description","img/");
        I2=new Item("Bar one","012345","012345",expectedS1,14.99,3,"description","img/");
        listOfItems.add(I1);
        listOfItems.add(I2);

        shoppingCartNULL = null;
        shoppingCart.add(I1);
        shoppingCart.add(I2);

        deliveryAddress = new GeoPoint(2.0, 2.0, "2616 Urban Quarters, Hatfield");

        groceryList = new GroceryList(groceryListID, "Seamus' party", listOfItems);
        groceryLists.add(groceryList);

        customer = new Customer("D", "S", "ds@smallClub.com", "0721234567", passwordEncoder.encode("validPassword@1"), new Date(), "", "", "", true,
                UserType.CUSTOMER, userID, deliveryAddress, groceryLists, listOfItems, null, null);
        existingCustomer = new Customer("Davido", "Styles", "ds@smallSpursy.com", "0721234567", "", new Date(), "", "", "", true,
                UserType.CUSTOMER, UUID.randomUUID(), deliveryAddress, null, null, null, null);

        jwtTokenCustomer = jwtTokenUtil.generateJWTTokenCustomer(customer);
        System.out.println("FUCK "+HEADER);
        jwtTokenCustomer = jwtTokenCustomer.replace("Bearer ","");
                //.replace(HEADER,"");
        claims = Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtTokenCustomer).getBody();

        List<String> authorities = (List) claims.get("authorities");

        String userType= (String) claims.get("userType");
        String email = (String) claims.get("email");

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
                authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
        HashMap<String, Object> info=new HashMap<String, Object>();
        info.put("userType",userType);
        info.put("email",email);
        auth.setDetails(info);
        SecurityContextHolder.getContext().setAuthentication(auth);

        itemRepo.saveAll(listOfItems);
        groceryListRepo.saveAll(groceryLists);
        customerRepo.save(customer);
        customerRepo.save(existingCustomer);
    }

    @AfterEach
    void tearDown(){}


    @Test
    @DisplayName("When request object is not specified")
    void IntegrationTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateCustomerDetails(null));
        assertEquals("UpdateCustomer Request is null - Could not update customer", thrown.getMessage());
    }

    @Test
    @DisplayName("When customer with given Email does not exist")
    void IntegrationTest_testingInvalidUser(){
        customer.setEmail("superleague301@gmail.com");
        customerRepo.save(customer);


        request = new UpdateCustomerDetailsRequest("Dean", "Smith", "ds@SmallFC.com",
                "0712345678", customer.getPassword(), deliveryAddress, "currentPassword");

        Throwable thrown = Assertions.assertThrows(CustomerDoesNotExistException.class, ()-> userService.updateCustomerDetails(request));
        assertEquals("User with given Email does not exist - could not update customer", thrown.getMessage());
    }

    @Test
    @DisplayName("When an Invalid email is given")
    void IntegrationTest_testingInvalidEmail(){
        request = new UpdateCustomerDetailsRequest("Dean", "Smith", "dsSmallFC.com",
                "0712345678", customer.getPassword(), deliveryAddress, "currentPassword");

        try {
            response = userService.updateCustomerDetails(request);
            assertEquals("Email is not valid", response.getMessage());
            assertFalse(response.isSuccess());
            assertNotNull(response.getTimestamp());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When an Invalid password is given")
    void IntegrationTest_testingInvalidPassword(){
        request = new UpdateCustomerDetailsRequest( "Dean", "Smith", "ds@smallFC.com",
                "0712345678", "nerd", deliveryAddress, "validPassword@1");

        try {
            response = userService.updateCustomerDetails(request);
            assertEquals("Password is not valid", response.getMessage());
            assertFalse(response.isSuccess());
            assertNotNull(response.getTimestamp());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When null update values are given")
    void IntegrationTest_testingNullUpdates(){
        request = new UpdateCustomerDetailsRequest( null, null, null,
                null, null, null, null);

        try {
            response = userService.updateCustomerDetails(request);
            assertEquals("Null values submitted - Nothing updated", response.getMessage());
            assertFalse(response.isSuccess());
            assertNotNull(response.getTimestamp());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When user tries to update to existingEmail")
    void IntegrationTest_testingExistingEmailUpdateAttempt(){
        request = new UpdateCustomerDetailsRequest( "Dean", "Smith", "ds@smallSpursy.com",
                "0712345678", "loL7&lol", deliveryAddress, "currentPassword");

        try {
            response = userService.updateCustomerDetails(request);
            assertEquals("Email is already taken", response.getMessage());
            assertFalse(response.isSuccess());
            assertNotNull(response.getTimestamp());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When nonnull update values are given")
    void IntegrationTest_testingSuccessfulUpdate(){
        request = new UpdateCustomerDetailsRequest("Dean", "Smith", "ds@smallFC.com",
                "0712345678", "loL7&lol", deliveryAddress, "validPassword@1");

        try {
            response = userService.updateCustomerDetails(request);
            assertEquals("Customer successfully updated", response.getMessage());
            assertTrue(response.isSuccess());
            assertNotNull(response.getTimestamp());

            /* Ensure customer with same ID's details have been changed */
            Optional<Customer> checkCustomer=customerRepo.findById(userID);
            assertNotNull(checkCustomer);
            assertEquals(userID, checkCustomer.get().getCustomerID());
            assertEquals(request.getEmail(),checkCustomer.get().getEmail());
            assertEquals(request.getName(),checkCustomer.get().getName());
            assertEquals(request.getSurname(),checkCustomer.get().getSurname());
            assertEquals(request.getPhoneNumber(),checkCustomer.get().getPhoneNumber());
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(15);
            passwordEncoder.matches(request.getPassword(),checkCustomer.get().getPassword());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
}
