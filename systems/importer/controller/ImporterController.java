package cs.superleague.importer.controller;

import cs.superleague.api.ImporterApi;
import cs.superleague.importer.ImporterServiceImpl;
import cs.superleague.importer.requests.ItemsCSVImporterRequest;
import cs.superleague.importer.requests.StoreCSVImporterRequest;
import cs.superleague.importer.responses.ItemsCSVImporterResponse;
import cs.superleague.importer.responses.StoreCSVImporterResponse;
import cs.superleague.integration.ServiceSelector;
import cs.superleague.models.*;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.importer.exceptions.InvalidRequestException;
import cs.superleague.shopping.repos.CatalogueRepo;
import cs.superleague.shopping.repos.ItemRepo;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.user.repos.CustomerRepo;
import cs.superleague.user.repos.ShopperRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;


@CrossOrigin
@RestController
public class ImporterController implements ImporterApi {

    @Autowired
    ImporterServiceImpl importerService;

    @Autowired
    StoreRepo storeRepo;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    CatalogueRepo catalogueRepo;

    @Autowired
    ItemRepo itemRepo;

    @Autowired
    ShopperRepo shopperRepo;

    @Autowired
    CustomerRepo customerRepo;


    @Override
    public ResponseEntity<ImporterItemsCSVImporterResponse> itemsCSVImporter(ImporterItemsCSVImporterRequest body) {
        //creating response object and default return status:
        ImporterItemsCSVImporterResponse response = new ImporterItemsCSVImporterResponse();
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            ItemsCSVImporterRequest req = new ItemsCSVImporterRequest(body.getFile());
            ItemsCSVImporterResponse itemsCSVImporterResponse = ServiceSelector.getImporterService().itemsCSVImporter(req);

            try {
                response.setMessage(itemsCSVImporterResponse.getMessage());
                response.setSuccess(itemsCSVImporterResponse.isSuccess());
                response.setTimestamp(itemsCSVImporterResponse.getTimestamp().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (InvalidRequestException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    public ResponseEntity<ImporterStoresCSVImporterResponse> storesCSVImporter(ImporterStoresCSVImporterRequest body) {

        ImporterStoresCSVImporterResponse response = new ImporterStoresCSVImporterResponse();
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            StoreCSVImporterRequest req = new StoreCSVImporterRequest(body.getFile());
            StoreCSVImporterResponse storeCSVImporterResponse = ServiceSelector.getImporterService().storeCSVImporter(req);

            try {
                response.setMessage(storeCSVImporterResponse.getMessage());
                response.setSuccess(storeCSVImporterResponse.isSuccess());
                response.setTimestamp(storeCSVImporterResponse.getTimestamp().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (InvalidRequestException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(response, httpStatus);
    }
}
