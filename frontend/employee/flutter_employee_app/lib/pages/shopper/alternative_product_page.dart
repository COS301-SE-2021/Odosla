import 'package:flutter/material.dart';
import 'package:flutter_employee_app/models/cart_item.dart';
import 'package:flutter_employee_app/services/ShoppingService.dart';
import 'package:get_it/get_it.dart';

import 'item_detail_page.dart';

class AlternativeItemsPage extends StatefulWidget {
  final String storeID;
  final String storeName;
  final Map<String, double> location;

  const AlternativeItemsPage(this.storeID, this.storeName, this.location);

  @override
  _AlternativeItemsPageState createState() =>
      _AlternativeItemsPageState(storeID, storeName, location);
}

class _AlternativeItemsPageState extends State<AlternativeItemsPage> {
  final ShoppingService _shoppingService = GetIt.I.get();

  final String storeID;
  final String storeName;
  final Map<String, double> location;

  _AlternativeItemsPageState(this.storeID, this.storeName, this.location);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        backgroundColor: Theme.of(context).backgroundColor,
        appBar: AppBar(
          title: Text(
            storeName,
            style: TextStyle(),
          ),
          centerTitle: true,
          toolbarHeight: 32,
          backgroundColor: Theme.of(context).primaryColor,
          elevation: 0,
          leading: IconButton(
            icon: Icon(Icons.arrow_back_ios),
            onPressed: () => Navigator.pop(context),
          ),
        ),
        body: buildItems(context));
  }

  Widget buildItems(BuildContext context) {
    final double spacing = 12;
    return FutureBuilder(
      future: _shoppingService.getItemsInStock(storeID, context),
      builder: (BuildContext context, snapshot) {
        //let's check if we got a response or not
        debugPrint(snapshot.data.toString() + "__");

        if (snapshot.hasError) {
          debugPrint("snapshot error: " + snapshot.error.toString());
          debugPrint(storeID);
        }

        if (snapshot.hasData) {
          debugPrint("HasData");
          //Now let's make a list of articles
          List<CartItem> items = snapshot.data as List<CartItem>;
          return GridView.builder(
              physics: BouncingScrollPhysics(),
              padding: EdgeInsets.only(
                  left: spacing, right: spacing, top: spacing, bottom: 160),
              gridDelegate: SliverGridDelegateWithFixedCrossAxisCount(
                crossAxisCount: 2,
                crossAxisSpacing: 7,
                mainAxisSpacing: 7,
                childAspectRatio: 4 / 4,
              ),
              itemCount: items.length,
              itemBuilder: (context, index) => GestureDetector(
                    onTap: () {
                      Navigator.of(context).push(MaterialPageRoute(
                          builder: (BuildContext context) => ItemDetailPage(
                              items[index],
                              storeID,
                              location,
                              true) //ProductPage(product: product),
                          ));
                    },
                    child: buildItem(items[index]),
                  ));
        }
        debugPrint("_2");
        return Center(
          child: CircularProgressIndicator(),
        );
      },
    );
  }

  Widget buildItem(CartItem product) => Container(
        decoration: BoxDecoration(
          color: Theme.of(context).primaryColor,
          borderRadius: BorderRadius.circular(12),
          border:
              Border.all(color: Theme.of(context).backgroundColor, width: 2),
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
                    'assets/' + product.imgUrl,
                  ), //product.imgUrl),
                ),
              ),
              SizedBox(height: 40),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Container(
                    child: Expanded(
                      child: Text(
                        product.title,
                        overflow: TextOverflow.ellipsis,
                        maxLines: 2,
                        softWrap: false,
                        style: TextStyle(
                            fontWeight: FontWeight.normal, fontSize: 16),
                      ),
                    ),
                  ),
                ],
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Expanded(
                    child: Text(
                      product.brand,
                      overflow: TextOverflow.ellipsis,
                      style: TextStyle(
                          fontWeight: FontWeight.normal,
                          color: Colors.grey[600],
                          fontSize: 12),
                    ),
                  ),
                  Text(
                    'R' + product.price.toString(),
                    style: TextStyle(fontWeight: FontWeight.bold, fontSize: 16),
                  ),
                ],
              ),
            ],
          ),
        ),
      );
}