package cs.superleague.payment.controller;

import cs.superleague.api.PaymentApi;
import cs.superleague.models.*;
import cs.superleague.payment.PaymentServiceImpl;
import cs.superleague.payment.dataclass.CartItem;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.*;
import cs.superleague.payment.responses.*;
import cs.superleague.shopping.dataclass.Item;
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

//    UUID storeID = UUID.fromString("01234567-9ABC-DEF0-1234-56789ABCDEF0");
//    UUID shopperID = UUID.randomUUID();
//    UUID userID = UUID.fromString("7bc59ea6-aa30-465d-bcab-64e894bef586");
//    UUID orderId_AWAITNG_PAYMENT = UUID.fromString("8d8fe4d6-492b-453e-8ef1-214d0e897e2d");
//    UUID orderId_PURCHASED = UUID.fromString("b809b6a4-f5c6-425b-a70b-dc941f3b9dad");
//    UUID orderId_IN_QUEUE = UUID.fromString("84681571-c046-4811-8b20-b22e25a4084c");
//    UUID orderID_PACKING = UUID.fromString("95ca0860-d2d5-4f85-a65f-54942110a363");
//    UUID orderID_COLLECTION = UUID.fromString("3197bb1-34e7-42d3-8735-09871ad2504c");
//    UUID orderID_DELIVERY_COLLECTED = UUID.fromString("34ff8f71-9ef0-4c4a-86fc-740fa9398b27");
//    UUID orderID_CUSTOMER_COLLECTED = UUID.fromString("b43aefcc-a8f9-40a6-a8ba-71f4d137a40e");
//    UUID orderID_DELIVERED = UUID.fromString("a8f54965-5c09-4748-b28f-e6a106985ff1");
//
//    List<Order> orders = new ArrayList<>();
    @Override
    public ResponseEntity<PaymentUpdateOrderResponse> updateOrder(PaymentUpdateOrderRequest body) {


        //add mock data to repo
//        List<Item> mockItemList = new ArrayList<>();
//        Item item1, item2;
//        item1=new Item("Heinz Tomato Sauce","p234058925","91234567-9ABC-DEF0-1234-56789ABCDEFF",storeID,36.99,1,"description","img/");
//        item2=new Item("Bar one","p123984123","62234567-9ABC-DEF0-1234-56789ABCDEFA", storeID,14.99,3,"description","img/");
//        mockItemList.add(item1); mockItemList.add(item2);
//
//        double totalCost = 14.99 + 36.99;
//        Order order = new Order();
//        order.setOrderID(orderId_AWAITNG_PAYMENT);
//        order.setUserID(userID);
//        order.setStoreID(storeID);
//        order.setShopperID(shopperID);
//        order.setCreateDate(Calendar.getInstance());
//        order.setTotalCost(totalCost);
//        order.setType(OrderType.DELIVERY);
//        order.setStatus(OrderStatus.AWAITING_PAYMENT);
//        order.setItems(mockItemList);
//        order.setStoreAddress(new GeoPoint(-25.74929765305105, 28.235606061624217, "Hatfield Plaza 1122 Burnett Street &, Grosvenor St, Hatfield, Pretoria, 0083"));
//        order.setDeliveryAddress(new GeoPoint(-25.74929765305105, 28.235606061624217, "Hatfield Plaza 1122 Burnett Street &, Grosvenor St, Hatfield, Pretoria, 0083"));
//        totalCost = 0;
//
//        orders.add(order);
//
//        order.setOrderID(orderId_PURCHASED);
//        order.setStatus(OrderStatus.PURCHASED);
//        orders.add(order);
//
//        order.setOrderID(orderId_IN_QUEUE);
//        order.setStatus(OrderStatus.IN_QUEUE);
//        orders.add(order);
//
//        order.setOrderID(orderID_PACKING);
//        order.setStatus(OrderStatus.PACKING);
//        orders.add(order);
//
//        order.setOrderID(orderID_COLLECTION);
//        order.setStatus(OrderStatus.AWAITING_COLLECTION);
//        orders.add(order);
//
//        order.setOrderID(orderID_DELIVERY_COLLECTED);
//        order.setStatus(OrderStatus.DELIVERY_COLLECTED);
//        orders.add(order);
//
//        order.setOrderID(orderID_CUSTOMER_COLLECTED);
//        order.setStatus(OrderStatus.CUSTOMER_COLLECTED);
//        orders.add(order);
//
//        order.setOrderID(orderID_DELIVERED);
//        order.setStatus(OrderStatus.DELIVERED);


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

            UUID orderID = UUID.fromString(body.getOrderId());
            GeoPoint deliveryAddress = new GeoPoint();

            if(body.getDeliveryAddress() != null) {
                deliveryAddress = new GeoPoint(body.getDeliveryAddress().getLatitude().doubleValue(), body.getDeliveryAddress().getLongitude().doubleValue(), body.getDeliveryAddress().getAddress());
            }else{
                deliveryAddress = null;
            }

            List<CartItemObject> cartItemObjectList = convertCartItems(body.getItems());

            UpdateOrderRequest request = new UpdateOrderRequest(orderID, assignCartItems(cartItemObjectList),
                    discount, orderType, deliveryAddress);

            UpdateOrderResponse updateOrderResponse = paymentService.updateOrder(request);
            try {
                response.setMessage(updateOrderResponse.getMessage());
                response.setOrder(updateOrderResponse.getOrder());
                response.setSuccess(updateOrderResponse.isSuccess());
                response.setTimestamp(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(updateOrderResponse.getTimestamp()));
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

        try{
            GetStatusRequest getStatusRequest = new GetStatusRequest(UUID.fromString(body.getOrderID()));
            GetStatusResponse getStatusResponse = paymentService.getStatus(getStatusRequest);
            try {
                response.setMessage(getStatusResponse.getMessage());
                response.setStatus(getStatusResponse.getStatus());
                response.setSuccess(getStatusResponse.isSuccess());
                response.setTimestamp(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(getStatusResponse.getTimestamp()));
            }catch(Exception e){

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    public ResponseEntity<PaymentSubmitOrderResponse> submitOrder(PaymentSubmitOrderRequest body) {

        //add mock data to repo

//        GeoPoint storeAddress=new GeoPoint(3.0, 3.0, "PnP, Hillcrest Boulevard");
//        Store store1 = new Store();
//        store1.setStoreID(storeID);
//        store1.setStoreBrand("PnP");
//        store1.setOpeningTime(7);
//        store1.setClosingTime(20);
//        store1.setOpen(true);
//        store1.setMaxOrders(5);
//        store1.setStoreLocation(storeAddress);

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
            SubmitOrderRequest submitOrderRequest = new SubmitOrderRequest(
                    assignCartItems(cartItemObjects), body.getDiscount().doubleValue(),
                    UUID.fromString(body.getStoreId()), orderType, body.getLongitude().doubleValue(),
                    body.getLatitude().doubleValue(), body.getDeliveryAddress());
            SubmitOrderResponse submitOrderResponse = paymentService.submitOrder(submitOrderRequest);
            System.out.println("AFTER THE CALL");
            try {
                response.setMessage(submitOrderResponse.getMessage());
                response.setOrderId(submitOrderResponse.getOrder().getOrderID().toString());
                response.setSuccess(submitOrderResponse.getsuccess());
                response.setTimestamp(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(submitOrderResponse.getTimestamp()));
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


        //add mock data to repo
//        List<Item> mockItemList = new ArrayList<>();
//        Item item1, item2;
//        item1=new Item("Heinz Tomato Sauce","p234058925","91234567-9ABC-DEF0-1234-56789ABCDEFF",storeID,36.99,1,"description","img/");
//        item2=new Item("Bar one","p123984123","62234567-9ABC-DEF0-1234-56789ABCDEFA", storeID,14.99,3,"description","img/");
//        mockItemList.add(item1); mockItemList.add(item2);
//
//        double totalCost = 14.99 + 36.99;
//        Order order = new Order();
//        order.setOrderID(orderId_AWAITNG_PAYMENT);
//        order.setUserID(userID);
//        order.setStoreID(storeID);
//        order.setShopperID(shopperID);
//        order.setCreateDate(Calendar.getInstance());
//        order.setTotalCost(totalCost);
//        order.setType(OrderType.DELIVERY);
//        order.setStatus(OrderStatus.AWAITING_PAYMENT);
//        order.setItems(mockItemList);
//        order.setStoreAddress(new GeoPoint(-25.74929765305105, 28.235606061624217, "Hatfield Plaza 1122 Burnett Street &, Grosvenor St, Hatfield, Pretoria, 0083"));
//        order.setDeliveryAddress(new GeoPoint(-25.74929765305105, 28.235606061624217, "Hatfield Plaza 1122 Burnett Street &, Grosvenor St, Hatfield, Pretoria, 0083"));
//        totalCost = 0;
//
//        orders.add(order);
//
//        order.setOrderID(orderId_PURCHASED);
//        order.setStatus(OrderStatus.PURCHASED);
//        orders.add(order);
//
//        order.setOrderID(orderId_IN_QUEUE);
//        order.setStatus(OrderStatus.IN_QUEUE);
//        orders.add(order);
//
//        order.setOrderID(orderID_PACKING);
//        order.setStatus(OrderStatus.PACKING);
//        orders.add(order);
//
//        order.setOrderID(orderID_COLLECTION);
//        order.setStatus(OrderStatus.AWAITING_COLLECTION);
//        orders.add(order);
//
//        order.setOrderID(orderID_DELIVERY_COLLECTED);
//        order.setStatus(OrderStatus.DELIVERY_COLLECTED);
//        orders.add(order);
//
//        order.setOrderID(orderID_CUSTOMER_COLLECTED);
//        order.setStatus(OrderStatus.CUSTOMER_COLLECTED);
//        orders.add(order);
//
//        order.setOrderID(orderID_DELIVERED);
//        order.setStatus(OrderStatus.DELIVERED);
//

        PaymentGetItemsResponse response = new PaymentGetItemsResponse();
        HttpStatus httpStatus = HttpStatus.OK;

        try{

            GetItemsRequest getItemsRequest = new GetItemsRequest(body.getOrderID());
            GetItemsResponse getItemsResponse = paymentService.getItems(getItemsRequest);
            try {
                response.setMessage(getItemsResponse.getMessage());
                response.setCartItems(populateCartItems(getItemsResponse.getCartItems()));
                response.setSuccess(getItemsResponse.isSuccess());
                response.setTimestamp(getItemsResponse.getTimestamp().toString());
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
                response.setTimestamp(String.valueOf(getOrderResponse.getTimestamp()));
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
    public ResponseEntity<PaymentGetOrdersResponse> getOrder(PaymentGetOrdersRequest body) {
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
                response.setTimestamp(String.valueOf(getOrdersResponse.getTimestamp()));
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
            item.setProductID(i.getProductId());
            item.setBarcode(i.getBarcode());
            item.setQuantity(i.getQuantity());
            item.setName(i.getName());
            item.setStoreID(UUID.fromString(i.getStoreId()));
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
            currentItem.setProductId(responseItems.get(i).getProductID());
            currentItem.setStoreId(responseItems.get(i).getStoreID().toString());
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
            orderObject.setOrderId(order.getOrderID().toString());
        if(order.getUserID() != null)
            orderObject.setUserId(order.getUserID().toString());
        if(order.getStoreID() != null)
            orderObject.setStoreId(order.getStoreID().toString());
        if(order.getShopperID() != null)
            orderObject.setShopperId(order.getShopperID().toString());
        if(order.getCreateDate()!=null)
            orderObject.setCreateDate(order.getCreateDate().toString());
        if(order.getProcessDate() != null)
            orderObject.setProcessDate(order.getProcessDate().toString());
        if(order.getTotalCost() != null)
            orderObject.setTotalPrice(BigDecimal.valueOf(order.getTotalCost()));
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
            item.setProductID(i.getProductId());
            item.setBarcode(i.getBarcode());
            item.setQuantity(i.getQuantity());
            item.setName(i.getName());
            item.setStoreID(UUID.fromString(i.getStoreId()));
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

        for (ItemObject i: itemObjectList) {
            CartItemObject item = new CartItemObject();
            item.setProductId(i.getProductId());
            item.setBarcode(i.getBarcode());
            item.setQuantity(i.getQuantity());
            item.setName(i.getName());
            item.setStoreId(i.getStoreId());
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
            item.setProductId(i.getProductID());
            item.setBarcode(i.getBarcode());
            item.setQuantity(i.getQuantity());
            item.setName(i.getName());
            item.setStoreId(i.getStoreID().toString());
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
}
