import 'dart:convert';

//import 'dart:js_util';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:odosla/model/cart_item.dart';
import 'package:odosla/model/store.dart';
import 'package:odosla/page/order_page.dart';
import 'package:odosla/page/store_page.dart';
import 'package:odosla/provider/cart_provider.dart';
import 'package:odosla/provider/status_provider.dart';
import 'package:provider/provider.dart';
import 'package:rflutter_alert/rflutter_alert.dart';

class ApiService {
  //final endpoint = "http://localhost:8080";
  final endpoint = "http://45bffa53a9b3.ngrok.io";

  Future<List<CartItem>> getItems(String storeID) async {
    String sId = storeID;

    //localhost:8080/shopping/getItems
    final response = await http.post(Uri.parse(endpoint + '/shopping/getItems'),
        headers: {
          "Accept": "application/json",
          "content-type": "application/json",
          "Access-Control-Allow-Origin": "*",
          "Access-Control-Allow-Methods": "POST, OPTIONS",
        },
        body: jsonEncode({"storeID": storeID}));

    if (response.statusCode == 200) {
      debugPrint("code _ 200");
      Map<String, dynamic> map = jsonDecode(response.body);
      debugPrint("2:::" + map.entries.toString());
      List<CartItem> list = cartItemsFromJson(map);
      debugPrint("1:::" + list.toString());
      return list; //CartItem.fromJson(map)
    } else {
      List<CartItem> list = List.empty();
      debugPrint(storeID);
      debugPrint("___ err " + response.statusCode.toString());
      return list; //CartItem.fromJson(map)

    }
  }

  List<CartItem> cartItemsFromJson(Map<String, dynamic> j) {
    List<CartItem> list;

    //Iterable i = json.decode(j['items']);
    debugPrint("x-1");
    list = (json.decode(json.encode(j['items'])) as List)
        .map((i) => CartItem.fromJson(i))
        .toList();
    debugPrint("x-2");

    return list;
  }

  Future<List<Store>> getStores() async {
    final response =
        await http.post(Uri.parse(endpoint + '/shopping/getStores'),
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

  List<Store> StoresFromJson(Map<String, dynamic> j) {
    List<Store> list;

    //Iterable i = json.decode(j['items']);
    list = (json.decode(json.encode(j['stores'])) as List)
        .map((i) => Store.fromJson(i))
        .toList();

    return list;
  }

  void submitOrder(List<CartItem> items, BuildContext context) async {
    final storeID = items.first.storeID;
    String pi = items.first.id;
    List<dynamic> itemsList = itemListToJson(items);
    debugPrint('_-_');
    debugPrint(itemsList.toString());
    final response =
        await http.post(Uri.parse(endpoint + '/payment/submitOrder'),
            headers: {
              "Accept": "application/json",
              "content-type": "application/json",
              "Access-Control-Allow-Origin": "*",
              "Access-Control-Allow-Methods": "POST, OPTIONS",
            },
            body: jsonEncode({
              "userId": "7bc59ea6-aa30-465d-bcab-64e894bef586", //getCurrentUser
              "listOfItems": itemsList,
              "discount": 0,
              "storeId": "$storeID",
              "orderType": "DELIVERY",
              "latitude": 3.4, //get lat
              "longitude": 4.6, //get long
              "deliveryAddress": "23 Sigard Street, Pretoria" //get address
            }));

    if (response.statusCode == 200) {
      debugPrint("submitted order");
      Map<String, dynamic> map = jsonDecode(response.body);
      Provider.of<CartProvider>(context, listen: false).activeOrder = true;
      Provider.of<CartProvider>(context, listen: false).activeOrderID =
          map["orderId"];
      Provider.of<CartProvider>(context, listen: false).clearItems();
      Navigator.of(context).push(MaterialPageRoute(
          builder: (BuildContext context) => OrderPage(map['orderId'])));
      debugPrint(map.toString());
    } else {
      List<Store> list = List.empty();
      debugPrint("error " + response.statusCode.toString());
      debugPrint(response.body.toString());
    }
  }

  List<Map<String, dynamic>> itemListToJson(List<CartItem> items) {
    List<Map<String, dynamic>> list =
        (items.map((item) => item.toJson())).toList();
    return list;
  }

  Future<String> getStatus(BuildContext context, String orderID) async {
    final oid = orderID;
    debugPrint("id: " + oid);
    final response = await http.post(Uri.parse(endpoint + '/payment/getStatus'),
        headers: {
          "Accept": "application/json",
          "content-type": "application/json",
          "Access-Control-Allow-Origin": "*",
          "Access-Control-Allow-Methods": "POST, OPTIONS",
        },
        body: jsonEncode({
          "orderID": "$oid",
        }));

    if (response.statusCode == 200) {
      debugPrint("fetched status");
      Map<String, dynamic> map = jsonDecode(response.body);
      String result = statusFromJson(map);
      debugPrint(result);
      Provider.of<StatusProvider>(context, listen: false).status = result;

      if (result == "DELIVERED") {
        Provider.of<CartProvider>(context, listen: false).activeOrder = false;
        Navigator.of(context).push(
            MaterialPageRoute(builder: (BuildContext context) => StorePage()));
        Alert(
          context: context,
          title: "Order delivered",
          desc: "Enjoy your day!",
          buttons: [
            DialogButton(
              color: Colors.deepOrange,
              child: Text(
                "THANKS",
                style: TextStyle(color: Colors.white, fontSize: 20),
              ),
              onPressed: () => Navigator.pop(context),
              width: 120,
            )
          ],
        ).show();
      }

      return result;
    } else {
      List<Store> list = List.empty();
      debugPrint("error " + response.statusCode.toString());
      debugPrint(response.body.toString());
      return "error getting status";
    }
  }

  String statusFromJson(Map<String, dynamic> j) {
    String s;

    //Iterable i = json.decode(j['items']);
    s = json.decode(json.encode(j['status'])) as String;

    return s;
  }
}
