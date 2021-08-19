import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';

class DriverProvider with ChangeNotifier {
  String _id = "";
  String _name = "";
  String _contact = "";
  int _trips = -1;
  double _rating = -1;
  bool _allocated = false;

  DriverProvider() {}

  String get id => _id;
  set id(String id) => id;

  bool get allocated => _allocated;
  set allocated(bool b) {
    _allocated = b;
    notifyListeners();
  }
}
