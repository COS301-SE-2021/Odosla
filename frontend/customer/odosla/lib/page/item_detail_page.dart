import 'package:flutter/material.dart';
import 'package:odosla/model/cart_item.dart';
import 'package:odosla/model/store.dart';
import 'package:odosla/provider/cart_provider.dart';
import 'package:odosla/services/api_service.dart';
import 'package:provider/provider.dart';
import 'package:rflutter_alert/rflutter_alert.dart';

class ItemDetailPage extends StatefulWidget {
  final CartItem item;
  final String storeID;
  final Map<String, double> location;

  const ItemDetailPage(this.item, this.storeID, this.location
      //Key key,
      ); // : super(key: key);

  @override
  _ItemDetailPage createState() => _ItemDetailPage(storeID, location);
}

class _ItemDetailPage extends State<ItemDetailPage> {
  int _count = 1;

  Widget _popUpPriceCheck(BuildContext context) {
    ApiService apiService = ApiService();
    return new AlertDialog(
      title: Column(
        children: [
          // Container(
          //     child: IconButton(onPressed: (){}, icon: Icon(Icons.cancel)),
          //   alignment: Alignment.topRight,
          // ),
          const Text(
            'PRICE CHECK',
            textAlign: TextAlign.center,
          ),
        ],
      ),
      content: new Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Text("Finding a cheaper price for " + widget.item.title,
              textAlign: TextAlign.center),
        ],
      ),
      actionsPadding: EdgeInsets.only(right: 30),
      actions: <Widget>[
        new FlatButton(
          onPressed: () async {
            List<CartItem> items = <CartItem>[];
            items.add(widget.item);
            await apiService.priceCheck(items, context).then((value) async => {
                  if (value != null && value.price != null)
                    {
                      await apiService
                          .getStoreByUUID(value.storeID, context)
                          .then((val) => {
                                Navigator.pop(context, false),
                                showDialog(
                                  context: context,
                                  builder: (BuildContext context) =>
                                      _popUpPriceCheckResults(context, true,
                                          val!, value.price, value),
                                )
                              })
                    }
                  else
                    {
                      Navigator.pop(context, false),
                      showDialog(
                        context: context,
                        builder: (BuildContext context) =>
                            _popUpPriceCheckResults(
                                context,
                                false,
                                Store("", "", 0, 0, false, "", 0, 0),
                                0,
                                widget.item),
                      )
                    }
                });
          },
          child: const Text('PRICE CHECK'),
        ),
        new FlatButton(
          onPressed: () async {
            Navigator.pop(context, false);
          },
          child: Icon(
            Icons.cancel_rounded,
            color: Colors.red,
          ),
        )
      ],
    );
  }

  Widget _popUpPriceCheckResults(BuildContext context, bool found, Store store,
      double cheaperPrice, CartItem item) {
    ApiService apiService = ApiService();
    return new AlertDialog(
      title: Column(
        children: [
          Text(
            found ? 'FOUND A CHEAPER PRICE' : 'NO RESULTS FOUND',
            textAlign: TextAlign.center,
          ),
        ],
      ),
      content: new Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Text(
              found
                  ? ('The cheapest price for this product is available at ' +
                      store.name)
                  : 'The cheapest price for this product is at current store',
              textAlign: TextAlign.center),
          found
              ? Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text("\nProduct: "),
                    Text("\n" + widget.item.title)
                  ],
                )
              : Container(),
          found
              ? Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text("Current price: "),
                    Text("R" + widget.item.price.toString()),
                  ],
                )
              : Container(),
          found
              ? Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    Text(store.name + "'s price: "),
                    Text("R" + cheaperPrice.toString()),
                  ],
                )
              : Container(),
        ],
      ),
      actionsPadding: EdgeInsets.only(right: 30),
      actions: <Widget>[
        found
            ? new FlatButton(
                onPressed: () async {
                  Navigator.of(context).push(MaterialPageRoute(
                      builder: (BuildContext context) => ItemDetailPage(item,
                          store.id, {"lat": store.lat, "long": store.long})));
                },
                child: const Text('SEE ITEM'),
              )
            : Container(),
        new FlatButton(
          onPressed: () async {
            Navigator.pop(context, false);
          },
          child: Icon(
            Icons.cancel_rounded,
            color: Colors.red,
          ),
        )
      ],
    );
  }

  final String storeID;
  final Map<String, double> location;

  _ItemDetailPage(this.storeID, this.location);

  void add() {
    if (_count < 9) {
      setState(() {
        _count++;
      });
    }
  }

  void sub() {
    if (_count > 1) {
      setState(() {
        _count--;
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      body: SafeArea(
        child: Stack(
          children: [
            Padding(
              padding: const EdgeInsets.all(12),
              child: buildProduct(context),
            ),
            Positioned(
                top: 5,
                left: 5,
                child: IconButton(
                  icon: Icon(Icons.arrow_back_ios, color: Colors.black),
                  onPressed: () => Navigator.pop(context),
                )),
            Positioned(
                top: 19,
                right: 41,
                child: GestureDetector(
                  onTap: () {
                    showDialog(
                      context: context,
                      builder: (BuildContext context) =>
                          _popUpPriceCheck(context),
                    );
                  },
                  child: Container(
                      alignment: Alignment.center,
                      child: Text(
                        "Price Check",
                        style: TextStyle(
                            fontWeight: FontWeight.w600,
                            decoration: (TextDecoration.underline)),
                      )),
                )),
            Positioned(
              top: 2,
              right: 3,
              child: IconButton(
                icon: Icon(
                  Icons.monetization_on_rounded,
                  color: Colors.black,
                  size: 30,
                ),
                onPressed: () => Navigator.pop(context),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget buildProduct(BuildContext context) {
    return Column(
      children: [
        SizedBox(
          height: 20,
        ),
        Text(
          widget.item.title,
          style: TextStyle(fontWeight: FontWeight.bold, fontSize: 32),
        ),
        SizedBox(
          height: 4,
        ),
        Text(
          widget.item.size, //item.size,
          style: TextStyle(
              color: Colors.black54, fontStyle: FontStyle.italic, fontSize: 18),
        ),
        Expanded(
          child: Container(
            margin: EdgeInsets.symmetric(vertical: 30),
            child: Image.asset(
              'assets/' + widget.item.imgUrl,
              fit: BoxFit.cover,
            ),
          ),
        ),
        createDetails(),
        SizedBox(height: 20),
        Container(
            margin: EdgeInsets.symmetric(horizontal: 10),
            child: Row(mainAxisAlignment: MainAxisAlignment.center, children: [
              Text(
                'R' + widget.item.price.toStringAsFixed(2),
                style: TextStyle(fontWeight: FontWeight.bold, fontSize: 28),
              ),
              SizedBox(
                width: 35,
              ),
              createQuantity(),
            ])),
        SizedBox(
          height: 30,
        ),
        Container(
            height: 50,
            width: 250,
            child: ElevatedButton(
                onPressed: () => {
                      // if (storeID ==
                      //         Provider.of<CartProvider>(context, listen: false)
                      //             .store ||
                      //     Provider.of<CartProvider>(context, listen: false)
                      //             .items
                      //             .length ==
                      //         0 ||
                      //     Provider.of<CartProvider>(context, listen: false)
                      //             .store ==
                      //         "")
                      if (!Provider.of<CartProvider>(context, listen: false)
                          .atMax())
                        {
                          debugPrint("!!"),
                          Provider.of<CartProvider>(context, listen: false)
                              .addItem(widget.item, _count),
                          Provider.of<CartProvider>(context, listen: false)
                              .store = storeID,
                          Provider.of<CartProvider>(context, listen: false)
                              .activeStoreLocation = location,
                          debugPrint("COUNT: " + _count.toString()),
                          Navigator.pop(context),
                        }
                      else
                        {
                          Alert(
                            context: context,
                            title: "Error:",
                            desc:
                                "You can only order items from a maximum of three stores at a time, please review your cart.",
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
                          ).show()
                        }
                    },
                child: Text(
                  "Add to Cart",
                  style: TextStyle(
                      color: Colors.white,
                      fontWeight: FontWeight.bold,
                      fontSize: 18),
                ),
                style: ButtonStyle(
                    backgroundColor:
                        MaterialStateProperty.all(Colors.deepOrange),
                    shape: MaterialStateProperty.all<RoundedRectangleBorder>(
                        RoundedRectangleBorder(
                            borderRadius: BorderRadius.circular(18.0),
                            side: BorderSide(color: Colors.orange)))))),
        SizedBox(
          height: 30,
        )
      ],
    );
  }

  Widget createDetails() => Padding(
        padding: EdgeInsets.symmetric(horizontal: 10),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            SizedBox(
              height: 12,
            ),
            Text(
              "About",
              style: TextStyle(fontWeight: FontWeight.bold, fontSize: 22),
            ),
            SizedBox(
              height: 8,
            ),
            Column(
                mainAxisAlignment: MainAxisAlignment.start,
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    "Brand: " + widget.item.brand,
                    style: TextStyle(fontSize: 17),
                  ),
                  Text(
                    widget.item.description, //item.description,
                    style: TextStyle(fontSize: 17),
                  ),
                ]),
            SizedBox(
              height: 30,
            )
          ],
        ),
      );

  Widget createQuantity() {
    return Container(
        decoration: BoxDecoration(
            borderRadius: BorderRadius.all(Radius.circular(4)),
            border: Border.all(color: Colors.black54)),
        child: Row(
          children: [
            MaterialButton(
                onPressed: () => sub(),
                minWidth: 50,
                child: Text('-',
                    style:
                        TextStyle(fontWeight: FontWeight.bold, fontSize: 26))),
            Text(_count.toString(),
                style: TextStyle(fontWeight: FontWeight.bold, fontSize: 28)),
            MaterialButton(
                onPressed: () => add(),
                minWidth: 50,
                child: Text('+',
                    style:
                        TextStyle(fontWeight: FontWeight.bold, fontSize: 26))),
          ],
        ));
  }
}
