
import 'dart:convert';

import 'package:flutter_employee_app/utilities/functions.dart';

class Order{
  
  String orderID;
  String customerID;
  String storeID;
  String createdDate;
  double totalCost;
  String discount;
  List <Item> items;

  Order(this.orderID, this.customerID, this.storeID, this.createdDate, this.totalCost, this.items, this.discount);

  Order.fromJson(Map<String, dynamic> json)
      : orderID=json["orderId"],
        customerID=json["userId"],
        storeID=json["storeId"],
        createdDate=json["createDate"],
        totalCost=json["totalPrice"],
        discount=json["discount"].toString(),
        items=ItemsFromJson(json);
}


class Item {

  String name;
  String productID;
  String barcode;
  double price;
  int quantity;
  String imgUrl;
  String brand;

  Item(this.name, this.barcode, this.price, this.quantity, this.imgUrl, this.brand, this.productID);

  Item.fromJson(Map<String, dynamic> json)
  :productID=json["productID"],
   name=json["name"],
   barcode=json["barcode"],
   price=json["price"],
   quantity=json["quantity"],
   imgUrl=json["imageUrl"],
   brand=json["brand"];


}