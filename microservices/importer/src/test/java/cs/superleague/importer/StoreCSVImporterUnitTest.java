//package cs.superleague.importer;
//
//import cs.superleague.importer.exceptions.InvalidRequestException;
//import cs.superleague.importer.requests.StoreCSVImporterRequest;
//import cs.superleague.importer.responses.StoreCSVImporterResponse;
//import cs.superleague.shopping.dataclass.Store;
//import cs.superleague.shopping.responses.GetStoresResponse;
//import org.junit.jupiter.api.*;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.Mockito;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.context.annotation.Description;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@ExtendWith(MockitoExtension.class)
//public class StoreCSVImporterUnitTest {
//
//    @InjectMocks
//    private ImporterServiceImpl importerService;
//
//    @Mock
//    private RestTemplate restTemplate;
//
//    @Mock
//    private RabbitTemplate rabbitTemplate;
//
//    String uri;
//    String importingStores;
//
//    Store store;
//    Store store2;
//    List<Store> storesNew;
//    List<Store> storesExists;
//
//    @BeforeEach
//    void setUp() {
//
//        uri = "http://localhost:8088/shopping/getStores";
//
//        importingStores = "storeid;closing_time;img_url;is_open;max_orders;max_shoppers;opening_time;store_brand;latitude;longitude;address\n";
//        importingStores += "0fb0a357-63b9-41d2-8631-d11c67f7a27f;16;shop/pnp.png;true;5;2;7;Pick n Pay;-25.770344;28.2429369;Pick n Pay Brooklyn\n";
//
//        store = new Store();
//        store2 = new Store();
//
//        store.setStoreID(UUID.fromString("0fb0a357-63b9-41d2-8631-d11c67f7a27f"));
//        store2.setStoreID(UUID.randomUUID());
//
//        storesExists = new ArrayList<>();
//        storesNew = new ArrayList<>();
//
//        storesExists.add(store);
//        storesNew.add(store2);
//    }
//
//    @AfterEach
//    void tearDown() {
//    }
//
//    @Test
//    @Description("Tests for when StoresCSVImporter is submitted with a null request object- exception should be thrown")
//    @DisplayName("When request object is not specified")
//    void UnitTest_testingNullRequestObject(){
//        Throwable thrown = Assertions.assertThrows(cs.superleague.importer.exceptions.InvalidRequestException.class, ()-> importerService.storeCSVImporter(null));
//        assertEquals("Request object is null", thrown.getMessage());
//    }
//
//    @Test
//    @Description("Tests for when string in request object is null- exception should be thrown")
//    @DisplayName("When request object parameter -file - is not specified")
//    void UnitTest_testingNull_file_Parameter_RequestObject(){
//        StoreCSVImporterRequest request=new StoreCSVImporterRequest(null);
//        Throwable thrown = Assertions.assertThrows(cs.superleague.importer.exceptions.InvalidRequestException.class, ()-> importerService.storeCSVImporter(request));
//        assertEquals("No file uploaded to import", thrown.getMessage());
//    }
//
//
//    @Test
//    @Description("Test for when Item Exists")
//    @DisplayName("When item exists")
//    void UnitTest_Item_Exists(){
//
//        GetStoresResponse getStoresResponse = new GetStoresResponse(false,
//                "Could not retrieve store", storesExists);
//
//        ResponseEntity<GetStoresResponse> responseEntity = new ResponseEntity<>(
//                getStoresResponse, HttpStatus.OK);
//
//        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
//
//        Mockito.when(restTemplate.postForEntity(uri,
//                parts, GetStoresResponse.class)).thenReturn(responseEntity);
//
//        StoreCSVImporterRequest request = new StoreCSVImporterRequest(importingStores);
//
//        Throwable thrown = Assertions.assertThrows(InvalidRequestException.class,
//                ()-> importerService.storeCSVImporter(request));
//        assertEquals("Store already exists", thrown.getMessage());
//    }
//
//
//    @Test
//    @Description("Test for when File imports correctly")
//    @DisplayName("When File imports successfully")
//    void UnitTest_File_Imports_Successfully() throws InvalidRequestException {
//
//
//        GetStoresResponse getStoresResponse = new GetStoresResponse(true,
//                "Could not retrieve store", storesNew);
//
//        ResponseEntity<GetStoresResponse> responseEntity = new ResponseEntity<>(
//                getStoresResponse, HttpStatus.OK);
//
//        MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
//
//        Mockito.when(restTemplate.postForEntity(uri,
//                parts, GetStoresResponse.class)).thenReturn(responseEntity);
//
//        StoreCSVImporterRequest request = new StoreCSVImporterRequest(importingStores);
//        StoreCSVImporterResponse response= importerService.storeCSVImporter(request);
//        assertNotNull(response);
//        assertEquals("Stores have been successfully imported.", response.getMessage());
//        assertTrue(response.isSuccess());
//    }
//
//    /** Checking request object is created correctly */
//    @Test
//    @Description("Tests whether the request object was created correctly")
//    @DisplayName("StoresCSVImporterRequest correctly constructed")
//    void UnitTest_StoresCSVImporterRequestConstruction() {
//
//        StoreCSVImporterRequest request=new StoreCSVImporterRequest(importingStores);
//
//        assertNotNull(request);
//        assertEquals(importingStores, request.getFile());
//    }
//}