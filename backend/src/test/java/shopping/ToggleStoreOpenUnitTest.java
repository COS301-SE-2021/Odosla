package shopping;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import shopping.exceptions.StoreClosedException;
import shopping.exceptions.StoreDoesNotExistException;
import shopping.repos.StoreRepo;
import shopping.requests.GetCatalogueRequest;
import shopping.requests.ToggleStoreOpenRequest;
import shopping.responses.GetCatalogueResponse;
import shopping.responses.ToggleStoreOpenResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ToggleStoreOpenUnitTest {

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
        i1=new Item("Heinz Tomatoe Sauce","123456","123456",storeUUID1,36.99,1,"description","img/");
        i2=new Item("Bar one","012345","012345",storeUUID1,14.99,3,"description","img/");
        listOfItems.add(i1);
        listOfItems.add(i2);
        c=new Catalogue(listOfItems);
        s=new Store(storeUUID1,"Woolworths",c,2,null,null,4,false);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Test for when Store with storeID does exist in database - should return correct store entity")
    @DisplayName("When Store with ID does exist")
    void UnitTest_Store_does_exist() throws InvalidRequestException, StoreDoesNotExistException, StoreClosedException {
        ToggleStoreOpenRequest request=new ToggleStoreOpenRequest(storeUUID1);
        when(storeRepo.findById(Mockito.any())).thenReturn(Optional.ofNullable(s));
        ToggleStoreOpenResponse response= shoppingService.toggleStoreOpen(request);
        assertNotNull(response);
        assertEquals(s.getOpen(),response.getOpen());
        assertEquals("Store is now open for business",response.getMessage());
    }
}
