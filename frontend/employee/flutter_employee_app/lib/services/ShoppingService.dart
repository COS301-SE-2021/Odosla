import 'dart:convert';
//import 'dart:js_util';

import 'package:flutter/cupertino.dart';
import 'package:flutter_employee_app/models/CartItem.dart';
import 'package:flutter_employee_app/models/Order.dart';
import 'package:flutter_employee_app/models/Store.dart';
import 'package:flutter_employee_app/provider/order_provider.dart';
import 'package:flutter_employee_app/provider/shop_provider.dart';
import 'package:flutter_employee_app/services/UserService.dart';
import 'package:flutter_employee_app/utilities/functions.dart';
import 'package:flutter_employee_app/utilities/settings.dart';
import 'package:get_it/get_it.dart';
import 'package:http/http.dart' as http;
import 'package:provider/provider.dart';

class ShoppingService {

  UserService _userService=GetIt.I.get();

  Future<List<Store>> getStores(BuildContext context) async {

    String jwt="";
    await _userService.getJWTAsString(context).then((value) => {
      jwt=value!
    });

    final response = await http.post(
        Uri.parse(shoppingEndPoint+'shopping/getStores'),
        headers: {
          "Accept": "application/json",
          "content-type": "application/json",
          "Access-Control-Allow-Origin": "*",
          "Access-Control-Allow-Methods": "POST, OPTIONS",
          "Authorization":jwt,
        },
        body: jsonEncode({}));
    if (response.statusCode == 200) {
      Map<String, dynamic> map = jsonDecode(response.body);
      List<Store> list = StoresFromJson(map);
      return list; //CartItem.fromJson(map)
    }
    else {
      List<Store> list = List.empty();
      return list; //CartItem.fromJson(map)
    }
  }

  Future<Order?> getNextQueued(String storeID, BuildContext context) async {

    final loginURL = Uri.parse(shoppingEndPoint+"shopping/getNextQueued");

    Map<String,String> headers =new Map<String,String>();

    String jwt="";
    await _userService.getJWTAsString(context).then((value) => {
      jwt=value!
    });


    headers =
    {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS",
      "Authorization":jwt
    };

    final data = {
      "storeID":"$storeID",
    };

    final response = await http.post(loginURL, headers: headers, body: jsonEncode(data));
    print("CURRENT ORDER");
    print(response.body);

    if (response.statusCode==200) {
      Map<String,dynamic> responseData = json.decode(response.body);
      print(responseData["newCurrentOrder"]);
      print(responseData["message"]);
      if (responseData["message"] != "") {
        print(responseData["newCurrentOrder"]);
        Order order= Order.fromJson(responseData["newCurrentOrder"]);
        Provider.of<OrderProvider>(context,listen: false).order=order;
        return order;
      }
    }
    return null;
  }

  Future<List<Order>> getAllOrdersInQueue(BuildContext context,String storeID) async {

    final loginURL = Uri.parse(shoppingEndPoint+"shopping/getQueue");

    Map<String,String> headers =new Map<String,String>();

    String jwt="";

    await _userService.getJWTAsString(context).then((value) => {
      jwt=value!
    });

    headers =
    {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS",
      "Authorization":jwt
    };

    print(storeID);
    final data = {
      "storeID":"$storeID"
    };

    final response = await http.post(loginURL, headers: headers, body: jsonEncode(data));
    print(response.body);
    if (response.statusCode==200) {
      Map<String, dynamic> map = jsonDecode(response.body);
      List<Order> list = OrdersFromJson(map);
      return list;
    }
    else {
      List<Order> list = List.empty();
      return list; //CartItem.fromJson(map)
    }

  }

  Future<Store> getStoreByID(BuildContext context,String storeID) async {

    final loginURL = Uri.parse(shoppingEndPoint+"shopping/getStoreByUUID");

    Map<String,String> headers =new Map<String,String>();

    String jwt="";

    await _userService.getJWTAsString(context).then((value) => {
      jwt=value!
    });

    headers =
    {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS",
      "Authorization":jwt
    };

    print(storeID);
    final data = {
      "storeID":"$storeID"
    };

    final response = await http.post(loginURL, headers: headers, body: jsonEncode(data));

    if (response.statusCode==200) {
      Map<String,dynamic> responseData = json.decode(response.body);
      Store _store=Store.fromJson(responseData[""]);
      Provider.of<ShopProvider>(context,listen: false).store=_store;

      return _store;
    }
    else {
      Store _store=Store("", "", 0, 0, true,"","","","");
      return _store;
    }

  }

  Future<Store> provideAlternative(BuildContext context,String storeID, String barcode) async {

    final loginURL = Uri.parse(shoppingEndPoint+"shopping/getStoreByUUID");

    Map<String,String> headers =new Map<String,String>();

    String jwt="";

    await _userService.getJWTAsString(context).then((value) => {
      jwt=value!
    });

    headers =
    {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS",
      "Authorization":jwt
    };

    print(storeID);
    final data = {
      "storeID":"$storeID"
    };

    final response = await http.post(loginURL, headers: headers, body: jsonEncode(data));

    if (response.statusCode==200) {
      Map<String,dynamic> responseData = json.decode(response.body);
      Store _store=Store.fromJson(responseData[""]);
      Provider.of<ShopProvider>(context,listen: false).store=_store;

      return _store;
    }
    else {
      Store _store=Store("", "", 0, 0, true,"","","","");
      return _store;
    }

  }

}