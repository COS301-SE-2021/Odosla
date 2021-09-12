package cs.superleague.importer;

import cs.superleague.importer.stub.dataclass.GeoPoint;
import cs.superleague.importer.stub.dataclass.Item;
import cs.superleague.importer.exceptions.InvalidRequestException;
import cs.superleague.importer.requests.ItemsCSVImporterRequest;
import cs.superleague.importer.requests.StoreCSVImporterRequest;
import cs.superleague.importer.responses.ItemsCSVImporterResponse;
import cs.superleague.importer.responses.StoreCSVImporterResponse;
import cs.superleague.importer.stub.dataclass.Store;
import cs.superleague.importer.stub.requests.SaveItemToRepoRequest;
import cs.superleague.importer.stub.requests.SaveStoreToRepoRequest;
import cs.superleague.importer.stub.responses.GetAlItemsResponse;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Service("importerServiceImpl")
public class ImporterServiceImpl implements ImporterService{

    private RabbitTemplate rabbitTemplate;

    @Autowired
    public ImporterServiceImpl(RabbitTemplate rabbitTemplate){
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public ItemsCSVImporterResponse itemsCSVImporter(ItemsCSVImporterRequest request) throws InvalidRequestException {
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
                                RestTemplate restTemplate = new RestTemplate();

                                List<HttpMessageConverter<?>> converters = new ArrayList<>();
                                converters.add(new MappingJackson2HttpMessageConverter());
                                restTemplate.setMessageConverters(converters);

                                MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();

                                ResponseEntity<GetAlItemsResponse> responseEntity = restTemplate.postForEntity(
                                        "http://localhost:8088/shopping/getAllItems", parts, GetAlItemsResponse.class);

                                if(responseEntity == null || !responseEntity.hasBody()){
                                    throw new InvalidRequestException("Could not retrieve stores");
                                }

                                GetAlItemsResponse getAlItemsResponse = responseEntity.getBody();
                                List<Item> itemList = getAlItemsResponse.getItems();

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
                        item.setStoreID(UUID.fromString(currentWord));
                        counter = 0;
                        currentWord = "";

                        SaveItemToRepoRequest saveItemToRepo = new SaveItemToRepoRequest(item);

                        // saveitemtorepo

                        rabbitTemplate.convertAndSend("ShoppingEXCHANGE", "RK_saveItemToRepo", saveItemToRepo);
                        item = new Item();
                    }
                }
            }

            return new ItemsCSVImporterResponse(true, Calendar.getInstance().getTime(), "Items have been successfully imported.");

        }
        else
        {
            throw new InvalidRequestException("Request object is null");
        }

    }

    @Override
    public StoreCSVImporterResponse storeCSVImporter(StoreCSVImporterRequest request) throws InvalidRequestException {
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
                                RestTemplate restTemplate = new RestTemplate();

                                List<HttpMessageConverter<?>> converters = new ArrayList<>();
                                converters.add(new MappingJackson2HttpMessageConverter());
                                restTemplate.setMessageConverters(converters);

                                MultiValueMap<String, Object> parts = new LinkedMultiValueMap<String, Object>();

                                ResponseEntity<GetAlItemsResponse> responseEntity = restTemplate.postForEntity(
                                        "http://localhost:8088/shopping/getStores", parts, GetAlItemsResponse.class);

                                if(responseEntity == null || !responseEntity.hasBody()){
                                    throw new InvalidRequestException("Could not retrieve stores");
                                }

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
                                location.setLatitude(Double.parseDouble(currentWord));
                                counter++;
                                currentWord = "";
                                break;

                            case 9:
                                location.setLongitude(Double.parseDouble(currentWord));
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

                        rabbitTemplate.convertAndSend("ShoppingEXCHANGE", "RK_saveStoreToRepo", saveStoreToRepo);

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
