import 'package:flutter/foundation.dart';
import 'package:meta/meta.dart';

class CartItem {
  final String id;
  final String title;
  final int quantity;
  final String imgUrl;
  final double price;

  const CartItem(
      this.id,
      this.title,
      this.quantity,
      this.imgUrl,
      this.price,
      );

  CartItem copy(
      String id,
      String title,
      int quantity,
      String imgUrl,
      double price,
      ) =>
      CartItem(id, title, quantity, imgUrl, price);

  CartItem.fromJson(Map<String, dynamic> json)
      : id = json['productId'],
        title = json['name'],
        quantity = json['quantity'],
        imgUrl = json['imageUrl'],
        price = json['price'];
}