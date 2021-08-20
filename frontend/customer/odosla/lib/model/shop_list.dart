import 'package:flutter/foundation.dart';
import 'package:meta/meta.dart';

class CartItem {
  final String id;
  final String title;
  final String barcode;
  final String storeID;
  final double price;
  final int quantity;
  final String description;
  final String imgUrl;
  final String brand;
  final String size;
  final String type;

  const CartItem(
    this.id,
    this.title,
    this.barcode,
    this.storeID,
    this.price,
    this.quantity,
    this.description,
    this.imgUrl,
    this.brand,
    this.size,
    this.type,
  );
  CartItem copy(
    String id,
    String title,
    String barcode,
    String storeID,
    double price,
    int quantity,
    String description,
    String imgUrl,
    String brand,
    String size,
    String type,
  ) =>
      CartItem(id, title, barcode, storeID, price, quantity, description,
          imgUrl, brand, size, type);

  CartItem.fromJson(Map<String, dynamic> json)
      : id = json['productId'],
        title = json['name'],
        barcode = json['barcode'],
        storeID = json['storeId'],
        price = json['price'],
        quantity = json['quantity'],
        description = json['description'],
        imgUrl = json['imageUrl'],
        brand = json['brand'],
        size = json['size'],
        type = json['itemType'];

  Map<String, dynamic> toJson() => {
        'productId': id,
        'name': title,
        'barcode': barcode,
        'storeId': storeID,
        'price': price,
        'quantity': quantity,
        'description': description,
        'imageUrl': imgUrl,
        'brand': brand,
        'size': size,
        'itemType': type,
      };
}
