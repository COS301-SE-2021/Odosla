import 'package:flutter/foundation.dart';
import 'package:odosla/model/cart_item.dart';
import 'package:odosla/services/api_service.dart';

class CartProvider with ChangeNotifier {
  Map<String, CartItem> _items = {};
  List<CartItem> _cheaperItems = <CartItem>[];
  double _total = 0.0;
  double _cheaperTotal = 0.0;
  String currStore = "";
  bool _activeOrder = false;
  String _activeOrderID = "";
  Map<String, double> _activeStoreLocation = {"lat": 0, "long": 0};
  var _ids = <String>[];
  String _storeIDOne = "";
  String _storeIDTwo = "";
  String _storeIDThree = "";
  String _cheaperStoreIDOne = "";
  String _cheaperStoreIDTwo = "";
  String _cheaperStoreIDThree = "";
  ApiService api = ApiService();

  String get storeIDOne => _storeIDOne;

  Map<String, double> get activeStoreLocation => _activeStoreLocation;

  set activeStoreLocation(Map<String, double> location) {
    _activeStoreLocation = location;
    notifyListeners();
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

  List<CartItem> get cheaperItems => _cheaperItems;

  set cheaperItems(List<CartItem> value) {
    _cheaperItems = value;
    calcTotalC();
  }

  set items(Map<String, CartItem> value) {
    _items = value;
  }

  bool atMax() {
    if (_storeIDThree == "") return false;
    if (_storeIDTwo == "") return false;
    if (_storeIDOne == "") return false;
    return true;
  }

  void setCheaperAsList() {
    _storeIDOne = _cheaperStoreIDOne;
    _storeIDTwo = _cheaperStoreIDTwo;
    _storeIDThree = _cheaperStoreIDThree;
    _total = _cheaperTotal;
    clearItems();
    for (var i in _cheaperItems){
      addItem(i, i.quantity);
    }
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
            item.soldOut),
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
            item.soldOut),
      );

      debugPrint("###item storeid  " + item.storeID);
      if (item.storeID == _storeIDOne ||
          item.storeID == _storeIDTwo ||
          item.storeID == _storeIDThree) {
      } else {
        if (_storeIDOne == "") {
          debugPrint("##HERE");
          _storeIDOne = item.storeID;
        } else if (_storeIDTwo == "") {
          _storeIDTwo = item.storeID;
        } else if (_storeIDThree == "") {
          _storeIDThree = item.storeID;
        }
        notifyListeners();
      }

      debugPrint(" sid1 " + _storeIDOne);
      debugPrint(" sid2 " + _storeIDTwo);
      debugPrint(" sid3 " + _storeIDThree);

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
              item.soldOut),
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

  double get cheaperTotal => _cheaperTotal;

  void calcTotal() {
    double temp = 0.0;
    for (var i in _items.values) {
      temp += i.price * i.quantity;
    }
    _total = temp;

    notifyListeners();
  }

  void calcTotalC() {
    double temp = 0.0;
    for (var i in _cheaperItems) {
      temp += i.price * i.quantity;
    }
    _cheaperTotal = temp;

    notifyListeners();
  }

  List<String> get ids => _ids;

  String get storeIDTwo => _storeIDTwo;

  String get storeIDThree => _storeIDThree;

  set storeIDThree(String value) {
    _storeIDThree = value;
    notifyListeners();
  }

  set storeIDTwo(String value) {
    _storeIDTwo = value;
    notifyListeners();
  }

  set storeIDOne(String value) {
    _storeIDOne = value;
    notifyListeners();
  }

  String get cheaperStoreIDOne => _cheaperStoreIDOne;

  String get cheaperStoreIDTwo => _cheaperStoreIDTwo;

  String get cheaperStoreIDThree => _cheaperStoreIDThree;

  set cheaperStoreIDThree(String value) {
    _cheaperStoreIDThree = value;
    notifyListeners();
  }

  set cheaperStoreIDTwo(String value) {
    _cheaperStoreIDTwo = value;
    notifyListeners();
  }

  set cheaperStoreIDOne(String value) {
    _cheaperStoreIDOne = value;
    notifyListeners();
  }
}
