import 'package:flutter/material.dart';
import 'package:flutter_employee_app/models/Order.dart';
import 'package:flutter_employee_app/models/Store.dart';
import 'package:flutter_employee_app/pages/shopper/shopper_main_page.dart';
import 'package:flutter_employee_app/provider/shop_provider.dart';
import 'package:flutter_employee_app/services/ShoppingService.dart';
import 'package:get_it/get_it.dart';
import 'package:line_awesome_flutter/line_awesome_flutter.dart';
import 'package:provider/provider.dart';

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

  Widget buildOrders() {
    final double spacing = 12;
    return FutureBuilder(
      future: _shoppingService.getAllOrdersInQueue(context, widget.store.id),
      builder: (BuildContext context, snapshot) {
        if (snapshot.hasData) {
          List<Order> items = snapshot.data as List<Order>;
          return ListView.builder(
              itemCount: items.length,
              itemBuilder: (context, index) => Container(
                child: customMenuItem(items[index].totalCost,items[index].items.length,items[index].createdDate),
              ));
        }
        return Center(
            child: CircularProgressIndicator(),
          );
      },
    );
  }

  Widget _getNextOrderBTN() {
    return Container(
      padding: EdgeInsets.symmetric(vertical: 15.0, horizontal: 10),
      width: double.infinity,
      child: RaisedButton(
        elevation: 5.0,
        onPressed: () {
          Store store=Provider.of<ShopProvider>(context,listen: false).store;
          _shoppingService.getNextQueued(store.id,context).then((value) =>
          {
            if(value==null){
              ScaffoldMessenger.of(context)
                  .showSnackBar(SnackBar(content: Text(
                  "Could not retrieve order")))
            } else{
              Navigator.of(context).push(MaterialPageRoute(
                  builder: (BuildContext context) => CurrentOrderScreen(context,store: widget.store)))
            }
          }
          );
        },
        padding: EdgeInsets.all(10.0),
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(30.0),
        ),
        color: Colors.white,
        child: Text(
          'Get Next Order',
          style: TextStyle(
            color: Colors.deepOrange,
            letterSpacing: 1.5,
            fontSize: 22.0,
            fontWeight: FontWeight.w900,
            fontFamily: 'OpenSans',
          ),
        ),
      ),
    );
  }

  Widget customMenuItem(var price, var itemsLength, var createdDate) {
    counter++;
    return Padding(
      padding: const EdgeInsets.symmetric(horizontal: 40.0, vertical: 8.0),
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
                            createdDate,
                            //createdDate.substring(0, createdDate.indexOf('CAT')),
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
      body: Container(
        height: MediaQuery.of(context).size.height,
        padding: EdgeInsets.symmetric(horizontal: 0, vertical: 2),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: [
            Column(
              children: [
                Stack(
                  children: [
                    ConstrainedBox(
                        constraints: new BoxConstraints(
                            minHeight: MediaQuery.of(context).size.height*0.2,
                            maxHeight: MediaQuery.of(context).size.height*0.3,
                            maxWidth: double.infinity,
                            minWidth: double.infinity
                        ),
                        child: Image(fit: BoxFit.fitWidth,
                          image:AssetImage(
                            "assets/"+widget.store.imageUrl,
                          ),
                        )

                    ),
                    Column(
                      children: [
                        SizedBox(height:MediaQuery.of(context).size.height*0.013),
                        Container(
                            height: 30,
                            child: GestureDetector(
                              onTap: (){
                                Navigator.of(context).push(MaterialPageRoute(
                                    builder: (BuildContext context) => ShopperHomeScreen(0)));
                              },
                              child: Icon(
                                Icons.chevron_left,
                                color: Colors.deepOrangeAccent,
                                size: 35,
                              ),
                            )
                        ),
                      ],
                    ),
                  ],
                ),
                Container(
                  color: Colors.deepOrangeAccent,
                  width: double.infinity,
                  height: MediaQuery.of(context).size.height*0.06,
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Text("Current Orders ",style: TextStyle(fontWeight: FontWeight.w700,fontSize: 27, color: Colors.white),textAlign: TextAlign.center,),

                    ],
                  ),

                ),
                Container(
                    height: MediaQuery.of(context).size.height*0.4,
                    child: Column(
                      children: [
                        Container(  height: MediaQuery.of(context).size.height*0.4, child: buildOrders()),
                      ],
                    )),
              ],
            ),

            Column(
              mainAxisAlignment: MainAxisAlignment.end,
              children: [

                _getNextOrderBTN(),
              ],
            )
          ],
        ),
      )
    );
  }
}