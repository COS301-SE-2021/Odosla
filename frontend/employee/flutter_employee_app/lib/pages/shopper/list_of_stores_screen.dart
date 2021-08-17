import 'dart:convert';

import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_employee_app/models/Store.dart';
import 'package:flutter_employee_app/pages/shopper/stores_information_page.dart';
import 'package:flutter_employee_app/services/ShoppingService.dart';
import 'package:get_it/get_it.dart';
import 'package:provider/provider.dart';
import 'package:http/http.dart' as http;


class StorePage extends StatefulWidget {
  @override
  _StorePageState createState() => _StorePageState();
}

class _StorePageState extends State<StorePage> {


  final ShoppingService _shoppingService= GetIt.I.get();

  @override
  Widget build(BuildContext context) => Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        title: Text(
          "Stores",
          style: TextStyle(color: Colors.black),
        ),
        centerTitle: true,
        backgroundColor: Colors.transparent,
        elevation: 0,

      ),
    body: buildStores(),
  );

  Widget buildStores() {
    final double spacing = 12;

    //final provider = Provider.of<StoreProvider>(context);

    return FutureBuilder(
      future: _shoppingService.getStores(),
      builder: (BuildContext context, snapshot) {
        if (snapshot.hasError) {
          debugPrint("snapshot error: " + snapshot.error.toString());
        }

        if (snapshot.hasData) {
          //Now let's make a list of articles
          List<Store> items = snapshot.data as List<Store>;
          return GridView.builder(
            //physics: BouncingScrollPhysics(),
              padding: EdgeInsets.all(spacing),
              gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 2,
                crossAxisSpacing: spacing,
                mainAxisSpacing: spacing,
                childAspectRatio: 3 / 4,
              ),
              itemCount: items.length,
              itemBuilder: (context, index) => GestureDetector(
                onTap: () {
                  debugPrint(items[index].id);
                  Navigator.of(context).push(MaterialPageRoute(
                      builder: (BuildContext context) => StoreInfoScreen(context, store:items[index]) //ProductPage(product: product),
                  ));
                },
                child: buildStore(items[index]),
              ));
        }
        return Center(
          child: CircularProgressIndicator(),
        );
      },
    );
  }

  Widget buildStore(Store product) => Container(
    decoration: BoxDecoration(
      color: Colors.white,
      borderRadius: BorderRadius.all(Radius.circular(16)),
    ),
    child: Padding(
      padding: const EdgeInsets.all(16),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          ConstrainedBox(
            constraints: BoxConstraints(
              maxHeight: 100,
            ),
            child: Center(
              child: Image.asset(
                ("assets/"+product.imageUrl),
              ), //product.imgUrl),
            ),
          ),
          SizedBox(height: 10),
          Text(
            product.name,
            style: TextStyle(fontWeight: FontWeight.normal, fontSize: 16),
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(
                "open",
                style: TextStyle(
                  fontStyle: FontStyle.italic,
                  color: Colors.green[700],
                ),
              ),
              Text(
                "closing " + product.closeTime.toString() + ':00',
                style: TextStyle(
                  fontStyle: FontStyle.italic,
                  color: Colors.grey[600],
                ),
              ),
            ],
          )
        ],
      ),
    ),
  );
}
