package cs.superleague.payment.controller;

import cs.superleague.api.PaymentApi;
import cs.superleague.models.*;
import cs.superleague.payment.PaymentServiceImpl;
import cs.superleague.payment.dataclass.CartItem;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.exceptions.InvalidRequestException;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.*;
import cs.superleague.payment.responses.*;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.responses.GetStoreByUUIDResponse;
import org.apache.http.Header;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
public class PaymentController implements PaymentApi {

    PaymentServiceImpl paymentService;

    OrderRepo orderRepo;

    RabbitTemplate rabbitTemplate;

    RestTemplate restTemplate;

    HttpServletRequest httpServletRequest;

    @Autowired
    public PaymentController(PaymentServiceImpl paymentService, OrderRepo orderRepo,
                              RabbitTemplate rabbitTemplate, RestTemplate restTemplate,
                             HttpServletRequest httpServletRequest){
        this.paymentService = paymentService;
        this.orderRepo = orderRepo;
        this.rabbitTemplate = rabbitTemplate;
        this.restTemplate = restTemplate;
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public ResponseEntity<PaymentUpdateOrderResponse> updateOrder(PaymentUpdateOrderRequest body) {

        PaymentUpdateOrderResponse response = new PaymentUpdateOrderResponse();
        HttpStatus httpStatus = HttpStatus.OK;

        try{

            Header header = new BasicHeader("Authorization", httpServletRequest.getHeader("Authorization"));
            List<Header> headers = new ArrayList<>();
            headers.add(header);
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

            OrderType orderType = null;
            if(body.getOrderType().equals("Collection")){
                orderType = OrderType.COLLECTION;
            }else if(body.getOrderType().equals("Delivery")){
                orderType = OrderType.DELIVERY;
            }

            double discount = 0.00;
            if(body.getDiscount() != null)
                discount = body.getDiscount().doubleValue();

            UUID orderID = UUID.fromString(body.getOrderID());
            GeoPoint deliveryAddress = new GeoPoint();

            if(body.getDeliveryAddress() != null) {
                deliveryAddress = new GeoPoint(body.getDeliveryAddress().getLatitude().doubleValue(), body.getDeliveryAddress().getLongitude().doubleValue(), body.getDeliveryAddress().getAddress());
            }else{
                deliveryAddress = null;
            }

            List<CartItemObject> cartItemObjectList = convertCartItems(body.getListOfItems());

            UpdateOrderRequest request = new UpdateOrderRequest(orderID, assignCartItems(cartItemObjectList),
                    discount, orderType, deliveryAddress);

            UpdateOrderResponse updateOrderResponse = paymentService.updateOrder(request);
            try {
                response.setMessage(updateOrderResponse.getMessage());
                response.setOrder(populateOrder(updateOrderResponse.getOrder()));
                response.setSuccess(updateOrderResponse.isSuccess());
                response.setTimestamp(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(updateOrderResponse.getTimestamp()));
            }catch(Exception e){

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    public ResponseEntity<PaymentGetStatusResponse> getStatus(PaymentGetStatusRequest body) {

        PaymentGetStatusResponse response = new PaymentGetStatusResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        System.out.println("controller getStatus order id: "+ body.getOrderID());
        try{
            Header header = new BasicHeader("Authorization", httpServletRequest.getHeader("Authorization"));
            List<Header> headers = new ArrayList<>();
            headers.add(header);
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

            GetStatusRequest getStatusRequest = new GetStatusRequest(UUID.fromString(body.getOrderID()));
            GetStatusResponse getStatusResponse = paymentService.getStatus(getStatusRequest);
            try {
                response.setMessage(getStatusResponse.getMessage());
                response.setStatus(getStatusResponse.getStatus());
                response.setSuccess(getStatusResponse.isSuccess());
                response.setTimestamp(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(getStatusResponse.getTimestamp()));
            }catch(Exception e){

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    public ResponseEntity<PaymentSubmitOrderResponse> submitOrder(PaymentSubmitOrderRequest body) {

        PaymentSubmitOrderResponse response = new PaymentSubmitOrderResponse();
        HttpStatus httpStatus = HttpStatus.OK;

        OrderType orderType = null;
        if(body.getOrderType().equals("DELIVERY")){
            orderType= OrderType.DELIVERY;
        } else if(body.getOrderType().equals("COLLECTION")){
            orderType= OrderType.COLLECTION;
        }

        try{

            Header header = new BasicHeader("Authorization", httpServletRequest.getHeader("Authorization"));
            List<Header> headers = new ArrayList<>();
            headers.add(header);
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

            List<CartItemObject> cartItemObjects = convertCartItems(body.getListOfItems());
            System.out.println("BEFORE THE CALL");
            System.out.println("Cart items list size : "+ cartItemObjects.size());
            SubmitOrderRequest submitOrderRequest = new SubmitOrderRequest(
                    assignCartItems(cartItemObjects), body.getDiscount().doubleValue(),
                    UUID.fromString(body.getStoreID()), orderType, body.getLongitude().doubleValue(),
                    body.getLatitude().doubleValue(), body.getAddress());
            SubmitOrderResponse submitOrderResponse = paymentService.submitOrder(submitOrderRequest);
            System.out.println("AFTER THE CALL");
            try {
                response.setMessage(submitOrderResponse.getMessage());
                response.setOrder(populateOrder(submitOrderResponse.getOrder()));
                response.setSuccess(submitOrderResponse.getsuccess());
                response.setTimestamp(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(submitOrderResponse.getTimestamp()));
            }catch(Exception e){

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    public ResponseEntity<PaymentGetCustomersActiveOrdersResponse> getCustomerActiveOrders(PaymentGetCustomersActiveOrdersRequest body) {
        PaymentGetCustomersActiveOrdersResponse response = new PaymentGetCustomersActiveOrdersResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            GetCustomersActiveOrdersRequest request = new GetCustomersActiveOrdersRequest();
            GetCustomersActiveOrdersResponse getCustomersActiveOrdersResponse = paymentService.getCustomersActiveOrders(request);
            response.setHasActiveOrder(getCustomersActiveOrdersResponse.isHasActiveOrder());
            response.setMessage(getCustomersActiveOrdersResponse.getMessage());
            response.setOrderID(String.valueOf(getCustomersActiveOrdersResponse.getOrderID()));
        }catch (Exception e){
            e.printStackTrace();
            response.setOrderID(null);
            response.setMessage(e.getMessage());
            response.setHasActiveOrder(false);
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    public ResponseEntity<PaymentGetItemsResponse> getItemsPayments(PaymentGetItemsRequest body) {

        PaymentGetItemsResponse response = new PaymentGetItemsResponse();
        HttpStatus httpStatus = HttpStatus.OK;

        try{

            GetItemsRequest getItemsRequest = new GetItemsRequest(body.getOrderID());
            GetItemsResponse getItemsResponse = paymentService.getItems(getItemsRequest);
            try {
                response.setMessage(getItemsResponse.getMessage());
                response.setItemList(populateCartItems(getItemsResponse.getCartItems()));
                response.setSuccess(getItemsResponse.isSuccess());
                response.setTimestamp(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(getItemsResponse.getTimestamp()));
            }catch(Exception e){

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    public ResponseEntity<PaymentGetOrderResponse> getOrder(PaymentGetOrderRequest body) {
        PaymentGetOrderResponse response = new PaymentGetOrderResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            GetOrderRequest getOrderRequest = new GetOrderRequest(UUID.fromString(body.getOrderID()));
            GetOrderResponse getOrderResponse = paymentService.getOrder(getOrderRequest);
            try {
                response.setOrder(populateOrder(getOrderResponse.getOrder()));
                response.setMessage(getOrderResponse.getMessage());
                response.setSuccess(getOrderResponse.isSuccess());
                response.setTimestamp(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(getOrderResponse.getTimestamp()));
            }catch (Exception e){
                e.printStackTrace();
                response.setOrder(null);
                response.setMessage(e.getMessage());
                response.setSuccess(false);
                response.setTimestamp(String.valueOf(Calendar.getInstance()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setOrder(null);
            response.setMessage(e.getMessage());
            response.setSuccess(false);
            response.setTimestamp(String.valueOf(Calendar.getInstance()));
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    public ResponseEntity<PaymentGetOrdersResponse> getOrders(PaymentGetOrdersRequest body) {
        PaymentGetOrdersResponse response = new PaymentGetOrdersResponse();
        HttpStatus httpStatus = HttpStatus.OK;
        try {
            GetOrdersRequest getOrdersRequest = new GetOrdersRequest();
            GetOrdersResponse getOrdersResponse = paymentService.getOrders(getOrdersRequest);
            try {
                List<OrderObject> orderObjects = new ArrayList<>();
                for (Order order : getOrdersResponse.getOrders()){
                    orderObjects.add(populateOrder(order));
                }
                response.setOrders(orderObjects);
                response.setMessage(getOrdersResponse.getMessage());
                response.setSuccess(getOrdersResponse.isSuccess());
                response.setTimestamp(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(getOrdersResponse.getTimestamp()));
            }catch (Exception e){
                e.printStackTrace();
                response.setOrders(null);
                response.setMessage(e.getMessage());
                response.setSuccess(false);
                response.setTimestamp(String.valueOf(Calendar.getInstance()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setOrders(null);
            response.setMessage(e.getMessage());
            response.setSuccess(false);
            response.setTimestamp(String.valueOf(Calendar.getInstance()));
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    // helper
    List<Item> assignItems(List<ItemObject> itemObjectList){

        double price = 0.00;

        List<Item> items = new ArrayList<>();

        if(itemObjectList == null){
            return null;
        }

        for (ItemObject i: itemObjectList) {
            Item item = new Item();
            item.setProductID(i.getProductID());
            item.setBarcode(i.getBarcode());
            item.setQuantity(i.getQuantity());
            item.setName(i.getName());
            item.setStoreID(UUID.fromString(i.getStoreID()));
            if(i.getPrice() != null)
                price = i.getPrice().doubleValue();

            item.setPrice(price);
            item.setImageUrl(i.getImageUrl());
            item.setBrand(i.getBrand());
            item.setSize(i.getSize());
            item.setItemType(i.getItemType());
            item.setDescription(i.getDescription());
            items.add(item);

        }
        return items;
    }

    private List<ItemObject> populateItems(List<Item> responseItems) throws NullPointerException{

        List<ItemObject> responseBody = new ArrayList<>();

        for(int i = 0; i < responseItems.size(); i++){

            ItemObject currentItem = new ItemObject();

            currentItem.setName(responseItems.get(i).getName());
            currentItem.setDescription(responseItems.get(i).getDescription());
            currentItem.setBarcode(responseItems.get(i).getBarcode());
            currentItem.setProductID(responseItems.get(i).getProductID());
            currentItem.setStoreID(responseItems.get(i).getStoreID().toString());
            currentItem.setPrice(BigDecimal.valueOf(responseItems.get(i).getPrice()));
            currentItem.setQuantity(responseItems.get(i).getQuantity());
            currentItem.setImageUrl(responseItems.get(i).getImageUrl());

            responseBody.add(currentItem);

        }

        return responseBody;
    }

    public GeoPointObject populateGeoPointObject(GeoPoint location){
        GeoPointObject locationObject = new GeoPointObject();
        locationObject.setAddress(location.getAddress());
        locationObject.setLongitude(BigDecimal.valueOf(location.getLongitude()));
        locationObject.setLatitude(BigDecimal.valueOf(location.getLatitude()));
        return locationObject;
    }

    public OrderObject populateOrder(Order order){
        OrderObject orderObject = new OrderObject();
        if(order.getOrderID() != null)
            orderObject.setOrderID(order.getOrderID().toString());
        if(order.getUserID() != null)
            orderObject.setUserID(order.getUserID().toString());
        if(order.getStoreID() != null)
            orderObject.setStoreID(order.getStoreID().toString());
        if(order.getShopperID() != null)
            orderObject.setShopperID(order.getShopperID().toString());
        if(order.getCreateDate()!=null)
            orderObject.setCreateDate(order.getCreateDate().toString());
        if(order.getProcessDate() != null)
            orderObject.setProcessDate(order.getProcessDate().toString());
        if(order.getTotalCost() != null)
            orderObject.setTotalCost(BigDecimal.valueOf(order.getTotalCost()));
        if(order.getStatus()!=null)
            orderObject.setStatus(order.getStatus().toString());
        if(order.getCartItems()!=null)
            orderObject.setCartItems(populateCartItems(order.getCartItems()));
        if(order.getDiscount()!=null)
            orderObject.setDiscount(BigDecimal.valueOf(order.getDiscount()));
        if(order.getDeliveryAddress()!=null)
            orderObject.setDeliveryAddress(populateGeoPointObject(order.getDeliveryAddress()));
        if(order.getStoreAddress()!=null)
            orderObject.setStoreAddress(populateGeoPointObject(order.getStoreAddress()));
        orderObject.setRequiresPharmacy(order.isRequiresPharmacy());
        return orderObject;
    }

    List<CartItem> assignCartItems(List<CartItemObject> cartObjectList){

        double price = 0.00;

        List<CartItem> cartItems = new ArrayList<>();

        if(cartObjectList == null){
            return null;
        }

        for (CartItemObject i: cartObjectList) {
            CartItem item = new CartItem();
            if(i.getCartItemNo()!=null)
            {
                item.setCartItemNo(UUID.fromString(i.getCartItemNo()));
            }
            item.setProductID(i.getProductID());
            item.setBarcode(i.getBarcode());
            item.setQuantity(i.getQuantity());
            item.setName(i.getName());
            item.setStoreID(UUID.fromString(i.getStoreID()));
            if(i.getPrice() != null)
                price = i.getPrice().doubleValue();
            item.setPrice(price);
            item.setImageUrl(i.getImageUrl());
            item.setBrand(i.getBrand());
            item.setSize(i.getSize());
            item.setItemType(i.getItemType());
            item.setDescription(i.getDescription());
            cartItems.add(item);

        }
        return cartItems;
    }

    List<CartItemObject> convertCartItems(List<ItemObject> itemObjectList){

        List<CartItemObject> cartItems = new ArrayList<>();

        if(itemObjectList == null){
            return null;
        }

        System.out.println("length of itemobjectlist: " + itemObjectList.size());

        for (ItemObject i: itemObjectList) {
            CartItemObject item = new CartItemObject();
            item.setProductID(i.getProductID());
            item.setBarcode(i.getBarcode());
            item.setQuantity(i.getQuantity());
            item.setName(i.getName());
            item.setStoreID(i.getStoreID());
            item.setPrice(i.getPrice());
            item.setImageUrl(i.getImageUrl());
            item.setBrand(i.getBrand());
            item.setSize(i.getSize());
            item.setItemType(i.getItemType());
            item.setDescription(i.getDescription());
            cartItems.add(item);

        }
        return cartItems;
    }

    private List<CartItemObject> populateCartItems(List<CartItem> responseItems) throws NullPointerException{

        List<CartItemObject> responseBody = new ArrayList<>();

        for (CartItem i: responseItems){

            CartItemObject item = new CartItemObject();
            if(i.getCartItemNo()!=null)
            {
                item.setCartItemNo(i.getCartItemNo().toString());
            }
            item.setProductID(i.getProductID());
            item.setBarcode(i.getBarcode());
            item.setQuantity(i.getQuantity());
            item.setName(i.getName());
            item.setStoreID(i.getStoreID().toString());
            item.setPrice(BigDecimal.valueOf(i.getPrice()));
            item.setImageUrl(i.getImageUrl());
            item.setBrand(i.getBrand());
            item.setSize(i.getSize());
            item.setItemType(i.getItemType());
            item.setDescription(i.getDescription());

            responseBody.add(item);

        }

        return responseBody;
    }

    @Override
    public ResponseEntity<PaymentGetAllCartItemsResponse> getAllCartItems(PaymentGetAllCartItemsRequest body) {
        PaymentGetAllCartItemsResponse response = new PaymentGetAllCartItemsResponse();
        HttpStatus httpStatus = HttpStatus.OK;

        try {
            GetAllCartItemsResponse getAllCartItemsResponse = paymentService.getAllCartItems(new GetAllCartItemsRequest());
            try {

                response.setCartItems(populateCartItems(getAllCartItemsResponse.getCartItems()));

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (InvalidRequestException e) {

        }

        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    public ResponseEntity<PaymentGetOrderByUUIDResponse> getOrderByUUID(PaymentGetOrderByUUIDRequest body){

        //creating response object and default return status:
        PaymentGetOrderByUUIDResponse response = new PaymentGetOrderByUUIDResponse();
        HttpStatus httpStatus = HttpStatus.OK;

        GetOrderByUUIDResponse getOrderByUUIDResponse = null;
        try {

            Header header = new BasicHeader("Authorization", httpServletRequest.getHeader("Authorization"));
            List<Header> headers = new ArrayList<>();
            headers.add(header);
            CloseableHttpClient httpClient = HttpClients.custom().setDefaultHeaders(headers).build();
            restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));

            getOrderByUUIDResponse = paymentService.getOrderByUUID(new GetOrderByUUIDRequest(UUID.fromString(body.getOrderID())));
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        }
        try {

            response.setOrder(populateOrder(getOrderByUUIDResponse.getOrder()));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(response, httpStatus);
    }
}
