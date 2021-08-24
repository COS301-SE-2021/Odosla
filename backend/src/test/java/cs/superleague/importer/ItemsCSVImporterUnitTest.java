package cs.superleague.importer;

import cs.superleague.importer.requests.ItemsCSVImporterRequest;
import cs.superleague.importer.responses.ItemsCSVImporterResponse;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.exceptions.InvalidRequestException;
import cs.superleague.shopping.repos.ItemRepo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class ItemsCSVImporterUnitTest {
    @Mock
    private ItemRepo itemRepo;

    @InjectMocks
    private ImporterServiceImpl importerService;

    String importingItems;
    Item mockItem;
    @BeforeEach
    void setUp() {

        importingItems="productid,barcode,brand,description,image_url,item_type,name,price,quantity,size,storeid\n";
        importingItems+= "p234058925,60019578,All Gold,\"South Africa''s firm favourite! It has a thick, smooth texture that can easily be poured and enjoyed on a variety of dishes.\",item/tomatoSauce.png,Sauce,Tomato Sauce,31.99,1,700ml,0fb0a357-63b9-41d2-8631-d11c67f7a27f\n";

        mockItem = new Item();
        mockItem.setProductID("p234058925");

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests for when ItemsCSVImporter is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> importerService.itemsCSVImporter(null));
        assertEquals("Request object is null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when string in request object is null- exception should be thrown")
    @DisplayName("When request object parameter -file - is not specified")
    void UnitTest_testingNull_file_Parameter_RequestObject(){
        ItemsCSVImporterRequest request=new ItemsCSVImporterRequest(null);
        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class, ()-> importerService.itemsCSVImporter(request));
        assertEquals("No file uploaded to import", thrown.getMessage());
    }


    @Test
    @Description("Test for when File imports correctly")
    @DisplayName("When File imports successfully")
    void UnitTest_File_Imports_Successfully() throws cs.superleague.importer.exceptions.InvalidRequestException {
        ItemsCSVImporterRequest request=new ItemsCSVImporterRequest(importingItems);
        ItemsCSVImporterResponse response= importerService.itemsCSVImporter(request);
        assertNotNull(response);
        assertEquals("Items have been successfully imported.", response.getMessage());
        assertTrue(response.isSuccess());

    }

    /** Checking request object is created correctly */
    @Test
    @Description("Tests whether the request object was created correctly")
    @DisplayName("ItemsCSVImporterRequest correctly constructed")
    void UnitTest_GetStoreRequestConstruction() {

        ItemsCSVImporterRequest request=new ItemsCSVImporterRequest(importingItems);

        assertNotNull(request);
        assertEquals(importingItems, request.getFile());
    }
}
