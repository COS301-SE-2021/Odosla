package cs.superleague.importer.integration;

import cs.superleague.importer.ImporterServiceImpl;
import cs.superleague.importer.requests.ItemsCSVImporterRequest;
import cs.superleague.importer.responses.ItemsCSVImporterResponse;
import cs.superleague.integration.ServiceSelector;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.importer.exceptions.InvalidRequestException;
import cs.superleague.shopping.repos.ItemRepo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Description;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
public class ItemsCSVImporterIntegrationTest {

    @Autowired
    ImporterServiceImpl importerService;

    @Autowired
    ItemRepo itemRepo;

    ItemsCSVImporterRequest request;

    String importingItems;
    Item mockItem;

    @BeforeEach
    void setUp() {

        importingItems = "productid,barcode,brand,description,image_url,item_type,name,price,quantity,size,storeid\n";
        importingItems += "p234058925,60019578,All Gold,South Africa's firm favourite! It has a thick smooth texture that can easily be poured and enjoyed on a variety of dishes.,item/tomatoSauce.png,Sauce,Tomato Sauce,31.99,1,700ml,0fb0a357-63b9-41d2-8631-d11c67f7a27f\n";

        mockItem = new Item();
        mockItem.setProductID("p123984123");
        itemRepo.save(mockItem);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests for when ItemsCSVImporter is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void IntegrationTest_ItemsCSVImporterNullRequest() throws InvalidRequestException {
        request = null;

        assertThrows(InvalidRequestException.class, ()-> {
            ItemsCSVImporterResponse response = ServiceSelector.getImporterService().itemsCSVImporter(request);
        });
    }

    @Test
    @Description("Tests for when string in request object is null- exception should be thrown")
    @DisplayName("When request object parameter -file - is not specified")
    void IntegrationTest_ItemsCSVImporterWithNullParameterRequest() throws InvalidRequestException {
        request = new ItemsCSVImporterRequest(null);
        assertThrows(InvalidRequestException.class, ()-> {
            ItemsCSVImporterResponse response = ServiceSelector.getImporterService().itemsCSVImporter(request);
        });
    }

    @Test
    @Description("Tests for if a an item already does exist")
    @DisplayName("When file in parameter contains an item that already exists")
    void IntegrationTest_ItemAlreadyExists() throws InvalidRequestException {
        importingItems += "p123984123,6001068595808,Nestle,Thick milk chocolate with nougat and caramel centre.,item/barOne.png,Chocolate,Bar one,10.99,1,55g,0fb0a357-63b9-41d2-8631-d11c67f7a27f";
        request = new ItemsCSVImporterRequest(importingItems);
        assertThrows(InvalidRequestException.class, ()-> {
            ItemsCSVImporterResponse response = ServiceSelector.getImporterService().itemsCSVImporter(request);
        });
    }

    @Test
    @Description("Tests whether the request object was created correctly")
    @DisplayName("ItemsCSVImporterRequest correctly constructed")
    void IntegrationTest_ItemsCSVImporterRequestConstruction() {

        request = new ItemsCSVImporterRequest(importingItems);
        assertNotNull(request);
    }

    @Test
    @Description("Test for when File imports correctly")
    @DisplayName("When File imports successfully")
    void IntegrationTest_FileImportSuccessful() throws InvalidRequestException {

        request=new ItemsCSVImporterRequest(importingItems);
        ItemsCSVImporterResponse response= ServiceSelector.getImporterService().itemsCSVImporter(request);

        assertNotNull(response);
        assertTrue(response.isSuccess());

    }


}
