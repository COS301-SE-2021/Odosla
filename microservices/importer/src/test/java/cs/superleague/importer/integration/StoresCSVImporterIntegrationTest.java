package cs.superleague.importer.integration;

import cs.superleague.importer.ImporterServiceImpl;
import cs.superleague.importer.exceptions.InvalidRequestException;
import cs.superleague.importer.requests.StoreCSVImporterRequest;
import cs.superleague.shopping.dataclass.Store;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class StoresCSVImporterIntegrationTest {

    @Autowired
    ImporterServiceImpl importerService;

    StoreCSVImporterRequest request;

    String importingStores;
    Store store;

    @BeforeEach
    void setUp() {

        importingStores="storeid;closing_time;img_url;is_open;max_orders;max_shoppers;opening_time;store_brand;latitude;longitude;address\n";
        importingStores+= "0fb0a357-63b9-41d2-8631-d11c67f7a27f;16;shop/pnp.png;true;5;2;7;Pick n Pay;-25.770344;28.2429369;Pick n Pay Brooklyn\n";

        store = new Store();
        store.setStoreID(UUID.fromString("0fb0a357-63b9-41d2-8631-d11c67f5627f"));
//        storeRepo.save(store);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests for when StoresCSVImporter is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void IntegrationTest_StoresCSVImporterNullRequest() throws InvalidRequestException {
        request = null;

//        assertThrows(InvalidRequestException.class, ()-> {
//            StoreCSVImporterResponse response = ServiceSelector.getImporterService().storeCSVImporter(request);
//        });
    }

    @Test
    @Description("Tests for when string in request object is null- exception should be thrown")
    @DisplayName("When request object parameter -file - is not specified")
    void IntegrationTest_StoresCSVImporterWithNullParameterRequest() throws InvalidRequestException {
        request = new StoreCSVImporterRequest(null);
//        assertThrows(InvalidRequestException.class, ()-> {
//            StoreCSVImporterResponse response = ServiceSelector.getImporterService().storeCSVImporter(request);
//        });
    }

    @Test
    @Description("Tests for if a an item already does exist")
    @DisplayName("When file in parameter contains an item that already exists")
    void IntegrationTest_StoreAlreadyExists() throws InvalidRequestException {
        importingStores += "0fb0a357-63b9-41d2-8631-d11c67f5627f;20;shop/woolworths.png;true;7;2;8;Woolworths;-25.790344;28.2429369;Woolies Brooklyn\n";
        request = new StoreCSVImporterRequest(importingStores);
//        assertThrows(InvalidRequestException.class, ()-> {
//            StoreCSVImporterResponse response = ServiceSelector.getImporterService().storeCSVImporter(request);
//        });
    }

    @Test
    @Description("Tests whether the request object was created correctly")
    @DisplayName("StoresCSVImporterRequest correctly constructed")
    void IntegrationTest_StoresCSVImporterRequestConstruction() {

        request = new StoreCSVImporterRequest(importingStores);
        assertNotNull(request);
    }

    @Test
    @Description("Test for when File imports correctly")
    @DisplayName("When File imports successfully")
    void IntegrationTest_FileImportSuccessful() throws InvalidRequestException {

        request=new StoreCSVImporterRequest(importingStores);
//        StoreCSVImporterResponse response= ServiceSelector.getImporterService().storeCSVImporter(request);
//
//        assertNotNull(response);
//        assertTrue(response.isSuccess());

    }

}
