import 'dart:convert';

import 'package:flutter_employee_app/models/GeoPoint.dart';
import 'package:flutter_employee_app/models/Order.dart';
import 'package:flutter_employee_app/models/Store.dart';

bool validateStructure(String value) {
  String pattern =
      r'^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[!@#\$&*~]).{8,}$';
  RegExp regExp = new RegExp(pattern);
  return regExp.hasMatch(value);
}

bool validatePasswordMatches(String value, String previousPassword) {
  if (value == (previousPassword)) {
    return true;
  } else {
    return false;
  }
}

bool validatePhoneNumber(String value) {
  if (value == "") {
    return true;
  }
  String pattern = r'[0-9]{10}';
  RegExp regExp = new RegExp(pattern);
  return regExp.hasMatch(value);
}

List<Store> StoresFromJson(Map<String, dynamic> j) {
  List<Store> list;
  list = (json.decode(json.encode(j['stores'])) as List)
      .map((i) => Store.fromJson(i))
      .toList();

  return list;
}

List<GeoPoint> GeoPointsFromJson(Map<String, dynamic> j) {
  List<GeoPoint> list;
  list = (json.decode(json.encode(j['pickUpLocations'])) as List)
      .map((i) => GeoPoint.fromJson(i))
      .toList();

  return list;
}

List<Item> ItemsFromJson(Map<String, dynamic> j) {
  List<Item> list;
  list = (json.decode(json.encode(j['cartItems'])) as List)
      .map((i) => Item.fromJson(i))
      .toList();

  return list;
}

List<Order> OrdersFromJson(Map<String, dynamic> j) {
  List<Order> list;

  //Iterable i = json.decode(j['items']);
  list = (json.decode(json.encode(j['queueOfOrders'])) as List)
      .map((i) => Order.fromJson(i))
      .toList();

  return list;
}
