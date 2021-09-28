import 'package:curved_navigation_bar/curved_navigation_bar.dart';
import 'package:flutter/material.dart';

import 'admin_profile_screen.dart';
import 'admin_work_screen.dart';

class AdminHomeScreen extends StatefulWidget {
  int index=1;


  AdminHomeScreen(this.index);

  @override
  _AdminHomeScreenState createState() => _AdminHomeScreenState();
}

class _AdminHomeScreenState extends State<AdminHomeScreen> {

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
        AdminProfileScreen(),
        AdminWorkScreen(),
        AdminProfileScreen(),
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







