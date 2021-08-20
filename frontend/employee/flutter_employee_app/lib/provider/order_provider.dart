
import 'package:flutter/cupertino.dart';
import 'package:flutter_employee_app/models/Order.dart';

class OrderProvider with ChangeNotifier{

  Order _order=new Order("","","","",0.0,List.empty(),"");

  OrderProvider();

  Order get order=>_order;

  set order(Order o){ _order=o; notifyListeners();}

}