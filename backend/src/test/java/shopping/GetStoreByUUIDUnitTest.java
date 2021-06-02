package shopping;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;
import payment.PaymentServiceImpl;
import payment.dataclass.OrderType;
import payment.repos.OrderRepo;
import payment.requests.SubmitOrderRequest;
import shopping.dataclass.*;
import shopping.exceptions.InvalidRequestException;
import shopping.exceptions.StoreDoesNotExistException;
import shopping.repos.StoreRepo;
import shopping.requests.GetStoreByUUIDRequest;
import shopping.responses.GetStoreByUUIDResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetStoreByUUIDUnitTest {

    @Mock
    private StoreRepo storeRepo;

    @InjectMocks
    private ShoppingServiceImpl shoppingService;

    UUID storeUUID1= UUID.randomUUID();
    Store s;
    Catalogue c;
    Item i1;
    Item i2;
    List<Item> listOfItems=new ArrayList<>();
    @BeforeEach
    void setUp() {
        i1=new Item("Heinz Tamatoe Sauce","123456","123456",storeUUID1,36.99,1,"description","img/");
        i2=new Item("Bar one","012345","012345",storeUUID1,14.99,3,"description","img/");
        listOfItems.add(i1);
        listOfItems.add(i2);
        c=new Catalogue(listOfItems);
        s=new Store(storeUUID1,"Woolworthes",c,2,null,null,4);
    }
    @AfterEach
    void tearDown() {
        storeRepo.deleteAll();
    }

    @Test
    @Description("Tests for when getStoreByUUID is submited with a null request object- exception should be thrown")
    @DisplayName("When request object is not specificed")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.getStoreByUUID(null));
        assertEquals("GetStoreByUUID request is null - could not return store entity", thrown.getMessage());
    }

    @Test
    @Description("Tests for whether an order is submited with a null parameter for storeID in request object- exception should be thrown")
    @DisplayName("When request object parameter -userID - is not specificed")
    void UnitTest_testingNull_storeID_Parameter_RequestObject(){
        List<Item> list=new ArrayList<>();
        GetStoreByUUIDRequest request=new GetStoreByUUIDRequest(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.getStoreByUUID(request));
        assertEquals("StoreID is null in GetStoreByUUIDRequest request - could not return store entity", thrown.getMessage());
    }

    @Test
    @Description("Test for when Store with storeID does not exist in database - StoreDoesNotExist Exception should be thrown")
    @DisplayName("When Store with ID doesn't exist")
    void UnitTest_Store_doesnt_exist(){
        GetStoreByUUIDRequest request=new GetStoreByUUIDRequest(storeUUID1);
        when(storeRepo.findById(Mockito.any())).thenReturn(null);
        Throwable thrown = Assertions.assertThrows(StoreDoesNotExistException.class, ()-> shoppingService.getStoreByUUID(request));
        assertEquals("Store with ID does not exist in repository - could not get Store entity", thrown.getMessage());
    }

    @Test
    @Description("Test for when Store with storeID does exist in database - should return correct store entity")
    @DisplayName("When Store with ID does exist")
    void UnitTest_Store_does_exist() throws InvalidRequestException, StoreDoesNotExistException {
        GetStoreByUUIDRequest request=new GetStoreByUUIDRequest(storeUUID1);
        when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(s));
        GetStoreByUUIDResponse response= shoppingService.getStoreByUUID(request);
        assertNotNull(response);
        assertEquals(s,response.getStore());
        assertEquals(response.getMessage(),"Store entity with corresponding id was returned");
    }

}
