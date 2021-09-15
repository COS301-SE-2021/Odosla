
import 'package:flutter/material.dart';
import 'package:flutter_employee_app/models/Store.dart';

class MyNavigator {

  static void goToHome(BuildContext context) {
    Navigator.pushNamed(context, "/home");
  }

  static void goToShopperIntro(BuildContext context) {
    Navigator.pushNamed(context, "/introShopper");
  }

  static void goToDriverIntro(BuildContext context) {
    Navigator.pushNamed(context, "/introDriver");
  }

  static void goToLogin(BuildContext context) {
    Navigator.pushNamed(context, "/login");
  }

  static void goToRegister(BuildContext context) {
    Navigator.pushNamed(context, "/register");
  }

  static void goToForgotPassword(BuildContext context) {
    Navigator.pushNamed(context, "/forgotPassword");
  }

  static void goToDriverActivateAccount(BuildContext context) {
    Navigator.pushNamed(context, "/activateDriverAccount");
  }

  static void goToShopperActivateAccount(BuildContext context) {
    Navigator.pushNamed(context, "/activateShopperAccount");
  }

  static void goToShopperHomePage(BuildContext context) {
    Navigator.pushNamed(context, "/shopperHomePage");
  }

  static void goToDriverHomePage(BuildContext context) {
    Navigator.pushNamed(context, "/driverHomePage");
  }

  static void goToBarcodeScanPage(BuildContext context) {
    Navigator.pushNamed(context, "/barcodeScanner");
  }

  static void goToCurrentOrderPage(BuildContext context) {
    Navigator.pushNamed(context, "/currentOrderPage");
  }

  static void goToStoreInfoScreen(BuildContext context, Store item){
    Navigator.pushNamed(context, "/storeInfoScreen");
  }

  static void goToAdminHomePgae(BuildContext context){
    Navigator.pushNamed(context, "/adminHomePage");
  }


}