import 'package:flutter_employee_app/models/GeoPoint.dart';
import 'package:flutter_employee_app/utilities/functions.dart';

class Delivery{

  String deliveryID="";
  GeoPoint pickUpLocationOne=new GeoPoint(0.0, 0.0,"");
  GeoPoint pickUpLocationTwo=new GeoPoint(0.0, 0.0,"");
  GeoPoint pickUpLocationThree=new GeoPoint(0.0, 0.0,"");
  GeoPoint dropOffLocation=new GeoPoint(0.0, 0.0, "");
  String orderID="";
  String customerID="";
  String driverID="";
  String storeID="";
  String deliveryStatus="";
  double cost=0.0;
  bool completed=false;

  bool isOneDelivery=false;
  bool isTwoDeliveries=false;
  bool isThreeDeliveries=false;



  Delivery(this.deliveryID, this.pickUpLocationOne, this.dropOffLocation,
      this.orderID, this.customerID, this.driverID, this.storeID,
      this.deliveryStatus, this.cost, this.completed, this.pickUpLocationTwo, this.pickUpLocationThree);

  Delivery.fromJson(Map<String, dynamic> json):
      deliveryID=json["deliveryID"],
        pickUpLocationOne=GeoPoint.fromJson(json["pickUpLocationOne"]),
      dropOffLocation=GeoPoint.fromJson(json["dropOffLocation"]),
      orderID=json["orderID"],
      customerID=json["customerId"],
      storeID=json["storeId"],
      deliveryStatus=json["status"],
      driverID=json["driverId"].toString(),
      cost=json["cost"],
      completed=json["completed"],
      pickUpLocationTwo=GeoPoint.fromJson(json["pickUpLocationOne"]),
      pickUpLocationThree=GeoPoint.fromJson(json["pickUpLocationThree"]);


}