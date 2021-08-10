package cs.superleague.user;

import cs.superleague.user.dataclass.Admin;
import cs.superleague.user.exceptions.AdminDoesNotExistException;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.AdminRepo;
import cs.superleague.user.requests.UpdateAdminDetailsRequest;
import cs.superleague.user.responses.UpdateAdminDetailsResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UpdateAdminDetailsUnitTest {


    @Mock
    AdminRepo adminRepo;

    @InjectMocks
    private UserServiceImpl userService;

    UpdateAdminDetailsRequest request;
    UUID adminId=UUID.randomUUID();
    Admin admin;
    UpdateAdminDetailsResponse response;

    @BeforeEach
    void setUp() {
        request=new UpdateAdminDetailsRequest(adminId,"name","surname","email@gmail.com","password","phoneNumber");
        admin=new Admin();
    }

    @AfterEach
    void tearDown(){}

    @Test
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateAdminDetails(null));
        assertEquals("UpdateAdmin Request is null - Could not update admin", thrown.getMessage());
    }

    @Test
    @DisplayName("When userID parameter is not specified")
    void UnitTest_testingNullRequestUserIDParameter(){
        request.setAdminID(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateAdminDetails(request));
        assertEquals("AdminId is null - could not update admin", thrown.getMessage());
    }

    @Test
    @DisplayName("Request object created correctly")
    void UnitTest_requestObjectCorrectlyCreated(){
        UpdateAdminDetailsRequest req=new UpdateAdminDetailsRequest(adminId,"n","s","e","pass","pN");
        assertNotNull(req);
        assertEquals(adminId,req.getAdminID());
        assertEquals("n",req.getName());
        assertEquals("s",req.getSurname());
        assertEquals("e",req.getEmail());
        assertEquals("pass",req.getPhoneNumber());
        assertEquals("pN",req.getPassword());
    }

    @Test
    @DisplayName("When admin with given UserID does not exist")
    void UnitTest_testingInvalidUser(){
        when(adminRepo.findById(Mockito.any())).thenReturn(null);
        Throwable thrown = Assertions.assertThrows(AdminDoesNotExistException.class, ()-> userService.updateAdminDetails(request));
        assertEquals("User with given userID does not exist - could not update admin", thrown.getMessage());
    }

    @Test
    @DisplayName("When an Invalid email is given")
    void UnitTest_testingInvalidEmail(){
        request.setEmail("invalid");
        when(adminRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(admin));

        try {
            response = userService.updateAdminDetails(request);
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
    void UnitTest_testingInvalidPassword(){

        when(adminRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(admin));

        try {
            response = userService.updateAdminDetails(request);
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
    void UnitTest_testingNullUpdates(){
        request = new UpdateAdminDetailsRequest(adminId, null, null, null,
                null, null);
        when(adminRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(admin));

        try {
            response = userService.updateAdminDetails(request);
            assertEquals("Null values submitted - Nothing updated", response.getMessage());
            assertFalse(response.isSuccess());
            assertNotNull(response.getMessage());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }

    @Test
    @DisplayName("When user tries to update to existingEmail")
    void UnitTest_testingExistingEmailUpdateAttempt(){
        admin.setEmail("validEmail@gmail.com");
        when(adminRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(admin));
        when(adminRepo.findAdminByEmail(Mockito.any())).thenReturn(admin);
        try {
            response = userService.updateAdminDetails(request);
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
    void UnitTest_testingSuccessfulUpdate(){

        request.setPassword("validPassword@1");
        when(adminRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(admin));

        try {
            response = userService.updateAdminDetails(request);
            assertEquals("Admin successfully updated", response.getMessage());
            assertTrue(response.isSuccess());
            assertNotNull(response.getTimestamp());
        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
}
