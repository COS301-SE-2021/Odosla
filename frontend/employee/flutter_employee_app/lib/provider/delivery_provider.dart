import 'package:flutter/cupertino.dart';
import 'package:flutter_employee_app/models/Customer.dart';
import 'package:flutter_employee_app/models/Delivery.dart';
import 'package:flutter_employee_app/models/GeoPoint.dart';

class DeliveryProvider with ChangeNotifier {
  bool _isOneDelivery = false;
  bool _isTwoDeliveries = false;
  bool _isThreeDeliveries = false;

  bool _completedOne = false;
  bool _completedTwo = false;
  bool _completedThree = false;
  bool _completed = false;

  Delivery _delivery = new Delivery(
      "",
      new GeoPoint(0.0, 0.0, ""),
      new GeoPoint(0.0, 0.0, ""),
      "",
      "",
      "",
      "",
      "",
      0.0,
      false,
      new GeoPoint(0.0, 0.0, ""),
      new GeoPoint(0.0, 0.0, ""));

  Customer _customer = new Customer("", "", "", "", "");

  DeliveryProvider();

  Customer get customer => _customer;

  set customer(Customer s) {
    _customer = s;
    notifyListeners();
  }

  Delivery get delivery => _delivery;

  set delivery(Delivery s) {
    _delivery = s;
    notifyListeners();
  }

  bool get isOneDelivery => _isOneDelivery;

  set isOneDelivery(bool s) {
    _isOneDelivery = s;
    notifyListeners();
  }

  bool get isTwoDeliveries => _isTwoDeliveries;

  set isTwoDeliveries(bool s) {
    _isTwoDeliveries = s;
    notifyListeners();
  }

  bool get isThreeDeliveries => _isThreeDeliveries;

  set isThreeDeliveries(bool s) {
    _isThreeDeliveries = s;
    notifyListeners();
  }

  bool get completed => _completed;

  set completed(bool s) {
    _completed = s;
    notifyListeners();
  }

  bool get completedOne => _completedOne;

  set completedOne(bool s) {
    _completedOne = s;
    notifyListeners();
  }

  bool get completedTwo => _completedTwo;

  set completedTwo(bool s) {
    _completedTwo = s;
    notifyListeners();
  }

  bool collected() {
    if (_completedOne == false) {
      return this.finishFirst();
    } else if (_completedTwo == false) {
      return this.finishSecond();
    } else if (_completedThree == false) {
      return this.finishThird();
    }
    return false;
  }

  void reset(){
     _isOneDelivery = false;
     _isTwoDeliveries = false;
     _isThreeDeliveries = false;

     _completedOne = false;
     _completedTwo = false;
     _completedThree = false;
     _completed = false;

     _delivery = new Delivery(
         "",
         new GeoPoint(0.0, 0.0, ""),
         new GeoPoint(0.0, 0.0, ""),
         "",
         "",
         "",
         "",
         "",
         0.0,
         false,
         new GeoPoint(0.0, 0.0, ""),
         new GeoPoint(0.0, 0.0, ""));

     _customer = new Customer("", "", "", "", "");
  }

  void printer(){
    print("IN DELIVERY PROVIDER");
    print("COMPLETED ONE: "+completedOne.toString());
    print("COMPLETED TWO: "+completedTwo.toString());
    print("COMPLETED THREE: "+_completedThree.toString());
    print("/nIS ONE DELIVERY"+_isOneDelivery.toString());
    print("IS TWO DELIVERY"+_isTwoDeliveries.toString());
    print("IS THREE DELIVERY"+_isThreeDeliveries.toString());
    print("Delivery location one address and lat and long: "+_delivery.pickUpLocationOne.address+" "+_delivery.pickUpLocationOne.latitude.toString()+" "+_delivery.pickUpLocationOne.longitude.toString());
    print("Delivery location second address and lat and long: "+_delivery.pickUpLocationTwo.address+" "+_delivery.pickUpLocationTwo.latitude.toString()+" "+_delivery.pickUpLocationTwo.longitude.toString());
    print("Delivery location three address and lat and long: "+_delivery.pickUpLocationThree.address+" "+_delivery.pickUpLocationThree.latitude.toString()+" "+_delivery.pickUpLocationThree.longitude.toString());
  }
  bool finishFirst() {
    if (this._isOneDelivery) {
      _completedOne = true;
      _completed = true;
      return true;
    } else {
      _completedOne = true;
      return false;
    }
  }

  bool finishSecond() {
    if (this._isTwoDeliveries) {
      _completedTwo = true;
      _completed = true;
      return true;
    } else {
      _completedTwo = true;
      return false;
    }
  }

  bool finishThird() {
    _completed = true;
    return true;
  }
}
