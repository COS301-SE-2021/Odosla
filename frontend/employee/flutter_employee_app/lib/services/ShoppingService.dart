import 'dart:convert';
//import 'dart:js_util';

import 'package:flutter/cupertino.dart';
import 'package:flutter_employee_app/models/CartItem.dart';
import 'package:flutter_employee_app/models/Order.dart';
import 'package:flutter_employee_app/models/Store.dart';
import 'package:flutter_employee_app/provider/order_provider.dart';
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
    print(jwt);
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
    print(response);
    if (response.statusCode == 200) {
      Map<String, dynamic> map = jsonDecode(response.body);
      List<Store> list = StoresFromJson(map);
      return list; //CartItem.fromJson(map)
    } else {
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
      "storeID":storeID,
    };

    final response = await http.post(loginURL, headers: headers, body: jsonEncode(data));
    print(response.body);

    if (response.statusCode==200) {
      Map<String,dynamic> responseData = json.decode(response.body);
      if (responseData["success"] == true) {
        Order order= Order.fromJson(responseData["newCurrentOrder"]);

        print("Total cost from response:   ");
        print(responseData["newCurrentOrder"]["totalPrice"]);
        print("Total cost from response after order from json:   ");
        print(order.totalCost);

        Provider.of<OrderProvider>(context,listen: false).order=order;
        print("Total cost from provider:   ");
        print(Provider.of<OrderProvider>(context,listen: false).order.totalCost);
        return order;
      }
    }
    return null;
  }

  Future<List<Order>?> getAllOrdersInQueue(BuildContext context,String storeID) async {

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
      "storeID":storeID
    };

    final response = await http.post(loginURL, headers: headers, body: jsonEncode(data));
    print(response.body);
    if (response.statusCode==200) {
      Map<String,dynamic> responseData = json.decode(response.body);
      if (responseData["response"] == true) {
        List<Order> orders=OrdersFromJson(responseData);
        print(orders);
        return orders;

      } else {
      }
    }
    return null;
  }
}