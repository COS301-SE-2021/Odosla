import 'package:flutter/material.dart';
import 'package:flutter_employee_app/models/Order.dart';
import 'package:flutter_employee_app/pages/shopper/shopper_main_page.dart';
import 'package:flutter_employee_app/provider/order_provider.dart';
import 'package:flutter_employee_app/services/UserService.dart';
import 'package:flutter_employee_app/utilities/my_navigator.dart';
import 'package:get_it/get_it.dart';
import 'package:provider/provider.dart';

class CurrentOrderScreen extends StatefulWidget {
  @override
  _CurrentOrderScreenState createState() => _CurrentOrderScreenState();
}

class _CurrentOrderScreenState extends State<CurrentOrderScreen> {

  var currentAmountPacked=<int>[];

  UserService _userService=GetIt.I.get();
  var completePackagingForItem=<bool>[];

  int counter=0;

  bool allItemsPacked=false;

  Widget _completePackingBTN() {
    return Container(
      padding: EdgeInsets.symmetric(vertical: 25.0),
      width: double.infinity,
      child: RaisedButton(
        elevation: 5.0,
        onPressed: () {
          Order order=Provider.of<OrderProvider>(context,listen: false).order;
          _userService.completePackagingOrder(order.orderID,context).then((value) =>
          {
            if(value==false){
              ScaffoldMessenger.of(context)
                  .showSnackBar(SnackBar(content: Text(
                  "Could send request")))
            } else{
              Navigator.of(context).push(MaterialPageRoute(
                  builder: (BuildContext context) => ShopperHomeScreen()))
            }
          }
          );
        },
        padding: EdgeInsets.all(15.0),
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(30.0),
        ),
        color: Colors.white,
        child: Text(
          'COMPLETE PACKAGING',
          style: TextStyle(
            color: Color(0xFFE9884A),
            letterSpacing: 1.5,
            fontSize: 18.0,
            fontWeight: FontWeight.bold,
            fontFamily: 'OpenSans',
          ),
        ),
      ),
    );
  }

  Widget customMenuItem(var img, var text, var price, var brand, var quantity, var barcode, int currentAmount) {
    bool completed=false;
    setState(() {
      counter=counter+1;
      if(quantity==currentAmount){
        completePackagingForItem[counter-1]=true;
        completed=true;
      }
    });

    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: <Widget>[
          GestureDetector(
          onTap: () {
            if(completed){
              ScaffoldMessenger.of(context)
                  .showSnackBar(SnackBar(content: Text(
                  "Already finished packing for item")));
            }else {
              MyNavigator.goToBarcodeScanPage(context);
            }
          },
            child: Container(
              child: Row(
                children: <Widget>[
                  Container(
                    height: 75.0,
                    width: 75.0,
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(12.0),
                      color: Color(0xffFFE4E0),
                    ),
                    child:  Padding(
                        padding: const EdgeInsets.all(8.0),
                        child: Image(fit: BoxFit.cover, image: AssetImage("assets/"+img)),
                      ),
                    ),
                  SizedBox(
                    width: 20.0,
                  ),
                  Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: <Widget>[
                      Padding(
                        padding: const EdgeInsets.all(3.0),
                        child: Container(
                          alignment: Alignment.centerLeft,
                          child: Expanded(
                            child: Text(
                              text,
                              overflow: TextOverflow.ellipsis,
                              maxLines: 2,
                              softWrap: false,
                              style: TextStyle(
                                fontWeight: FontWeight.w600,
                                fontSize: 13.0,
                                letterSpacing: 0.75,
                              ),
                            ),
                          ),
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.all(6.0),
                        child: Row(
                          children: <Widget>[
                            Text(
                              brand.toString(),
                              style: TextStyle(
                                color: Colors.orangeAccent,
                                fontWeight: FontWeight.w500,
                                fontSize: 10.0,
                              ),
                            ),
                            SizedBox(
                              width: 12.0,
                            ),
                          ],
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.all(6.0),
                        child: Row(
                          children: <Widget>[
                            Text(
                              "Price",
                              style: TextStyle(
                                color: Colors.orangeAccent,
                                fontWeight: FontWeight.w500,
                                fontSize: 16.0,
                              ),
                            ),
                            SizedBox(
                              width: 12.0,
                            ),
                            Text(
                              "R "+price.toString(),
                              style: TextStyle(
                                color: Colors.grey,
                                fontWeight: FontWeight.w500,
                                fontSize: 12.0,
                              ),
                            ),
                          ],
                        ),
                      ),
                      Padding(
                        padding: const EdgeInsets.all(6.0),
                        child: Row(
                          children: <Widget>[
                            Text(
                              "Quantity",
                              style: TextStyle(
                                color: Colors.orangeAccent,
                                fontWeight: FontWeight.w500,
                                fontSize: 16.0,
                              ),
                            ),
                            SizedBox(
                              width: 12.0,
                            ),
                            Text(
                              "$quantity",
                              style: TextStyle(
                                color: Colors.grey,
                                fontWeight: FontWeight.w500,
                                fontSize: 12.0,
                              ),
                            ),
                          ],
                        ),
                      ),

                    ],
                  ),
                ],
              ),
            ),
          ),
        ],
      ),
    );
  @override
  Widget build(BuildContext context) {
    // TODO: implement build
    throw UnimplementedError();
  }
  
}

  @override
  Widget build(BuildContext context) {

    Order order=Provider.of<OrderProvider>(context).order;
    for (var i in order.items) currentAmountPacked.add(0);
    int count = 0;
      return Scaffold(
        body: ListView(
          children: <Widget>[
            Padding(
              padding:
              const EdgeInsets.symmetric(horizontal: 10.0, vertical: 15.0),
              child: Row(
                children: <Widget>[
                  IconButton(icon: Icon(Icons.chevron_left), onPressed: () {
                    MyNavigator.goToShopperHomePage(context);
                  }),
                ],
              ),
            ),
            Padding(
              padding:
              const EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
              child: Text(
                "Order: ",
                style: TextStyle(
                  fontSize: 28.0,
                  fontWeight: FontWeight.bold,
                  letterSpacing: 1.2,
                ),
              ),
            ),
            SizedBox(
              height: 28.0,
            ),
            Container(
              height: 280.0,
              child: Row(
                children: <Widget>[
                  Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 80.0),
                    child: Container(
                      height: 240,
                      width: MediaQuery
                          .of(context)
                          .size
                          .width * 0.50,
                      child: Image(
                        fit: BoxFit.fill,
                        image: AssetImage("assets/grocery.jpg"),
                      ),
                    ),
                  ),
                ],
              ),
            ),
            SizedBox(
              height: 22.0,
            ),
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16.0),
              child: Text(
                "Created date: " + order.createdDate,
                style: TextStyle(
                  fontSize: 10.0,
                  fontWeight: FontWeight.w500,
                  letterSpacing: 0.75,
                ),
              ),
            ),
            SizedBox(
              height: 5.0,
            ),
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16.0),
              child: Text(
                "Total : R " +
                    (order.totalCost).toString(),
                style: TextStyle(
                  fontSize: 20.0,
                  fontWeight: FontWeight.w500,
                  letterSpacing: 0.75,
                ),
              ),
            ),
            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16.0),
              child: Text(
                "Discount : R" + order.discount.toString(),
                style: TextStyle(
                  fontSize: 15.0,
                  fontWeight: FontWeight.w500,
                  letterSpacing: 0.75,
                ),
              ),
            ),
            SizedBox(
              height: 11.0,
            ),

            Padding(
              padding: const EdgeInsets.symmetric(horizontal: 16.0),
              child: Text(
                "ITEMS",
                style: TextStyle(
                  fontSize: 20.0,
                  fontWeight: FontWeight.w500,
                  letterSpacing: 0.75,
                ),
              ),
            ),
            SizedBox(
              height: 22.0,
            ),
            Container(
              child: Column(
                children: [
                  for (var i in order.items) customMenuItem(
                      i.imgUrl,
                      i.name,
                      i.price,
                      i.brand,
                      i.quantity,
                      i.barcode,
                      currentAmountPacked[counter])
                ],
              ),
            ),
            _completePackingBTN(),
          ],
        ),
      );
    }

  }
