import 'package:flutter/material.dart';
import 'package:get_it/get_it.dart';
import 'package:odosla/page/login/splash_screen.dart';
import 'package:odosla/provider/cart_provider.dart';
import 'package:odosla/provider/status_provider.dart';
import 'package:odosla/provider/wallet_provider.dart';
import 'package:odosla/services/UserService.dart';
import 'package:odosla/services/api_service.dart';
import 'package:provider/provider.dart';

void main() {
  runApp(MyApp());
  //SystemChrome.setEnabledSystemUIOverlays([]);

  final userService = UserService();
  GetIt.I.registerSingleton(userService);
}

class MyApp extends StatelessWidget {
  static final String title = 'Odosla Catalogue';
  static final String stores = 'Stores';
  static final String items = 'Products';
  final cartProvState = CartProvider();
  final statusProvState = StatusProvider();
  final walletProvState = WalletProvider();

  ApiService api = ApiService();

  @override
  Widget build(BuildContext context) => MultiProvider(
          providers: [
            ChangeNotifierProvider(create: (_) => cartProvState),
            ChangeNotifierProvider(create: (_) => statusProvState),
            ChangeNotifierProvider(create: (_) => walletProvState)
          ],
          child: MaterialApp(
            debugShowCheckedModeBanner: false,
            title: title,
            theme: ThemeData(
              primaryColor: Colors.deepOrange,
              primaryColorDark: Colors.white,
            ),
            home: SplashScreen(),
          ));
}
