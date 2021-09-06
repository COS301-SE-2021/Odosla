import 'package:animated_theme_switcher/animated_theme_switcher.dart';
import 'package:flutter/material.dart';
import 'package:flutter_employee_app/pages/admin/admin_main_screen.dart';
import 'package:flutter_employee_app/pages/admin/home.dart';
import 'package:flutter_employee_app/pages/driver/driver_main_screen.dart';
import 'package:flutter_employee_app/pages/driver/driver_map.dart';
import 'package:flutter_employee_app/pages/login_registration/activate_driver_account_screen.dart';
import 'package:flutter_employee_app/pages/login_registration/activate_shopper_account_screen.dart';
import 'package:flutter_employee_app/pages/login_registration/forgot_password_screen.dart';
import 'package:flutter_employee_app/pages/home_screen.dart';
import 'package:flutter_employee_app/pages/login_registration/intro_screen_driver.dart';
import 'package:flutter_employee_app/pages/login_registration/intro_screen_shopper.dart';
import 'package:flutter_employee_app/pages/login_registration/login_screen.dart';
import 'package:flutter_employee_app/pages/login_registration/register_screen.dart';
import 'package:flutter_employee_app/pages/shopper/barcode_scanner_screen.dart';
import 'package:flutter_employee_app/pages/shopper/current_order_page.dart';
import 'package:flutter_employee_app/pages/shopper/shopper_main_page.dart';
import 'package:flutter_employee_app/provider/UtilityProvider.dart';
import 'package:flutter_employee_app/provider/delivery_provider.dart';
import 'package:flutter_employee_app/provider/jwt_provider.dart';
import 'package:flutter_employee_app/provider/order_provider.dart';
import 'package:flutter_employee_app/provider/shop_provider.dart';
import 'package:flutter_employee_app/provider/user_provider.dart';
import 'package:flutter_employee_app/services/DeliveryService.dart';
import 'package:flutter_employee_app/services/ShoppingService.dart';
import 'package:flutter_employee_app/services/UserService.dart';
import 'package:flutter_employee_app/utilities/constants.dart';
import 'package:get_it/get_it.dart';
import 'package:provider/provider.dart';


var routes = <String, WidgetBuilder>{
  "/home": (BuildContext context) => HomeScreen(),
  "/introShopper": (BuildContext context) => IntroShopperScreen(),
  "/introDriver": (BuildContext context) => IntroDriverScreen(),
  "/login": (BuildContext context) => LoginScreen(),
  "/register": (BuildContext context)=> RegisterScreen(),
  "/forgotPassword": (BuildContext context) => ForgotPasswordScreen(),
  "/activateDriverAccount":(BuildContext context) =>ActivateDriverAccountScreen(),
  "/activateShopperAccount":(BuildContext context) =>ActivateShopperAccountScreen(),
  "/shopperHomePage":(BuildContext context) =>ShopperHomeScreen(1),
  "/driverHomePage":(BuildContext context) =>DriverHomeScreen(1),
  "/barcodeScanner":(BuildContext context) =>BarcodeScanPage(),
  "/currentOrderPage":(BuildContext context)=>CurrentOrderScreen(),
};



void main() async {


  final userService = UserService();
  GetIt.I.registerSingleton(userService);


  final shoppingService=ShoppingService();
  GetIt.I.registerSingleton(shoppingService);

  final deliveryService=DeliveryService();
  GetIt.I.registerSingleton(deliveryService);
  //await Future.wait([notificationService.init(), contactsModel.initialise()]);

  runApp(OdoslaApp());
}

class OdoslaApp extends StatelessWidget  {

  final orderProviderState=OrderProvider();
  final shopProviderState=ShopProvider();
  final userProviderState=UserProvider();
  final deliveryProviderState=DeliveryProvider();
  final JWTProviderState=JWTProvider();
  final UtilityProviderState=UtilityProvider();
  @override
  Widget build(BuildContext context) => MultiProvider (
    providers: [ChangeNotifierProvider(create: (_)=> orderProviderState),ChangeNotifierProvider(create: (_)=> shopProviderState),ChangeNotifierProvider(create: (_)=>userProviderState),ChangeNotifierProvider(create: (_)=> deliveryProviderState),ChangeNotifierProvider(create: (_)=> JWTProviderState),ChangeNotifierProvider(create: (_)=> UtilityProviderState)],
    child: ThemeProvider(
      initTheme: kLightTheme,
      child: Builder(
        builder: (context) {
          return MaterialApp(
            debugShowCheckedModeBanner: false,
            theme: ThemeProvider.of(context),
            home:HomePageScreen(),
            routes: routes,
          );
        },
      ),
    )
  );
}
