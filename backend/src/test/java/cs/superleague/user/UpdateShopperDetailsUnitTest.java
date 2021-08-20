//package cs.superleague.user;
//
//import cs.superleague.user.dataclass.Shopper;
//import cs.superleague.user.exceptions.InvalidRequestException;
//import cs.superleague.user.exceptions.ShopperDoesNotExistException;
//import cs.superleague.user.repos.ShopperRepo;
//import cs.superleague.user.requests.UpdateShopperDetailsRequest;
//import cs.superleague.user.responses.UpdateShopperDetailsResponse;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.junit.jupiter.api.Assertions.fail;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class UpdateShopperDetailsUnitTest {
//
//    @Mock
//    ShopperRepo shopperRepo;
//
//    @InjectMocks
//    private UserServiceImpl userService;
//
//    UpdateShopperDetailsRequest request;
//    UUID shopperId= UUID.randomUUID();
//    Shopper shopper;
//    UpdateShopperDetailsResponse response;
//    @BeforeEach
//    void setUp(){
//        request=new UpdateShopperDetailsRequest(shopperId,"name","surname","email@gmail.com","password","phoneNumber");
//        shopper=new Shopper();
//    }
//
//    @AfterEach
//    void tearDown(){}
//
//    @Test
//    @DisplayName("When request object is not specified")
//    void UnitTest_testingNullRequestObject(){
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateShopperDetails(null));
//        assertEquals("UpdateShopper Request is null - Could not update shopper", thrown.getMessage());
//    }
//
//    @Test
//    @DisplayName("When userID parameter is not specified")
//    void UnitTest_testingNullRequestUserIDParameter(){
//        request.setShopperID(null);
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateShopperDetails(request));
//        assertEquals("ShopperId is null - could not update shopper", thrown.getMessage());
//    }
//
//    @Test
//    @DisplayName("Request object created correctly")
//    void UnitTest_requestObjectCorrectlyCreated(){
//        UUID storeID=UUID.randomUUID();
//        UpdateShopperDetailsRequest req=new UpdateShopperDetailsRequest(shopperId,"n","s","e","pass","pN",storeID);
//        assertNotNull(req);
//        assertEquals(shopperId,req.getShopperID());
//        assertEquals("n",req.getName());
//        assertEquals("s",req.getSurname());
//        assertEquals("e",req.getEmail());
//        assertEquals("pass",req.getPassword());
//        assertEquals("pN",req.getPhoneNumber());
//        assertEquals(storeID,req.getStoreID());
//    }
//
//    @Test
//    @DisplayName("When shopper with given UserID does not exist")
//    void UnitTest_testingInvalidUser(){
//        when(shopperRepo.findById(Mockito.any())).thenReturn(null);
//        Throwable thrown = Assertions.assertThrows(ShopperDoesNotExistException.class, ()-> userService.updateShopperDetails(request));
//        assertEquals("User with given userID does not exist - could not update shopper", thrown.getMessage());
//    }
//
//    @Test
//    @DisplayName("When an Invalid email is given")
//    void UnitTest_testingInvalidEmail(){
//        request.setEmail("invalid");
//        when(shopperRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(shopper));
//
//        try {
//            response = userService.updateShopperDetails(request);
//            assertEquals("Email is not valid", response.getMessage());
//            assertFalse(response.isSuccess());
//            assertNotNull(response.getTimestamp());
//        }catch(Exception e){
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    @Test
//    @DisplayName("When an Invalid password is given")
//    void UnitTest_testingInvalidPassword(){
//
//        when(shopperRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(shopper));
//
//        try {
//            response = userService.updateShopperDetails(request);
//            assertEquals("Password is not valid", response.getMessage());
//            assertFalse(response.isSuccess());
//            assertNotNull(response.getTimestamp());
//        }catch(Exception e){
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    @Test
//    @DisplayName("When null update values are given")
//    void UnitTest_testingNullUpdates(){
//        request = new UpdateShopperDetailsRequest(shopperId, null, null, null,
//                null, null, null);
//        when(shopperRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(shopper));
//
//        try {
//            response = userService.updateShopperDetails(request);
//            assertEquals("Null values submitted - Nothing updated", response.getMessage());
//            assertFalse(response.isSuccess());
//            assertNotNull(response.getMessage());
//        }catch(Exception e){
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    @Test
//    @DisplayName("When user tries to update to existingEmail")
//    void UnitTest_testingExistingEmailUpdateAttempt(){
//        shopper.setEmail("validEmail@gmail.com");
//        when(shopperRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(shopper));
//        when(shopperRepo.findByEmail(Mockito.any())).thenReturn(Optional.ofNullable(shopper));
//        try {
//            response = userService.updateShopperDetails(request);
//            assertEquals("Email is already taken", response.getMessage());
//            assertFalse(response.isSuccess());
//            assertNotNull(response.getTimestamp());
//        }catch(Exception e){
//            e.printStackTrace();
//            fail();
//        }
//    }
//
//    @Test
//    @DisplayName("When nonnull update values are given")
//    void UnitTest_testingSuccessfulUpdate(){
//
//        request.setPassword("validPassword@1");
//        when(shopperRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(shopper));
//
//        try {
//            response = userService.updateShopperDetails(request);
//            assertEquals("Shopper successfully updated", response.getMessage());
//            assertTrue(response.isSuccess());
//            assertNotNull(response.getTimestamp());
//        }catch(Exception e){
//            e.printStackTrace();
//            fail();
//        }
//    }
//}
