import 'dart:async';
import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:http/http.dart' as http;
import 'package:odosla/provider/status_provider.dart';
import 'package:odosla/services/settings.dart';
import 'package:provider/provider.dart';

class UserService {
  final _storage = new FlutterSecureStorage();

  Future<bool> loginUser(String email, String password, String userType,
      BuildContext context) async {
    final loginURL = Uri.parse(userEndpoint + "user/loginUser");

    Map<String, String> headers = new Map<String, String>();

    headers = {
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

    final response =
        await http.post(loginURL, headers: headers, body: jsonEncode(data)).timeout(Duration(seconds: 15), onTimeout: () {
          return (http.Response('TimeOut', 500));});

    print(response.body);
    if (response.statusCode == 200) {
      Map<String, dynamic> responseData = json.decode(response.body);

      if (responseData["success"] == true) {
        String token = responseData["Token"];
        await Future.wait([
          _storage.write(key: "jwt", value: token),
        ]);
        Provider.of<StatusProvider>(context, listen: false).jwt = token;

        return true;
      } else if (responseData["message"] != null &&
          responseData["message"].contains("Please verify account")) {
        ScaffoldMessenger.of(context).showSnackBar(
            SnackBar(content: Text("Please verify account before logging in")));
        return false;
      }
    }else if(response.statusCode==500){
      ScaffoldMessenger.of(context).showSnackBar(
          SnackBar(content: Text("Request timed out")));
      return false;
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

  Future<String> register(String name, String surname, String email,
      String password, String phoneNumber) async {
    final loginURL = Uri.parse(userEndpoint + "user/registerCustomer");

    Map<String, String> headers = new Map<String, String>();

    headers = {
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

    final response = await http
        .post(loginURL, headers: headers, body: jsonEncode(data))
        .timeout(Duration(seconds: 15), onTimeout: () {
      return (http.Response('TimeOut', 500));
    });

    if (response.statusCode == 200) {
      Map<String, dynamic> responseData = json.decode(response.body);
      if (responseData["success"] == true) {
        // await Future.wait([
        //   _storage.write(key: "jwt", value: token),
        // ]);
        return "true";
      } else {
        if (responseData["message"] == "Email has already been used") {
          return "Email has already been used";
        }
        return "false";
      }
    } else if (response.statusCode == 500) {
      return "timeout";
    } else {
      return "false";
    }
  }

  Future<String> verifyAccount(
      String email, String activationCode, String userType) async {
    final loginURL = Uri.parse(userEndpoint + "user/verifyAccount");

    Map<String, String> headers = new Map<String, String>();

    headers = {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS"
    };

    final data = {
      "email": email,
      "activationCode": activationCode,
      "userType": userType
    };

    final response =
        await http.post(loginURL, headers: headers, body: jsonEncode(data));

    print(response.body);
    if (response.statusCode == 200) {
      Map<String, dynamic> responseData = json.decode(response.body);
      if (responseData["success"] == true) {
        print("successfully verified Account");
        return "true";
      } else {
        if (responseData["message"] ==
            "Shopper with email '" +
                email +
                "' has already activated their Shopper account") {
          return "Activated";
        } else if (responseData["message"] ==
            "Driver with email '" +
                email +
                "' has already activated their Driver account") {
          return "Activated";
        }
        return "false";
      }
    } else {
      return "false";
    }
  }

  // Future<bool> updateCustomerDetails(String name, String surname, String email, String phoneNumber, String currentPassword, String newPassword, BuildContext context) async{
  //
  //   final url = Uri.parse(userEndpoint+"user/updateCustomerDetails");
  //
  //   Map<String,String> headers =new Map<String,String>();
  //
  //   String jwt=Provider.of<JWTProvider>(context,listen: false).jwt;
  //   while (jwt==""){
  //     await getJWTAsString(context).then((value) =>
  //     jwt=value!,
  //     );
  //   }
  //   print(jwt);
  //
  //   headers =
  //   {
  //     "Accept": "application/json",
  //     "content-type": "application/json",
  //     "Access-Control-Allow-Origin": "*",
  //     "Access-Control-Allow-Methods": "POST, OPTIONS",
  //     "Authorization":jwt
  //   };
  //   var data;
  //   if(newPassword=="noChange"){
  //     data = {
  //       "name":name,
  //       "surname":surname,
  //       "email":email,
  //       "phoneNumber":phoneNumber,
  //       "password":null,
  //       "currentPassword":null
  //     };
  //   }else if(name=="noChange"){
  //     data = {
  //       "name":null,
  //       "surname":null,
  //       "email":null,
  //       "phoneNumber":null,
  //       "password":newPassword,
  //       "currentPassword":currentPassword
  //     };
  //   }else{
  //     data = {
  //       "name":null,
  //       "surname":null,
  //       "email":null,
  //       "phoneNumber":null,
  //       "password":null,
  //       "currentPassword":null,
  //     };
  //   }
  //
  //
  //   print(data);
  //
  //
  //   final response = await http.post(url, headers: headers, body: jsonEncode(data));
  //   print(response.body);
  //   if (response.statusCode==200) {
  //     Map<String,dynamic> responseData = json.decode(response.body);
  //
  //     if (responseData["success"] == true) {
  //       String token=responseData["jwtToken"];
  //       if(token!=null) {
  //         await Future.wait([
  //           _storage.write(key: "jwt", value: token),
  //         ]);
  //         Provider
  //             .of<JWTProvider>(context, listen: false)
  //             .jwt = responseData["jwtToken"];
  //       }
  //       return true;
  //
  //     } else if(responseData["message"]!=null && responseData["message"].contains("Email is already taken")){
  //       ScaffoldMessenger.of(context)
  //           .showSnackBar(SnackBar(content: Text("Email already taken")));
  //       return false;
  //     }else if(responseData["message"]!=null && responseData["message"].contains("Incorrect password")){
  //       ScaffoldMessenger.of(context)
  //           .showSnackBar(SnackBar(content: Text("Incorrect password")));
  //       return false;
  //     } else if(responseData["message"]!=null && responseData["message"].contains("Null values submitted - Nothing updated")){
  //       ScaffoldMessenger.of(context)
  //           .showSnackBar(SnackBar(content: Text("No new changes")));
  //       return false;
  //     }
  //   }
  //   ScaffoldMessenger.of(context)
  //       .showSnackBar(SnackBar(content: Text("Error with logging in")));
  //   return false;
  // }
}
