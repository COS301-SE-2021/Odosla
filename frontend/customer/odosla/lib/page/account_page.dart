import 'package:flutter/material.dart';
import 'package:odosla/page/account_settings_page.dart';

class AccountPage extends StatefulWidget {
  final String orderID;
  final bool active;
  final List<Widget> pages;
  const AccountPage(this.orderID, this.active, this.pages, {Key? key})
      : super(key: key);

  @override
  _AccountPageState createState() => _AccountPageState();
}

class _AccountPageState extends State<AccountPage> {
  int _page = 0;

  @override
  void initState() {
    _page = widget.active ? 0 : 1;
    super.initState();
  }

  void onTabTapped(int index) {
    setState(() {
      _page = index;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Container(
        child: Column(
      children: [
        Expanded(
          child: Scaffold(
              bottomNavigationBar: BottomNavigationBar(
                  currentIndex: _page,
                  onTap: (index) {
                    onTabTapped(index);
                  },
                  selectedItemColor: Colors.black,
                  unselectedItemColor: Colors.grey,
                  items: <BottomNavigationBarItem>[
                    new BottomNavigationBarItem(
                        icon: Icon(Icons.shopping_cart_sharp), label: "Order"),
                    new BottomNavigationBarItem(
                        icon: Icon(Icons.person), label: "Account"),
                  ]),
              // body: PageView(
              //   controller: _pageController,
              //   onPageChanged: (newpage) {
              //     setState(() {
              //       this._page = newpage;
              //     });
              //   },
              //   children: [
              //     buildOrderPage(),
              //     //buildAccountPage(),
              //   ],
              // ),
              body: widget.pages[_page]),
        ),
        //SizedBox(height: 70),
      ],
    ));
  }
}

Widget buildOrderPage() {
  return Container();
  //return OrderPage();
}

Widget buildAccountPage() {
  return AccountSettingsPage();
}
