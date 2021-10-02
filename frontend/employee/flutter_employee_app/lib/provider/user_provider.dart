import 'package:flutter/cupertino.dart';
import 'package:flutter_employee_app/models/User.dart';

class UserProvider with ChangeNotifier {
  User _user = new User.withParameters("", "", "", "", "", false, "0");
  DateTime timestamp = DateTime.now();

  UserProvider();

  User get user => _user;

  set user(User u) {
    _user = u;
    timestamp = DateTime.now();
    notifyListeners();
  }

  bool isUser() {
    DateTime currentTime = DateTime.now();
    var duration = const Duration(minutes: 10);
    currentTime = currentTime.subtract(duration);
    if (_user.email == null || _user.email == "") {
      return false;
    } else if (timestamp.isBefore(currentTime)) {
      _user = User.withParameters("", "", "", "", "", false, "0");
      return false;
    } else {
      return true;
    }
  }
}
