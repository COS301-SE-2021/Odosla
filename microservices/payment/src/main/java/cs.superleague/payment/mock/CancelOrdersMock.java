//package cs.superleague.payment.mock;
//
//import cs.superleague.payment.dataclass.GeoPoint;
//import cs.superleague.shopping.dataclass.Item;
//import cs.superleague.payment.dataclass.Order;
//
//import java.util.*;
//
//public class CancelOrdersMock {
//
//    private List<Item> items;
//    private List<Order> orders;
//
//    public List<Order> getOrders() {
//        // will add mock orders here
//        items = new ArrayList<>();
//        orders = new ArrayList<>();
//
//        UUID store1ID = UUID.randomUUID();
//        UUID shoper1PnP = UUID.randomUUID();
//        UUID shoper2PnP = UUID.randomUUID();
//        double totalCost = 0;
//
//        Item item = new Item();
//        item.setName("B-Well Canola Oil");
//        item.setQuantity(1);
//        item.setPrice(30.99);
//        item.setStoreID(store1ID);
//        item.setDescription("Locally produced B-well Canola Oil is naturally cholesterol free, low in saturated fat, high in Omega 3 and is perfect for a healthy lifestyle. Take your food and your health to the next level with a unique and nutritious cooking oil that cares. Make the smart choice with B-well.");
//        item.setBarcode("6009644140053");
//        // item.setProductID();
//        // item.setImageUrl();
//        totalCost += item.getPrice();
//        items.add(item);
//
//        item.setName("PnP Salted Butter 500g");
//        item.setQuantity(1);
//        item.setPrice(64.99);
//        item.setStoreID(store1ID);
//        item.setDescription(" Perfect for cooking, baking and frying");
//        item.setBarcode("6001007094874");
//        totalCost += item.getPrice();
//        // item.setProductID();
//        // item.setImageUrl();
//        items.add(item);
//
//        /*
//        private Calendar createDate;
//        private float discount;
//        private GeoPoint deliveryAddress;
//        private GeoPoint storeAddress;
//        private boolean requiresPharmacy;
//        */
//
//        Order order = new Order();
//        order.setOrderID(UUID.randomUUID());
//        order.setUserID(UUID.randomUUID());
//        order.setStoreID(store1ID);
//        order.setShopperID(shoper1PnP);
//        order.setCreateDate(new Date());
//        order.setTotalCost((Double) totalCost);
////        order.Type(OrderType.DELIVERY);
////        order.setStatus(OrderStatus.AWAITING_PAYMENT);
//        order.setItems(items);
//        order.setStoreAddress(new GeoPoint(-25.74929765305105, 28.235606061624217, "Hatfield Plaza 1122 Burnett Street &, Grosvenor St, Hatfield, Pretoria, 0083"));
//        order.setDeliveryAddress(new GeoPoint(-25.74929765305105, 28.235606061624217, "Hatfield Plaza 1122 Burnett Street &, Grosvenor St, Hatfield, Pretoria, 0083"));
//        totalCost = 0;
//
//        orders.add(order);
//        return orders;
//    }
//
//    public void setOrders(List<Order> orders) {
//        this.orders = orders;
//    }
//}