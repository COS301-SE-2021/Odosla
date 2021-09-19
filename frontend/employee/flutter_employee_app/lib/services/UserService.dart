import 'dart:async';
import 'dart:convert';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_employee_app/models/Delivery.dart';
import 'package:flutter_employee_app/models/Order.dart';
import 'package:flutter_employee_app/models/Shopper.dart';
import 'package:flutter_employee_app/models/User.dart';
import 'package:flutter_employee_app/provider/delivery_provider.dart';
import 'package:flutter_employee_app/provider/jwt_provider.dart';
import 'package:flutter_employee_app/provider/order_provider.dart';
import 'package:flutter_employee_app/provider/shop_provider.dart';
import 'package:flutter_employee_app/provider/user_provider.dart';
import 'package:flutter_employee_app/utilities/my_navigator.dart';
import 'package:flutter_employee_app/utilities/settings.dart';
import 'package:http/http.dart' as http;
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:provider/provider.dart';

class UserService{

  final _storage = new FlutterSecureStorage();

  Future<String?> getRegistrationPassword() async {
    return await _storage.read(key: "passwordRegistration");
  }

  Future<void> setRegistrationPassword(_password) async {
    await Future.wait([
      _storage.write(key: "passwordRegistration", value: _password),
    ]);
  }

  Future<String?> getJWTAsString(BuildContext context) async {
    String jwt = Provider.of<JWTProvider>(context,listen: false).jwt;
    if(jwt == "" || jwt == null) {
      String? jwt= await _storage.read(key: "jwt");
      if(jwt!=null){
        Provider.of<JWTProvider>(context,listen: false).jwt=jwt;
      }
      return jwt;
    }else{
      return jwt;
    }
  }

  Future<void> deleteStorage(BuildContext context) async {
    await _storage.deleteAll();
    Provider.of<JWTProvider>(context,listen: false).jwt="";
    Provider.of<UserProvider>(context,listen: false).user=new User.withParameters("","","","","",false,"0");
  }

  Future<bool> loginUser(String email, String password, String userType, BuildContext context) async{

    final url = Uri.parse(userEndPoint+"user/loginUser");

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

    final response = await http.post(url, headers: headers, body: jsonEncode(data)).timeout(
        Duration(seconds: 40),
        onTimeout:(){ return(http.Response('TimeOut',408));
        });;

    if (response.statusCode==200) {

      Map<String,dynamic> responseData = json.decode(response.body);

      if (responseData["success"] == true) {
        String token=responseData["Token"];
        await Future.wait([
          _storage.write(key: "jwt", value: token),
        ]);

        Provider.of<JWTProvider>(context,listen: false).jwt=responseData["Token"];
        User? user=await this.getCurrentUser(context);
        return true;
      }
      else if(responseData["message"]!=null && responseData["message"].contains("Please verify account")){

        ScaffoldMessenger.of(context)
            .showSnackBar(SnackBar(content: Text("Please verify account before logging in")));
        return false;
      }
    }
    else if (response.statusCode == 403) {
      ScaffoldMessenger.of(context)
          .showSnackBar(
          SnackBar(content: Text("You are unauthorized to use this app")));
    }
    else if (response.statusCode == 500) {
      ScaffoldMessenger.of(context)
          .showSnackBar(SnackBar(content: Text("Internal server error")));
    }
    else if (response.statusCode==408){
      ScaffoldMessenger.of(context)
          .showSnackBar(SnackBar(content: Text("Request timed out")));
    }
    else {
      ScaffoldMessenger.of(context)
          .showSnackBar(SnackBar(content: Text("Error with logging in")));
    }
    return false;
  }

  Future<String> registerDriver(String name,String surname, String email, String password, String phoneNumber) async {

    final url = Uri.parse(userEndPoint+"user/registerDriver");

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

    final response = await http.post(url, headers: headers, body: jsonEncode(data)).timeout(
        Duration(seconds: 15),
        onTimeout:(){ return(http.Response('TimeOut',408));
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
    }else if(response.statusCode==408){
      print(response);
      return "timeout";
    }else{
      return "false";
    }
  }

  Future<String> registerShopper(String name,String surname, String email, String password, String phoneNumber) async {

    final url = Uri.parse(userEndPoint+"user/registerShopper");

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

    final response = await http.post(url, headers: headers, body: jsonEncode(data)).timeout(
        Duration(seconds: 17),
        onTimeout:(){ return(http.Response('TimeOut',408));
        }
    );


    if (response.statusCode==200) {
      Map<String,dynamic> responseData = json.decode(response.body);
      if (responseData["success"] == true) {
        print("successfully registerDriver");
        return "true";
      } else {
        if(responseData["message"] == "Email has already been used"){
          return "Email has already been used";
        }
        return "false";
      }
    }
    else if(response.statusCode==408){
      print(response);
      return "timeout";
    }
    else{
      return "false";
    }
  }

  Future<String> verifyAccount(String email, String activationCode, String userType) async {

    final url = Uri.parse(userEndPoint+"user/verifyAccount");

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
    final response = await http.post(url, headers: headers, body: jsonEncode(data)).timeout(
        Duration(seconds: 17),
        onTimeout:(){ return(http.Response('TimeOut',408));
        }
    );
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

    final url = Uri.parse(userEndPoint+"user/getCurrentUser");

    Map<String,String> headers =new Map<String,String>();

    String jwt="";

    await this.getJWTAsString(context).then((value) => {
      jwt=value!
    });

    final data = {
      "JWTToken":jwt
    };

    headers =
    {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS",
      "Authorization":jwt,
    };

    final response = await http.post(url, headers: headers, body: jsonEncode(data));
    print(response.statusCode);
    if (response.statusCode==200) {
      Map<String,dynamic> responseData = json.decode(response.body);
      print(responseData);

      if (responseData["success"] == true) {
        String userType=responseData["user"]["accountType"];

        if(userType=="SHOPPER"){

          Shopper shopper = Shopper.fromJson(responseData["user"]);

          shopper.setOrdersCompleted(responseData["user"]["ordersCompleted"].toString());
          shopper.setStoreID(responseData["user"]["storeID"].toString());
          shopper.setOnShift(responseData["user"]["onShift"]);
          Provider.of<ShopProvider>(context,listen: false).store.id=(responseData["user"]["storeID"]);

          if(shopper.onShift==null){
            shopper.onShift=false;
          }else{
            Provider.of<UserProvider>(context,listen: false).user.onShift=shopper.onShift;
          }
          return shopper;

        } else if (userType=="DRIVER"){

          Shopper shopper = Shopper.fromJson(responseData["user"]);
          Provider.of<UserProvider>(context,listen: false).user=shopper;
          shopper.deliveriesCompleted=responseData["user"]["deliveriesCompleted"].toString();
          Provider.of<UserProvider>(context,listen: false).user.deliveriesCompleted=(shopper.deliveriesCompleted).toString();
          shopper.setOnShift(responseData["user"]["onShift"]);
          shopper.setRating(responseData["user"]["rating"].toString());
          Provider.of<UserProvider>(context,listen: false).user.onShift=shopper.onShift;

          return shopper;
        } else if (userType=="ADMIN"){
          Shopper shopper = Shopper.fromJson(responseData["user"]);
          Provider.of<UserProvider>(context,listen: false).user=shopper;
        }
      }else if (responseData["message"].contains("JWT expired at")){
        MyNavigator.goToLogin(context);
      }
    }
    else if (response.statusCode==403){
      ScaffoldMessenger.of(context)
          .showSnackBar(SnackBar(content: Text("You are unauthorized to use this app")));
      return User("","","","","");
    }else{
      return User("","","","","");
    }
  }

  Future<bool> setShopperShift(bool onShift,String storeID, BuildContext context) async {

    final url = Uri.parse(userEndPoint+"user/updateShopperShift");

    Map<String,String> headers =new Map<String,String>();

    String jwt="";

    await this.getJWTAsString(context).then((value) => {
      jwt=value!
    });

    print(jwt);

    headers =
    {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS",
      "Authorization":jwt,
    };

    final data = {
      "onShift":onShift,
      "storeID":storeID
    };

    print(data);

    final response = await http.post(url, headers: headers, body: jsonEncode(data)).timeout(
        Duration(seconds: 40),
        onTimeout:(){ return(http.Response('TimeOut',408));
        });

    if (response.statusCode==200) {
      Map<String,dynamic> responseData = json.decode(response.body);
      print(responseData);
      if (responseData["success"] == true) {
        Provider.of<UserProvider>(context,listen: false).user.onShift=(onShift);
        return true;
      }
      else if (response.statusCode == 403) {
        ScaffoldMessenger.of(context)
            .showSnackBar(
            SnackBar(content: Text("You are unauthorized to use this app")));
      }
      else if (response.statusCode == 500) {
        ScaffoldMessenger.of(context)
            .showSnackBar(SnackBar(content: Text("Internal server error")));
      }
      else if (response.statusCode==408){
        ScaffoldMessenger.of(context)
            .showSnackBar(SnackBar(content: Text("Request timed out")));
      }
    }
    return false;
  }

  Future<bool> setDriverShift(bool onShift, BuildContext context) async {

    final url = Uri.parse(userEndPoint+"user/updateDriverShift");

    Map<String,String> headers =new Map<String,String>();

    String jwt="";

    await this.getJWTAsString(context).then((value) => {
      jwt=value!
    });

    print(jwt);

    headers =
    {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS",
      "Authorization":jwt,
    };


    final data = {
      "onShift":onShift,
    };

    final response = await http.post(url, headers: headers, body: jsonEncode(data));

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

    final url = Uri.parse(userEndPoint+"user/completePackagingOrder");

    Map<String,String> headers =new Map<String,String>();

    String jwt="";

    await this.getJWTAsString(context).then((value) => {
      jwt=value!
    });

    print(jwt);

    headers =
    {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS",
      "Authorization":jwt,
    };
    print(orderID);
    final data = {
      "orderID":orderID,
      "getNext":false,
    };

    final response = await http.post(url, headers: headers, body: jsonEncode(data));

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

  Future<bool> updateDriverLocation(double latitude, double longitude, BuildContext context) async {

    final url = Uri.parse(userEndPoint+"user/setCurrentLocation");

    Map<String,String> headers =new Map<String,String>();
    String jwt="";
    await this.getJWTAsString(context).then((value) => {
      jwt=value!
    });

    print(jwt);

    headers =
    {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS",
      "Authorization":jwt,
    };
    Delivery _delivery=Provider.of<DeliveryProvider>(context,listen: false).delivery;
    final data = {
      "driverID":_delivery.driverID,
      "latitude":latitude,
      "longitude":longitude,
      "address":"Driver current location"
    };

    final response = await http.post(url, headers: headers, body: jsonEncode(data));

    if (response.statusCode==200) {
      Map<String,dynamic> responseData = json.decode(response.body);
      print(responseData);
      if (responseData["success"] == true) {
        return true;
      }
    }
    return false;
  }

  Future<bool> updateShopperDetails(String name, String surname, String email, String phoneNumber, String currentPassword, String newPassword, BuildContext context) async{

    final url = Uri.parse(userEndPoint+"user/updateShopperDetails");

    Map<String,String> headers =new Map<String,String>();

    String jwt=Provider.of<JWTProvider>(context,listen: false).jwt;
    while (jwt==""){
      await getJWTAsString(context).then((value) =>
      jwt=value!,
      );
    }
    print(jwt);

    headers =
    {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS",
      "Authorization":jwt
    };
    var data;
    if(newPassword=="noChange"){
      data = {
        "name":name,
        "surname":surname,
        "email":email,
        "phoneNumber":phoneNumber,
        "password":null,
        "currentPassword":null
      };
    }else if(name=="noChange"){
      data = {
        "name":null,
        "surname":null,
        "email":null,
        "phoneNumber":null,
        "password":newPassword,
        "currentPassword":currentPassword
      };
    }else{
      data = {
        "name":null,
        "surname":null,
        "email":null,
        "phoneNumber":null,
        "password":null,
        "currentPassword":null,
      };
    }


    print(data);


    final response = await http.post(url, headers: headers, body: jsonEncode(data));
    print(response.body);
    if (response.statusCode==200) {
      Map<String,dynamic> responseData = json.decode(response.body);

      if (responseData["success"] == true) {
        String token=responseData["jwtToken"];
        if(token!=null) {
          await Future.wait([
            _storage.write(key: "jwt", value: token),
          ]);
          Provider
              .of<JWTProvider>(context, listen: false)
              .jwt = responseData["jwtToken"];
        }
        return true;

      } else if(responseData["message"]!=null && responseData["message"].contains("Email is already taken")){
        ScaffoldMessenger.of(context)
            .showSnackBar(SnackBar(content: Text("Email already taken")));
        return false;
      }else if(responseData["message"]!=null && responseData["message"].contains("Incorrect password")){
        ScaffoldMessenger.of(context)
            .showSnackBar(SnackBar(content: Text("Incorrect password")));
        return false;
      } else if(responseData["message"]!=null && responseData["message"].contains("Null values submitted - Nothing updated")){
        ScaffoldMessenger.of(context)
            .showSnackBar(SnackBar(content: Text("No new changes")));
        return false;
      }
    }
    ScaffoldMessenger.of(context)
        .showSnackBar(SnackBar(content: Text("Error with logging in")));
    return false;
  }

  Future<bool> updateDriverDetails(String name, String surname, String email, String phoneNumber, String currentPassword, String newPassword, BuildContext context) async{

    final url = Uri.parse(userEndPoint+"user/updateDriverDetails");

    Map<String,String> headers =new Map<String,String>();

    String jwt=Provider.of<JWTProvider>(context,listen: false).jwt;
    while (jwt==""){
      await getJWTAsString(context).then((value) =>
      jwt=value!,
      );
    }
    print(jwt);

    headers =
    {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS",
      "Authorization":jwt
    };
    var data;
    if(newPassword=="noChange"){
      data = {
        "name":name,
        "surname":surname,
        "email":email,
        "phoneNumber":phoneNumber,
        "password":null,
        "currentPassword":null
      };
    }else if(name=="noChange"){
      data = {
        "name":null,
        "surname":null,
        "email":null,
        "phoneNumber":null,
        "password":newPassword,
        "currentPassword":currentPassword
      };
    }else{
      data = {
        "name":null,
        "surname":null,
        "email":null,
        "phoneNumber":null,
        "password":null,
        "currentPassword":null,
      };
    }


    print(data);


    final response = await http.post(url, headers: headers, body: jsonEncode(data));
    print(response.body);
    if (response.statusCode==200) {
      Map<String,dynamic> responseData = json.decode(response.body);

      if (responseData["success"] == true) {
        String token=responseData["jwtToken"];
        if(token!=null) {
          await Future.wait([
            _storage.write(key: "jwt", value: token),
          ]);
          Provider
              .of<JWTProvider>(context, listen: false)
              .jwt = responseData["jwtToken"];
        }
        return true;

      } else if(responseData["message"]!=null && responseData["message"].contains("Email is already taken")){
        ScaffoldMessenger.of(context)
            .showSnackBar(SnackBar(content: Text("Email already taken")));
        return false;
      }else if(responseData["message"]!=null && responseData["message"].contains("Incorrect password")){
        ScaffoldMessenger.of(context)
            .showSnackBar(SnackBar(content: Text("Incorrect password")));
        return false;
      } else if(responseData["message"]!=null && responseData["message"].contains("Null values submitted - Nothing updated")){
        ScaffoldMessenger.of(context)
            .showSnackBar(SnackBar(content: Text("No new changes")));
        return false;
      }
    }
    ScaffoldMessenger.of(context)
        .showSnackBar(SnackBar(content: Text("Error with logging in")));
    return false;
  }

  Future<bool> updateAdminDetails(String name, String surname, String email, String phoneNumber, String currentPassword, String newPassword, BuildContext context) async{

    final url = Uri.parse(userEndPoint+"user/updateAdminDetails");

    Map<String,String> headers =new Map<String,String>();

    String jwt=Provider.of<JWTProvider>(context,listen: false).jwt;
    while (jwt==""){
      await getJWTAsString(context).then((value) =>
      jwt=value!,
      );
    }
    print(jwt);

    headers =
    {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS",
      "Authorization":jwt
    };
    var data;
    if(newPassword=="noChange"){
      data = {
        "name":name,
        "surname":surname,
        "email":email,
        "phoneNumber":phoneNumber,
        "password":null,
        "currentPassword":null
      };
    }else if(name=="noChange"){
      data = {
        "name":null,
        "surname":null,
        "email":null,
        "phoneNumber":null,
        "password":newPassword,
        "currentPassword":currentPassword
      };
    }else{
      data = {
        "name":null,
        "surname":null,
        "email":null,
        "phoneNumber":null,
        "password":null,
        "currentPassword":null,
      };
    }


    print(data);


    final response = await http.post(url, headers: headers, body: jsonEncode(data));
    print(response.body);
    if (response.statusCode==200) {
      Map<String,dynamic> responseData = json.decode(response.body);

      if (responseData["success"] == true) {
        String token=responseData["jwtToken"];
        if(token!=null) {
          await Future.wait([
            _storage.write(key: "jwt", value: token),
          ]);
          Provider
              .of<JWTProvider>(context, listen: false)
              .jwt = responseData["jwtToken"];
        }
        return true;

      } else if(responseData["message"]!=null && responseData["message"].contains("Email is already taken")){
        ScaffoldMessenger.of(context)
            .showSnackBar(SnackBar(content: Text("Email already taken")));
        return false;
      }else if(responseData["message"]!=null && responseData["message"].contains("Incorrect password")){
        ScaffoldMessenger.of(context)
            .showSnackBar(SnackBar(content: Text("Incorrect password")));
        return false;
      } else if(responseData["message"]!=null && responseData["message"].contains("Null values submitted - Nothing updated")){
        ScaffoldMessenger.of(context)
            .showSnackBar(SnackBar(content: Text("No new changes")));
        return false;
      }
    }
    ScaffoldMessenger.of(context)
        .showSnackBar(SnackBar(content: Text("Error with logging in")));
    return false;
  }
}