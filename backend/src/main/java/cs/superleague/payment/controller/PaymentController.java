package cs.superleague.payment.controller;

import cs.superleague.api.PaymentApi;
import cs.superleague.integration.ServiceSelector;
import cs.superleague.models.*;
import cs.superleague.payment.PaymentServiceImpl;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.GetStatusRequest;
import cs.superleague.payment.requests.SubmitOrderRequest;
import cs.superleague.payment.requests.UpdateOrderRequest;
import cs.superleague.payment.responses.GetStatusResponse;
import cs.superleague.payment.responses.SubmitOrderResponse;
import cs.superleague.payment.responses.UpdateOrderResponse;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.dataclass.Store;
import cs.superleague.shopping.repos.ItemRepo;
import cs.superleague.shopping.repos.StoreRepo;
import cs.superleague.user.dataclass.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@CrossOrigin
@RestController
public class PaymentController implements PaymentApi {

    @Autowired
    PaymentServiceImpl paymentService;

    @Autowired
    OrderRepo orderRepo;

    @Autowired
    ItemRepo itemRepo;

    @Autowired
    StoreRepo storeRepo;

    UUID storeID = UUID.fromString("01234567-9ABC-DEF0-1234-56789ABCDEF0");
    UUID shopperID = UUID.randomUUID();
    UUID userID = UUID.fromString("7bc59ea6-aa30-465d-bcab-64e894bef586");
    UUID orderId_AWAITNG_PAYMENT = UUID.fromString("8d8fe4d6-492b-453e-8ef1-214d0e897e2d");
    UUID orderId_PURCHASED = UUID.fromString("b809b6a4-f5c6-425b-a70b-dc941f3b9dad");
    UUID orderId_IN_QUEUE = UUID.fromString("84681571-c046-4811-8b20-b22e25a4084c");
    UUID orderID_PACKING = UUID.fromString("95ca0860-d2d5-4f85-a65f-54942110a363");
    UUID orderID_COLLECTION = UUID.fromString("3197bb1-34e7-42d3-8735-09871ad2504c");
    UUID orderID_DELIVERY_COLLECTED = UUID.fromString("34ff8f71-9ef0-4c4a-86fc-740fa9398b27");
    UUID orderID_CUSTOMER_COLLECTED = UUID.fromString("b43aefcc-a8f9-40a6-a8ba-71f4d137a40e");
    UUID orderID_DELIVERED = UUID.fromString("a8f54965-5c09-4748-b28f-e6a106985ff1");

    List<Order> orders = new ArrayList<>();
    @Override
    public ResponseEntity<PaymentUpdateOrderResponse> updateOrder(PaymentUpdateOrderRequest body) {


        //add mock data to repo
        List<Item> mockItemList = new ArrayList<>();
        Item item1, item2;
        item1=new Item("Heinz Tomato Sauce","p234058925","91234567-9ABC-DEF0-1234-56789ABCDEFF",storeID,36.99,1,"description","img/");
        item2=new Item("Bar one","p123984123","62234567-9ABC-DEF0-1234-56789ABCDEFA", storeID,14.99,3,"description","img/");
        itemRepo.save(item1); itemRepo.save(item2);
        mockItemList.add(item1); mockItemList.add(item2);

        double totalCost = 14.99 + 36.99;
        Order order = new Order();
        order.setOrderID(orderId_AWAITNG_PAYMENT);
        order.setUserID(userID);
        order.setStoreID(storeID);
        order.setShopperID(shopperID);
        order.setCreateDate(Calendar.getInstance());
        order.setTotalCost(totalCost);
        order.setType(OrderType.DELIVERY);
        order.setStatus(OrderStatus.AWAITING_PAYMENT);
        order.setItems(mockItemList);
        order.setStoreAddress(new GeoPoint(-25.74929765305105, 28.235606061624217, "Hatfield Plaza 1122 Burnett Street &, Grosvenor St, Hatfield, Pretoria, 0083"));
        order.setDeliveryAddress(new GeoPoint(-25.74929765305105, 28.235606061624217, "Hatfield Plaza 1122 Burnett Street &, Grosvenor St, Hatfield, Pretoria, 0083"));
        totalCost = 0;

        orders.add(order);
        orderRepo.save(order);

        order.setOrderID(orderId_PURCHASED);
        order.setStatus(OrderStatus.PURCHASED);
        orderRepo.save(order);
        orders.add(order);

        order.setOrderID(orderId_IN_QUEUE);
        order.setStatus(OrderStatus.IN_QUEUE);
        orderRepo.save(order);
        orders.add(order);

        order.setOrderID(orderID_PACKING);
        order.setStatus(OrderStatus.PACKING);
        orderRepo.save(order);
        orders.add(order);

        order.setOrderID(orderID_COLLECTION);
        order.setStatus(OrderStatus.AWAITING_COLLECTION);
        orderRepo.save(order);
        orders.add(order);

        order.setOrderID(orderID_DELIVERY_COLLECTED);
        order.setStatus(OrderStatus.DELIVERY_COLLECTED);
        orderRepo.save(order);
        orders.add(order);

        order.setOrderID(orderID_CUSTOMER_COLLECTED);
        order.setStatus(OrderStatus.CUSTOMER_COLLECTED);
        orderRepo.save(order);
        orders.add(order);

        order.setOrderID(orderID_DELIVERED);
        order.setStatus(OrderStatus.DELIVERED);
        orders.add(order);
        orderRepo.save(order);


        PaymentUpdateOrderResponse response = new PaymentUpdateOrderResponse();
        HttpStatus httpStatus = HttpStatus.OK;

        try{
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
            UUID userID = UUID.fromString(body.getUserId());

            UpdateOrderRequest request = new UpdateOrderRequest(orderID, userID, assignItems(body.getItems()), discount, orderType, order.getDeliveryAddress());

            UpdateOrderResponse updateOrderResponse = ServiceSelector.getPaymentService().updateOrder(request);
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

        orderRepo.deleteAll();
        itemRepo.deleteAll();

        return new ResponseEntity<>(response, httpStatus);
    }

    @Override
    public ResponseEntity<PaymentGetStatusResponse> getStatus(PaymentGetStatusRequest body) {

        PaymentGetStatusResponse response = new PaymentGetStatusResponse();
        HttpStatus httpStatus = HttpStatus.OK;

        try{
            GetStatusRequest getStatusRequest = new GetStatusRequest(UUID.fromString(body.getOrderID()));
            GetStatusResponse getStatusResponse = ServiceSelector.getPaymentService().getStatus(getStatusRequest);
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

        GeoPoint storeAddress=new GeoPoint(3.0, 3.0, "PnP, Hillcrest Boulevard");
        Store store1 = new Store();
        store1.setStoreID(storeID);
        store1.setStoreBrand("PnP");
        store1.setOpeningTime(7);
        store1.setClosingTime(20);
        store1.setOpen(true);
        store1.setMaxOrders(5);
        store1.setStoreLocation(storeAddress);
        storeRepo.save(store1);

        PaymentSubmitOrderResponse response = new PaymentSubmitOrderResponse();
        HttpStatus httpStatus = HttpStatus.OK;

        OrderType orderType = null;
        if(body.getOrderType().equals("DELIVERY")){
            orderType= OrderType.DELIVERY;
        } else if(body.getOrderType().equals("COLLECTION")){
            orderType= OrderType.COLLECTION;
        }

        try{
            SubmitOrderRequest submitOrderRequest = new SubmitOrderRequest(UUID.fromString(body.getUserId()), assignItems(body.getListOfItems()), body.getDiscount().doubleValue(), UUID.fromString(body.getStoreId()), orderType, body.getLongitude().doubleValue(), body.getLatitude().doubleValue(), body.getDeliveryAddress());
            SubmitOrderResponse submitOrderResponse = ServiceSelector.getPaymentService().submitOrder(submitOrderRequest);
            try {
                response.setMessage(submitOrderResponse.getMessage());
                response.setOrderStatus(submitOrderResponse.getOrder().getStatus().toString());
                response.setSuccess(submitOrderResponse.getsuccess());
                response.setTimestamp(new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").format(submitOrderResponse.getTimestamp()));
            }catch(Exception e){

            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity<>(response, httpStatus);
    }

    // helper
    List<Item> assignItems(List<ItemObject> itemObjectList){

        double price = 0.00;
        Item item = new Item();
        List<Item> items = new ArrayList<>();

        if(itemObjectList == null){
            return null;
        }

        for (ItemObject i: itemObjectList) {
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
            items.add(itemRepo.save(item));

        }
        return items;
    }
}
