import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_employee_app/models/Store.dart';
import 'package:flutter_employee_app/pages/shopper/get_new_order.dart';
import 'package:flutter_employee_app/pages/shopper/shopper_main_page.dart';
import 'package:flutter_employee_app/provider/shop_provider.dart';
import 'package:flutter_employee_app/provider/user_provider.dart';
import 'package:flutter_employee_app/services/ShoppingService.dart';
import 'package:flutter_employee_app/services/UserService.dart';
import 'package:flutter_employee_app/utilities/constants.dart';
import 'package:flutter_employee_app/utilities/my_navigator.dart';
import 'package:get_it/get_it.dart';
import 'package:provider/provider.dart';

class StoreInfoScreen extends StatefulWidget {

  final Store store;

  const StoreInfoScreen(BuildContext context, {Key? key, required this.store}) : super(key: key);

  @override
  _StoreInfoScreenState createState() => _StoreInfoScreenState();
}

class _StoreInfoScreenState extends State<StoreInfoScreen> {


 final UserService _userService=GetIt.I.get();
 bool _onShift=false;
 bool sameStore=false;

 initState(){

   _userService.getCurrentUser(context).then((value) =>
   {
     setState(() {
       _onShift=value!.onShift;
       if(_onShift==null){
         _onShift=false;
       }
     })
   }
   );
 }



  Widget _startShiftBTN() {
      return Align(
        alignment: Alignment.bottomCenter,
        child: Container(
          width: double.infinity,
          padding: EdgeInsets.symmetric(vertical: 25.0,horizontal: 10),
          child: RaisedButton(
            elevation: 5.0,
            onPressed: _onShift==false?() async {
                await _userService.setShopperShift(true, widget.store.id, context).then((value) =>
                {
                  if(value==true){
                    Provider.of<ShopProvider>(context,listen: false).store=widget.store,
                    Navigator.of(context).push(MaterialPageRoute(
                    builder: (BuildContext context) =>
                        GetNewOrderScreen(context, store: widget
                            .store) //ProductPage(product: product),
                    ))
                  }
                });

            }:null,
            padding: EdgeInsets.all(22.0),
            shape: RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(40.0),
            ),
            color: Colors.white,
            textColor: Colors.deepOrangeAccent,
            disabledColor:Color(0xA65A5959),
            disabledElevation: 0.0,
            disabledTextColor: Colors.white,
            child: Text(
              'START SHIFT',
              style: TextStyle(
                // color: _onShift?Color(0xFF3E3D3D):Color(0xFFE9884A),
                letterSpacing: 1.5,
                fontSize: 18.0,
                fontWeight: FontWeight.bold,
                fontFamily: 'OpenSans',
              ),
            ),
          ),
        ),
      );
  }

 Widget _endShiftBTN() {
   return Align(
     alignment: Alignment.bottomCenter,
     child: Container(
       width: double.infinity,
       padding: EdgeInsets.symmetric(vertical: 25.0,horizontal: 10),
       child: RaisedButton(
         elevation: 5.0,
         onPressed: () async {
           await _userService.setShopperShift(false, widget.store.id, context).then((value) =>
           {
             if(value==true){
               Provider.of<ShopProvider>(context,listen: false).store==Store("","",0,0,true,""),
               Navigator.of(context).push(MaterialPageRoute(
                   builder: (BuildContext context) =>
                       ShopperHomeScreen(1) //ProductPage(product: product),
               ))
             }
           });

         },
         padding: EdgeInsets.all(22.0),
         shape: RoundedRectangleBorder(
           borderRadius: BorderRadius.circular(40.0),
         ),
         color: Colors.white,
         textColor: Color(0xFFCB4E2C),
         child: Text(
           'END SHIFT',
           style: TextStyle(
             // color: _onShift?Color(0xFF3E3D3D):Color(0xFFE9884A),
             letterSpacing: 1.5,
             fontSize: 18.0,
             fontWeight: FontWeight.bold,
             fontFamily: 'OpenSans',
           ),
         ),
       ),
     ),
   );
 }

  @override
  Widget build(BuildContext context) {
   bool _sameStore=false;
   Store store = Provider.of<ShopProvider>(context).store;
   if(store.id==widget.store.id){
     if(_onShift==true){
       setState(() {
         _sameStore=true;
       });
     }
   }
    return Scaffold(
        body: ListView(
          children: [Column(
            children: [
              Padding(
                padding:
                const EdgeInsets.symmetric(horizontal: 10.0, vertical: 5.0),
                child: Row(
                  children: <Widget>[
                    IconButton(icon: Icon(Icons.chevron_left), onPressed: () {
                      MyNavigator.goToShopperHomePage(context);
                    }),
                  ],
                ),
              ),
              Container(
                  child: Image.asset(
                    "assets/"+widget.store.imageUrl,
                  ),
                height: 250,
              ),
              SizedBox(height:40),
              Container(
                child: Padding(
                  padding: const EdgeInsets.symmetric(vertical: 0,horizontal: 16),
                  child: Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [

                      Container(
                        child: Text(
                          "Store ID: ",
                          style: kTitleTextStyle.copyWith(
                            fontWeight: FontWeight.w400,
                            fontSize: 20
                          ),
                        ),
                      ),
                      Container(
                        child: Text(
                          widget.store.id,
                          style: kTitleTextStyle.copyWith(
                              fontWeight: FontWeight.w700,
                              fontSize: 11
                          ),
                        ),
                      ),
                    ],
                  ),
                )
              ),
              SizedBox(height: 15),
              Container(
                  child: Padding(
                    padding: const EdgeInsets.symmetric(vertical: 0,horizontal: 16),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Container(
                          child: Text(
                            "Brand: ",
                            style: kTitleTextStyle.copyWith(
                                fontWeight: FontWeight.w400,
                                fontSize: 20
                            ),
                          ),
                        ),
                        Container(
                          child: Text(
                            widget.store.name,
                            style: kTitleTextStyle.copyWith(
                                fontWeight: FontWeight.w700,
                                fontSize: 11
                            ),
                          ),
                        ),
                      ],

                    ),
                  )
              ),
              SizedBox(height: 15),
              Container(
                  child: Padding(
                    padding: const EdgeInsets.symmetric(vertical: 0,horizontal: 16),
                    child: Row(

                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Container(
                          child: Text(
                            "Opening times: ",
                            style: kTitleTextStyle.copyWith(
                                fontWeight: FontWeight.w400,
                                fontSize: 20
                            ),
                          ),
                        ),
                        Container(
                          child: Text(
                            widget.store.openTime.toString()+":00",
                            style: kTitleTextStyle.copyWith(
                                fontWeight: FontWeight.w700,
                                fontSize: 11
                            ),
                          ),
                        ),
                      ],

                    ),
                  )
              ),
              SizedBox(height: 15),
              Container(
                  child: Padding(
                    padding: const EdgeInsets.symmetric(vertical: 0,horizontal: 16),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Container(
                          child: Text(
                            "Closing times: ",
                            style: kTitleTextStyle.copyWith(
                                fontWeight: FontWeight.w400,
                                fontSize: 20
                            ),
                          ),
                        ),
                        Container(
                          child: Text(
                            widget.store.closeTime.toString()+":00",
                            style: kTitleTextStyle.copyWith(
                                fontWeight: FontWeight.w700,
                                fontSize: 11
                            ),
                          ),
                        ),
                      ],

                    ),
                  )
              ),
              SizedBox(height: 15),
              Container(
                  child: Padding(
                    padding: const EdgeInsets.symmetric(vertical: 0,horizontal: 16),
                    child: Row(
                      mainAxisAlignment: MainAxisAlignment.spaceBetween,
                      children: [
                        Container(
                          child: Text(
                            "Description: ",
                            style: kTitleTextStyle.copyWith(
                                fontWeight: FontWeight.w400,
                                fontSize: 20
                            ),
                          ),
                        ),
                      ],
                    ),
                  )
              ),
              Container(
                child: Center(
                  child: Padding(
                    padding: const EdgeInsets.all(6.0),
                    child: Text(
                      "\n From Our Pantry to Yours! Stock up for Less When You Shop Online.\n Browse Pasta, Rice & Grains, Oils, Vinegars, Olives, Pickles & Baking Supplies. Shop Now! Safe & Secure Shopping. \nStreamlined Refund Policy. Join Our WRewards Program.",
                      style: kTitleTextStyle.copyWith(
                          fontWeight: FontWeight.w700,
                          fontSize: 11
                      ),
                      textAlign: TextAlign.center,
                    ),
                  ),
                ),
              ),
              SizedBox(height: 30),
              Column(
                mainAxisAlignment: MainAxisAlignment.end,
                  children: [
                    _sameStore?_endShiftBTN():_startShiftBTN()
                  ]
              ),

            ],
          ),
          ]
        ),


    );
  }

}