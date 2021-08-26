import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:font_awesome_flutter/font_awesome_flutter.dart';

final kHintTextStyle = TextStyle(
  color: Colors.white54,
  fontFamily: 'OpenSans',
);

final kLabelStyle = TextStyle(
  color: Colors.white,
  fontWeight: FontWeight.bold,
  fontFamily: 'OpenSans',
);

final kBoxDecorationStyle = BoxDecoration(
  color: Color(0xFFE9884A),
  borderRadius: BorderRadius.circular(10.0),
  boxShadow: [
    BoxShadow(
      color: Colors.black12,
      blurRadius: 6.0,
      offset: Offset(0, 2),
    ),
  ],
);



const kSpacingUnit = 10;

const kDarkPrimaryColor = Color(0xFF212121);
const kDarkSecondaryColor = Color(0xFF373737);
const kLightPrimaryColor = Color(0xFFFFFFFF);
const kLightSecondaryColor = Color(0xFFF3F7FB);
const kAccentColor = Color(0xFFFFC107);

final kTitleTextStyle = TextStyle(
  fontSize: ScreenUtil().setSp(kSpacingUnit.w * 1.7),
  fontWeight: FontWeight.w600,
);

final kCaptionTextStyle = TextStyle(
  fontSize: ScreenUtil().setSp(kSpacingUnit.w * 1.3),
  fontWeight: FontWeight.w100,
);

final kButtonTextStyle = TextStyle(
  fontSize: ScreenUtil().setSp(kSpacingUnit.w * 1.5),
  fontWeight: FontWeight.w400,
  color: kDarkPrimaryColor,
);

final kDarkTheme = ThemeData(
  brightness: Brightness.dark,
  fontFamily: 'SFProText',
  primaryColor: kDarkPrimaryColor,
  canvasColor: kDarkPrimaryColor,
  backgroundColor: kDarkSecondaryColor,
  accentColor: kAccentColor,
  iconTheme: ThemeData.dark().iconTheme.copyWith(
    color: kLightSecondaryColor,
  ),
  textTheme: ThemeData.dark().textTheme.apply(
    fontFamily: 'SFProText',
    bodyColor: kLightSecondaryColor,
    displayColor: kLightSecondaryColor,
  ),
);

final kLightTheme = ThemeData(
  brightness: Brightness.light,
  fontFamily: 'SFProText',
  primaryColor: kLightPrimaryColor,
  canvasColor: kLightPrimaryColor,
  backgroundColor: kLightSecondaryColor,
  accentColor: kAccentColor,
  iconTheme: ThemeData.light().iconTheme.copyWith(
    color: kDarkSecondaryColor,
  ),
  textTheme: ThemeData.light().textTheme.apply(
    fontFamily: 'SFProText',
    bodyColor: kDarkSecondaryColor,
    displayColor: kDarkSecondaryColor,
  ),
);

const mainColor = const Color(0xFFFB777A);
const btnColor = const Color(0xFFfb8385);
const textColor = const Color(0xFF000000);
const secTextColor = const Color(0xFFFFFFFF);

const card1 = const Color(0xFFFFBF37);
const card2 = const Color(0xFF00CECE);
const card3 = const Color(0xFFFB777A);
const card4 = const Color(0xFFA5A5A5);

class ColorConstants {
  static const kwhiteColor = Colors.white;
  static const kblackColor = Colors.black;
  static const korangeColor = Color(0xfffe752f);
  static const kgreyColor = Color(0xff909090);
  static const gblackColor = Color(0xff242424);
  static const cblackColor = Color(0xFF313131);
  static const corangeColor = Color(0xffdd601e);
}


class Constants {
  static List<Icon> iconList = [
    Icon(
      Icons.mail_outline,
      color: ColorConstants.kwhiteColor,
    ),
    Icon(
      FontAwesomeIcons.paypal,
      color: ColorConstants.kwhiteColor,
    ),
    Icon(
      CupertinoIcons.settings,
      color: ColorConstants.kwhiteColor,
    ),
  ];

  static List<String> titleList = [
    "Salary",
    "Paypal",
    "Car Repair",
  ];

  static List<String> subtitleList = [
    "Belong Interactive",
    "Webtech",
    "Car Engine repair",
  ];

  static List<int> amountList = [
    2010,
    12010,
    232010,
  ];

  static List<String> strList = [
    "1 \n Month",
    "6 \n Month",
    "1 \n Year",
  ];
}