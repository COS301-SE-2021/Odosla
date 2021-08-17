import 'dart:convert';
//import 'dart:js_util';

import 'package:flutter/cupertino.dart';
import 'package:flutter_employee_app/models/CartItem.dart';
import 'package:flutter_employee_app/models/Order.dart';
import 'package:flutter_employee_app/models/Store.dart';
import 'package:flutter_employee_app/provider/order_provider.dart';
import 'package:flutter_employee_app/services/UserService.dart';
import 'package:flutter_employee_app/utilities/functions.dart';
import 'package:get_it/get_it.dart';
import 'package:http/http.dart' as http;
import 'package:provider/provider.dart';

class ShoppingService {

  UserService _userService=GetIt.I.get();

  //final String endPoint = "c71682b066b8.ngrok.io/";
  final String endPoint = "10.0.2.2:8080/";
  Future<List<CartItem>> getItems(String storeID) async {
    String sId = "01234567-9ABC-DEF0-1234-56789ABCDEF0";

    //localhost:8080/shopping/getItems
    final response = await http.post(
        Uri.parse('http://a70dc1d7d5d0.ngrok.io/shopping/getItems'),
        headers: {
          "Accept": "application/json",
          "content-type": "application/json",
          "Access-Control-Allow-Origin": "*",
          "Access-Control-Allow-Methods": "POST, OPTIONS",
        },
        body: jsonEncode({"storeId": "01234567-9ABC-DEF0-1234-56789ABCDEF0"}));

    if (response.statusCode == 200) {
      debugPrint("code _ 200");
      Map<String, dynamic> map = jsonDecode(response.body);
      List<CartItem> list = cartItemsFromJson(map);
      debugPrint(list.toString());
      return list; //CartItem.fromJson(map)
    } else {
      List<CartItem> list = List.empty();
      debugPrint("___ err " + response.statusCode.toString());
      return list; //CartItem.fromJson(map)
    }
  }

  List<CartItem> cartItemsFromJson(Map<String, dynamic> j) {
    List<CartItem> list;

    //Iterable i = json.decode(j['items']);
    list = (json.decode(json.encode(j['items'])) as List)
        .map((i) => CartItem.fromJson(i))
        .toList();

    return list;
  }

  Future<List<Store>> getStores() async {
    final response = await http.post(
        Uri.parse('http://'+endPoint+'shopping/getStores'),
        headers: {
          "Accept": "application/json",
          "content-type": "application/json",
          "Access-Control-Allow-Origin": "*",
          "Access-Control-Allow-Methods": "POST, OPTIONS",
        },
        body: jsonEncode({}));

    if (response.statusCode == 200) {
      debugPrint("code _ 200");
      Map<String, dynamic> map = jsonDecode(response.body);
      List<Store> list = StoresFromJson(map);
      debugPrint(list.toString());
      return list; //CartItem.fromJson(map)
    } else {
      List<Store> list = List.empty();
      debugPrint("___ err " + response.statusCode.toString());
      return list; //CartItem.fromJson(map)
    }
  }



  Future<Order?> getNextQueued(String storeID, BuildContext context) async {

    final loginURL = Uri.parse("http://"+endPoint+"shopping/getNextQueued");

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

    print("JWT: "+jwt);
    print(storeID);
    final data = {
      "storeID":storeID,
      "jwtToken":jwt
    };

    final response = await http.post(loginURL, headers: headers, body: jsonEncode(data));
    print(response.body);

    if (response.statusCode==200) {
      Map<String,dynamic> responseData = json.decode(response.body);
      if (responseData["success"] == true) {
        Order order= Order.fromJson(responseData["newCurrentOrder"]);
        for(var i in order.items){
          print(i.price);
          print(i.brand);
        }

        Provider.of<OrderProvider>(context,listen: false).order=order;
        print(order.totalCost);
        Provider.of<OrderProvider>(context,listen: false).order.totalCost=order.totalCost;
        print(Provider.of<OrderProvider>(context,listen: false).order.totalCost);
        Provider.of<OrderProvider>(context,listen: false).order.orderID=order.orderID;
        print(Provider.of<OrderProvider>(context,listen: false).order.totalCost);
        return order;
      }
    }
    return null;
  }

  Future<List<Order>?> getAllOrdersInQueue(String storeID) async {

    final loginURL = Uri.parse("http://"+endPoint+"shopping/getQueue");

    Map<String,String> headers =new Map<String,String>();

    headers =
    {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS"
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