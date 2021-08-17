
import 'package:flutter/cupertino.dart';
import 'package:flutter_employee_app/models/Delivery.dart';
import 'package:flutter_employee_app/models/GeoPoint.dart';

class DeliveryProvider with ChangeNotifier{

  Delivery _delivery=new Delivery("", new GeoPoint(0.0, 0.0, ""),new GeoPoint(0.0, 0.0, ""), "","", "", "", "", 0.0, false);

  DeliveryProvider();

  Delivery get delivery=>_delivery;
  set delivery(Delivery s){_delivery;notifyListeners();}
}