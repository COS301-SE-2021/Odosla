package cs.superleague.importer;

import cs.superleague.importer.requests.StoreCSVImporterRequest;
import cs.superleague.importer.responses.StoreCSVImporterResponse;
import cs.superleague.shopping.repos.StoreRepo;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class StoreCSVImporterUnitTest {

    @Mock
    private StoreRepo storeRepo;

    @InjectMocks
    private ImporterServiceImpl importerService;

    String importingStores;

    @BeforeEach
    void setUp() {

        importingStores="storeid,closing_time,img_url,is_open,max_orders,max_shoppers,opening_time,store_brand,latitude,longitude,address\n";
        importingStores+= "0fb0a357-63b9-41d2-8631-d11c67f7a27f,16,shop/pnp.png,t,5,2,7,Pick n Pay,-25.770344,28.2429369,Pick n Pay Brooklyn\n";

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests for when StoresCSVImporter is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(cs.superleague.importer.exceptions.InvalidRequestException.class, ()-> importerService.storeCSVImporter(null));
        assertEquals("Request object is null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when string in request object is null- exception should be thrown")
    @DisplayName("When request object parameter -file - is not specified")
    void UnitTest_testingNull_file_Parameter_RequestObject(){
        StoreCSVImporterRequest request=new StoreCSVImporterRequest(null);
        Throwable thrown = Assertions.assertThrows(cs.superleague.importer.exceptions.InvalidRequestException.class, ()-> importerService.storeCSVImporter(request));
        assertEquals("No file uploaded to import", thrown.getMessage());
    }


    @Test
    @Description("Test for when File imports correctly")
    @DisplayName("When File imports successfully")
    void UnitTest_File_Imports_Successfully() throws cs.superleague.importer.exceptions.InvalidRequestException {
        StoreCSVImporterRequest request=new StoreCSVImporterRequest(importingStores);
        StoreCSVImporterResponse response= importerService.storeCSVImporter(request);
        assertNotNull(response);
        assertEquals("Stores have been successfully imported.", response.getMessage());
        assertTrue(response.isSuccess());

    }

    /** Checking request object is created correctly */
    @Test
    @Description("Tests whether the request object was created correctly")
    @DisplayName("StoresCSVImporterRequest correctly constructed")
    void UnitTest_StoresCSVImporterRequestConstruction() {

        StoreCSVImporterRequest request=new StoreCSVImporterRequest(importingStores);

        assertNotNull(request);
        assertEquals(importingStores, request.getFile());
    }
}
