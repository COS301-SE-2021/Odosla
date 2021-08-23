package cs.superleague.importer;

import cs.superleague.importer.exceptions.InvalidRequestException;
import cs.superleague.importer.requests.ItemsCSVImporterRequest;
import cs.superleague.importer.requests.StoreCSVImporterRequest;
import cs.superleague.importer.responses.ItemsCSVImporterResponse;
import cs.superleague.importer.responses.StoreCSVImporterResponse;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service("importerServiceImpl")
public class ImporterServiceImpl implements ImporterService{


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
                String file = request.getFile();
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
                                item.setProductID(currentWord);
                                counter++;
                                currentWord = "";

                            case 1:
                                item.setBarcode(currentWord);
                                counter++;
                                currentWord = "";

                            case 2:
                                item.setBrand(currentWord);
                                counter++;
                                currentWord = "";

                            case 3:
                                item.setDescription(currentWord);
                                counter++;
                                currentWord = "";

                            case 4:
                                item.setImageUrl(String.valueOf(currentWord);
                                counter++;
                                currentWord = "";

                            case 5:
                                item.setItemType(currentWord);
                                counter++;
                                currentWord = "";

                            case 6:
                                item.setName(currentWord);
                                counter++;
                                currentWord = "";

                            case 7:
                                item.setPrice(Double.parseDouble(currentWord));
                                counter++;
                                currentWord = "";

                            case 8:
                                item.setQuantity(Integer.parseInt(currentWord));
                                counter++;
                                currentWord = "";

                            case 9:
                                item.setSize(currentWord);
                                counter++;
                                currentWord = "";
                        }
                    }
                    else if (file.charAt(k) == '\n')
                    {
                        item.setStoreID(UUID.fromString(currentWord));
                        counter=0;
                        currentWord = "";
                    }
                }
            }
        }
        else
        {
            throw new InvalidRequestException("Request object is null");
        }

        return null;
    }

    @Override
    public StoreCSVImporterResponse storeCSVImporter(StoreCSVImporterRequest request) throws InvalidRequestException {
        if (request!=null)
        {
            if(request.getFile()==null)
            {
                throw new InvalidRequestException("No file uploaded to import");
            }
        }
        else
        {
            throw new InvalidRequestException("Request object is null");
        }
        return null;
    }
}
