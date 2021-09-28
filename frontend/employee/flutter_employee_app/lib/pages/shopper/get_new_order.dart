import 'package:flutter/material.dart';
import 'package:flutter_employee_app/models/Order.dart';
import 'package:flutter_employee_app/models/Store.dart';
import 'package:flutter_employee_app/services/ShoppingService.dart';
import 'package:flutter_employee_app/utilities/my_navigator.dart';
import 'package:get_it/get_it.dart';
import 'package:line_awesome_flutter/line_awesome_flutter.dart';

import 'current_order_page.dart';

class GetNewOrderScreen extends StatefulWidget {
  final Store store;

  const GetNewOrderScreen(BuildContext context, {Key? key, required this.store}) : super(key: key);

  @override
  _GetNewOrderScreenState createState() => _GetNewOrderScreenState();
}

class _GetNewOrderScreenState extends State<GetNewOrderScreen> {

  int counter=0;
  final ShoppingService _shoppingService = GetIt.I.get();
  List<Order> orders=List.empty();
  bool isOrders=false;

  void initState(){
    _shoppingService.getAllOrdersInQueue(widget.store.id).then((value) => {
      if(value!=null){
        setState(() {
          orders = value;
          isOrders=true;

        })
      }
    });

  }

  Widget _menuItems() {
    if(isOrders){
       return Column(
         children: [
           for (var i in orders)customMenuItem(i.totalCost,i.items.length,i.createdDate)
         ],
       );
    }else{
      return Container();
    }
  }

  Widget _completePackingBTN() {
    return Container(
      padding: EdgeInsets.symmetric(vertical: 25.0),
      width: double.infinity,
      child: RaisedButton(
        elevation: 5.0,
        onPressed: () {
          _shoppingService.getNextQueued(widget.store.id,context).then((value) =>
              {
                  if(value==null){
                      ScaffoldMessenger.of(context)
                      .showSnackBar(SnackBar(content: Text(
                      "Could not retrieve order")))
                } else{
                    Navigator.of(context).push(MaterialPageRoute(
                    builder: (BuildContext context) => CurrentOrderScreen()))
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
          'GET NEXT ORDER',
          style: TextStyle(
            color: Colors.deepOrangeAccent,
            letterSpacing: 1.5,
            fontSize: 18.0,
            fontWeight: FontWeight.bold,
            fontFamily: 'OpenSans',
          ),
        ),
      ),
    );
  }
  Widget customMenuItem(var price, var itemsLength, var createdDate) {
    counter++;
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 16.0, vertical: 8.0),
      child: Row(
        mainAxisAlignment: MainAxisAlignment.spaceBetween,
        children: <Widget>[
          Container(
            child: Row(
              children: <Widget>[
                Container(
                  child:  Padding(
                    padding: const EdgeInsets.all(8.0),
                    child: Icon(LineAwesomeIcons.clipboard_with_check,
                    size: 50,),
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
                        child: Text(
                          "Order "+counter.toString(),

                          style: TextStyle(
                            fontWeight: FontWeight.w600,
                            fontSize: 17.0,
                            letterSpacing: 0.75,
                          ),
                        ),

                      ),
                    ),
                    Padding(
                      padding: const EdgeInsets.all(6.0),
                      child: Row(
                        children: <Widget>[
                          Text(
                            "Number of items:"+itemsLength.toString(),
                            style: TextStyle(
                              color: Colors.deepOrangeAccent,
                              fontWeight: FontWeight.w500,
                              fontSize: 12.0,
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
                            "price",
                            style: TextStyle(
                              color: Colors.deepOrangeAccent,
                              fontWeight: FontWeight.w500,
                              fontSize: 12.0,
                            ),
                          ),
                          SizedBox(
                            width: 12.0,
                          ),
                          Text(
                            "R"+price.toString(),
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
                            createdDate.substring(0, createdDate.indexOf('CAT')),
                            style: TextStyle(
                              color: Colors.deepOrangeAccent,
                              fontWeight: FontWeight.w500,
                              fontSize: 12.0,
                            ),
                          ),
                          SizedBox(
                            width: 12.0,
                          ),
                        ],
                      ),
                    ),
                  ],
                ),
              ],
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
    setState(() {
      counter=0;
    });

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
              "Orders",
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
                    width: MediaQuery.of(context).size.width * 0.50,
                    child: Image(
                      fit: BoxFit.fill,
                      image: AssetImage("assets/cart.png"),
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
              "List of stores current orders",
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
          _menuItems(),
          _completePackingBTN(),
        ],
      ),
    );
  }
}