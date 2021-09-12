package cs.superleague.importer;

import cs.superleague.importer.exceptions.InvalidRequestException;
import cs.superleague.importer.requests.ItemsCSVImporterRequest;
import cs.superleague.importer.responses.ItemsCSVImporterResponse;
import cs.superleague.importer.stub.shopping.dataclass.Item;
import cs.superleague.importer.stub.shopping.responses.GetAllItemsResponse;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Description;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ItemsCSVImporterUnitTest {

    @InjectMocks
    private ImporterServiceImpl importerService;

    @Mock
    RestTemplate restTemplate;

    @Mock
    RabbitTemplate rabbitTemplate;

    String uri;
    String importingItems;
    Item mockItem;
    List<Item> mockItems;

    @BeforeEach
    void setUp() {

        uri = "http://localhost:8088/shopping/getAllItems";

        importingItems = "productid;barcode;brand;description;image_url;item_type;name;price;quantity;size;storeid\n";
        importingItems += "p234058925;60019578;All Gold;South Africa's firm favourite! It has a thick smooth texture that can easily be poured and enjoyed on a variety of dishes.;item/tomatoSauce.png;Sauce;Tomato Sauce;31.99;1;700ml;0fb0a357-63b9-41d2-8631-d11c67f7a27f\n";

        mockItem = new Item();
        mockItems = new ArrayList<>();

        mockItem.setBarcode("60019578");
        mockItem.setItemType("Sauce");
        mockItem.setProductID("p234058925");
        mockItem.setBrand("All Gold");
        mockItem.setDescription("South Africa's firm favourite! It has a thick smooth texture that can easily be poured and enjoyed on a variety of dishes.");
        mockItem.setImageUrl("item/tomatoSauce.png");
        mockItem.setName("Tomato Sauce");
        mockItem.setPrice(31.99);
        mockItem.setQuantity(1);
        mockItem.setSize("700ml");
        mockItem.setStoreID(UUID.fromString("0fb0a357-63b9-41d2-8631-d11c67f7a27f"));

        mockItems.add(mockItem);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @Description("Tests for when ItemsCSVImporter is submitted with a null request object- exception should be thrown")
    @DisplayName("When request object is not specified")
    void UnitTest_testingNullRequestObject(){
        Throwable thrown = Assertions.assertThrows(cs.superleague.importer.exceptions.InvalidRequestException.class, ()-> importerService.itemsCSVImporter(null));
        assertEquals("Request object is null", thrown.getMessage());
    }

    @Test
    @Description("Tests for when string in request object is null- exception should be thrown")
    @DisplayName("When request object parameter -file - is not specified")
    void UnitTest_testingNull_file_Parameter_RequestObject(){
        ItemsCSVImporterRequest request=new ItemsCSVImporterRequest(null);
        Throwable thrown = Assertions.assertThrows(cs.superleague.importer.exceptions.InvalidRequestException.class, ()-> importerService.itemsCSVImporter(request));
        assertEquals("No file uploaded to import", thrown.getMessage());
    }

    @Test
    @Description("Test for when Item Exists")
    @DisplayName("When item exists")
    void UnitTest_Item_Exists(){

        GetAllItemsResponse getAllItemsResponse = new GetAllItemsResponse(mockItems);

        ResponseEntity<GetAllItemsResponse> responseEntity = new ResponseEntity<>(
                getAllItemsResponse, HttpStatus.OK);

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

        Mockito.when(restTemplate.postForEntity(uri,
                parts, GetAllItemsResponse.class)).thenReturn(responseEntity);

        ItemsCSVImporterRequest request = new ItemsCSVImporterRequest(importingItems);

        Throwable thrown = Assertions.assertThrows(cs.superleague.importer.exceptions
                .InvalidRequestException.class, ()-> importerService.itemsCSVImporter(request));
        assertEquals("Item already exists", thrown.getMessage());
    }


    @Test
    @Description("Test for when File imports correctly")
    @DisplayName("When File imports successfully")
    void UnitTest_File_Imports_Successfully() throws InvalidRequestException {

        mockItems.clear();
        mockItem.setProductID("p234058926");
        mockItems.add(mockItem);

        GetAllItemsResponse getAlItemsResponse = new GetAllItemsResponse(mockItems);

        ResponseEntity<GetAllItemsResponse> responseEntity = new ResponseEntity<>(
                getAlItemsResponse, HttpStatus.OK);

        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();

        Mockito.when(restTemplate.postForEntity(uri,
                parts, GetAllItemsResponse.class)).thenReturn(responseEntity);

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
    void UnitTest_ItemsCSVImporterRequestConstruction() {

        ItemsCSVImporterRequest request=new ItemsCSVImporterRequest(importingItems);

        assertNotNull(request);
        assertEquals(importingItems, request.getFile());
    }
}
