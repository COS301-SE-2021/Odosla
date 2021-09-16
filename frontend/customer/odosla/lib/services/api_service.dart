import 'dart:convert';

//import 'dart:js_util';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:get_it/get_it.dart';
import 'package:http/http.dart' as http;
import 'package:odosla/model/cart_item.dart';
import 'package:odosla/model/store.dart';
import 'package:odosla/model/user.dart';
import 'package:odosla/page/order_page.dart';
import 'package:odosla/page/store_page.dart';
import 'package:odosla/provider/cart_provider.dart';
import 'package:odosla/provider/driver_provider.dart';
import 'package:odosla/provider/status_provider.dart';
import 'package:odosla/services/settings.dart';
import 'package:provider/provider.dart';
import 'package:rating_dialog/rating_dialog.dart';

import 'UserService.dart';

class ApiService {
  Future<List<CartItem>> getItems(BuildContext context, String storeID) async {
    String sId = storeID;

    //localhost:8080/shopping/getItems
    final response = await http.post(Uri.parse(endpoint + '/shopping/getItems'),
        headers: {
          "Authorization":
              Provider.of<StatusProvider>(context, listen: false).jwt,
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

  List<CartItem> recommendarCartItemsFromJson(Map<String, dynamic> j) {
    List<CartItem> list;

    //Iterable i = json.decode(j['items']);
    debugPrint("x-1");
    list = (json.decode(json.encode(j['recommendations'])) as List)
        .map((i) => CartItem.fromJson(i))
        .toList();
    debugPrint("x-2");

    return list;
  }

  Future<List<Store>> getStores(BuildContext context) async {
    final response =
        await http.post(Uri.parse(endpoint + '/shopping/getStores'),
            headers: {
              "Authorization":
                  Provider.of<StatusProvider>(context, listen: false).jwt,
              "Accept": "application/json",
              "content-type": "application/json",
              "Access-Control-Allow-Origin": "*",
              "Access-Control-Allow-Methods": "POST, OPTIONS",
            },
            body: jsonEncode({}));

    if (response.statusCode == 200) {
      debugPrint("code _ 200");
      Map<String, dynamic> map = jsonDecode(response.body);
      debugPrint(map.entries.toString());
      List<Store> list = StoresFromJson(map);
      debugPrint(list.toString());
      return list; //CartItem.fromJson(map)
    } else {
      List<Store> list = List.empty();
      debugPrint("___ err " + response.statusCode.toString());
      debugPrint(
          Provider.of<StatusProvider>(context, listen: false).jwt + " <- jwt");
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
    debugPrint(items.toString());
    final storeID = items.first.storeID;
    String pi = items.first.id;
    List<dynamic> itemsList = itemListToJson(items);
    UserService _userService = GetIt.I.get();
    debugPrint('_-_');
    debugPrint(itemsList.toString());
    final response =
        await http.post(Uri.parse(endpoint + '/payment/submitOrder'),
            headers: {
              "Authorization":
                  Provider.of<StatusProvider>(context, listen: false).jwt,
              "Accept": "application/json",
              "content-type": "application/json",
              "Access-Control-Allow-Origin": "*",
              "Access-Control-Allow-Methods": "POST, OPTIONS",
            },
            body: jsonEncode({
              "jwtToken": Provider.of<StatusProvider>(context, listen: false)
                  .jwt, //getCurrentUser
              "listOfItems": itemsList,
              "discount": 0,
              "storeId": "$storeID",
              "orderType": "DELIVERY",
              "latitude": -25.763428, //get lat
              "longitude": 28.260879, //get long
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
          "Authorization":
              Provider.of<StatusProvider>(context, listen: false).jwt,
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
      String result = statusFromJson(context, map);
      debugPrint(result);
      Provider.of<StatusProvider>(context, listen: false).status = result;

      if (result == "ASSIGNED_DRIVER") {
        allocateDriver(context, orderID);
      } else if (result == "DELIVERED") {
        Provider.of<DriverProvider>(context, listen: false).allocated = false;
        Provider.of<CartProvider>(context, listen: false).activeOrder = false;
        Navigator.of(context).push(
            MaterialPageRoute(builder: (BuildContext context) => StorePage()));
        showDialog(
            context: context,
            builder: (context) => RatingDialog(
                  // your app's name?
                  title: 'Order Delivered!',
                  // encourage your user to leave a high rating?
                  message: 'Rate your driver.',
                  // your app's logo?
                  submitButton: 'Thanks!',
                  onCancelled: () => print('cancelled'),
                  onSubmitted: (response) {
                    rateDriver(
                        context,
                        Provider.of<DriverProvider>(context, listen: false).id,
                        response.rating);
                  },
                ));
        Provider.of<StatusProvider>(context).status = "PENDING";
      }

      return result;
    } else {
      List<Store> list = List.empty();
      debugPrint("error " + response.statusCode.toString());
      debugPrint(response.body.toString());
      return "error getting status";
    }
  }

  void allocateDriver(BuildContext context, String id) async {
    final response = await http.post(
        Uri.parse(endpoint + '/delivery/getDeliveryDriverByOrderId'),
        headers: {
          "Authorization":
              Provider.of<StatusProvider>(context, listen: false).jwt,
          "Accept": "application/json",
          "content-type": "application/json",
          "Access-Control-Allow-Origin": "*",
          "Access-Control-Allow-Methods": "POST, OPTIONS",
        },
        body: jsonEncode({
          "orderID": "$id",
        }));

    if (response.statusCode == 200 &&
        !Provider.of<DriverProvider>(context, listen: false).allocated) {
      debugPrint("_____DRIVER_INFO_____");
      debugPrint(response.body.toString());
      Provider.of<DriverProvider>(context, listen: false).id =
          json.decode(response.body)['driver']['driverID'].toString();

      Provider.of<DriverProvider>(context, listen: false).initialize(
          json.decode(response.body)['driver']['driverID'].toString(),
          json.decode(response.body)['driver']['name'].toString(),
          json.decode(response.body)['driver']['phoneNumber'].toString(),
          int.parse(json
              .decode(response.body)['driver']['deliveriesCompleted']
              .toString()),
          double.parse(
              json.decode(response.body)['driver']['rating'].toString()),
          double.parse(json
              .decode(response.body)['driver']['currentAddress']['latitude']
              .toString()),
          double.parse(json
              .decode(response.body)['driver']['currentAddress']['longitude']
              .toString()),
          json.decode(response.body)['deliveryID'].toString());

      Provider.of<DriverProvider>(context, listen: false).allocated = true;
    }
  }

  String statusFromJson(BuildContext context, Map<String, dynamic> j) {
    String s;

    //Iterable i = json.decode(j['items']);
    s = json.decode(json.encode(j['status'])) as String;

    return s;
  }

  Future<List<dynamic>> getGroceryLists(
      BuildContext context, String jwt) async {
    debugPrint(jwt);
    final response =
        await http.post(Uri.parse(endpoint + '/user/getGroceryLists'),
            headers: {
              "Authorization":
                  Provider.of<StatusProvider>(context, listen: false).jwt,
              "Accept": "application/json",
              "content-type": "application/json",
              "Access-Control-Allow-Origin": "*",
              "Access-Control-Allow-Methods": "POST, OPTIONS",
            },
            body: jsonEncode({"JWTToken": jwt}));

    if (response.statusCode == 200) {
      debugPrint("code _ 200");
      Map<String, dynamic> map = jsonDecode(response.body);
      debugPrint(map['groceryLists'][0].toString());

      return ((map['groceryLists'] as List).map((e) => e['name']).toList());

      //return map['groceryLists']; //CartItem.fromJson(map)
    } else {
      debugPrint(jwt);
      debugPrint("___ err " + response.statusCode.toString());
      return []; //artItem.fromJson(map)

    }
  }

  //user/driverSetRating

  rateDriver(BuildContext context, String driverID, int rating) async {
    debugPrint("__" + driverID);
    final response =
        await http.post(Uri.parse(endpoint + '/user/driverSetRating'),
            headers: {
              "Authorization":
                  Provider.of<StatusProvider>(context, listen: false).jwt,
              "Accept": "application/json",
              "content-type": "application/json",
              "Access-Control-Allow-Origin": "*",
              "Access-Control-Allow-Methods": "POST, OPTIONS",
            },
            body: jsonEncode({"driverID": driverID, "rating": rating}));

    if (response.statusCode == 200) {
      debugPrint("code _ 200");
      // Map<String, dynamic> map = jsonDecode(response.body);
      // debugPrint(map['groceryLists'][0].toString());
      // return map['groceryLists']; //CartItem.fromJson(map)
    } else {
      debugPrint("rating err " + response.statusCode.toString());
      // return Map(); //artItem.fromJson(map)

    }
  }

  // Map<String, List<CartItem>> createLists(Map<String, dynamic> input) {
  //   Map<String, List<CartItem>> map = Map();
  //   List<(json.decode(json.encode(input['groceryLists'])) as List)
  // }

  void setGroceryLists(BuildContext context, String jwt) async {
    final response =
        await http.post(Uri.parse(endpoint + '/user/makeGroceryList'),
            headers: {
              "Authorization":
                  Provider.of<StatusProvider>(context, listen: false).jwt,
              "Accept": "application/json",
              "content-type": "application/json",
              "Access-Control-Allow-Origin": "*",
              "Access-Control-Allow-Methods": "POST, OPTIONS",
            },
            body: jsonEncode({
              "JWTToken": jwt,
              "productIds": ["p123984123", "p392874287"],
              "name": "grocery list namt"
            }));

    if (response.statusCode == 200) {
      debugPrint("code _ 200");
      Map<String, dynamic> map = jsonDecode(response.body);
      debugPrint(map.entries.toString());
      return map['groceryLists']; //CartItem.fromJson(map)
    } else {
      debugPrint(jwt);
      debugPrint("___ err " + response.statusCode.toString());
    }
  }

  void updateDriverLocation(BuildContext context, String deliveryID) async {
    final response =
        await http.post(Uri.parse(endpoint + '/delivery/trackDelivery'),
            headers: {
              "Authorization":
                  Provider.of<StatusProvider>(context, listen: false).jwt,
              "Accept": "application/json",
              "content-type": "application/json",
              "Access-Control-Allow-Origin": "*",
              "Access-Control-Allow-Methods": "POST, OPTIONS",
            },
            body: jsonEncode({"deliveryID": deliveryID}));

    if (response.statusCode == 200) {
      debugPrint("code _ 200");
      Map<String, dynamic> map = jsonDecode(response.body);
      debugPrint("_TRACKING_");
      debugPrint(map.entries.toString());

      Provider.of<DriverProvider>(context, listen: false).lat =
          jsonDecode(response.body)['currentLocation']['latitude'];
      Provider.of<DriverProvider>(context, listen: false).long =
          jsonDecode(response.body)['currentLocation']['longitude'];
    } else {
      debugPrint("_track_ err " + response.statusCode.toString());
    }
  }

  Future<User?> getCurrentUser(BuildContext context) async {
    final loginURL = Uri.parse(endpoint + "/user/getCurrentUser");

    Map<String, String> headers = new Map<String, String>();

    headers = {
      "Authorization": Provider.of<StatusProvider>(context, listen: false).jwt,
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS"
    };

    String jwt = Provider.of<StatusProvider>(context, listen: false).jwt;
    final data = {"JWTToken": jwt};

    final response =
        await http.post(loginURL, headers: headers, body: jsonEncode(data));

    if (response.statusCode == 200) {
      Map<String, dynamic> responseData = json.decode(response.body);
      print(responseData);
      if (responseData["success"] == true) {
        User u = User.fromJson(responseData["user"]);
        return u;
      }
    }
  }

  Future<List<CartItem>> getCartRecommendations(
      List<String> arrayOfItemIDs, BuildContext context) async {
    List<dynamic> itemsList = arrayOfItemIDs;
    UserService userService = GetIt.I.get();

    final response = await http.post(
        Uri.parse(endpoint + '/recommendation/getCartRecommendation'),
        headers: {
          "Accept": "application/json",
          "content-type": "application/json",
          "Access-Control-Allow-Origin": "*",
          "Access-Control-Allow-Methods": "POST, OPTIONS",
          "Authorization":
              Provider.of<StatusProvider>(context, listen: false).jwt
        },
        body: jsonEncode({"itemIDs": itemsList}));
    print(response.body);
    print("JWT");
    print(Provider.of<StatusProvider>(context, listen: false).jwt);
    if (response.statusCode == 200) {
      debugPrint("gotRecommendations");
      Map<String, dynamic> map = jsonDecode(response.body);
      List<CartItem> list = recommendarCartItemsFromJson(map);
      print("THIS IS THE LIST");
      print(list);
      return list;
      debugPrint(map.toString());
    } else {
      List<CartItem> list = List.empty();
      debugPrint("__ err " + response.statusCode.toString());
      return list;
    }
  }
}
