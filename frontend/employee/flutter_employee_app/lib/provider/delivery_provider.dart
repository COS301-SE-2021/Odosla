
import 'package:flutter/cupertino.dart';
import 'package:flutter_employee_app/models/Customer.dart';
import 'package:flutter_employee_app/models/Delivery.dart';
import 'package:flutter_employee_app/models/GeoPoint.dart';

class DeliveryProvider with ChangeNotifier{

  bool _isOneDelivery=false;
  bool _isTwoDeliveries=false;
  bool _isThreeDeliveries=false;

  bool _completedOne = false;
  bool _completedTwo = false;
  bool _completedThree = false;
  bool _completed = false;

  Delivery _delivery=new Delivery("", new GeoPoint(0.0, 0.0, ""),new GeoPoint(0.0, 0.0, ""), "","", "", "", "", 0.0, false,new GeoPoint(0.0, 0.0, ""),new GeoPoint(0.0, 0.0, ""));

  Customer _customer=new Customer("","","","","");

  DeliveryProvider();

  Customer get customer=>_customer;

  set customer(Customer s){_customer=s; notifyListeners();}

  Delivery get delivery=>_delivery;

  set delivery(Delivery s){_delivery=s;notifyListeners();}

  bool get isOneDelivery=>_isOneDelivery;

  set isOneDelivery(bool s){_isOneDelivery=s;notifyListeners();}

  bool get isTwoDeliveries=>_isTwoDeliveries;

  set isTwoDeliveries(bool s){_isTwoDeliveries=s;notifyListeners();}

  bool get isThreeDeliveries=>_isThreeDeliveries;

  set isThreeDeliveries(bool s){_isThreeDeliveries=s;notifyListeners();}

  bool get completed=>_completed;

  set completed(bool s){_completed=s;notifyListeners();}

  bool get completedOne=>_completedOne;

  set completedOne(bool s){_completedOne=s;notifyListeners();}

  bool get completedTwo=>_completedTwo;

  set completedTwo(bool s){_completedTwo=s;notifyListeners();}

  GeoPoint finishFirst(){
    if (this._isOneDelivery){
      _completedOne=true;
      _completed = true;
      return GeoPoint(0.0, 0.0, "");
    } else{
      _completedOne=true;
      return this.delivery.pickUpLocationTwo;
    }
  }

  GeoPoint finishSecond(){
    if(this._isTwoDeliveries){
      _completedTwo=true;
      _completed=true;
      return GeoPoint(0.0, 0.0, "");
    }else{
      _completedTwo=true;
      return this.delivery.pickUpLocationThree;
    }
  }

  GeoPoint finishThird(){
      _completed=true;
      return GeoPoint(0.0, 0.0, "");
  }
}