package cs.superleague.importer.controller;

import cs.superleague.api.ImporterApi;
import cs.superleague.importer.ImporterServiceImpl;
import cs.superleague.importer.requests.ItemsCSVImporterRequest;
import cs.superleague.importer.requests.StoreCSVImporterRequest;
import cs.superleague.importer.responses.ItemsCSVImporterResponse;
import cs.superleague.importer.responses.StoreCSVImporterResponse;
import cs.superleague.importer.exceptions.InvalidRequestException;
import cs.superleague.models.ImporterItemsCSVImporterRequest;
import cs.superleague.models.ImporterItemsCSVImporterResponse;
import cs.superleague.models.ImporterStoresCSVImporterRequest;
import cs.superleague.models.ImporterStoresCSVImporterResponse;
import org.apache.http.Header;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;


@CrossOrigin
@RestController
public class ImporterController implements ImporterApi {

    private final ImporterServiceImpl importerService;
    private final RestTemplate restTemplate;
    private final HttpServletRequest httpServletRequest;

    @Autowired
    public ImporterController(ImporterServiceImpl importerService, RestTemplate restTemplate,
                              HttpServletRequest httpServletRequest){
        this.importerService = importerService;
        this.restTemplate = restTemplate;
        this.httpServletRequest = httpServletRequest;
    }


    @Override
    public ResponseEntity<ImporterItemsCSVImporterResponse> itemsCSVImporter(ImporterItemsCSVImporterRequest body) {
        //creating response object and default return status:
        ImporterItemsCSVImporterResponse response = new ImporterItemsCSVImporterResponse();
        HttpStatus httpStatus = HttpStatus.OK;

        Header header = new BasicHeader("Authorization", httpServletRequest.getHeader("Authorization"));
        List<Header> headers = new ArrayList<>();
        headers.add(header);
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

        try {
            ItemsCSVImporterRequest req = new ItemsCSVImporterRequest(body.getFile());
            ItemsCSVImporterResponse itemsCSVImporterResponse = importerService.itemsCSVImporter(req);

            try {
                response.setMessage(itemsCSVImporterResponse.getMessage());
                response.setSuccess(itemsCSVImporterResponse.isSuccess());
                response.setTimestamp(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(itemsCSVImporterResponse.getTimestamp()));
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (InvalidRequestException | URISyntaxException e) {
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
            StoreCSVImporterResponse storeCSVImporterResponse = importerService.storeCSVImporter(req);

            try {
                response.setMessage(storeCSVImporterResponse.getMessage());
                response.setSuccess(storeCSVImporterResponse.isSuccess());
                response.setTimestamp(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(storeCSVImporterResponse.getTimestamp()));
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (InvalidRequestException | URISyntaxException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(response, httpStatus);
    }
}
