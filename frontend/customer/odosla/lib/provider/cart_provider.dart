import 'package:flutter/foundation.dart';
import 'package:odosla/model/cart_item.dart';
import 'package:odosla/services/api_service.dart';

class CartProvider with ChangeNotifier {
  Map<String, CartItem> _items = {};
  double _total = 0.0;
  String currStore = "";
  bool _activeOrder = false;
  String _activeOrderID = "";
  Map<String, double> _activeStoreLocation = {"lat": 0, "long": 0};
  var _ids = <String>[];
  String _storeIDOne = "";
  String _storeIDTwo = "";
  String _storeIDThree = "";
  ApiService api = ApiService();

  String get storeIDOne => _storeIDOne;

  Map<String, double> get activeStoreLocation => _activeStoreLocation;

  set activeStoreLocation(Map<String, double> location) {
    _activeStoreLocation = location;
  }

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
          item.type,
        ),
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
          item.type,
        ),
      );

      List list = _items.entries.map((e) => e.value.storeID).toList();
      if (list.contains(item.storeID)) {
      } else {
        if (_storeIDOne == "") {
          _storeIDOne = item.storeID;
        } else if (_storeIDTwo == "") {
          _storeIDTwo = item.storeID;
        } else if (_storeIDThree == "") {
          _storeIDThree = item.storeID;
        }
      }

      _ids.add(item.id);
    }

    calcTotal();
    notifyListeners();
  }

  void clearItems() {
    _items.clear();
    calcTotal();
    notifyListeners();
    _ids = <String>[];
    _storeIDOne = "";
    _storeIDTwo = "";
    _storeIDThree = "";
  }

  void decrementItem(CartItem item) {
    if (items.containsKey(item.id)) {
      if (item.quantity == 1) {
        items.remove(item.id);
        _ids.remove(item.id);

        List list = _items.entries.map((e) => e.value.storeID).toList();
        if (!list.contains(_storeIDOne)) {
          _storeIDOne = "";
        }
        if (!list.contains(_storeIDTwo)) {
          _storeIDTwo = "";
        }
        if (!list.contains(_storeIDThree)) {
          _storeIDThree = "";
        }
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
            item.type,
          ),
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

  List<String> get ids => _ids;

  String get storeIDTwo => _storeIDTwo;

  String get storeIDThree => _storeIDThree;

  set storeIDThree(String value) {
    _storeIDThree = value;
  }

  set storeIDTwo(String value) {
    _storeIDTwo = value;
  }

  set storeIDOne(String value) {
    _storeIDOne = value;
  }
}
