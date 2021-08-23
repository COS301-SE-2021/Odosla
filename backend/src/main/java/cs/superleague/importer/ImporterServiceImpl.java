package cs.superleague.importer;

import antlr.debug.NewLineEvent;
import cs.superleague.delivery.repos.DeliveryDetailRepo;
import cs.superleague.delivery.repos.DeliveryRepo;
import cs.superleague.importer.exceptions.InvalidRequestException;
import cs.superleague.importer.requests.ItemsCSVImporterRequest;
import cs.superleague.importer.requests.StoreCSVImporterRequest;
import cs.superleague.importer.responses.ItemsCSVImporterResponse;
import cs.superleague.importer.responses.StoreCSVImporterResponse;
import cs.superleague.payment.PaymentService;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.repos.ItemRepo;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.user.UserService;
import cs.superleague.user.repos.AdminRepo;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.DriverRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Service("importerServiceImpl")
public class ImporterServiceImpl implements ImporterService{


    private final StoreRepo storeRepo;
    private final ItemRepo itemRepo;

    @Autowired
    public ImporterServiceImpl(StoreRepo storeRepo, ItemRepo itemRepo) {
        this.storeRepo= storeRepo;
        this.itemRepo= itemRepo;
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
                String file = request.getFile().substring(index+2);
                String currentWord = "";
                int counter = 0;
                for(k=0; k < file.length(); k++)
                {
                    Item item = new Item();
                    if(file.charAt(k) != ',' && file.charAt(k) != '\n' )
                    {
                        currentWord += file.charAt(k);
                    }
                    else if(file.charAt(k) == ',')
                    {
                        switch (counter){
                            case 0:
                                if (itemRepo!=null)
                                {
                                    List<Item> itemList= itemRepo.findAll();
                                    for(int j=0; j< itemList.size(); j++)
                                    {
                                        if(itemList.get(j).getProductID().equals(currentWord))
                                        {
                                            throw new InvalidRequestException("Item already exists");
                                        }
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
                                item.setImageUrl(String.valueOf(currentWord));
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
                        counter=0;
                        currentWord = "";

                        if (itemRepo!=null)
                        itemRepo.save(item);
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
                String file = request.getFile().substring(index+2);
                String currentWord = "";
                int counter = 0;
                for(k=0; k < file.length(); k++)
                {
                    Store store = new Store();
                    GeoPoint location= new GeoPoint();
                    if(file.charAt(k) != ',' && file.charAt(k) != '\n' )
                    {
                        currentWord += file.charAt(k);
                    }
                    else if(file.charAt(k) == ',')
                    {
                        switch (counter){
                            case 0:
                                if (storeRepo!=null)
                                {
                                    List<Store> storeList= storeRepo.findAll();
                                    for(int j=0; j< storeList.size(); j++)
                                    {
                                        if(storeList.get(j).getStoreID().equals(UUID.fromString(currentWord)))
                                        {
                                            throw new InvalidRequestException("Store already exists");
                                        }
                                    }
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
                                location.setLatitude(Double.valueOf(currentWord));
                                counter++;
                                currentWord = "";
                                break;

                            case 9:
                                location.setLongitude(Double.valueOf(currentWord));
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
                        if (storeRepo!=null)
                        storeRepo.save(store);
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
