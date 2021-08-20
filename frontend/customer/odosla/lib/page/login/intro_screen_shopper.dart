import 'package:flutter/material.dart';
import 'package:odosla/page/store_page.dart';
import 'package:odosla/utilities/Odosla.dart';
import 'package:odosla/widget/walkthrough.dart';

class IntroShopperScreen extends StatefulWidget {
  @override
  IntroShopperScreenState createState() {
    return IntroShopperScreenState();
  }
}

class IntroShopperScreenState extends State<IntroShopperScreen> {
  final PageController controller = new PageController();
  int currentPage = 0;
  bool lastPage = false;

  void _onPageChanged(int page) {
    setState(() {
      currentPage = page;
      if (currentPage == 3) {
        lastPage = true;
      } else {
        lastPage = false;
      }
    });
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      color: Color(0xFFEEEEEE),
      padding: EdgeInsets.all(10.0),
      child: Column(
        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
        children: <Widget>[
          Expanded(
            flex: 1,
            child: Container(),
          ),
          Expanded(
            flex: 3,
            child: PageView(
              children: <Widget>[
                Walkthrough(
                  title: Odosla.wt1,
                  content: Odosla.wc1,
                  imageIcon: Icons.mobile_screen_share,
                ),
                Walkthrough(
                  title: Odosla.wt2,
                  content: Odosla.wc2,
                  imageIcon: Icons.search,
                ),
                Walkthrough(
                  title: Odosla.wt3,
                  content: Odosla.wc3,
                  imageIcon: Icons.shopping_cart,
                ),
                Walkthrough(
                  title: Odosla.wt4,
                  content: Odosla.wc4,
                  imageIcon: Icons.verified_user,
                ),
              ],
              controller: controller,
              onPageChanged: _onPageChanged,
            ),
          ),
          Expanded(
            flex: 1,
            child: Row(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              crossAxisAlignment: CrossAxisAlignment.end,
              children: <Widget>[
                FlatButton(
                  child: Text(lastPage ? "" : Odosla.skip,
                      style: TextStyle(
                          color: Colors.black,
                          fontWeight: FontWeight.bold,
                          fontSize: 16.0)),
                  onPressed: () => lastPage
                      ? null
                      : Navigator.of(context).push(MaterialPageRoute(
                          builder: (BuildContext context) => StorePage())),
                ),
                FlatButton(
                  child: Text(lastPage ? Odosla.gotIt : Odosla.next,
                      style: TextStyle(
                          color: Colors.black,
                          fontWeight: FontWeight.bold,
                          fontSize: 16.0)),
                  onPressed: () => lastPage
                      ? Navigator.of(context).push(MaterialPageRoute(
                          builder: (BuildContext context) => StorePage()))
                      : controller.nextPage(
                          duration: Duration(milliseconds: 300),
                          curve: Curves.easeIn),
                ),
              ],
            ),
          )
        ],
      ),
    );
  }
}
