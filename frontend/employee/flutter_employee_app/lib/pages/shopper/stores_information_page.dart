import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_employee_app/models/Store.dart';
import 'package:flutter_employee_app/pages/shopper/get_new_order.dart';
import 'package:flutter_employee_app/pages/shopper/out_of_stock_page.dart';
import 'package:flutter_employee_app/pages/shopper/shopper_main_page.dart';
import 'package:flutter_employee_app/provider/shop_provider.dart';
import 'package:flutter_employee_app/services/UserService.dart';
import 'package:flutter_employee_app/utilities/constants.dart';
import 'package:get_it/get_it.dart';
import 'package:provider/provider.dart';

class StoreInfoScreen extends StatefulWidget {
  final Store store;

  const StoreInfoScreen(BuildContext context, {Key? key, required this.store})
      : super(key: key);

  @override
  _StoreInfoScreenState createState() => _StoreInfoScreenState();
}

class _StoreInfoScreenState extends State<StoreInfoScreen> {
  final UserService _userService = GetIt.I.get();
  bool _onShift = false;
  bool sameStore = false;

  initState() {
    _userService.getCurrentUser(context).then((value) => {
          setState(() {
            _onShift = value!.onShift;
            if (_onShift == null) {
              _onShift = false;
            }
          })
        });
  }

  Widget _startShiftBTN() {
    return Align(
      alignment: Alignment.bottomCenter,
      child: Container(
        width: double.infinity,
        padding: EdgeInsets.symmetric(vertical: 25.0, horizontal: 10),
        child: RaisedButton(
          elevation: 5.0,
          onPressed: _onShift == false
              ? () async {
                  await _userService
                      .setShopperShift(true, widget.store.id, context)
                      .then((value) => {
                            if (value == true)
                              {
                                Provider.of<ShopProvider>(context,
                                        listen: false)
                                    .store = widget.store,
                                Navigator.of(context).push(MaterialPageRoute(
                                    builder: (BuildContext context) =>
                                        GetNewOrderScreen(context,
                                            store: widget
                                                .store) //ProductPage(product: product),
                                    ))
                              }
                          });
                }
              : null,
          padding: EdgeInsets.all(22.0),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(40.0),
          ),
          color: Colors.white,
          textColor: Colors.deepOrangeAccent,
          disabledColor: Color(0xA65A5959),
          disabledElevation: 0.0,
          disabledTextColor: Colors.white,
          child: Text(
            'START SHIFT',
            style: TextStyle(
              // color: _onShift?Color(0xFF3E3D3D):Color(0xFFE9884A),
              letterSpacing: 1.5,
              fontSize: 18.0,
              fontWeight: FontWeight.bold,
              fontFamily: 'OpenSans',
            ),
          ),
        ),
      ),
    );
  }

  Widget _endShiftBTN() {
    return Column(
      children: [
        Padding(
          padding: const EdgeInsets.symmetric(horizontal: 40.0),
          child: Row(
            children: [
              Expanded(
                child: RaisedButton(
                  elevation: 15.0,
                  onPressed: () async {
                    Provider.of<ShopProvider>(context, listen: false).store =
                        widget.store;
                    Navigator.of(context).push(MaterialPageRoute(
                        builder: (BuildContext context) => GetNewOrderScreen(
                            context,
                            store:
                                widget.store) //ProductPage(product: product),
                        ));
                  },
                  padding: EdgeInsets.all(5.0),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(10.0),
                  ),
                  color: Colors.deepOrangeAccent,
                  textColor: Color(0xFFCB4E2C),
                  child: Text(
                    'Current Orders',
                    style: TextStyle(
                      color: Colors.white,
                      letterSpacing: 1.5,
                      fontSize: 15.0,
                      fontWeight: FontWeight.bold,
                      fontFamily: 'OpenSans',
                    ),
                  ),
                ),
              ),
              SizedBox(
                width: 12,
              ),
              Expanded(
                child: RaisedButton(
                  elevation: 15.0,
                  onPressed: () async {
                    Provider.of<ShopProvider>(context, listen: false).store =
                        widget.store;
                    Navigator.of(context).push(MaterialPageRoute(
                        builder: (BuildContext context) =>
                            ItemsPage(widget.store.id, widget.store.name, {
                              'lat': double.parse(
                                  widget.store.storeLocationLatitude),
                              'long': double.parse(
                                  widget.store.storeLocationLongitude)
                            }) //ProductPage(product: product),
                        ));
                  },
                  padding: EdgeInsets.symmetric(vertical: 5.0),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(10.0),
                  ),
                  color: Colors.deepOrangeAccent,
                  textColor: Color(0xFFCB4E2C),
                  child: Text(
                    'Out of Stock',
                    style: TextStyle(
                      color: Colors.white,
                      letterSpacing: 1.5,
                      fontSize: 15.0,
                      fontWeight: FontWeight.bold,
                      fontFamily: 'OpenSans',
                    ),
                  ),
                ),
              ),
            ],
          ),
        ),
        Container(
          width: double.infinity,
          padding: EdgeInsets.symmetric(vertical: 5.0, horizontal: 40),
          child: RaisedButton(
            elevation: 15.0,
            onPressed: () async {
              await _userService
                  .setShopperShift(false, widget.store.id, context)
                  .then((value) => {
                        if (value == true)
                          {
                            Provider.of<ShopProvider>(context, listen: false)
                                    .store ==
                                Store("", "", 0, 0, true, "", "", "", ""),
                            Navigator.of(context).push(MaterialPageRoute(
                                builder: (BuildContext context) =>
                                    ShopperHomeScreen(
                                        1) //ProductPage(product: product),
                                ))
                          }
                      });
            },
            padding: EdgeInsets.all(22.0),
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(40.0),
            ),
            color: Colors.white,
            textColor: Color(0xFFCB4E2C),
            child: Text(
              'END SHIFT',
              style: TextStyle(
                // color: _onShift?Color(0xFF3E3D3D):Color(0xFFE9884A),
                letterSpacing: 1.5,
                fontSize: 18.0,
                fontWeight: FontWeight.bold,
                fontFamily: 'OpenSans',
              ),
            ),
          ),
        ),
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    bool _sameStore = false;
    Store store = Provider.of<ShopProvider>(context).store;
    if (store.id == widget.store.id) {
      if (_onShift == true) {
        setState(() {
          _sameStore = true;
        });
      }
    }
    return Scaffold(
      appBar: AppBar(
        title: Text(
          widget.store.name,
          style: TextStyle(),
        ),
        centerTitle: true,
        toolbarHeight: 32,
        backgroundColor: Colors.transparent,
        elevation: 0,
        leading: IconButton(
          icon: Icon(Icons.arrow_back_ios),
          onPressed: () => Navigator.pop(context),
        ),
      ),
      body: ListView(children: [
        Container(
          height: MediaQuery.of(context).size.height * 0.83,
          child: Column(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Column(
                children: [
                  Stack(
                    children: [
                      ConstrainedBox(
                          constraints: new BoxConstraints(
                              minHeight:
                                  MediaQuery.of(context).size.height * 0.2,
                              maxHeight:
                                  MediaQuery.of(context).size.height * 0.3,
                              maxWidth: double.infinity,
                              minWidth: double.infinity),
                          child: Image(
                            fit: BoxFit.fitWidth,
                            image: AssetImage(
                              "assets/" + widget.store.imageUrl,
                            ),
                          )),
                      // Padding(
                      //   padding:
                      //   const EdgeInsets.symmetric(horizontal: 0.0, vertical: 5.0),
                      //   child: Row(
                      //     children: <Widget>[
                      //       IconButton(icon: Icon(Icons.chevron_left), color: Colors.deepOrangeAccent, iconSize: 40, onPressed: () {
                      //         MyNavigator.goToShopperHomePage(context);
                      //       }),
                      //     ],
                      //   ),
                      // ),
                    ],
                  ),
                  Container(
                    color: Colors.deepOrangeAccent,
                    width: double.infinity,
                    height: MediaQuery.of(context).size.height * 0.07,
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Text(
                          "Store Information ",
                          style: TextStyle(
                              fontWeight: FontWeight.w700,
                              fontSize: 27,
                              color: Colors.white),
                          textAlign: TextAlign.center,
                        ),
                      ],
                    ),
                  ),
                  SizedBox(
                    height: MediaQuery.of(context).size.height * 0.02,
                  ),
                  Column(
                    children: [
                      Container(
                          child: Padding(
                        padding: const EdgeInsets.symmetric(
                            vertical: 0, horizontal: 16),
                      )),
                      SizedBox(height: 15),
                      Container(
                          child: Padding(
                        padding: const EdgeInsets.symmetric(
                            vertical: 0, horizontal: 16),
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Container(
                              child: Text(
                                "Brand: ",
                                style: kTitleTextStyle.copyWith(
                                    fontWeight: FontWeight.w400, fontSize: 17),
                              ),
                            ),
                            Container(
                              child: Expanded(
                                child: Text(
                                  widget.store.name,
                                  style: kTitleTextStyle.copyWith(
                                      fontWeight: FontWeight.w700,
                                      fontSize: 19),
                                  textAlign: TextAlign.right,
                                  overflow: TextOverflow.ellipsis,
                                ),
                              ),
                            ),
                          ],
                        ),
                      )),
                      SizedBox(height: 15),
                      Container(
                          child: Padding(
                        padding: const EdgeInsets.symmetric(
                            vertical: 0, horizontal: 16),
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Container(
                              child: Text(
                                "Opening times: ",
                                style: kTitleTextStyle.copyWith(
                                    fontWeight: FontWeight.w400, fontSize: 17),
                              ),
                            ),
                            Container(
                              child: Text(
                                widget.store.openTime.toString() + ":00",
                                style: kTitleTextStyle.copyWith(
                                    fontWeight: FontWeight.w700, fontSize: 19),
                              ),
                            ),
                          ],
                        ),
                      )),
                      SizedBox(height: 15),
                      Container(
                          child: Padding(
                        padding: const EdgeInsets.symmetric(
                            vertical: 0, horizontal: 16),
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Container(
                              child: Text(
                                "Closing times: ",
                                style: kTitleTextStyle.copyWith(
                                    fontWeight: FontWeight.w400, fontSize: 17),
                              ),
                            ),
                            Container(
                              child: Text(
                                widget.store.closeTime.toString() + ":00",
                                style: kTitleTextStyle.copyWith(
                                    fontWeight: FontWeight.w700, fontSize: 19),
                              ),
                            ),
                          ],
                        ),
                      )),
                      SizedBox(height: 15),
                      Container(
                          child: Padding(
                        padding: const EdgeInsets.symmetric(
                            vertical: 0, horizontal: 16),
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Container(
                              child: Text(
                                "Address: ",
                                style: kTitleTextStyle.copyWith(
                                    fontWeight: FontWeight.w400, fontSize: 17),
                              ),
                            ),
                            Container(
                              child: Expanded(
                                child: Text(
                                  widget.store.address,
                                  style: kTitleTextStyle.copyWith(
                                      fontWeight: FontWeight.w700,
                                      fontSize: 19),
                                  textAlign: TextAlign.right,
                                  overflow: TextOverflow.ellipsis,
                                ),
                              ),
                            ),
                          ],
                        ),
                      )),
                      SizedBox(height: 15),
                      Container(
                          child: Padding(
                        padding: const EdgeInsets.symmetric(
                            vertical: 0, horizontal: 16),
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Container(
                              child: Text(
                                "Location (lat,long): ",
                                style: kTitleTextStyle.copyWith(
                                    fontWeight: FontWeight.w400, fontSize: 17),
                              ),
                            ),
                            Container(
                              child: Expanded(
                                  child: Text(
                                "(" +
                                    widget.store.storeLocationLatitude +
                                    "," +
                                    widget.store.storeLocationLongitude +
                                    ")",
                                style: kTitleTextStyle.copyWith(
                                    fontWeight: FontWeight.w700, fontSize: 16),
                                overflow: TextOverflow.ellipsis,
                                textAlign: TextAlign.right,
                              )),
                            ),
                          ],
                        ),
                      )),
                    ],
                  ),
                ],
              ),
              Column(
                  children: [_sameStore ? _endShiftBTN() : _startShiftBTN()]),
            ],
          ),
        ),
      ]),
    );
  }
}
