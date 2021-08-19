import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_employee_app/models/Delivery.dart';
import 'package:flutter_employee_app/models/Order.dart';
import 'package:flutter_employee_app/provider/delivery_provider.dart';
import 'package:get_it/get_it.dart';
import 'package:http/http.dart' as http;
import 'package:provider/provider.dart';

import 'UserService.dart';
class DeliveryService{

   final String endPoint = "75c59b94a2f1.ngrok.io/";

   final UserService _userService=GetIt.I.get();

   Future<String> getNextOrderForDriver(BuildContext context) async {

     final loginURL = Uri.parse("http://"+endPoint+"delivery/getNextOrderForDriver");

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
       "jwtToken":jwt,
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
       print("RESPONSE DATA");
       print(responseData);
       if (responseData["message"] == "Driver can take the following delivery.") {
         Delivery delivery=Delivery.fromJson(responseData["delivery"]);
         print("FROM JSON");
         print(delivery.deliveryID);
         print(delivery.customerID);
         Provider.of<DeliveryProvider>(context,listen: false).delivery=delivery;
         print("Provider");
         print(Provider.of<DeliveryProvider>(context,listen: false).delivery.deliveryID);
         print("??????????????");
         print(Provider.of<DeliveryProvider>(context,listen: false).delivery.customerID);
         return responseData["delivery"]["deliveryID"];
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
         print("driver id get returned");
         print(responseData["driverID"]);
         Provider.of<DeliveryProvider>(context,listen: false).delivery.driverID=responseData["driverID"];
         return true;
       }
     }
     return false;
   }

   Future<bool> UpdateDeliveryStatus(String deliveryID, String status,BuildContext context) async {

     final loginURL = Uri.parse("http://"+endPoint+"delivery/updateDeliveryStatus");

     Map<String,String> headers =new Map<String,String>();

     headers =
     {
       "Accept": "application/json",
       "content-type": "application/json",
       "Access-Control-Allow-Origin": "*",
       "Access-Control-Allow-Methods": "POST, OPTIONS"
     };

     deliveryID=Provider
         .of<DeliveryProvider>(context, listen: false)
         .delivery.deliveryID;
     final data = {
       "status":status,
       "deliveryID":deliveryID,
       "detail":"Driver is on way to collect delivery from store"
     };

     final response = await http.post(loginURL, headers: headers, body: jsonEncode(data));

     if (response.statusCode==200) {
       Map<String,dynamic> responseData = json.decode(response.body);
       print(responseData["message"]);
       if(responseData["message"]=="Successful status update.") {
         Provider
             .of<DeliveryProvider>(context, listen: false)
             .delivery
             .deliveryStatus = status;
         return true;
       }
       else return false;
     }else{
       return false;
     }
   }
}