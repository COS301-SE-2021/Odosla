import 'dart:async';
import 'dart:convert';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_employee_app/models/Order.dart';
import 'package:flutter_employee_app/models/Shopper.dart';
import 'package:flutter_employee_app/models/User.dart';
import 'package:flutter_employee_app/provider/order_provider.dart';
import 'package:flutter_employee_app/provider/shop_provider.dart';
import 'package:flutter_employee_app/provider/user_provider.dart';
import 'package:flutter_employee_app/utilities/my_navigator.dart';
import 'package:http/http.dart' as http;
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:flutter/foundation.dart';
import 'package:provider/provider.dart';
class UserService{

  final _storage = new FlutterSecureStorage();
  //final String endPoint = "c71682b066b8.ngrok.io/";
  final String endPoint = "10.0.2.2:8080/";

  Future<bool> loginUser(String email, String password, String userType, BuildContext context) async{
    final loginURL = Uri.parse("http://"+endPoint+"user/loginUser");

    Map<String,String> headers =new Map<String,String>();

    headers =
    {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS"
    };


    final data = {
      "email": email,
      "password": password,
      "userType": userType,
    };

    print(data);


    final response = await http.post(loginURL, headers: headers, body: jsonEncode(data));
    print(response.body);
    if (response.statusCode==200) {
      Map<String,dynamic> responseData = json.decode(response.body);

      if (responseData["success"] == true) {
        String token=responseData["Token"];
        await Future.wait([
          _storage.write(key: "jwt", value: token),
        ]);

        return true;

      } else if(responseData["message"]!=null && responseData["message"].contains("Please verify account")){
        ScaffoldMessenger.of(context)
            .showSnackBar(SnackBar(content: Text("Please verify account before logging in")));
        return false;
      }
    }
    ScaffoldMessenger.of(context)
        .showSnackBar(SnackBar(content: Text("Error with logging in")));
    return false;
  }

  Future<String?> getJWTAsString() async {
    return await _storage.read(key: "jwt");
  }

  Future<String?> getRegistrationPassword() async {
    return await _storage.read(key: "passwordRegistration");
  }

  Future<void> setRegistrationPassword(_password) async {
    await Future.wait([
      _storage.write(key: "passwordRegistration", value: _password),
    ]);
  }

  Future<String> registerDriver(String name,String surname, String email, String password, String phoneNumber) async {

    final loginURL = Uri.parse("http://"+endPoint+"user/registerDriver");

    Map<String,String> headers =new Map<String,String>();

    headers =
    {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS"
    };

    final data = {
      "email": email,
      "password": password,
      "name": name,
      "surname": surname,
      "phoneNumber": phoneNumber,
    };

    final response = await http.post(loginURL, headers: headers, body: jsonEncode(data)).timeout(
        Duration(seconds: 15),
        onTimeout:(){ return(http.Response('TimeOut',500));
        }
    );

    if (response.statusCode==200) {
      Map<String,dynamic> responseData = json.decode(response.body);
      if (responseData["success"] == true) {
        print("successfully registerDriver");
        // await Future.wait([
        //   _storage.write(key: "jwt", value: token),
        // ]);
        return "true";
      } else {
        if(responseData["message"] == "Email has already been used"){
          return "Email has already been used";
        }
        return "false";
      }
    }else if(response.statusCode==500){
      print(response);
      return "timeout";
    }else{
      return "false";
    }
  }

  Future<String> registerShopper(String name,String surname, String email, String password, String phoneNumber) async {

    final loginURL = Uri.parse("http://"+endPoint+"user/registerShopper");

    Map<String,String> headers =new Map<String,String>();

    headers =
    {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS"
    };

    final data = {
      "email": email,
      "password": password,
      "name": name,
      "surname": surname,
      "phoneNumber": phoneNumber,
    };

    final response = await http.post(loginURL, headers: headers, body: jsonEncode(data)).timeout(
        Duration(seconds: 15),
        onTimeout:(){ return(http.Response('TimeOut',500));
        }
    );


    if (response.statusCode==200) {
      Map<String,dynamic> responseData = json.decode(response.body);
      if (responseData["success"] == true) {
        print("successfully registerDriver");
        // await Future.wait([
        //   _storage.write(key: "jwt", value: token),
        // ]);
        return "true";
      } else {
        if(responseData["message"] == "Email has already been used"){
          return "Email has already been used";
        }
        return "false";
      }
    }
    else if(response.statusCode==500){
      print(response);
      return "timeout";
    }
    else{
      return "false";
    }
  }

  Future<String> verifyAccount(String email, String activationCode, String userType) async {

    final loginURL = Uri.parse("http://"+endPoint+"user/verifyAccount");

    Map<String,String> headers =new Map<String,String>();

    headers =
    {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS"
    };

    final data = {
      "email": email,
      "activationCode":activationCode,
      "userType":userType
    };

    final response = await http.post(loginURL, headers: headers, body: jsonEncode(data));

    print(response.body);
    if (response.statusCode==200) {
      Map<String,dynamic> responseData = json.decode(response.body);
      if (responseData["success"] == true) {
        print("successfully verified Account");
        return "true";
      } else {
        if(responseData["message"] == "Shopper with email '"+email+"' has already activated their Shopper account"){
          return "Activated";
        }else if(responseData["message"] == "Driver with email '"+email+"' has already activated their Driver account") {
          return "Activated";
        }
        return "false";
      }
    }else{
      return "false";
    }
  }

  Future<User?> getCurrentUser(BuildContext context) async {

    final loginURL = Uri.parse("http://"+endPoint+"user/getCurrentUser");

    Map<String,String> headers =new Map<String,String>();

    headers =
    {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS"
    };
    String jwt="";
    await this.getJWTAsString().then((value) => {
      jwt=value!
    }

    );
    while (jwt==""){
      await getJWTAsString().then((value) =>
      jwt=value!,
      );
    }

    print(jwt);
    final data = {
      "JWTToken":jwt
    };

    final response = await http.post(loginURL, headers: headers, body: jsonEncode(data));

    if (response.statusCode==200) {
      Map<String,dynamic> responseData = json.decode(response.body);
      print(responseData);
      if (responseData["success"] == true) {

        String userType=responseData["user"]["accountType"];
        print(userType);
        if(userType=="SHOPPER"){
          Shopper shopper = Shopper.fromJson(responseData["user"]);
          Provider.of<UserProvider>(context,listen: false).user=shopper;
          shopper.setOrdersCompleted(responseData["user"]["ordersCompleted"].toString());
          Provider.of<UserProvider>(context,listen: false).user.ordersCompleted=(responseData["user"]["ordersCompleted"]).toString();
          shopper.setOnShift(responseData["user"]["onShift"]);

          if(shopper.onShift==null){
            shopper.onShift=false;
          }else{
            Provider.of<UserProvider>(context,listen: false).user.onShift=shopper.onShift;
          }
          return shopper;
        } else if (userType=="DRIVER"){
          Shopper shopper = Shopper.fromJson(responseData["user"]);
          Provider.of<UserProvider>(context,listen: false).user=shopper;
          //shopper.setOrdersCompleted(responseData["user"]["ordersCompleted"].toString());
          //Provider.of<UserProvider>(context,listen: false).user.ordersCompleted=(responseData["user"]["ordersCompleted"]).toString();
          shopper.setOnShift(responseData["user"]["onShift"]);
          shopper.setRating(responseData["user"]["rating"].toString());
          if(shopper.onShift==null){
            shopper.onShift=false;
          }else{
            Provider.of<UserProvider>(context,listen: false).user.onShift=shopper.onShift;
          }
          return shopper;
        }
      }else if (responseData["message"].contains("JWT expired at")){
        MyNavigator.goToLogin(context);
      }
    }else{
      return null;
    }
  }

  Future<bool> setShopperShift(bool onShift,String storeID, BuildContext context) async {

    final loginURL = Uri.parse("http://"+endPoint+"user/updateShopperShift");

    Map<String,String> headers =new Map<String,String>();

    headers =
    {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS"
    };
    String jwt="";
    await this.getJWTAsString().then((value) => {
      jwt=value!
    }

    );
    while (jwt==""){
      await getJWTAsString().then((value) =>
      jwt=value!,
      );
    }

    print(jwt);
    print(storeID);
    final data = {
      "jwtToken":jwt,
      "onShift":onShift,
      "storeID":storeID
    };

    final response = await http.post(loginURL, headers: headers, body: jsonEncode(data));

    if (response.statusCode==200) {
      Map<String,dynamic> responseData = json.decode(response.body);
      print(responseData);
      if (responseData["success"] == true) {
        Provider.of<UserProvider>(context,listen: false).user.onShift=(onShift);

        return true;
      }
    }
    return false;
  }

  Future<bool> setDriverShift(bool onShift, BuildContext context) async {

    final loginURL = Uri.parse("http://"+endPoint+"user/updateDriverShift");

    Map<String,String> headers =new Map<String,String>();

    headers =
    {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS"
    };
    String jwt="";
    await this.getJWTAsString().then((value) => {
      jwt=value!
    }

    );
    while (jwt==""){
      await getJWTAsString().then((value) =>
      jwt=value!,
      );
    }

    final data = {
      "jwtToken":jwt,
      "onShift":onShift,
    };

    final response = await http.post(loginURL, headers: headers, body: jsonEncode(data));

    if (response.statusCode==200) {
      Map<String,dynamic> responseData = json.decode(response.body);
      print(responseData);
      if (responseData["success"] == true) {
        Provider.of<UserProvider>(context,listen: false).user.onShift=(onShift);

        return true;
      }
    }
    return false;
  }

  Future<bool> completePackagingOrder(String orderID, BuildContext context) async {

    final loginURL = Uri.parse("http://"+endPoint+"user/completePackagingOrder");

    Map<String,String> headers =new Map<String,String>();

    headers =
    {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS"
    };
    print(orderID);
    final data = {
      "orderID":orderID,
      "getNext":false,
    };

    final response = await http.post(loginURL, headers: headers, body: jsonEncode(data));

    if (response.statusCode==200) {
      Map<String,dynamic> responseData = json.decode(response.body);
      print(responseData);
      if (responseData["success"] == true) {
        Provider.of<OrderProvider>(context,listen: false).order=Order("","","","",0.0,List.empty(),"");

        return true;
      }
    }
    return false;
  }


}