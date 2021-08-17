import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_employee_app/models/Order.dart';
import 'package:get_it/get_it.dart';
import 'package:http/http.dart' as http;

import 'UserService.dart';
class DeliveryService{

   final String endPoint = "10.0.2.2:8080/";
   //final String endPoint = "c71682b066b8.ngrok.io/";

   final UserService _userService=GetIt.I.get();

   Future<String> getNextOrderForDriver(String storeID, BuildContext context) async {

     final loginURL = Uri.parse("http://"+endPoint+"delivery/getNextOrderForDriver");

     Map<String,String> headers =new Map<String,String>();

     headers =
     {
       "Accept": "application/json",
       "content-type": "application/json",
       "Access-Control-Allow-Origin": "*",
       "Access-Control-Allow-Methods": "POST, OPTIONS"
     };

     String driverID="e693ca68-7575-4a52-9535-a141ba9ca64c";

     final data = {
       "driverID":driverID,
       "rangeOfDelivery":1000,
       "currentLocation":{
         "latitude":-25.748931,
         "longitude": 28.243325,
          "address":"Urban Quarters"
        }
     };

     final response = await http.post(loginURL, headers: headers, body: jsonEncode(data));
     print(response.body);

     if (response.statusCode==200) {
       Map<String,dynamic> responseData = json.decode(response.body);
       if (responseData["message"] == "Driver can take the following delivery.") {
         return responseData["deliveryID"];
         }

         return "";
       }
     return "";
     }



   Future<bool> assignDriverToDelivery(String deliveryID, BuildContext context) async {

     final loginURL = Uri.parse("http://"+endPoint+"delivery/assignDriverToDelivery");

     Map<String,String> headers =new Map<String,String>();

     headers =
     {
       "Accept": "application/json",
       "content-type": "application/json",
       "Access-Control-Allow-Origin": "*",
       "Access-Control-Allow-Methods": "POST, OPTIONS"
     };

     String jwt="";
     await _userService.getJWTAsString().then((value) => {
       jwt=value!
     }

     );
     while (jwt==""){
       await _userService.getJWTAsString().then((value) =>
       jwt=value!,
       );
     }

     print(jwt);

     final data = {
       "deliveryID":deliveryID,
       "jwtToken":jwt,
     };

     final response = await http.post(loginURL, headers: headers, body: jsonEncode(data));
     print(response.body);

     if (response.statusCode==200) {
       Map<String,dynamic> responseData = json.decode(response.body);
       if (responseData["isAssigned"] == true) {
         return true;
       }
     }
     return false;
   }

   Future<bool> UpdateDeliveryStatus(String deliveryID, BuildContext context) async {

     final loginURL = Uri.parse("http://"+endPoint+"delivery/updateDeliveryStatus");

     Map<String,String> headers =new Map<String,String>();

     headers =
     {
       "Accept": "application/json",
       "content-type": "application/json",
       "Access-Control-Allow-Origin": "*",
       "Access-Control-Allow-Methods": "POST, OPTIONS"
     };


     final data = {
       "status":"Delivered",
       "deliveryID":deliveryID,
       "detail":""
     };

     final response = await http.post(loginURL, headers: headers, body: jsonEncode(data));
     print(response.body);

     if (response.statusCode==200) {
       return true;
     }else{
       return false;
     }
   }
}