package cs.superleague.shopping;

import cs.superleague.shopping.requests.UpdateCatalogueRequest;
import cs.superleague.shopping.responses.UpdateCatalogueResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;
import cs.superleague.shopping.dataclass.*;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.exceptions.StoreDoesNotExistException;
import cs.superleague.shopping.repos.StoreRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UpdateCatalogueUnitTest {
    @Mock
    private StoreRepo storeRepo;

    @InjectMocks
    private ShoppingServiceImpl shoppingService;

    UUID storeUUID1= UUID.randomUUID();
    Store s;
    Catalogue c;
    Catalogue c2;
    Item i1;
    Item i2;
    Item i3;
    Item i4;
    List<Item> listOfItems=new ArrayList<>();
    List<Item> listOfItems2=new ArrayList<>();

    @BeforeEach
    void setUp() {
        i1=new Item("Heinz Tomato Sauce","123456","123456",storeUUID1,36.99,1,"description","img/");
        i2=new Item("Bar one","012345","012345",storeUUID1,14.99,3,"description","img/");
        i3=new Item("Milk","901234","901234",storeUUID1,30.00,1,"description","img/");
        i4=new Item("Bread","890123","890123",storeUUID1,36.99,1,"description","img/");
        listOfItems.add(i1);
        listOfItems.add(i2);
        c=new Catalogue(storeUUID1, listOfItems);
        listOfItems2.add(i1);
        listOfItems2.add(i2);
        listOfItems2.add(i3);
        listOfItems2.add(i4);
        c2=new Catalogue(storeUUID1, listOfItems2);
        s=new Store(storeUUID1,"Woolworths",c,2,null,null,4,true);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests for when UpdateCatalogue is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.updateCatalogue(null));
        assertEquals("The request object for GetCatalogueRequest is null - Could not update catalogue for the shop", thrown.getMessage());
    }

    @Test
    @Description("Tests for whether a request is submitted with a null parameter for storeID in request object- exception should be thrown")
    @DisplayName("When request object parameter -storeID - is not specified")
    void UnitTest_testingNull_storeID_Parameter_RequestObject(){
        UpdateCatalogueRequest request=new UpdateCatalogueRequest(null, c2);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.updateCatalogue(request));
        assertEquals("The Store ID in UpdateCatalogueRequest parameter is null - Could not update catalogue for the shop", thrown.getMessage());
    }

    @Test
    @Description("Tests for whether a request is submitted with a null parameter for catalogue in request object- exception should be thrown")
    @DisplayName("When request object parameter -catalogue - is not specified")
    void UnitTest_testingNull_catalogue_Parameter_RequestObject(){
        UpdateCatalogueRequest request=new UpdateCatalogueRequest(storeUUID1, null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> shoppingService.updateCatalogue(request));
        assertEquals("The Catalogue in UpdateCatalogueRequest parameter is null - Could not update catalogue for the shop", thrown.getMessage());
    }

    @Test
    @Description("Test for when Store with storeID does not exist in database - StoreDoesNotExist Exception should be thrown")
    @DisplayName("When Store with ID doesn't exist")
    void UnitTest_Store_doesnt_exist(){
        UpdateCatalogueRequest request=new UpdateCatalogueRequest(storeUUID1, c2);
        when(storeRepo.findById(Mockito.any())).thenReturn(null);
        Throwable thrown = Assertions.assertThrows(StoreDoesNotExistException.class, ()-> shoppingService.updateCatalogue(request));
        assertEquals("Store with ID does not exist in repository - could not update Catalog entity", thrown.getMessage());
    }

    @Test
    @Description("Test for when Store with storeID does exist in database - should return correct store entity")
    @DisplayName("When Store with ID does exist and catalogue changes")
    void UnitTest_Store_does_exist_changing_catalogue() throws InvalidRequestException, StoreDoesNotExistException {
        UpdateCatalogueRequest request=new UpdateCatalogueRequest(storeUUID1, c2);
        when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(s));
        UpdateCatalogueResponse response= shoppingService.updateCatalogue(request);
        assertNotNull(response);
        assertEquals(true,response.isResponse());
        assertEquals("Catalogue updated for the store",response.getMessage());
        assertEquals(storeUUID1, response.getStoreID());
        assertEquals(s.getStock(), c2);
    }

    @Test
    @Description("Test for when Store with storeID does exist in database - should return correct store entity")
    @DisplayName("When Store with ID does exist but catalogue is not changing")
    void UnitTest_Store_does_exist_updating_to_same_catalogue() throws InvalidRequestException, StoreDoesNotExistException {
        UpdateCatalogueRequest request=new UpdateCatalogueRequest(storeUUID1, c);
        when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(s));
        UpdateCatalogueResponse response= shoppingService.updateCatalogue(request);
        assertNotNull(response);
        assertEquals(true,response.isResponse());
        assertEquals("Catalogue updated for the store",response.getMessage());
        assertEquals(storeUUID1, response.getStoreID());
        assertEquals(s.getStock(), c);
    }

    /** Checking request object is created correctly */
    @Test
    @Description("Tests whether the UpdateCatalogue request object was created correctly")
    @DisplayName("UpdateCatalogueRequest correctly constructed")
    void UnitTest_UpdateCatalogueRequestConstruction() {
        UpdateCatalogueRequest request = new UpdateCatalogueRequest(storeUUID1, c);
        assertNotNull(request);
        assertEquals(storeUUID1, request.getStoreID());
        assertEquals(c, request.getCatalogue());
    }
}
