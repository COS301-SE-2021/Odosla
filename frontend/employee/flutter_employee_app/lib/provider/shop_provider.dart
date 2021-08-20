

import 'package:flutter/cupertino.dart';
import 'package:flutter_employee_app/models/Store.dart';
import 'package:provider/provider.dart';

class ShopProvider with ChangeNotifier{
  Store _store=new Store("", "", 0, 0, true,"");

  ShopProvider();

  Store get store=>_store;
  set store(Store s){_store=s;notifyListeners();}
}