import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:odosla/model/cart_item.dart';

class GroceryListProvider with ChangeNotifier {
  Map<String, List<CartItem>> lists = Map();

  GroceryListProvider() {}

  Map<String, List<CartItem>> get groceries {
    return lists;
  }

  set add(String val) {
    notifyListeners();
  }

  void delete(String name) {
    notifyListeners();
  }
}
