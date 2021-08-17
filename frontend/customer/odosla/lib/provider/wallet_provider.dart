import 'package:flutter/cupertino.dart';
import 'package:flutter/foundation.dart';
import 'package:odosla/services/api_service.dart';
import 'dart:async';

class WalletProvider with ChangeNotifier {
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
}
