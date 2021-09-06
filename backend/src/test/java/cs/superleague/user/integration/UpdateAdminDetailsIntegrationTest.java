package cs.superleague.user.integration;

import cs.superleague.integration.security.JwtUtil;
import cs.superleague.user.UserServiceImpl;
import cs.superleague.user.dataclass.Admin;
import cs.superleague.user.dataclass.UserType;
import cs.superleague.user.exceptions.AdminDoesNotExistException;
import cs.superleague.user.exceptions.InvalidRequestException;
import cs.superleague.user.repos.AdminRepo;
import cs.superleague.user.requests.UpdateAdminDetailsRequest;
import cs.superleague.user.responses.UpdateAdminDetailsResponse;
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

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UpdateAdminDetailsIntegrationTest {

    @Autowired
    AdminRepo adminRepo;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    JwtUtil jwtTokenUtil;

    BCryptPasswordEncoder passwordEncoder;
    UpdateAdminDetailsRequest request;
    UUID adminId = UUID.randomUUID();
    Admin admin;
    UpdateAdminDetailsResponse response;

    String jwtTokenAdmin;

    Claims claims;

    @Value("${env.SECRET}")
    private final String SECRET = "stub";

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder(15);

        request = new UpdateAdminDetailsRequest("name","surname","email@gmail.com","phoneNumber","password", "validPassword@1");
        admin = new Admin("", "", "validEmail@gmail.com", "0721234567", passwordEncoder.encode("validPassword@1"), "", UserType.ADMIN, adminId);
        admin.setAccountType(UserType.ADMIN);

        jwtTokenAdmin = jwtTokenUtil.generateJWTTokenAdmin(admin).replace("Bearer ","");
        System.out.println(jwtTokenAdmin);
        claims = Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtTokenAdmin).getBody();

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

        adminRepo.save(admin);
    }

    @AfterEach
    void tearDown(){

    }

    @Test
    @DisplayName("When request object is not specified")
    void IntegrationTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> userService.updateAdminDetails(null));
        assertEquals("UpdateAdmin Request is null - Could not update admin", thrown.getMessage());
    }

    @Test
    @DisplayName("Request object created correctly")
    void IntegrationTest_requestObjectCorrectlyCreated(){
        UpdateAdminDetailsRequest req=new UpdateAdminDetailsRequest("n","s","e","pass","pN", "currentPassword");
        assertNotNull(req);
        assertEquals("n",req.getName());
        assertEquals("s",req.getSurname());
        assertEquals("e",req.getEmail());
        assertEquals("pass",req.getPhoneNumber());
        assertEquals("pN",req.getPassword());
        assertEquals("currentPassword", req.getCurrentPassword());
    }

    @Test
    @DisplayName("When admin with given Email does not exist")
    void IntegrationTest_testingInvalidUser(){
        admin.setEmail("superleague301@gmail.com");
        adminRepo.save(admin);

        Throwable thrown = Assertions.assertThrows(AdminDoesNotExistException.class, ()-> userService.updateAdminDetails(request));
        assertEquals("User with given Email does not exist - could not update admin", thrown.getMessage());
    }

    @Test
    @DisplayName("When an Invalid email is given")
    void IntegrationTest_testingInvalidEmail(){
        request.setEmail("invalid");
        admin.setAdminID(adminId);
        adminRepo.save(admin);

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
    void IntegrationTest_testingInvalidPassword(){

        admin.setAdminID(adminId);
        admin.setPassword(passwordEncoder.encode(request.getCurrentPassword()));
        adminRepo.save(admin);

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
    void IntegrationTest_testingNullUpdates(){
        request = new UpdateAdminDetailsRequest(null, null, null,
                null, null, "currentPassword");

        admin.setAdminID(adminId);
        adminRepo.save(admin);

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
    void IntegrationTest_testingExistingEmailUpdateAttempt(){
        admin.setEmail("validEmail@gmail.com");
        admin.setAdminID(adminId);
        adminRepo.save(admin);

        Admin newAdmin = new Admin();
        newAdmin.setEmail(request.getEmail());
        newAdmin.setAdminID(UUID.randomUUID());
        adminRepo.save(newAdmin);

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
    void IntegrationTest_testingSuccessfulUpdate(){

        request.setPassword("validPassword@3");

        try {
            response = userService.updateAdminDetails(request);
            assertEquals("Admin successfully updated", response.getMessage());
            assertTrue(response.isSuccess());
            assertNotNull(response.getTimestamp());

            /* Ensure admin with same ID's details have been changed */
            Optional<Admin> checkAdmin=adminRepo.findById(adminId);
            assertNotNull(checkAdmin);
            assertEquals(adminId, checkAdmin.get().getAdminID());
            assertEquals(request.getEmail(),checkAdmin.get().getEmail());
            assertEquals(request.getName(),checkAdmin.get().getName());
            assertEquals(request.getSurname(),checkAdmin.get().getSurname());
            assertEquals(request.getPhoneNumber(),checkAdmin.get().getPhoneNumber());
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(15);
            passwordEncoder.matches(request.getPassword(),checkAdmin.get().getPassword());

        }catch(Exception e){
            e.printStackTrace();
            fail();
        }
    }
}
