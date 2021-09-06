import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_employee_app/models/Customer.dart';
import 'package:flutter_employee_app/models/Delivery.dart';
import 'package:flutter_employee_app/provider/delivery_provider.dart';
import 'package:flutter_employee_app/utilities/settings.dart';
import 'package:get_it/get_it.dart';
import 'package:http/http.dart' as http;
import 'package:provider/provider.dart';

import 'UserService.dart';
class DeliveryService{

   final UserService _userService=GetIt.I.get();

   Future<String> getNextOrderForDriver(BuildContext context) async {

     final loginURL = Uri.parse(endPoint+"delivery/getNextOrderForDriver");

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
         Provider.of<DeliveryProvider>(context,listen: false).delivery=delivery;
         this.getCustomer(delivery.customerID, context);
         return responseData["delivery"]["deliveryID"];
         }
         return "";
       }
     return "";
     }

   Future<bool> assignDriverToDelivery(String deliveryID, BuildContext context) async {

     final loginURL = Uri.parse(endPoint+"delivery/assignDriverToDelivery");

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

   // Future<bool> completeDelivery(String orderID, BuildContext context) async {
   //
   //   final loginURL = Uri.parse(endPoint+"delivery/completeDelivery");
   //
   //   Map<String,String> headers =new Map<String,String>();
   //
   //   headers =
   //   {
   //     "Accept": "application/json",
   //     "content-type": "application/json",
   //     "Access-Control-Allow-Origin": "*",
   //     "Access-Control-Allow-Methods": "POST, OPTIONS"
   //   };
   //
   //   String orderID=Provider.of<DeliveryProvider>(context, listen: false).delivery.orderID;
   //
   //
   //   final data = {
   //     "orderID":orderID
   //   };
   //
   //   final response = await http.post(loginURL, headers: headers, body: jsonEncode(data));
   //   print(response.body);
   //
   //   if (response.statusCode==200) {
   //     Map<String,dynamic> responseData = json.decode(response.body);
   //     if (responseData["message"] == "Order successfully been delivered and status has been changed") {
   //       return true;
   //     }
   //   }
   //   return false;
   // }

   Future<bool> UpdateDeliveryStatus(String deliveryID, String status,BuildContext context) async {

     final loginURL = Uri.parse(endPoint+"delivery/updateDeliveryStatus");

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

   Future<bool> getCustomer(String customerID, BuildContext context) async {

     final loginURL = Uri.parse(endPoint+"user/getCustomerByUUID");

     Map<String,String> headers =new Map<String,String>();

     headers =
     {
       "Accept": "application/json",
       "content-type": "application/json",
       "Access-Control-Allow-Origin": "*",
       "Access-Control-Allow-Methods": "POST, OPTIONS"
     };

     final data = {
       "userID":customerID
     };

     final response = await http.post(loginURL, headers: headers, body: jsonEncode(data));
     print(response.body);
     if (response.statusCode==200) {
       Map<String,dynamic> responseData = json.decode(response.body);
       print(responseData["message"]);
       if(responseData["message"].contains("Customer entity with corresponding user id was returned")) {
         Customer customer=Customer.fromJson(responseData["customer"]);
         Provider.of<DeliveryProvider>(context,listen: false).customer=customer;
         return true;
       }
       else return false;
     }else{
       return false;
     }
   }
}