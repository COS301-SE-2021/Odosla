package cs.superleague.importer;

import cs.superleague.importer.exceptions.InvalidRequestException;
import cs.superleague.importer.requests.ItemsCSVImporterRequest;
import cs.superleague.importer.requests.StoreCSVImporterRequest;
import cs.superleague.importer.responses.ItemsCSVImporterResponse;
import cs.superleague.importer.responses.StoreCSVImporterResponse;

import java.net.URISyntaxException;

public interface ImporterService {

    ItemsCSVImporterResponse itemsCSVImporter(ItemsCSVImporterRequest request) throws InvalidRequestException, URISyntaxException;

    StoreCSVImporterResponse storeCSVImporter(StoreCSVImporterRequest request) throws InvalidRequestException, URISyntaxException;
}
