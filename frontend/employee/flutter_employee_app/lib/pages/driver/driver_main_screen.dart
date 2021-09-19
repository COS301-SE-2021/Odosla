

import 'package:animated_theme_switcher/animated_theme_switcher.dart';
import 'package:curved_navigation_bar/curved_navigation_bar.dart';
import 'package:flutter/material.dart';
import 'package:flutter_employee_app/models/Delivery.dart';
import 'package:flutter_employee_app/models/GeoPoint.dart';
import 'package:flutter_employee_app/models/Order.dart';
import 'package:flutter_employee_app/models/Store.dart';
import 'package:flutter_employee_app/pages/driver/driver_map.dart';
import 'package:flutter_employee_app/pages/shopper/current_order_page.dart';
import 'package:flutter_employee_app/pages/shopper/list_of_stores_screen.dart';
import 'package:flutter_employee_app/pages/shopper/shopper_work_screen.dart';
import 'package:flutter_employee_app/pages/shopper/shopper_profile_screen.dart';
import 'package:flutter_employee_app/provider/UtilityProvider.dart';
import 'package:flutter_employee_app/provider/delivery_provider.dart';
import 'package:provider/provider.dart';

import 'driver_profile_screen.dart';
import 'driver_work_screen.dart';

class DriverHomeScreen extends StatefulWidget {
  int index=1;


  DriverHomeScreen(this.index);

  @override
  _DriverHomeScreenState createState() => _DriverHomeScreenState();
}

class _DriverHomeScreenState extends State<DriverHomeScreen> {

  int bottomSelectedIndex = 1;

  PageController pageController = PageController(
    initialPage: 1,
    keepPage: true,
  );

  Widget buildPageView() {
    return PageView(
      controller: pageController,
      onPageChanged: (index) {
        pageChanged(index);
      },
      children: <Widget>[
        DriverMapScreen(),
        DriverWorkScreen(),
        DriverProfileScreen(),
      ],
    );
  }

  @override
  void initState() {
    super.initState();
    bottomSelectedIndex=widget.index;
    pageController = PageController(
      initialPage: bottomSelectedIndex,
      keepPage: true,
    );
  }

  void pageChanged(int index) {
    setState(() {

      bottomSelectedIndex = index;
    });
  }

  void bottomTapped(int index) {
    setState(() {
      bottomSelectedIndex = index;
      pageController.animateToPage(index, duration: Duration(milliseconds: 500), curve: Curves.ease);
    });
  }

  @override
  Widget build(BuildContext context) {
    Provider.of<DeliveryProvider>(context, listen: false).delivery=Delivery("123",GeoPoint(-25.754618,28.233255,"f"),GeoPoint(-25.188660,28.142833,"G"),"","","","","DeliveringToCustomer",40,false);
    return Scaffold(
      body: buildPageView(),
      bottomNavigationBar: CurvedNavigationBar(
        color: Colors.deepOrangeAccent,
        backgroundColor: Theme.of(context).primaryColor,
        buttonBackgroundColor: Colors.deepOrangeAccent,
        height: 50,
        onTap: (index) {
          bottomTapped(index);
        },
        index: bottomSelectedIndex,
        items: <Widget>[
          Icon(Icons.map,size:20),
          Icon(Icons.work,size: 20),
          Icon(Icons.account_circle,size:20),
        ],
        animationDuration: Duration(
          milliseconds: 200,
        ),
        animationCurve: Curves.bounceInOut,
      ),
    );
  }
}







