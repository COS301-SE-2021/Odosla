import 'package:flutter/material.dart';

class UtilityProvider with ChangeNotifier{

  bool _redo=true;

  UtilityProvider();

  bool get redo=>_redo;

  set redo(bool s){_redo=s;notifyListeners();}
}