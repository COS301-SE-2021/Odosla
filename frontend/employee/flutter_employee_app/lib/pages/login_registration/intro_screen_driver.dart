import 'package:flutter/material.dart';
import 'package:flutter_employee_app/utilities/Odosla.dart';
import 'package:flutter_employee_app/utilities/my_navigator.dart';
import 'package:flutter_employee_app/widgets/walkthrough.dart';



class IntroDriverScreen extends StatefulWidget {
  @override
  IntroDriverScreenState createState() {
    return IntroDriverScreenState();
  }
}

class IntroDriverScreenState extends State<IntroDriverScreen> {
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
                  title: Odosla.wt5,
                  content: Odosla.wc5,
                  imageIcon: Icons.wysiwyg_outlined,
                ),
                Walkthrough(
                  title: Odosla.wt6,
                  content: Odosla.wc6,
                  imageIcon: Icons.directions_car,
                ),
                Walkthrough(
                  title: Odosla.wt7,
                  content: Odosla.wc7,
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
                  onPressed: () =>
                  lastPage ? null : MyNavigator.goToDriverHomePage(context),
                ),
                FlatButton(
                  child: Text(lastPage ? Odosla.gotIt : Odosla.next,
                      style: TextStyle(
                          color: Colors.black,
                          fontWeight: FontWeight.bold,
                          fontSize: 16.0)),
                  onPressed: () => lastPage
                      ? MyNavigator.goToDriverHomePage(context)
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