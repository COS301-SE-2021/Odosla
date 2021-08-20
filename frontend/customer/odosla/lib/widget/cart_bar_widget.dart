import 'package:flutter/material.dart';
import 'package:provider/provider.dart';
import 'package:odosla/model/cart_item.dart';
import 'package:odosla/provider/cart_provider.dart';

class CartBarWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final provider = Provider.of<CartProvider>(context);

    return Container(
        decoration: BoxDecoration(color: Colors.deepOrange),
        padding: EdgeInsets.symmetric(horizontal: 20, vertical: 10),
        child:
            Row(mainAxisAlignment: MainAxisAlignment.spaceBetween, children: [
          Text(
            "Cart",
            style: TextStyle(
              color: Colors.white,
              fontSize: 24,
              fontWeight: FontWeight.bold,
            ),
          ),
          Text(
            provider.items.length.toString() + ' items',
            style: TextStyle(
              color: Colors.white,
              fontSize: 20,
              fontWeight: FontWeight.w400,
            ),
          ),
          Text(
            "R" + provider.total.toStringAsFixed(2),
            style: TextStyle(
              color: Colors.white,
              fontSize: 22,
              fontWeight: FontWeight.bold,
            ),
          ),
        ]));
  }
}
