import 'package:ars_dialog/ars_dialog.dart';
import 'package:flutter/material.dart';
import 'package:flutter_bounce/flutter_bounce.dart';
import 'package:flutter_slider_drawer/flutter_slider_drawer.dart';
import 'package:get_it/get_it.dart';
import 'package:odosla/model/cart_item.dart';
import 'package:odosla/page/wallet_page.dart';
import 'package:odosla/provider/cart_provider.dart';
import 'package:odosla/provider/wallet_provider.dart';
import 'package:odosla/services/UserService.dart';
import 'package:odosla/services/api_service.dart';
import 'package:provider/provider.dart';
import 'package:rflutter_alert/rflutter_alert.dart';

class CartPage extends StatefulWidget {
  @override
  _CartPage createState() => _CartPage();
}

class _CartPage extends State<CartPage> {
  GlobalKey<SliderMenuContainerState> _key =
      new GlobalKey<SliderMenuContainerState>();
  late String title;

  UserService _userService = GetIt.I.get();

  @override
  void initState() {
    title = "Cart";
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    ApiService apiService = ApiService();

    return Scaffold(
      body: SliderMenuContainer(
          appBarColor: Colors.white,
          hasAppBar: false,
          slideDirection: SlideDirection.RIGHT_TO_LEFT,
          key: _key,
          sliderMenuOpenSize: 200,
          title: Text(
            title,
            style: TextStyle(fontSize: 22, fontWeight: FontWeight.w700),
          ),
          sliderMenu: Scaffold(
            backgroundColor: Colors.deepOrangeAccent,
            body: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: [
                SizedBox(height: 50),
                Container(
                  height: 50,
                  width: 200,
                  child: TextButton(
                      child: Text("Save list",
                          style: TextStyle(fontSize: 22, color: Colors.white)),
                      onPressed: () => {
                            _userService.getJWTAsString().then(
                                (value) => apiService.setGroceryLists(value!))
                          }),
                ),
                SizedBox(
                  height: 10,
                ),
                Container(
                  height: 50,
                  width: 200,
                  child: TextButton(
                    child: Text("Load list",
                        style: TextStyle(fontSize: 22, color: Colors.white)),
                    onPressed: () => {
                      ArsDialog(
                          title: Text("Saved lists:"),
                          content: FutureBuilder(
                              future: _userService.getJWTAsString().then(
                                  (value) =>
                                      apiService.getGroceryLists(value!)),
                              builder: (BuildContext context, snapshot) {
                                if (snapshot.hasError) {
                                  debugPrint("snapshot error: " +
                                      snapshot.error.toString());
                                }

                                if (snapshot.hasData) {
                                  List<String> lists =
                                      snapshot.data as List<String>;

                                  return Container(
                                    height: 200,
                                    child: Column(
                                      children: [
                                        (lists.map((e) => TextButton(
                                            onPressed: () => {},
                                            child: Text("List 1"))) as Widget)
                                      ],
                                    ),
                                  );
                                } else {
                                  return Container(
                                    height: 200,
                                    child: Column(
                                      children: [
                                        TextButton(
                                            onPressed: () => {},
                                            child: SizedBox(
                                              height: 10,
                                            )),
                                      ],
                                    ),
                                  );
                                }
                              })).show(context,
                          transitionType: DialogTransitionType.Bubble)
                    },
                  ),
                ),
                SizedBox(height: 100),
                Expanded(child: Container()),
                Container(
                  height: 50,
                  width: 200,
                  child: TextButton(
                    child: Text("Clear Cart",
                        style: TextStyle(fontSize: 22, color: Colors.white)),
                    onPressed: () =>
                        Provider.of<CartProvider>(context, listen: false)
                            .clearItems(),
                  ),
                ),
                SizedBox(height: 110),
              ],
            ),
          ),
          sliderMain: buildCartPage(context)),
    );
  }
}

Widget buildCartPage(BuildContext context) {
  return Scaffold(
    backgroundColor: Colors.white54,
    body: Padding(
      padding: const EdgeInsets.only(left: 10, right: 10, bottom: 5),
      child: SafeArea(
        child: Column(
          children: [
            SizedBox(height: 5),
            Container(height: 5, color: Colors.deepOrange),
            SizedBox(height: 25),
            Row(mainAxisAlignment: MainAxisAlignment.spaceBetween, children: [
              Bounce(
                duration: Duration(milliseconds: 220),
                onPressed: () {},
                child: Icon(
                  Icons.shopping_cart_outlined,
                  size: 65,
                  color: Colors.deepOrange,
                ),
              ),
              Icon(Icons.arrow_back_rounded, size: 35)
            ]),
            SizedBox(height: 25),
            Expanded(child: Container(child: buildCartItems(context))),
            SizedBox(height: 25),
            Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              children: [
                Text(
                  'Total',
                  style: TextStyle(color: Colors.black, fontSize: 18),
                ),
                Text(
                  'R' +
                      Provider.of<CartProvider>(context)
                          .total
                          .toStringAsFixed(2),
                  style: TextStyle(
                    color: Colors.black,
                    fontSize: 18,
                    fontWeight: FontWeight.bold,
                  ),
                )
              ],
            ),
            SizedBox(height: 25),
            Container(
                height: 45,
                width: double.infinity,
                child: buildCheckoutButton(context)),
            SizedBox(height: 80)
          ],
        ),
      ),
    ),
  );
}

Widget buildCartItems(BuildContext context) {
  final provider = Provider.of<CartProvider>(context);

  if (provider.items.isEmpty) {
    return Center(
        child: Text(
      'Your cart is empty..',
      style: TextStyle(
        color: Colors.black,
        fontSize: 20,
      ),
    ));
  } else
    return ListView(
      children: provider.items.values
          .map(
            (i) => buildCartItem(i, context),
          )
          .toList(),
    );
}

Widget buildCartItem(CartItem cartItem, BuildContext context) {
  final provider = Provider.of<CartProvider>(context);

  return ListTile(
    leading: ElevatedButton(
      onPressed: () {
        provider.decrementItem(cartItem);
      },
      child: Icon(Icons.remove_circle, color: Colors.deepOrange),
      style: ElevatedButton.styleFrom(
        shape: CircleBorder(),
        padding: EdgeInsets.all(20),
        primary: Colors.white, // <-- Button color
        onPrimary: Colors.deepOrange, // <-- Splash color
      ),
    ),
    title: Row(mainAxisAlignment: MainAxisAlignment.spaceBetween, children: [
      Expanded(
          child: Text(
        cartItem.title,
        style: TextStyle(
          color: Colors.black,
          fontSize: 20,
        ),
        overflow: TextOverflow.ellipsis,
        maxLines: 1,
      )),
      Text(
        'x${cartItem.quantity}',
        style: TextStyle(
          color: Colors.black,
          fontSize: 20,
          fontWeight: FontWeight.bold,
        ),
      )
    ]),
    trailing: Text(
      'R${cartItem.price}',
      style: TextStyle(
        color: Colors.black,
        fontSize: 20,
      ),
    ),
  );
}

Widget buildCheckoutButton(BuildContext context) {
  if (!Provider.of<CartProvider>(context).items.isEmpty) {
    return TextButton(
      onPressed: () => checkout(context),
      child: Text(
        'Checkout',
        style: TextStyle(
            color: Colors.white, fontWeight: FontWeight.bold, fontSize: 18),
      ),
      style: ButtonStyle(
          backgroundColor: MaterialStateProperty.all(Colors.deepOrangeAccent)),
    );
  } else {
    return TextButton(
      onPressed: () => {
        Alert(
          context: context,
          title: "Error:",
          desc: "Your cart is empty!",
          buttons: [
            DialogButton(
              color: Colors.deepOrange,
              child: Text(
                "OK",
                style: TextStyle(color: Colors.white, fontSize: 20),
              ),
              onPressed: () => Navigator.pop(context),
              width: 120,
            )
          ],
        ).show()
      },
      child: Text(
        'Checkout',
        style: TextStyle(
          color: Colors.white,
          fontWeight: FontWeight.bold,
          fontSize: 18,
        ),
      ),
      style: ButtonStyle(
          backgroundColor: MaterialStateProperty.all(Colors.deepOrange)),
    );
  }
}

void checkout(BuildContext context) {
  if (!Provider.of<WalletProvider>(context, listen: false).present) {
    Alert(
      context: context,
      type: AlertType.warning,
      title: "Error:",
      desc: "You have not set a payment method. ",
      buttons: [
        DialogButton(
          child: Text(
            "ADD CARD",
            style: TextStyle(color: Colors.white, fontSize: 18),
          ),
          onPressed: () => {
            Navigator.pop(context),
            Navigator.of(context).push(MaterialPageRoute(
                builder: (BuildContext context) => WalletPage()))
          },
          color: Color.fromRGBO(0, 179, 134, 1.0),
        ),
        DialogButton(
          child: Text(
            "BACK",
            style: TextStyle(color: Colors.white, fontSize: 18),
          ),
          onPressed: () => Navigator.pop(context),
          gradient: LinearGradient(colors: [
            Color.fromRGBO(116, 116, 191, 1.0),
            Color.fromRGBO(52, 138, 199, 1.0),
          ]),
        )
      ],
    ).show();
  } else if (Provider.of<CartProvider>(context, listen: false).activeOrder) {
    Alert(
      context: context,
      title: "Error:",
      desc: "You already have an active order!",
      buttons: [
        DialogButton(
          color: Colors.deepOrange,
          child: Text(
            "OK",
            style: TextStyle(color: Colors.white, fontSize: 20),
          ),
          onPressed: () => Navigator.pop(context),
          width: 120,
        )
      ],
    ).show();
  } else {
    ApiService api = ApiService();
    api.submitOrder(
        Provider.of<CartProvider>(context, listen: false).items.values.toList(),
        context);
  }
  // Provider.of<StatusProvider>(context, listen: false)
  //     .startListeningStatus("orderID", context);
}
