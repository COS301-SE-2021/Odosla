import 'package:flutter/foundation.dart';
import 'package:odosla/model/cart_item.dart';
import 'package:odosla/services/api_service.dart';

class CartProvider with ChangeNotifier {
  Map<String, CartItem> _items = {};
  double _total = 0.0;
  String currStore = "";
  bool _activeOrder = false;
  String _activeOrderID = "";

  ApiService api = ApiService();

  CartProvider() {
    _items = {};
  }

  set activeOrder(bool active) {
    _activeOrder = active;
    notifyListeners();
  }

  bool get activeOrder => _activeOrder;

  set activeOrderID(String orderID) {
    _activeOrderID = orderID;
    notifyListeners();
  }

  String get activeOrderID => _activeOrderID;

  Map<String, CartItem> get items => _items;

  set items(Map<String, CartItem> value) {
    _items = value;
  }

  void addItem(CartItem item, int q) {
    if (items.containsKey(item.id)) {
      items.update(
        item.id,
        (cartItem) => cartItem.copy(
            item.id,
            item.title,
            item.barcode,
            item.storeID,
            item.price,
            cartItem.quantity + q,
            item.description,
            item.imgUrl,
            item.brand,
            item.size,
            item.type),
      );
    } else {
      items.putIfAbsent(
        item.id,
        () => item.copy(
            item.id,
            item.title,
            item.barcode,
            item.storeID,
            item.price,
            q,
            item.description,
            item.imgUrl,
            item.brand,
            item.size,
            item.type),
      );
    }

    calcTotal();
    notifyListeners();
  }

  void clearItems() {
    _items.clear();
    calcTotal();
    notifyListeners();
  }

  void decrementItem(CartItem item) {
    if (items.containsKey(item.id)) {
      if (item.quantity == 1) {
        items.remove(item.id);
      } else
        items.update(
          item.id,
          (cartItem) => cartItem.copy(
              item.id,
              item.title,
              item.barcode,
              item.storeID,
              item.price,
              cartItem.quantity - 1,
              item.description,
              item.imgUrl,
              item.brand,
              item.size,
              item.type),
        );
    } else {
      //?

    }

    calcTotal();
    notifyListeners();
  }

  String get store => currStore;
  set store(String storeID) {
    currStore = storeID;
  }

  double get total => _total;

  void calcTotal() {
    double temp = 0.0;
    for (var i in _items.values) {
      temp += i.price * i.quantity;
    }
    _total = temp;

    notifyListeners();
  }
}
