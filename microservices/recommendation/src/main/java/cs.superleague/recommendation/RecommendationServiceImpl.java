package cs.superleague.recommendation;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import cs.superleague.notifications.responses.SendPDFEmailResponse;
import cs.superleague.payment.dataclass.CartItem;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.responses.GetOrderByUUIDResponse;
import cs.superleague.recommendation.dataclass.Recommendation;
import cs.superleague.recommendation.exceptions.InvalidRequestException;
import cs.superleague.recommendation.exceptions.RecommendationRepoException;
import cs.superleague.recommendation.repos.RecommendationRepo;
import cs.superleague.recommendation.requests.AddRecommendationRequest;
import cs.superleague.recommendation.requests.GetCartRecommendationRequest;
import cs.superleague.recommendation.requests.GetOrderRecommendationRequest;
import cs.superleague.recommendation.requests.RemoveRecommendationRequest;
import cs.superleague.recommendation.responses.GenerateRecommendationTablePDFResponse;
import cs.superleague.shopping.exceptions.ItemDoesNotExistException;
import cs.superleague.recommendation.requests.*;
import cs.superleague.recommendation.responses.GenerateRecommendationTableResponse;
import cs.superleague.recommendation.responses.GetCartRecommendationResponse;
import cs.superleague.recommendation.responses.GetOrderRecommendationResponse;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.responses.GetItemsByIDResponse;
import cs.superleague.shopping.responses.GetItemsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;
import java.util.List;

@Service("recommendationServiceImpl")
public class RecommendationServiceImpl implements RecommendationService {
    @Value("${shoppingHost}")
    private String shoppingHost;
    @Value("${shoppingPort}")
    private String shoppingPort;
    @Value("${notificationsHost}")
    private String notificationHost;
    @Value("${notificationsPort}")
    private String notificationPort;

    private final RecommendationRepo recommendationRepo;
    private final RestTemplate restTemplate;

    @Autowired
    public RecommendationServiceImpl(RecommendationRepo recommendationRepo, RestTemplate restTemplate) {
        this.recommendationRepo = recommendationRepo;
        this.restTemplate = restTemplate;
    }


    @Override
    public GetCartRecommendationResponse getCartRecommendation(GetCartRecommendationRequest request) throws InvalidRequestException, RecommendationRepoException, URISyntaxException, ItemDoesNotExistException {
        if (request == null) {
            throw new InvalidRequestException("No request object specified, need storeID to make recommendations.");
        }
        if (request.getStoreOneID() == null){
            throw new InvalidRequestException("Need StoreID to make recommendations.");
        }
        if (request.getItemIDs() == null) {
            return getRandomRecommendations("Null item list.", request.getStoreOneID(), new ArrayList<>());
        }
        if (request.getItemIDs().size() == 0) {
            return getRandomRecommendations("No items in item list.", request.getStoreOneID(), new ArrayList<>());
        }
        UUID storeTwoID = request.getStoreTwoID();
        UUID storeThreeID = request.getStoreThreeID();
        if (request.getStoreTwoID() == null){
            storeTwoID = request.getStoreOneID();
        }
        if (request.getStoreThreeID() == null){
            storeThreeID = request.getStoreOneID();
        }
        if (recommendationRepo != null) {
            List<UUID> orderIDs = new ArrayList<>();
            List<Integer> frequencyOfOrders = new ArrayList<>();
            for (String productID : request.getItemIDs()) {
                List<Recommendation> recommendation = recommendationRepo.findRecommendationByProductID(productID);
                if (recommendation != null) {
                    for (Recommendation orderID : recommendation) {
                        if (!orderIDs.contains(orderID.getOrderID())) {
                            orderIDs.add(orderID.getOrderID());
                            frequencyOfOrders.add(1);
                        } else {
                            frequencyOfOrders.set(orderIDs.indexOf(orderID.getOrderID()), frequencyOfOrders.get(orderIDs.indexOf(orderID.getOrderID())) + 1);
                        }
                    }
                }
            }
            if (orderIDs.size() == 0) {
                return getRandomRecommendations("None of these items have been bought before.", request.getStoreOneID(), request.getItemIDs());
            }
            List<Order> finalRecommendation = new ArrayList<>();
            List<String> productsToRecommend = new ArrayList<>();
            List<Integer> frequencyOfItems = new ArrayList<>();
            for (Integer frequency : frequencyOfOrders) {
                if (frequency >= request.getItemIDs().size() / 2) {
                    List<Recommendation> recommendationList = recommendationRepo.findRecommendationsByOrderID(orderIDs.get(frequencyOfOrders.indexOf(frequency)));
                    for (Recommendation recommendation : recommendationList){
                        if (productsToRecommend.contains(recommendation.getProductID())){
                            frequencyOfItems.set(productsToRecommend.indexOf(recommendation.getProductID()), (frequencyOfItems.get(productsToRecommend.indexOf(recommendation.getProductID())) + 1));
                        } else{
                            frequencyOfItems.add(1);
                            productsToRecommend.add(recommendation.getProductID());
                        }
                    }
                }
            }
            List<CartItem> finalItemsRecommendation = new ArrayList<>();
            int highestFrequency = Collections.max(frequencyOfItems);
            List<String> bestRecommendations = new ArrayList<>();
            for (Integer frequency : frequencyOfItems){
                if (frequency == null){
                    continue;
                }
                if (frequency == highestFrequency){
                    bestRecommendations.add(productsToRecommend.get(frequencyOfItems.indexOf(frequency)));
                    frequencyOfItems.set(frequencyOfItems.indexOf(frequency), null);
                }
            }
            Map<String, Object> parts = new HashMap<>();
            parts.put("itemIDs", bestRecommendations);
            String stringUri = "http://" + shoppingHost + ":" + shoppingPort + "/shopping/getItemsByID";
            URI uri = new URI(stringUri);
            ResponseEntity<GetItemsByIDResponse> itemResponse = restTemplate.postForEntity(uri, parts, GetItemsByIDResponse.class);
            if (itemResponse == null || itemResponse.getBody() == null){
                throw new ItemDoesNotExistException("Item not found in database.");
            }
            List<Item> finalItems = itemResponse.getBody().getItems();
            List<String> barcodes = new ArrayList<>();
            for (Item item : finalItems){
                if (request.getItemIDs().contains(item.getProductID())){
                    barcodes.add(item.getBarcode());
                }
            }
            for (Item item : finalItems){
                if ((item.getStoreID().compareTo(request.getStoreOneID()) == 0 || item.getStoreID().compareTo(storeTwoID) == 0 || item.getStoreID().compareTo(storeThreeID) == 0) && !barcodes.contains(item.getBarcode()) && !item.isSoldOut()){
                    CartItem cartItem = new CartItem(item.getName(), item.getProductID(), item.getBarcode(), null, item.getPrice(), 1, item.getDescription(), item.getImageUrl(), item.getBrand(), item.getSize(), item.getItemType(), item.getPrice(), item.getStoreID());
                    finalItemsRecommendation.add(cartItem);
                    if (finalItemsRecommendation.size() == 3){
                        break;
                    }
                }
            }
            if (finalItemsRecommendation.size() == 0) {
                return getRandomRecommendations("There are no orders that have all the requested items in them.", request.getStoreOneID(), request.getItemIDs());
            }
            while (finalItemsRecommendation.size() < 3){
                GetCartRecommendationResponse getCartRecommendationResponse = getRandomRecommendations("", request.getStoreOneID(), request.getItemIDs());
                for (CartItem item : getCartRecommendationResponse.getRecommendations()){
                    if (finalItemsRecommendation.size() == 3){
                        break;
                    }
                    if (finalItemsRecommendation.contains(item)){
                        continue;
                    }
                    finalItemsRecommendation.add(item);
                }
            }
            System.out.println("Standard Recommendation");
            GetCartRecommendationResponse response = new GetCartRecommendationResponse(finalItemsRecommendation, true, "The following items are recommended to go with the cart.");
            return response;
        } else {
            return getRandomRecommendations("No recommendation repository found.", request.getStoreOneID(), request.getItemIDs());
        }
    }

    @Override
    public GetOrderRecommendationResponse getOrderRecommendation(GetOrderRecommendationRequest request) {
        return null;
    }

    @Override
    public void addRecommendation(AddRecommendationRequest request) throws InvalidRequestException {
        if (request == null) {
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getOrderID() == null || request.getProductID() == null) {
            throw new InvalidRequestException("Null parameters.");
        }
        for (String productID : request.getProductID()) {
            UUID recommendationID = UUID.randomUUID();
            while (recommendationRepo.findRecommendationByRecommendationID(recommendationID) != null) {
                recommendationID = UUID.randomUUID();
            }
            Recommendation recommendation = new Recommendation(recommendationID, productID, request.getOrderID());
            recommendationRepo.save(recommendation);
        }
    }

    @Override
    public void removeRecommendation(RemoveRecommendationRequest request) throws InvalidRequestException {
        if (request == null) {
            throw new InvalidRequestException("Null request object.");
        }
        if (request.getOrderID() == null) {
            throw new InvalidRequestException("Null parameters.");
        }
        List<Recommendation> recommendations = recommendationRepo.findRecommendationsByOrderID(request.getOrderID());
        for (Recommendation recommendation : recommendations) {
            recommendationRepo.delete(recommendation);
        }
    }

    // Helper/s

    private GetCartRecommendationResponse getRandomRecommendations(String errorMessage, UUID storeOneID, List<String> productIDs) throws URISyntaxException {
        System.out.println("Random Recommendation because " + errorMessage);
        int count = 0;
        int randomInt = 0;
        List<CartItem> allItems;
        List<CartItem> randomItems = new ArrayList<>();
        Random random = new Random();
        String stringUri = "http://" + shoppingHost + ":" + shoppingPort + "/shopping/getItems";
        URI uri = new URI(stringUri);
        Map<String, Object> parts = new HashMap<>();
        parts.put("storeID", storeOneID.toString());

        ResponseEntity<GetItemsResponse> responseEntity = restTemplate.postForEntity(
                uri,
                parts, GetItemsResponse.class);

        if (responseEntity == null || !responseEntity.hasBody() || responseEntity.getBody() == null
                || responseEntity.getBody().getItems() == null) {
            return new GetCartRecommendationResponse(new ArrayList<>(),
                    false, "Could not retrieve Items");
        }

        allItems = new ArrayList<>();
        for (Item item : responseEntity.getBody().getItems()){
            if (item.isSoldOut()){
                continue;
            }
            CartItem cartItem = new CartItem(item.getName(), item.getProductID(), item.getBarcode(), null, item.getPrice(), 1, item.getDescription(), item.getImageUrl(), item.getBrand(), item.getSize(), item.getItemType(), item.getPrice(), item.getStoreID());
            allItems.add(cartItem);
        }
        if (allItems.size() < 3) {
            count = allItems.size();
        } else {
            count = 3;
        }
        List<String> barcodes = new ArrayList<>();
        if (productIDs != null) {
            for (CartItem item : allItems) {
                if (productIDs.contains(item.getProductID())) {
                    barcodes.add(item.getBarcode());
                }
            }
        }
        System.out.println(count);
        for (int i = 0; i < count; i++) {
            randomInt = random.nextInt(allItems.size());
            if (randomItems.contains(allItems.get(randomInt)) || productIDs.contains((allItems.get(randomInt).getProductID())) || !(allItems.get(randomInt).getStoreID().equals(storeOneID)) || barcodes.contains(allItems.get(randomInt).getBarcode())){
                i--;
                continue;
            }
            randomItems.add(allItems.get(randomInt));
        }

        errorMessage = "Random recommendations returned because " + errorMessage;
        return new GetCartRecommendationResponse(randomItems, false, errorMessage);
    }

    @Override
    public GenerateRecommendationTableResponse generateRecommendationTable(GenerateRecommendationTableRequest request) throws InvalidRequestException, URISyntaxException {
        if (request == null){
            throw new InvalidRequestException("Null request object.");
        }
        else{
            List<Recommendation> recommendations = recommendationRepo.findAll();
            List<UUID> orderIDs = new ArrayList<>();
            List<List<String>> recommendationTable = new ArrayList<List<String>>();
            int sizeOfTable = 0;
            int index=0;

            if(recommendations!=null){

                List<String> uniqueProductIDs = new ArrayList<>();

                for(int k = 0; k < recommendations.size(); k++)
                {
                    if(k == 0){
                        sizeOfTable++;
                        orderIDs.add(recommendations.get(k).getOrderID());
                        uniqueProductIDs.add(recommendations.get(k).getProductID());
                    }
                    else{
                        boolean found = false;
                        boolean foundProduct = false;
                        for (UUID orderID : orderIDs) {
                            if (orderID.compareTo(recommendations.get(k).getOrderID()) == 0) {
                                found = true;
                                break;
                            }
                        }

                        for (String prodID : uniqueProductIDs) {
                            if (prodID.equals(recommendations.get(k).getProductID())) {
                                foundProduct = true;
                                break;
                            }
                        }

                        if(!found){
                            sizeOfTable++;
                            orderIDs.add(recommendations.get(k).getOrderID());
                        }
                        if(!foundProduct){
                            uniqueProductIDs.add(recommendations.get(k).getProductID());
                        }
                    }
                }

                for(int i = 0; i < sizeOfTable + 1; i++)  {
                    recommendationTable.add(new ArrayList<String>());
                }

                Map<String, Object> parts = new HashMap<>();
                parts.put("itemIDs", uniqueProductIDs);
                String stringUri = "http://" + shoppingHost + ":" + shoppingPort + "/shopping/getItemsByID";
                URI uri = new URI(stringUri);
                ResponseEntity<GetItemsByIDResponse> responseEntity = restTemplate.postForEntity(
                        uri, parts, GetItemsByIDResponse.class);

                List<Item> items = null;
                if (responseEntity.getBody() != null) {
                    items = responseEntity.getBody().getItems();
                }
                recommendationTable.get(0).add("");
                for (Item value : items) {
                    recommendationTable.get(0).add(value.getName());
                }

                for(int i = 1; i < sizeOfTable + 1; i++)  {
                    recommendationTable.get(i).add(orderIDs.get(i - 1).toString());
                }

                for(int k = 1; k < sizeOfTable + 1; k++){
                    List<Recommendation> recommendationList = recommendationRepo.findRecommendationsByOrderID(orderIDs.get(k - 1));
                    List<String> itemProductID = new ArrayList<>();
                    for (Recommendation recommendation : recommendationList){
                        itemProductID.add(recommendation.getProductID());
                        System.out.println(recommendation.getProductID());
                    }
                    parts = new HashMap<>();
                    parts.put("itemIDs", itemProductID);
                    stringUri = "http://" + shoppingHost + ":" + shoppingPort + "/shopping/getItemsByID";
                    uri = new URI(stringUri);
                    ResponseEntity<GetItemsByIDResponse> itemResponse = restTemplate.postForEntity(uri, parts, GetItemsByIDResponse.class);
                    List<Item> itemList = itemResponse.getBody().getItems();

                    for (int i = 0; i < items.size(); i++) {
                        boolean itemAdded = false;
                        for (int j = 0; j < itemProductID.size(); j++) {
                            if (itemProductID.get(j).equals(items.get(i).getProductID())) {
                                itemAdded = true;
                                recommendationTable.get(k).add("1");
                                continue;
                            }
                        }
                        if (itemAdded == false){
                            recommendationTable.get(k).add("0");
                        }
                    }
                }

                return new GenerateRecommendationTableResponse(true, "Table created successfully", recommendationTable);

            }else{
                throw new InvalidRequestException("No recommendations in the table");
            }

        }
    }

    @Override
    public GenerateRecommendationTablePDFResponse generateRecommendationTablePDF(
            GenerateRecommendationTablePDFRequest request) throws InvalidRequestException, URISyntaxException, DocumentException {

        if(request == null){
            throw new InvalidRequestException("GenerateRecommendationTablePDF Request object is null");
        }

        if(request.getEmail() == null){
            throw new InvalidRequestException("Email in GenerateRecommendationTablePDF Request object is null");
        }

        PdfPTable table = null;
        PdfPTable table2 = null;
        Document document = new Document();
        List<List<String>> recommendationTable;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, byteArrayOutputStream);

        GenerateRecommendationTableResponse response = generateRecommendationTable(new
                GenerateRecommendationTableRequest());

        recommendationTable = response.getRecommendationTable();

        document.open();
        Paragraph title = new Paragraph("Odosla - Recommendation Table", FontFactory.getFont(FontFactory.TIMES, 40, Font.BOLD));
        document.add(title);
        document.add(new Paragraph(" "));

        table = new PdfPTable(recommendationTable.get(0).size());

        for (int i = 0; i < recommendationTable.size(); i++) {
            for (int j = 0; j < recommendationTable.get(0).size(); j++) {
                table.addCell(recommendationTable.get(i).get(j));
            }
        }

        document.add(table);
        document.close();

        Map<String, Object> properties = new HashMap<>();
        properties.put("email", request.getEmail());
        properties.put("PDF", byteArrayOutputStream.toByteArray());

        ResponseEntity<SendPDFEmailResponse> responseEntity =
                restTemplate.postForEntity("http://" + notificationHost + ":" + notificationPort +
                        "/notification/sendPDFEmail", properties, SendPDFEmailResponse.class);

        if(responseEntity == null || responseEntity.getBody() == null){
            return new GenerateRecommendationTablePDFResponse(false, "Error sending email - Notification Error");
        }

        System.out.println(responseEntity.getBody().getMessage());

        if(!responseEntity.getBody().isSuccess()){
            return new GenerateRecommendationTablePDFResponse(false, "Could not send email");
        }

        return new GenerateRecommendationTablePDFResponse(true, "Email with PDF successfully sent");
    }

}
