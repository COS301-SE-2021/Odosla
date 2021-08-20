import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:odosla/model/cart_item.dart';
import 'package:odosla/page/account_page.dart';
import 'package:odosla/page/account_settings_page.dart';
import 'package:odosla/page/cart_page.dart';
import 'package:odosla/page/item_detail_page.dart';
import 'package:odosla/page/order_page.dart';
import 'package:odosla/provider/cart_provider.dart';
import 'package:odosla/services/api_service.dart';
import 'package:odosla/widget/cart_bar_widget.dart';
import 'package:provider/provider.dart';
import 'package:sliding_up_panel/sliding_up_panel.dart';

class ItemsPage extends StatefulWidget {
  final String storeID;
  final String storeName;
  final Map<String, double> location;

  const ItemsPage(this.storeID, this.storeName, this.location);

  @override
  _ItemsPageState createState() =>
      _ItemsPageState(storeID, storeName, location);
}

class _ItemsPageState extends State<ItemsPage> {
  ApiService apiService = ApiService();
  final String storeID;
  final String storeName;
  final Map<String, double> location;

  _ItemsPageState(this.storeID, this.storeName, this.location);

  @override
  Widget build(BuildContext context) {
    final List<Widget> pages = [
      OrderPage(Provider.of<CartProvider>(context).activeOrderID),
      AccountSettingsPage()
    ];

    return Scaffold(
      backgroundColor: Colors.white,
      appBar: AppBar(
        title: Text(
          storeName + " Catalogue",
          style: TextStyle(color: Colors.black),
        ),
        centerTitle: true,
        backgroundColor: Colors.transparent,
        elevation: 0,
        leading: IconButton(
          icon: Icon(Icons.arrow_back_ios, color: Colors.black),
          onPressed: () => Navigator.pop(context),
        ),
        actions: [
          IconButton(
            onPressed: () {
              Navigator.of(context).push(MaterialPageRoute(
                  builder: (BuildContext context) => AccountPage(
                      Provider.of<CartProvider>(context).activeOrderID,
                      Provider.of<CartProvider>(context).activeOrder,
                      pages)));
            },
            icon: Icon(Icons.person_sharp, color: Colors.black),
          ),
          const SizedBox(width: 5),
        ],
      ),
      body: SlidingUpPanel(
          minHeight: 70,
          maxHeight: 700,
          parallaxEnabled: true,
          parallaxOffset: -0.025,
          panelBuilder: (_) => CartPage(),
          collapsed: CartBarWidget(),
          body: buildItems(context)),
    );
  }

  Widget buildItems(BuildContext context) {
    final double spacing = 12;

    //final provider = Provider.of<CartItemProvider>(context);

    return FutureBuilder(
      future: apiService.getItems(storeID),
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
                              location) //ProductPage(product: product),
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
          color: Colors.white,
          borderRadius: BorderRadius.circular(12),
          border: Border.all(color: Colors.deepOrange, width: 2),
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
