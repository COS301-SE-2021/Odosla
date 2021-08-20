import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';

class DriverProvider with ChangeNotifier {
  String _id = "";
  String _name = "";
  String _contact = "";
  int _trips = -1;
  double _rating = -1;
  bool _allocated = false;
  double _lat = -25;
  double _long = 28;
  String _deliveryID = "";
  bool _tracking = false;

  DriverProvider() {}

  void initialize(String id, String name, String contact, int trips,
      double rating, double lat, double long, String deliveryID) {
    _id = id;
    _name = name;
    _contact = contact;
    _trips = trips;
    _rating = rating;
    _lat = lat;
    _long = long;
    _deliveryID = deliveryID;
  }

  bool get tracking => _tracking;
  set tracking(bool b) {
    _tracking = b;
    notifyListeners();
  }

  String get deliveryID => _deliveryID;
  String get name => _name;
  String get contact => _contact;
  int get trips => _trips;
  double get rating => _rating;

  set deliveryID(String id) {
    _deliveryID = id;
    notifyListeners();
  }

  String get id {
    return _id;
  }

  set id(String id) {
    _id = id;
    notifyListeners();
  }

  double get lat => _lat;
  set lat(double l) {
    _lat = l;
    notifyListeners();
  }

  double get long => _long;
  set long(double l) {
    _long = l;
    notifyListeners();
  }

  bool get allocated => _allocated;
  set allocated(bool b) {
    _allocated = b;
    notifyListeners();
  }
}
