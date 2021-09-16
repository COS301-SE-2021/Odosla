import 'package:flutter/cupertino.dart';
import 'package:odosla/model/store.dart';

class StoreProvider with ChangeNotifier {
  Store _store = new Store("", "", 0, 0, false, "", 0, 0);

  GroceryListProvider() {}

  Store get store => _store;

  set store(Store store) {
    _store = store;
  }
}
