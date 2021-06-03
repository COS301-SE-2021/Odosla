package shopping;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;
import shopping.dataclass.Catalogue;
import shopping.dataclass.Item;
import shopping.dataclass.Store;
import shopping.exceptions.InvalidRequestException;
import shopping.exceptions.StoreDoesNotExistException;
import shopping.repos.StoreRepo;
import shopping.requests.GetCatalogueRequest;
import shopping.requests.GetStoreByUUIDRequest;
import shopping.responses.GetCatalogueResponse;
import shopping.responses.GetStoreByUUIDResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GetCatalogueUnitTest {
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

    /*
     * setUp() initializes each object before the functions are called.
     * */
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
    }

    @Test
    @Description("Tests for when getCatalogue is submited with a null request object- exception should be thrown")
    @DisplayName("When request object is not specificed")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.getCatalogue(null));
        assertEquals("The request object for GetCatalaogueRequest is null - Could not get catalogue from shop", thrown.getMessage());
    }

    @Test
    @Description("Tests for when storeID in request object is null- exception should be thrown")
    @DisplayName("When request object parameter -storeID - is not specificed")
    void UnitTest_testingNull_storeID_Parameter_RequestObject(){
        GetCatalogueRequest request=new GetCatalogueRequest(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.getCatalogue(request));
        assertEquals("The Store ID in GetCatalogueRequest parameter is null - Could not get catalogue from shop", thrown.getMessage());
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
        GetCatalogueRequest request=new GetCatalogueRequest(storeUUID1);
        when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(s));
        GetCatalogueResponse response= shoppingService.getCatalogue(request);
        assertNotNull(response);
        assertEquals(s.getStock(),response.getCatalogue());
        assertEquals("Catalogue entity from store was correctly returned",response.getMessage());
    }

}
