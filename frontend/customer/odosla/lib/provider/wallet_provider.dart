import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';

class WalletProvider with ChangeNotifier {
  String _name = "";
  String _email = "";

  String _cardEnding = "";
  bool _present = false;

  WalletProvider() {}

  bool get present {
    return _present;
  }

  String get card {
    return _cardEnding;
  }

  set present(bool val) {
    _present = val;
    notifyListeners();
  }

  set card(String val) {
    _cardEnding = val.substring(val.length - 4, val.length);
    debugPrint(val);
    debugPrint(_cardEnding);
    notifyListeners();
  }

  set name(String n) {
    _name = n;
    notifyListeners();
  }

  set email(String n) {
    _email = n;
    notifyListeners();
  }

  String get name => _name;
  String get email => _email;
}
