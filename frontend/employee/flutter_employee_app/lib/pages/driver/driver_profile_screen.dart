import 'package:animated_theme_switcher/animated_theme_switcher.dart';
import 'package:flutter/material.dart';
import 'package:flutter_employee_app/pages/driver/edit_page_driver.dart';
import 'package:flutter_employee_app/pages/wallet_page.dart';
import 'package:flutter_employee_app/services/UserService.dart';
import 'package:flutter_employee_app/utilities/constants.dart';
import 'package:flutter_employee_app/utilities/my_navigator.dart';
import 'package:flutter_employee_app/utilities/profile_list_item.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get_it/get_it.dart';
import 'package:line_awesome_flutter/line_awesome_flutter.dart';

import '../contact_us_page.dart';

class DriverProfileScreen extends StatefulWidget {
  @override
  _DriverProfileScreenState createState() => _DriverProfileScreenState();
}

class _DriverProfileScreenState extends State<DriverProfileScreen> {
  final UserService _userService = GetIt.I.get();
  String _name = "name";
  String _surname = "surname";
  String _email = "email@gmail.com";
  String _numberOfDeliveriesCompleted = "0";
  String _rating = "0";

  void initState() {
    _userService.getCurrentUser(context).then((value) => {
          setState(() {
            _name = value!.name;
            _surname = value.surname;
            _email = value.email;
            _rating = value.rating;
            _numberOfDeliveriesCompleted = value.deliveriesCompleted;
          })
        });
  }

  @override
  Widget build(BuildContext context) {
    ScreenUtil.init(
        BoxConstraints(
            maxWidth: MediaQuery.of(context).size.width,
            maxHeight: MediaQuery.of(context).size.height),
        designSize: Size(414, 896),
        orientation: Orientation.portrait);

    var profileInfo = Expanded(
      child: Column(
        children: <Widget>[
          Container(
            height: kSpacingUnit.w * 11,
            width: kSpacingUnit.w * 11,
            margin: EdgeInsets.only(
                top: kSpacingUnit.w * 3, left: kSpacingUnit.w * 1),
            child: Stack(
              children: <Widget>[
                CircleAvatar(
                  radius: kSpacingUnit.w * 9,
                  backgroundImage: AssetImage('assets/profile.jpg'),
                ),
                Align(
                  alignment: Alignment.bottomRight,
                  child: Container(
                    height: kSpacingUnit.w * 2.5,
                    width: kSpacingUnit.w * 2.5,
                    decoration: BoxDecoration(
                      color: Theme.of(context).accentColor,
                      shape: BoxShape.circle,
                    ),
                    child: Center(
                      heightFactor: kSpacingUnit.w * 1.5,
                      widthFactor: kSpacingUnit.w * 1.5,
                      child: Icon(
                        LineAwesomeIcons.pen,
                        color: kDarkPrimaryColor,
                        size: ScreenUtil().setSp(kSpacingUnit.w * 1.5),
                      ),
                    ),
                  ),
                ),
              ],
            ),
          ),
          SizedBox(height: kSpacingUnit.w * 2),
          Text(
            _name + " " + _surname,
            style: kTitleTextStyle,
          ),
          SizedBox(height: kSpacingUnit.w * 0.5),
          Text(
            _email,
            style: kCaptionTextStyle.copyWith(
              fontSize: 17,
            ),
          ),
        ],
      ),
    );

    var themeSwitcher = ThemeSwitcher(
      builder: (context) {
        return AnimatedCrossFade(
          duration: Duration(milliseconds: 200),
          crossFadeState:
              ThemeProvider.of(context)!.brightness == Brightness.dark
                  ? CrossFadeState.showFirst
                  : CrossFadeState.showSecond,
          firstChild: GestureDetector(
            onTap: () =>
                ThemeSwitcher.of(context)!.changeTheme(theme: kLightTheme),
            child: Icon(
              LineAwesomeIcons.sun,
              size: ScreenUtil().setSp(kSpacingUnit.w * 3),
            ),
          ),
          secondChild: GestureDetector(
            onTap: () =>
                ThemeSwitcher.of(context)!.changeTheme(theme: kDarkTheme),
            child: Icon(
              LineAwesomeIcons.moon,
              size: ScreenUtil().setSp(kSpacingUnit.w * 3),
            ),
          ),
        );
      },
    );

    var header = Row(
      mainAxisAlignment: MainAxisAlignment.spaceBetween,
      crossAxisAlignment: CrossAxisAlignment.start,
      children: <Widget>[
        SizedBox(width: kSpacingUnit.w * 4),
        profileInfo,
        themeSwitcher,
        SizedBox(width: kSpacingUnit.w * 3),
      ],
    );

    return ThemeSwitchingArea(
      child: Builder(
        builder: (context) {
          return Scaffold(
            body: Column(
              children: <Widget>[
                SizedBox(height: kSpacingUnit.w * 3),
                header,
                Expanded(
                  child: ListView(
                    children: <Widget>[
                      Container(
                        margin: EdgeInsets.symmetric(
                            horizontal: kSpacingUnit.w * 4,
                            vertical: kSpacingUnit.w * 0.5),
                        decoration: new BoxDecoration(
                          boxShadow: [
                            new BoxShadow(
                              color: Colors.white24,
                              blurRadius: 2.0,
                            ),
                          ],
                        ),
                        child: Card(
                            margin: EdgeInsets.symmetric(
                                horizontal: kSpacingUnit.w * 0.2,
                                vertical: kSpacingUnit.w * 0.1),
                            clipBehavior: Clip.antiAlias,
                            color: Theme.of(context).backgroundColor,
                            elevation: 5.0,
                            child: Padding(
                                padding: const EdgeInsets.symmetric(
                                    horizontal: 5.0, vertical: 20.0),
                                child: Row(
                                  children: <Widget>[
                                    Expanded(
                                        child: Column(
                                      children: <Widget>[
                                        Text(
                                          "Deliveries Completed",
                                          style: kTitleTextStyle.copyWith(
                                            fontWeight: FontWeight.w500,
                                            fontSize: kSpacingUnit.w * 1.7,
                                          ),
                                          textAlign: TextAlign.center,
                                        ),
                                        SizedBox(height: kSpacingUnit.w * 2),
                                        Text(
                                          _numberOfDeliveriesCompleted,
                                          style: kTitleTextStyle.copyWith(
                                            fontWeight: FontWeight.w500,
                                            fontSize: kSpacingUnit.w * 3,
                                          ),
                                        ),
                                      ],
                                    )),
                                    Expanded(
                                        child: Column(
                                      children: <Widget>[
                                        Text(
                                          "Rating",
                                          style: kTitleTextStyle.copyWith(
                                            fontWeight: FontWeight.w500,
                                            fontSize: kSpacingUnit.w * 1.7,
                                          ),
                                          textAlign: TextAlign.center,
                                        ),
                                        SizedBox(height: kSpacingUnit.w * 2),
                                        Text(
                                          _rating,
                                          style: kTitleTextStyle.copyWith(
                                            fontWeight: FontWeight.w500,
                                            fontSize: kSpacingUnit.w * 3,
                                          ),
                                        ),
                                      ],
                                    )),
                                  ],
                                ))),
                      ),
                      SizedBox(height: kSpacingUnit.w * 2),
                      GestureDetector(
                        onTap: () {
                          Navigator.of(context).push(MaterialPageRoute(
                              builder: (BuildContext context) => WalletPage()));
                        },
                        child:
                            ProfileListItem(LineAwesomeIcons.wallet, 'Wallet'),
                      ),
                      // ProfileListItem(
                      //   LineAwesomeIcons.history,
                      //   'Purchase History',
                      // ),
                      GestureDetector(
                        onTap: () {
                          Navigator.of(context).push(MaterialPageRoute(
                              builder: (BuildContext context) =>
                                  ContactPage()));
                        },
                        child: ProfileListItem(
                          LineAwesomeIcons.question_circle,
                          'Help & Support',
                        ),
                      ),
                      GestureDetector(
                        onTap: () {
                          Navigator.of(context).push(MaterialPageRoute(
                              builder: (BuildContext context) =>
                                  EditDriverProfilePage()));
                        },
                        child: ProfileListItem(
                          LineAwesomeIcons.cog,
                          'Settings',
                        ),
                      ),
                      GestureDetector(
                        onTap: () async {
                          await _userService.deleteStorage(context);
                          MyNavigator.goToLogin(context);
                          /* implement dleting from storage */
                        },
                        child: ProfileListItem(
                          LineAwesomeIcons.alternate_sign_out,
                          'Logout',
                        ),
                      ),
                    ],
                  ),
                )
              ],
            ),
          );
        },
      ),
    );
  }
}
