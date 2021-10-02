import 'package:flutter/material.dart';
import 'package:flutter_employee_app/models/cart_item.dart';

class ItemDetailPage extends StatefulWidget {
  final CartItem item;
  final String storeID;
  final Map<String, double> location;
  final bool _isAlternative;

  const ItemDetailPage(this.item, this.storeID, this.location,
      this._isAlternative); // : super(key: key);

  @override
  _ItemDetailPage createState() => _ItemDetailPage(storeID, location);
}

class _ItemDetailPage extends State<ItemDetailPage> {
  int _count = 1;

  final String storeID;
  final Map<String, double> location;

  _ItemDetailPage(this.storeID, this.location);

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
                ))
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
        Text(
          widget.item.soldOut ? "SOLD OUT" : "IN STOCK", //item.size,
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
        Container(
            margin: EdgeInsets.symmetric(horizontal: 10),
            child: Row(mainAxisAlignment: MainAxisAlignment.center, children: [
              Text(
                'Barcode: ' + widget.item.barcode,
                style: TextStyle(fontWeight: FontWeight.bold, fontSize: 25),
              ),
              SizedBox(
                width: 35,
              ),
            ])),
        createDetails(),
        SizedBox(height: 20),
        Container(
            margin: EdgeInsets.symmetric(horizontal: 10),
            child: Row(mainAxisAlignment: MainAxisAlignment.center, children: [
              Text(
                'R' + widget.item.price.toStringAsFixed(2),
                style: TextStyle(fontWeight: FontWeight.bold, fontSize: 25),
              ),
              SizedBox(
                width: 35,
              ),
            ])),
        Container(
          child: Text("Update details of item to:"),
        ),
        SizedBox(
          height: 20,
        ),
        Container(
            height: 50,
            width: 250,
            child: ElevatedButton(
                onPressed: () => {
                      if (widget._isAlternative)
                        {}
                      else if (widget.item.soldOut == true)
                        {}
                      else if (widget.item.soldOut == false)
                        {}
                    },
                child: Text(
                  widget._isAlternative
                      ? "Offer alternative"
                      : widget.item.soldOut
                          ? "In Stock"
                          : "Out of stock",
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
}
