import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:odosla/main.dart';
import 'package:odosla/model/store.dart';
import 'package:odosla/page/account_page.dart';
import 'package:odosla/page/account_settings_page.dart';
import 'package:odosla/page/cart_page.dart';
import 'package:odosla/page/items_page.dart';
import 'package:odosla/page/order_page.dart';
import 'package:odosla/provider/cart_provider.dart';
import 'package:odosla/provider/store_provider.dart';
import 'package:odosla/services/api_service.dart';
import 'package:odosla/widget/cart_bar_widget.dart';
import 'package:provider/provider.dart';
import 'package:rflutter_alert/rflutter_alert.dart';
import 'package:sliding_up_panel/sliding_up_panel.dart';

class StorePage extends StatefulWidget {
  @override
  _StorePageState createState() => _StorePageState();
}

class _StorePageState extends State<StorePage> {
  ApiService apiService = ApiService();

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
            MyApp.stores,
            style: TextStyle(color: Colors.black),
          ),
          centerTitle: true,
          backgroundColor: Colors.transparent,
          elevation: 0,
          leading: IconButton(
            icon: Icon(Icons.arrow_back_ios, color: Colors.black),
            onPressed: () => {},
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
        body: SafeArea(
          child: SlidingUpPanel(
              minHeight: 70,
              maxHeight: 700,
              parallaxEnabled: true,
              parallaxOffset: -0.025,
              panelBuilder: (_) => CartPage(),
              collapsed: CartBarWidget(),
              body: buildStores()),
        ));
  }

  Widget buildStores() {
    final double spacing = 12;

    //final provider = Provider.of<StoreProvider>(context);

    return FutureBuilder(
      future: apiService.getStores(context),
      builder: (BuildContext context, snapshot) {
        //let's check if we got a response or not
        debugPrint(snapshot.data.toString() + "__");

        if (snapshot.hasError) {
          debugPrint("snapshot error: " + snapshot.error.toString());
        }

        if (snapshot.hasData) {
          debugPrint("HasData");
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
                        Provider.of<StoreProvider>(context, listen: false)
                            .store = items[index];
                        Navigator.of(context).push(MaterialPageRoute(
                            builder: (BuildContext context) => ItemsPage(
                                    items[index].id, items[index].name, {
                                  'lat': items[index].lat,
                                  'long': items[index].long
                                }) //ProductPage(product: product),
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
        debugPrint("_2");
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
                    "assets/" + product.imgUrl,
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
