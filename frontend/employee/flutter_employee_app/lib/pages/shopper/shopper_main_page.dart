

import 'package:curved_navigation_bar/curved_navigation_bar.dart';
import 'package:flutter/material.dart';
import 'package:flutter_employee_app/models/Order.dart';
import 'package:flutter_employee_app/models/Store.dart';
import 'package:flutter_employee_app/pages/shopper/current_order_page.dart';
import 'package:flutter_employee_app/pages/shopper/list_of_stores_screen.dart';
import 'package:flutter_employee_app/pages/shopper/shopper_work_screen.dart';
import 'package:flutter_employee_app/pages/shopper/shopper_profile_screen.dart';

class ShopperHomeScreen extends StatefulWidget {
  int index=1;


  ShopperHomeScreen(this.index);

  @override
  _ShopperHomeScreenState createState() => _ShopperHomeScreenState();
}

class _ShopperHomeScreenState extends State<ShopperHomeScreen> {

  int bottomSelectedIndex = 1;
  Order? currentOrder=null;


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
        StorePage(),
        ShopperWorkScreen(),
        ShopperProfileScreen(),
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
    return Scaffold(
      body: buildPageView(),
      bottomNavigationBar: CurvedNavigationBar(
        color: Colors.deepOrangeAccent,
        backgroundColor: Theme.of(context).primaryColor,
        buttonBackgroundColor: Colors.deepOrangeAccent,
        height: 50,
        index: bottomSelectedIndex,
        onTap: (index) {
          bottomTapped(index);
        },
        items: <Widget>[

          Icon(Icons.store,size:20),
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







