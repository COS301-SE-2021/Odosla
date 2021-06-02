package shopping;

import order.dataclass.Order;
import order.dataclass.OrderStatus;
import shopping.exceptions.InvalidRequestException;
import shopping.requests.AddToQueueRequest;
import shopping.requests.ScanItemRequest;
import shopping.responses.AddToQueueResponse;
import shopping.responses.GetStockListResponse;
import shopping.responses.ScanItemResponse;

import java.util.Calendar;

public class ShoppingServiceImpl implements ShoppingService {

    @Override
    public GetStockListResponse getStockList() {
        return null;
    }

    @Override
    public AddToQueueResponse addToQueue(AddToQueueRequest request) throws InvalidRequestException {

        AddToQueueResponse response = null;

        boolean invalidReq = false;
        String invalidMessage = "";

        if (request == null || request.getOrder() == null){
            invalidReq = true;
            invalidMessage = "Invalid request: null value received";
        } else {
            Order order = request.getOrder();
            if (order.getOrderID() == null){
                invalidMessage = "Invalid request: Missing order ID";
            } else if (order.getUserID() == null){
                invalidMessage = "Invalid request: missing user ID";
            } else if (order.getStoreID() == null){
                invalidMessage = "Invalid request: missing store ID";
            } else if (order.getTotalCost() == null){
                invalidMessage = "Invalid request: missing order cost";
            } else if (order.getStatus() != OrderStatus.PURCHASED){
                invalidMessage = "Invalid request: order has incompatible status";
            } else if (order.getItems() == null || order.getItems().isEmpty()){
                invalidMessage = "Invalid request: item list is empty or null";
            }
        }

        if (invalidReq) throw new InvalidRequestException(invalidMessage);

        Order updatedOrder = request.getOrder();

        // // Update the order status and create time // //
        updatedOrder.setStatus(OrderStatus.IN_QUEUE);
        updatedOrder.setProcessDate(Calendar.getInstance());

        // <paymentService>.updateOrder(updatedOrder);


        // // Add order to respective store order queue in db // //
        // ...

        response = new AddToQueueResponse(true, "Order successfuly created", Calendar.getInstance().getTime());

        return response;
    }

    @Override
    public ScanItemResponse scanItem(ScanItemRequest request) {
        return null;
    }
}
