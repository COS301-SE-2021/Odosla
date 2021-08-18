import 'package:flutter_employee_app/models/GeoPoint.dart';

class Delivery{

  String deliveryID="";
  GeoPoint pickUpLocation=new GeoPoint(0.0,0.0,"");
  GeoPoint dropOffLocation=new GeoPoint(0.0, 0.0, "");
  String orderID="";
  String customerID="";
  String driverID="";
  String storeID="";
  String deliveryStatus="";
  double cost=0.0;
  bool completed=false;


  Delivery(this.deliveryID, this.pickUpLocation, this.dropOffLocation,
      this.orderID, this.customerID, this.driverID, this.storeID,
      this.deliveryStatus, this.cost, this.completed);

  Delivery.fromJson(Map<String, dynamic> json):
      deliveryID=json["deliveryID"],
      pickUpLocation=GeoPoint.fromJson(json["pickUpLocation"]),
      dropOffLocation=GeoPoint.fromJson(json["dropOffLocation"]),
      orderID=json["orderID"],
      customerID=json["customerId"],
      storeID=json["storeId"],
      deliveryStatus=json["status"],
      driverID=json["driverId"],
      cost=json["cost"],
      completed=json["completed"];


}