package cs.superleague.importer;

import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.shopping.dataclass.Catalogue;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.importer.exceptions.InvalidRequestException;
import cs.superleague.importer.requests.ItemsCSVImporterRequest;
import cs.superleague.importer.requests.StoreCSVImporterRequest;
import cs.superleague.importer.responses.ItemsCSVImporterResponse;
import cs.superleague.importer.responses.StoreCSVImporterResponse;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.requests.SaveCatalogueToRepoRequest;
import cs.superleague.shopping.requests.SaveItemToRepoRequest;
import cs.superleague.shopping.requests.SaveStoreToRepoRequest;
import cs.superleague.shopping.responses.GetAllItemsResponse;
import cs.superleague.shopping.responses.GetStoreByUUIDResponse;
import cs.superleague.shopping.responses.GetStoresResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Service("importerServiceImpl")
public class ImporterServiceImpl implements ImporterService{
    @Value("${shoppingHost}")
    private String shoppingHost;
    @Value("${shoppingPort}")
    private String shoppingPort;
    private final RabbitTemplate rabbitTemplate;
    private final RestTemplate restTemplate;

    @Autowired
    public ImporterServiceImpl(RabbitTemplate rabbitTemplate, RestTemplate restTemplate){

        this.rabbitTemplate = rabbitTemplate;
        this.restTemplate = restTemplate;
    }

    @Override
    public ItemsCSVImporterResponse itemsCSVImporter(ItemsCSVImporterRequest request) throws InvalidRequestException, URISyntaxException {
        if (request != null)
        {
            List<Item> items = new ArrayList<>();
            UUID storeID = null;
            if(request.getFile() == null)
            {
                throw new InvalidRequestException("No file uploaded to import");
            }
            else
            {
                int k = 0;
                int index = request.getFile().indexOf('\n');
                String file = request.getFile().substring(index+1);
                String currentWord = "";
                int counter = 0;
                Item item = new Item();
                for(k=0; k < file.length(); k++)
                {
                    if(file.charAt(k) != ';' && file.charAt(k) != '\n' )
                    {
                        currentWord += file.charAt(k);
                    }
                    else if(file.charAt(k) == ';')
                    {
                        switch (counter){
                            case 0:

                                Map<String, Object> parts = new HashMap<String, Object>();

                                String stringUri = "http://"+shoppingHost+":"+shoppingPort+"/shopping/getAllItems";
                                URI uri = new URI(stringUri);

                                ResponseEntity<GetAllItemsResponse> responseEntity = restTemplate.postForEntity(
                                        uri, parts, GetAllItemsResponse.class);

                                if(responseEntity == null || !responseEntity.hasBody()){
                                    throw new InvalidRequestException("Could not retrieve Items");
                                }

                                GetAllItemsResponse getAllItemsResponse = responseEntity.getBody();
                                List<Item> itemList = getAllItemsResponse.getItems();

                                for(int j=0; j< itemList.size(); j++)
                                {
                                    if(itemList.get(j).getProductID().equals(currentWord))
                                    {
                                        throw new InvalidRequestException("Item already exists");
                                    }
                                }


                                item.setProductID(currentWord);
                                counter++;
                                currentWord = "";
                                break;

                            case 1:
                                item.setBarcode(currentWord);
                                counter++;
                                currentWord = "";
                                break;

                            case 2:
                                item.setBrand(currentWord);
                                counter++;
                                currentWord = "";
                                break;

                            case 3:
                                item.setDescription(currentWord);
                                counter++;
                                currentWord = "";
                                break;

                            case 4:
                                item.setImageUrl(currentWord);
                                counter++;
                                currentWord = "";
                                break;

                            case 5:
                                item.setItemType(currentWord);
                                counter++;
                                currentWord = "";
                                break;

                            case 6:
                                item.setName(currentWord);
                                counter++;
                                currentWord = "";
                                break;

                            case 7:
                                item.setPrice(Double.parseDouble(currentWord));
                                counter++;
                                currentWord = "";
                                break;

                            case 8:
                                item.setQuantity(Integer.parseInt(currentWord));
                                counter++;
                                currentWord = "";
                                break;

                            case 9:
                                item.setSize(currentWord);
                                counter++;
                                currentWord = "";
                                break;
                        }
                    }
                    else if (file.charAt(k) == '\n')
                    {
                        storeID= UUID.fromString(currentWord);
                        System.out.println(currentWord);
                        item.setStoreID(UUID.fromString(currentWord));
                        counter = 0;
                        currentWord = "";

                        SaveItemToRepoRequest saveItemToRepo = new SaveItemToRepoRequest(item);

                        items.add(item);

                        rabbitTemplate.convertAndSend("ShoppingEXCHANGE", "RK_SaveItemToRepo", saveItemToRepo);
                        item = new Item();
                    }
                }
            }

            Catalogue catalogue = new Catalogue(storeID, items);
            SaveCatalogueToRepoRequest saveCatalogueToRepoRequest = new SaveCatalogueToRepoRequest(catalogue);
            rabbitTemplate.convertAndSend("ShoppingEXCHANGE", "RK_SaveCatalogueToRepo", saveCatalogueToRepoRequest);

            Map<String, Object> parts = new HashMap<>();
            parts.put("storeID", storeID);
            String stringUri = "http://"+shoppingHost+":"+shoppingPort+"/shopping/getStoreByUUID";
            URI uri = new URI(stringUri);
            ResponseEntity<GetStoreByUUIDResponse> responseEntity = restTemplate.postForEntity(
                    uri, parts, GetStoreByUUIDResponse.class);

            if(responseEntity.getBody() != null) {
                Store store = responseEntity.getBody().getStore();
                store.setStock(catalogue);
                SaveStoreToRepoRequest saveStoreToRepoRequest = new SaveStoreToRepoRequest(store);
                rabbitTemplate.convertAndSend("ShoppingEXCHANGE", "RK_SaveStoreToRepo", saveStoreToRepoRequest);
            }
            else
            {
                throw new InvalidRequestException("Could not get store");
            }


            return new ItemsCSVImporterResponse(true, Calendar.getInstance().getTime(), "Items have been successfully imported.");

        }
        else
        {
            throw new InvalidRequestException("Request object is null");
        }

    }

    @Override
    public StoreCSVImporterResponse storeCSVImporter(StoreCSVImporterRequest request) throws InvalidRequestException, URISyntaxException {
        if (request != null)
        {
            if(request.getFile() == null)
            {
                throw new InvalidRequestException("No file uploaded to import");
            }
            else
            {
                int k = 0;
                int index = request.getFile().indexOf('\n');
                String file = request.getFile().substring(index+1);
                String currentWord = "";
                int counter = 0;
                Store store = new Store();
                String storeBrand = "";
                double latitude = 0;
                double longitude = 0;
                String storeAddress = "";
                String f = file;
                GeoPoint location= new GeoPoint();

                for(k=0; k < file.length(); k++)
                {
                    if(file.charAt(k) != ';' && file.charAt(k) != '\n' )
                    {
                        currentWord += file.charAt(k);
                    }
                    else if(file.charAt(k) == ';')
                    {
                        switch (counter){
                            case 0:

//                                int position = 6;
//
//
//                                int pos = f.indexOf(";");
//                                while(--position > 0 && pos != -1){
//                                    pos = f.indexOf(";", pos + 1);
//                                }
//
//                                // extract store brand from CSV file
//                                storeBrand = f.substring(pos).substring(1);
//                                storeBrand = storeBrand.split(";")[0];
//
//                                pos = f.indexOf(";", pos + 1);
//
//                                // extract latitude value from CSV file
//                                latitude = Double.parseDouble(f.substring(pos).substring(1).split(";")[0]
//                                        .replaceAll(",", "."));
//
//                                pos = f.indexOf(";", pos + 1);
//
//                                // extract longitude value from CSV file
//                                longitude = Double.parseDouble(f.substring(pos).substring(1).split(";")[0]
//                                        .replaceAll(",", "."));
//
//                                pos = f.indexOf(";", pos + 1);
//
//                                // extract address value from CSV file
//                                storeAddress = f.substring(pos).substring(1);
//                                pos = storeAddress.indexOf("\n");
//
//
//                                if(pos != -1) {
//                                    f = storeAddress.substring(pos);
//                                    storeAddress = storeAddress.split("\n")[0];
//                                }
//
//                                Map<String, Object> parts = new HashMap<>();
//
//                                String stringUri = "http://"+shoppingHost+":"+shoppingPort+"/shopping/getStores";
//                                URI uri = new URI(stringUri);
//
//                                ResponseEntity<GetStoresResponse> responseEntity = restTemplate.postForEntity(
//                                        uri, parts, GetStoresResponse.class);
//
//                                if(responseEntity == null || !responseEntity.hasBody()
//                                || responseEntity.getBody() == null){
//                                    return new StoreCSVImporterResponse(false, new Date(), "Could not retrieve stores");
//                                }
//
//                                List<Store> storeList = responseEntity.getBody().getStores();
//
//                                for (Store s: storeList) {
//                                    if (s.getStoreBrand().equals(storeBrand) &&
//                                            s.getStoreLocation().getAddress().equals(storeAddress) &&
//                                            s.getStoreLocation().getLatitude() == latitude &&
//                                            s.getStoreLocation().getLongitude() == longitude) {
//                                        return new StoreCSVImporterResponse(false, new Date(), "Store already exists");
//                                    }
//                                }

                                store.setStoreID(UUID.fromString(currentWord));
                                counter++;
                                currentWord = "";
                                break;

                            case 1:
                                store.setClosingTime(Integer.parseInt(currentWord));
                                counter++;
                                currentWord = "";
                                break;

                            case 2:
                                store.setImgUrl(currentWord);
                                counter++;
                                currentWord = "";
                                break;

                            case 3:
                                store.setOpen(Boolean.valueOf(currentWord));
                                counter++;
                                currentWord = "";
                                break;

                            case 4:
                                store.setMaxOrders(Integer.parseInt(currentWord));
                                counter++;
                                currentWord = "";
                                break;

                            case 5:
                                store.setMaxShoppers(Integer.parseInt(currentWord));
                                counter++;
                                currentWord = "";
                                break;

                            case 6:
                                store.setOpeningTime(Integer.parseInt(currentWord));
                                counter++;
                                currentWord = "";
                                break;

                            case 7:
                                store.setStoreBrand(currentWord);
                                counter++;
                                currentWord = "";
                                break;

                            case 8:

                                location.setLatitude(Double.parseDouble(currentWord.replaceAll(",", ".")));
                                counter++;
                                currentWord = "";
                                break;

                            case 9:
                                location.setLongitude(Double.parseDouble(currentWord.replaceAll(",", ".")));
                                counter++;
                                currentWord = "";
                                break;
                        }
                    }
                    else if (file.charAt(k) == '\n')
                    {
                        location.setAddress(currentWord);
                        counter=0;
                        currentWord = "";
                        store.setStoreLocation(location);

                        SaveStoreToRepoRequest saveStoreToRepo = new SaveStoreToRepoRequest(store);

                        rabbitTemplate.convertAndSend("ShoppingEXCHANGE", "RK_SaveStoreToRepo", saveStoreToRepo);

                        location= new GeoPoint();
                        store= new Store();

                    }
                }
            }
            return new StoreCSVImporterResponse(true, Calendar.getInstance().getTime(), "Stores have been successfully imported.");
        }
        else
        {
            throw new InvalidRequestException("Request object is null");
        }

    }
}
