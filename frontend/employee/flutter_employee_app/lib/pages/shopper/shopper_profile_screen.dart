import 'package:flutter/material.dart';
import 'package:flutter_employee_app/pages/shopper/edit_profile_page.dart';
import 'package:flutter_employee_app/services/UserService.dart';
import 'package:flutter_employee_app/utilities/constants.dart';
import 'package:flutter_employee_app/utilities/my_navigator.dart';
import 'package:flutter_employee_app/utilities/profile_list_item.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get_it/get_it.dart';
import 'package:line_awesome_flutter/line_awesome_flutter.dart';
import 'package:animated_theme_switcher/animated_theme_switcher.dart';

class ShopperProfileScreen extends StatefulWidget {
  @override
  _ShopperProfileScreenState createState() => _ShopperProfileScreenState();
}

class _ShopperProfileScreenState extends State<ShopperProfileScreen> {

  final UserService _userService = GetIt.I.get();
  String _name="";
  String _surname="";
  String _email="";
  String _numberOfOrderCompleted="0";

  void initState() {
    _userService.getCurrentUser(context).then((value) =>
    {
      print(value),
      setState(() {
        _name = value!.name;
        _surname = value.surname;
        _email = value.email;
        _numberOfOrderCompleted=value.getOrdersCompleted();
        if(_numberOfOrderCompleted==null){
          _numberOfOrderCompleted="0";
        }
      })
    }

    );
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
            margin: EdgeInsets.only(top: kSpacingUnit.w * 3,left: kSpacingUnit.w*1),
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
            _name+" "+_surname,
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
        SizedBox(width: kSpacingUnit.w * 3),
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

                SizedBox(height: kSpacingUnit.w * 5),
                header,
                Expanded(
                  child: ListView(
                    children: <Widget>[
                      Container(
                        margin: EdgeInsets.symmetric(horizontal: kSpacingUnit.w*4,vertical: kSpacingUnit.w*0.1),
                        decoration: new BoxDecoration(
                          boxShadow: [
                            new BoxShadow(
                              color: Colors.white24,
                              blurRadius: 2.0,
                            ),
                          ],
                        ),
                        child: Card(
                            margin: EdgeInsets.symmetric(horizontal: kSpacingUnit.w*0.2,vertical: kSpacingUnit.w*0.1),
                            clipBehavior: Clip.antiAlias,
                            color: Colors.deepOrangeAccent,
                            elevation: 5.0,
                            child: Padding(
                                padding: const EdgeInsets.symmetric(horizontal: 5.0,vertical: 20.0),
                                child: Row(
                                  children: <Widget>[
                                    Expanded(
                                        child:Column(
                                          children: <Widget>[
                                            Text(
                                                "Number of Orders Completed",
                                                style: kTitleTextStyle.copyWith(
                                                  fontWeight: FontWeight.w500,
                                                  fontSize: kSpacingUnit.w*2,
                                                )
                                            ),
                                            SizedBox(height:kSpacingUnit.w*2),
                                            Text(
                                              _numberOfOrderCompleted,
                                              style: kTitleTextStyle.copyWith(
                                                fontWeight: FontWeight.w500,
                                                fontSize: kSpacingUnit.w*3,
                                              ),
                                            ),

                                          ],
                                        )
                                    ),

                                  ],
                                )
                            )
                        ),
                      ),
                      SizedBox(height: kSpacingUnit.w*2),
                      GestureDetector(
                        onTap: (){},
                        child: ProfileListItem(
                            LineAwesomeIcons.wallet,
                            'Wallet'
                        ),
                      ),
                      // ProfileListItem(
                      //   LineAwesomeIcons.history,
                      //   'Purchase History',
                      // ),
                      GestureDetector(
                        onTap: (){},
                        child: ProfileListItem(
                          LineAwesomeIcons.question_circle,
                          'Help & Support',
                        ),
                      ),
                      GestureDetector(
                        onTap: (){
                          Navigator.of(context).push(MaterialPageRoute(
                              builder: (BuildContext context) => EditProfilePage()));
                        },
                        child:ProfileListItem(
                          LineAwesomeIcons.cog,
                          'Settings',
                        ),
                      ),
                      GestureDetector(
                        onTap: (){

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