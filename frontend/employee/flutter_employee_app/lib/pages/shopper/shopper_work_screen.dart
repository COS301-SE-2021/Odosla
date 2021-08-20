import 'package:drag_and_drop_gridview/devdrag.dart';
import 'package:flutter/material.dart';
import 'package:flutter_employee_app/models/Order.dart';
import 'package:flutter_employee_app/models/Store.dart';
import 'package:flutter_employee_app/models/User.dart';
import 'package:flutter_employee_app/pages/shopper/shopper_main_page.dart';
import 'package:flutter_employee_app/provider/order_provider.dart';
import 'package:flutter_employee_app/provider/shop_provider.dart';
import 'package:flutter_employee_app/provider/user_provider.dart';
import 'package:flutter_employee_app/services/ShoppingService.dart';
import 'package:flutter_employee_app/services/UserService.dart';
import 'package:flutter_employee_app/utilities/constants.dart';
import 'package:flutter_employee_app/utilities/my_navigator.dart';
import 'package:flutter_employee_app/utilities/profile_list_item.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get_it/get_it.dart';
import 'package:line_awesome_flutter/line_awesome_flutter.dart';
import 'package:provider/provider.dart';

import 'current_order_page.dart';
import 'list_of_stores_screen.dart';

class ShopperWorkScreen extends StatefulWidget {
  @override
  _ShopperWorkScreenState createState() =>  _ShopperWorkScreenState();
}

class  _ShopperWorkScreenState extends State<ShopperWorkScreen> {

  final UserService _userService = GetIt.I.get();
  final ShoppingService _shoppingService = GetIt.I.get();
  String _email="";
  int variableSet = 0;
  double? width;
  double? height;
  bool _isCurrentOrder=false;
  var currentAmountPacked=<int>[];

  var completePackagingForItem=<bool>[];
  FixedExtentScrollController _scrollController =
  FixedExtentScrollController(initialItem: 1);


  int counter=0;

  bool allItemsPacked=false;

  Widget _completePackingBTN()  {
    return Expanded(
      child: Align(
        alignment: Alignment.bottomCenter,
        child: Container(
          padding: EdgeInsets.symmetric(vertical: 15.0),
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
                      builder: (BuildContext context) => ShopperHomeScreen(1)))
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
        ),
      ),
    );
  }

  Widget _getNextOrderBTN() {
    return Expanded(
      child: Align(
        alignment: Alignment.bottomCenter,
        child: Container(
          padding: EdgeInsets.symmetric(vertical: 15.0),
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
              'Get Next Order',
              style: TextStyle(
                color: Color(0xFFE9884A),
                letterSpacing: 1.5,
                fontSize: 18.0,
                fontWeight: FontWeight.bold,
                fontFamily: 'OpenSans',
              ),
            ),
          ),
        ),
      ),
    );
  }

  bool _onShift=false;
  initState(){

    _userService.getCurrentUser(context).then((value) =>
    {
      setState(() {
        _email = value!.email;
        _onShift=value.onShift;
        if(_onShift==null){
          _onShift=false;
        }
      })
    }
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

    return Row(
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
                        padding: EdgeInsets.only(right:15),
                        width: MediaQuery.of(context).size.width*0.7,
                        alignment: Alignment.centerLeft,
                        child: Text(
                          text,
                          overflow: TextOverflow.ellipsis,
                          maxLines: 3,
                          softWrap: false,
                          style: TextStyle(
                            fontWeight: FontWeight.w600,
                            fontSize: 14.0,
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
                            brand.toString(),
                            style: TextStyle(
                              color: Colors.deepOrangeAccent,
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
                              color: Colors.deepOrangeAccent,
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
                              color: Colors.deepOrangeAccent,
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
    );
    @override
    Widget build(BuildContext context) {
      // TODO: implement build
      throw UnimplementedError();
    }

  }

  Widget _currentOrder(order){
    return Column(
        children: [
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
              "Total : R "+(order.totalCost).toString(),
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
        ],

    );
  }

  Widget _notCurrentOrder(){
    return Column(
      children: [
        SizedBox(height:10),
        Container(
          height: 300.0,
          child: Row(
            children: <Widget>[
              Padding(
                padding: const EdgeInsets.symmetric(horizontal: 80.0),
                child: Container(
                  height: 210,
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
        Text(_onShift?"Currently not packing an order\n Start packing order \n by clicking below":"Start a shift to get \n new orders",
          style: TextStyle(
              color: Colors.deepOrangeAccent,
              fontWeight: FontWeight.w600,
            fontSize: 20,
          ),
          textAlign: TextAlign.center,
        )
      ],
    );
  }

  Widget _other(){
    return _isCurrentOrder?_completePackingBTN():_getNextOrderBTN();
  }

  @override
  Widget build(BuildContext context) {
    Order _order=Provider.of<OrderProvider>(context).order;
    User _user=Provider.of<UserProvider>(context).user;
    setState(() {
      _onShift=_user.onShift;
    });


    for (var i in _order.items) currentAmountPacked.add(0);
    if(_order.storeID!=""){
      setState(() {
        _isCurrentOrder=true;
      });
    }else{
      setState(() {
        _isCurrentOrder=false;
      });
    }

    ScreenUtil.init(
        BoxConstraints(
            maxWidth: MediaQuery.of(context).size.width,
            maxHeight: MediaQuery.of(context).size.height),
        designSize: Size(414, 896),
        orientation: Orientation.portrait);
    return Scaffold(
      body: Container(
        child: Column(
          children: [
            SizedBox(height: 35),
            Card(
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(30.0),
                ),
                margin: EdgeInsets.symmetric(horizontal: kSpacingUnit.w*1.7,vertical: kSpacingUnit.w*1),
                clipBehavior: Clip.antiAlias,
                color:Colors.deepOrangeAccent,
                shadowColor: Colors.deepOrangeAccent,
                elevation: 5.0,
                child: Padding(
                    padding: const EdgeInsets.symmetric(horizontal: 5.0,vertical: 20.0),
                    child: Row(
                      children: <Widget>[
                        Expanded(
                            child:Column(
                              children: <Widget>[
                                // Text(
                                //     "STATUS",
                                //     style: kTitleTextStyle.copyWith(
                                //       fontWeight: FontWeight.w500,
                                //       fontSize: kSpacingUnit.w*2,
                                //     )
                                // ),
                                SizedBox(height:kSpacingUnit.w*1.1),
                                Text(
                                  _onShift?"• Currently on shift":"• Currently not on shift",
                                  style: kTitleTextStyle.copyWith(
                                    fontWeight: FontWeight.w500,
                                    fontSize: kSpacingUnit.w*1.7,
                                    color: Colors.white,
                                  ),
                                  textAlign: TextAlign.center,
                                ),
                                SizedBox(height: 5),
                                Text(
                                  _isCurrentOrder?"• Currently packing order":"• Currently not packing \n order",
                                  style: kTitleTextStyle.copyWith(
                                    fontWeight: FontWeight.w500,
                                    fontSize: kSpacingUnit.w*1.7,
                                    color: Colors.white,
                                  ),
                                  textAlign: TextAlign.center,
                                ),
                                SizedBox(height: 8),
                                _onShift?RaisedButton(onPressed: () async {
                                  Store store=Provider.of<ShopProvider>(context,listen: false).store;
                                  await _userService.setShopperShift(false, store.id, context).then((value) =>
                                  {
                                    if(value==true){
                                      Provider.of<ShopProvider>(context,listen: false).store=Store("","",0,0,true,""),
                                      Navigator.of(context).push(MaterialPageRoute(
                                          builder: (BuildContext context) =>
                                              ShopperHomeScreen(1) //ProductPage(product: product),
                                      ))
                                    }
                                    else{
                                      ScaffoldMessenger.of(context)
                                          .showSnackBar(SnackBar(content: Text(
                                          "Couldn't register ending shift")))
                                    }
                                  });
                                },
                                child: Text(
                                  "END SHIFT",
                                ),
                                  color: Theme.of(context).backgroundColor,
                                  shape: RoundedRectangleBorder(
                                    borderRadius: BorderRadius.circular(30.0),
                                  ),
                                  // color: Color(0xFFFA8940),
                                ):RaisedButton(onPressed: () async {
                                  Store store=Provider.of<ShopProvider>(context,listen: false).store;

                                  //fix
                                  await _userService.setShopperShift(false, store.id, context).then((value) =>
                                  {
                                    Navigator.of(context).push(MaterialPageRoute(
                                        builder: (BuildContext context) => ShopperHomeScreen(0)))
                                  });
                                },
                                  child: Text(
                                    "START SHIFT",
                                  ),
                                  color: Theme.of(context).backgroundColor,
                                  shape: RoundedRectangleBorder(
                                    borderRadius: BorderRadius.circular(30.0),
                                  ),
                                  // color: Color(0xFFFA8940),
                                ),
                              ],
                            )
                        ),
                        Expanded(
                            child:Column(
                              children: <Widget>[
                                CircleAvatar(
                                  radius: kSpacingUnit.w * 3,
                                  backgroundImage: AssetImage('assets/profile.jpg'),
                                ),
                                SizedBox(height:kSpacingUnit.w*2),
                                Text(
                                  _email,
                                  style: kTitleTextStyle.copyWith(
                                    fontWeight: FontWeight.w500,
                                    fontSize: kSpacingUnit.w*1,
                                  ),

                                ),
                            SizedBox(height:kSpacingUnit.w*1),
                            Text(
                              "SHOPPER",
                              style: kTitleTextStyle.copyWith(
                                fontWeight: FontWeight.w500,
                                fontSize: kSpacingUnit.w*1,
                              ),
                            )


                              ],
                            )
                        ),

                      ],
                    )
                )
            ),
            Expanded(
              child: ListView(
                children: [
                  _isCurrentOrder?_currentOrder(_order):_notCurrentOrder(),
                  _onShift? _other():Container(),
                ],
              ),
            )

          ],
        ),
      )
    );

  }
}

