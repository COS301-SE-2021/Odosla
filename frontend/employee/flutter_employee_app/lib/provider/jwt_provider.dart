import 'package:flutter/material.dart';

class JWTProvider with ChangeNotifier{

  String _jwt="";

  JWTProvider();

 String get jwt=>_jwt;

  set jwt(String s){_jwt=s;notifyListeners();}
}