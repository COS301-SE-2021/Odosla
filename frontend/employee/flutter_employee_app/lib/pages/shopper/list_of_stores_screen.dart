import 'dart:convert';


import 'package:flutter/material.dart';
import 'package:flutter_employee_app/models/Store.dart';
import 'package:flutter_employee_app/pages/shopper/stores_information_page.dart';
import 'package:flutter_employee_app/services/ShoppingService.dart';
import 'package:get_it/get_it.dart';
import 'package:rflutter_alert/rflutter_alert.dart';

class StorePage extends StatefulWidget {
  @override
  _StorePageState createState() => _StorePageState();
}

class _StorePageState extends State<StorePage> {
  final ShoppingService _shoppingService=GetIt.I.get();

  @override
  Widget build(BuildContext context) {

    return Scaffold(
        backgroundColor: Colors.white,
        appBar: AppBar(
          title: Text(
            "STORES",
            style: TextStyle(color: Colors.black),
          ),
          centerTitle: true,
          backgroundColor: Colors.transparent,
        ),
        body: buildStores()
        );
  }

  Widget buildStores() {
    final double spacing = 12;

    //final provider = Provider.of<StoreProvider>(context);

    return FutureBuilder(
      future: _shoppingService.getStores(context),
      builder: (BuildContext context, snapshot) {

        if (snapshot.hasData) {
          //Now let's make a list of articles
          List<Store> items = snapshot.data as List<Store>;
          return GridView.builder(
              physics: BouncingScrollPhysics(),
              padding: EdgeInsets.only(
                  left: spacing, right: spacing, top: spacing, bottom: 140),
              gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 2,
                crossAxisSpacing: 5,
                mainAxisSpacing: 5,
                childAspectRatio: 4 / 4,
              ),
              itemCount: items.length,
              itemBuilder: (context, index) => GestureDetector(
                onTap: () {
                  if (isOpen(
                      items[index].openTime, items[index].closeTime)) {
                    Navigator.of(context).push(MaterialPageRoute(
                        builder: (BuildContext context) => StoreInfoScreen(context, store: items[index])//ProductPage(product: product),
                    ));
                  } else {
                    Alert(
                      context: context,
                      title: items[index].name + " is closed",
                      desc: "The store opens again at " +
                          items[index].openTime.toString() +
                          ":00.",
                      buttons: [
                        DialogButton(
                          color: Colors.deepOrange,
                          child: Text(
                            "OK",
                            style: TextStyle(
                                color: Colors.white, fontSize: 20),
                          ),
                          onPressed: () => Navigator.pop(context),
                          width: 120,
                        )
                      ],
                    ).show();
                  }
                },
                child: Container(
                  child: buildStore(items[index]),
                ),
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
      borderRadius: BorderRadius.circular(12),
      border: Border.all(color: Colors.white, width: 2),
    ),
    child: Padding(
      padding: const EdgeInsets.all(16),
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Expanded(
            //constraints: BoxConstraints(
            //  maxHeight: 100,
            //),
            child: Center(
              child: Image.asset(
                "assets/"+product.imageUrl,
              ), //product.imgUrl),
            ),
          ),
          SizedBox(height: 10),
          Container(
            child: Container(
              alignment: Alignment.topLeft,
              child: Text(
                product.name,
                overflow: TextOverflow.ellipsis,
                maxLines: 2,
                softWrap: false,
                style:
                TextStyle(fontWeight: FontWeight.normal, fontSize: 16),
              ),
            ),
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              isOpenWidget(product.openTime, product.closeTime),
              openingWidget(product, product.openTime, product.closeTime),
            ],
          )
        ],
      ),
    ),
  );

  bool isOpen(int openTime, int closeTime) {
    var now = DateTime.now();
    if (now.hour < closeTime && now.hour > openTime)
      return true;
    else
      return false;
  }

  Widget isOpenWidget(int openTime, int closeTime) {
    var now = DateTime.now();
    if (isOpen(openTime, closeTime)) {
      return Text(
        "open",
        style: TextStyle(
          fontStyle: FontStyle.italic,
          color: Colors.green[700],
        ),
      );
    } else
      return Text(
        "closed",
        style: TextStyle(
          fontStyle: FontStyle.italic,
          color: Colors.red[700],
        ),
      );
  }

  Widget openingWidget(Store shop, int openTime, int closeTime) {
    if (isOpen(openTime, closeTime)) {
      return Text(
        "closing " + shop.closeTime.toString() + ':00',
        style: TextStyle(
          fontStyle: FontStyle.italic,
          color: Colors.grey[600],
        ),
      );
    } else {
      return Text(
        "opening " + shop.openTime.toString() + ':00',
        style: TextStyle(
          fontStyle: FontStyle.italic,
          color: Colors.grey[600],
        ),
      );
    }
  }
}
