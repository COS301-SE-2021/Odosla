
import 'package:flutter/cupertino.dart';
import 'package:flutter_employee_app/models/Customer.dart';
import 'package:flutter_employee_app/models/Delivery.dart';
import 'package:flutter_employee_app/models/GeoPoint.dart';

class DeliveryProvider with ChangeNotifier{

  Delivery _delivery=new Delivery("", new GeoPoint(0.0, 0.0, ""),new GeoPoint(0.0, 0.0, ""), "","", "", "", "", 0.0, false);

  Customer _customer=new Customer("","","","","");

  DeliveryProvider();

  Customer get customer=>_customer;

  set customer(Customer s){_customer=s; notifyListeners();}

  Delivery get delivery=>_delivery;

  set delivery(Delivery s){_delivery=s;notifyListeners();}
}