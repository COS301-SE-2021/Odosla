
import 'package:flutter/cupertino.dart';
import 'package:flutter_employee_app/models/User.dart';

class UserProvider with ChangeNotifier{

  User _user=new User.withParameters("","","","","",false,"0");

  UserProvider();

  User get user=>_user;

  set user(User u){_user;notifyListeners();}
}