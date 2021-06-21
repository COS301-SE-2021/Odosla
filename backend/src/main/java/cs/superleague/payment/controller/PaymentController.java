package cs.superleague.payment.controller;

import cs.superleague.api.PaymentApi;
import cs.superleague.integration.ServiceSelector;
import cs.superleague.models.PaymentUpdateOrderRequest;
import cs.superleague.models.PaymentUpdateOrderResponse;
import cs.superleague.payment.PaymentServiceImpl;
import cs.superleague.payment.dataclass.GeoPoint;
import cs.superleague.payment.dataclass.Order;
import cs.superleague.payment.dataclass.OrderStatus;
import cs.superleague.payment.dataclass.OrderType;
import cs.superleague.payment.repos.OrderRepo;
import cs.superleague.payment.requests.UpdateOrderRequest;
import cs.superleague.payment.responses.UpdateOrderResponse;
import cs.superleague.shopping.dataclass.Item;
import cs.superleague.shopping.repos.ItemRepo;
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

    UUID storeID = UUID.fromString("01234567-9ABC-DEF0-1234-56789ABCDEF0");
    UUID shopperID = UUID.randomUUID();
    UUID userID = UUID.fromString("7bc59ea6-aa30-465d-bcab-64e894bef586");
    UUID orderId_AWAITNG_PAYMENT = UUID.fromString("b5e2ef07-865c-4025-b393-045b0a0abe62");
    UUID orderId_PURCHASED = UUID.fromString("b809b6a4-f5c6-425b-a70b-dc941f3b9dad");
    UUID orderId_IN_QUEUE = UUID.fromString("84681571-c046-4811-8b20-b22e25a4084c");
    UUID orderID_PACKING = UUID.fromString("95ca0860-d2d5-4f85-a65f-54942110a363");
    UUID orderID_COLLECTION = UUID.fromString("3197bb1-34e7-42d3-8735-09871ad2504c");
    UUID orderID_DELIVERY_COLLECTED = UUID.fromString("34ff8f71-9ef0-4c4a-86fc-740fa9398b27");
    UUID orderID_CUSTOMER_COLLECTED = UUID.fromString("b43aefcc-a8f9-40a6-a8ba-71f4d137a40e");
    UUID orderID_DELIVERED = UUID.fromString("a8f54965-5c09-4748-b28f-e6a106985ff1");

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
        order.setUserID(storeID);
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

        orderRepo.save(order);

        order.setOrderID(orderId_PURCHASED);
        order.setStatus(OrderStatus.PURCHASED);
        orderRepo.save(order);

        order.setOrderID(orderId_IN_QUEUE);
        order.setStatus(OrderStatus.IN_QUEUE);
        orderRepo.save(order);

        order.setOrderID(orderID_PACKING);
        order.setStatus(OrderStatus.PACKING);
        orderRepo.save(order);


        order.setOrderID(orderID_COLLECTION);
        order.setStatus(OrderStatus.AWAITING_COLLECTION);
        orderRepo.save(order);

        order.setOrderID(orderID_DELIVERY_COLLECTED);
        order.setStatus(OrderStatus.DELIVERY_COLLECTED);
        orderRepo.save(order);

        order.setOrderID(orderID_CUSTOMER_COLLECTED);
        order.setStatus(OrderStatus.CUSTOMER_COLLECTED);
        orderRepo.save(order);

        order.setOrderID(orderID_DELIVERED);
        order.setStatus(OrderStatus.DELIVERED);
        orderRepo.save(order);


        PaymentUpdateOrderResponse response = new PaymentUpdateOrderResponse();
        HttpStatus httpStatus = HttpStatus.OK;

        try{
            OrderType orderType = null;
            if(body.getOrderType() == "Collection"){
                orderType = OrderType.COLLECTION;
            }else if(body.getOrderType() == "Delivery"){
                orderType = OrderType.DELIVERY;
            }

            System.out.println(body.getOrderId());
//                GetItemsResponse getItemsResponse = ServiceSelector.getShoppingService().getItems(new GetItemsRequest(UUID.fromString("01234567-9ABC-DEF0-1234-56789ABCDEF0")));
            System.out.println("Ta");
            double discount = 0.00;
            if(body.getDiscount() != null)
                discount = body.getDiscount().doubleValue();

            UpdateOrderRequest request = new UpdateOrderRequest(order.getOrderID(), order.getUserID(), body.getItems(), discount, orderType, order.getDeliveryAddress());

            UpdateOrderResponse updateOrderResponse = ServiceSelector.getPaymentService().updateOrder(request);
            System.out.println("ola");
            try {
                System.out.println("hello");
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
}
