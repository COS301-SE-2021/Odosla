import 'package:flutter/material.dart';
import 'package:flutter_employee_app/models/Delivery.dart';
import 'package:flutter_employee_app/pages/driver/driver_map.dart';
import 'package:flutter_employee_app/provider/delivery_provider.dart';
import 'package:flutter_employee_app/services/DeliveryService.dart';
import 'package:flutter_employee_app/services/UserService.dart';
import 'package:flutter_employee_app/utilities/constants.dart';
import 'package:flutter_employee_app/utilities/my_navigator.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get_it/get_it.dart';
import 'package:provider/provider.dart';

import 'driver_main_screen.dart';

class DriverWorkScreen extends StatefulWidget {
  @override
  _DriverWorkScreenState createState() =>  _DriverWorkScreenState();
}

class  _DriverWorkScreenState extends State<DriverWorkScreen> {


  final UserService _userService=GetIt.I.get();

  String deliveryID="";
  bool _isDelivery=false;
  bool _onShift=false;
  String _email="";
  bool _startedDelivery=false;
  bool _collectedFromStore=false;
  DeliveryService _deliveryService=GetIt.I.get();
  Widget _popUpSuccessfulFoundDeliveries(BuildContext context){

    Delivery _delivery=Provider.of<DeliveryProvider>(context,listen: false).delivery;

    return new AlertDialog(
      title: const Text('FOUND A DELIVERY'),
      content: new Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Text("There is a delivery from "+_delivery.pickUpLocation.address+"to "+_delivery.dropOffLocation.address+"\n Accept it?",
              textAlign: TextAlign.center
          ),
        ],
      ),
      actions: <Widget>[
        new FlatButton(
          onPressed: () async {
            await _deliveryService.assignDriverToDelivery(_delivery.deliveryID, context).then((value) =>
            {
              setState((){
                if(value==true){
                  _isDelivery=true;
                }else{
                  ScaffoldMessenger.of(context)
                      .showSnackBar(SnackBar(content: Text(
                      "Could not get delivery")));
                  MyNavigator.goToDriverHomePage(context);
                }
              })
            }
            );
          },
          child: const Text('Accept'),
        ),
        new FlatButton(
          onPressed: () async {
            Navigator.pop(context, false);
          },
          child: const Text('Decline'),
        ),
      ],
    );
  }

  initState(){

    _userService.getCurrentUser(context).then((value) =>
    {
      setState(() {
        _email = value!.email;
        _onShift=value.onShift;
        if(_onShift==null){
          _onShift=false;
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
    Delivery _delivery=Provider.of<DeliveryProvider>(context,listen: false).delivery;
    return Scaffold(
      body: Align(
          alignment: Alignment.center,
          child:Column(
            children: [
              SizedBox(height: 35),
              Card(
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(30.0),
                  ),
                  margin: EdgeInsets.symmetric(horizontal: kSpacingUnit.w*1.7,vertical: kSpacingUnit.w*1),
                  clipBehavior: Clip.antiAlias,
                  color:Colors.deepOrangeAccent,
                  shadowColor: Color(0xFFE9884A),
                  elevation: 5.0,
                  child: Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 5.0,vertical: 20.0),
                      child: Row(
                        children: <Widget>[
                          Expanded(
                              child:Column(
                                children: <Widget>[
                                  // Text(
                                  //     "STATUS",
                                  //     style: kTitleTextStyle.copyWith(
                                  //       fontWeight: FontWeight.w500,
                                  //       fontSize: kSpacingUnit.w*2,
                                  //     )
                                  // ),
                                  SizedBox(height:kSpacingUnit.w*1.1),
                                  Text(
                                    _onShift?"\t\t     • Currently on shift":"\t\t     • Currently not on\n\t\t\t\t\t\t\t\t\tshift",
                                    style: kTitleTextStyle.copyWith(
                                      fontWeight: FontWeight.w500,
                                      fontSize: kSpacingUnit.w*1.7,
                                      color: Colors.white,
                                    ),

                                  ),
                                  SizedBox(height: 5),
                                  Text(
                                    _isDelivery?"\t\t\t\t\t\t• Currently delivering order":"• Currently not \n\t\t\t\t\t\tdelivering order",
                                    style: kTitleTextStyle.copyWith(
                                      fontWeight: FontWeight.w500,
                                      fontSize: kSpacingUnit.w*1.7,
                                      color: Colors.white,
                                    ),
                                    textAlign: TextAlign.center,
                                  ),
                                  SizedBox(height: 8),
                                  _onShift?RaisedButton(onPressed: () async {
                                    _userService.setDriverShift(false,context).then((value) =>
                                    {
                                      if(_isDelivery){
                                        ScaffoldMessenger.of(context)
                                            .showSnackBar(SnackBar(content: Text(
                                            "Please complete trip before ending shift")))
                                      }
                                      else if(value==true){
                                        setState((){
                                          _onShift=false;
                                        })
                                      }
                                      else{
                                        ScaffoldMessenger.of(context)
                                            .showSnackBar(SnackBar(content: Text(
                                            "Couldn't update shift")))
                                      }
                                    }
                                    );

                                  },
                                    child: Text(
                                      "END SHIFT",
                                    ),
                                    color: Theme.of(context).backgroundColor,
                                    shape: RoundedRectangleBorder(
                                      borderRadius: BorderRadius.circular(30.0),
                                    ),
                                    // color: Color(0xFFFA8940),
                                  ):RaisedButton(onPressed: () async {
                                    _userService.setDriverShift(true,context).then((value) =>
                                    {
                                      if(value==true){
                                        setState((){
                                          _onShift=true;
                                        })
                                      }
                                      else{
                                        ScaffoldMessenger.of(context)
                                            .showSnackBar(SnackBar(content: Text(
                                            "Couldn't update shift")))
                                      }
                                    }
                                    );
                                  },
                                    child: Text(
                                      "START SHIFT",
                                    ),
                                    color: Theme.of(context).backgroundColor,
                                    shape: RoundedRectangleBorder(
                                      borderRadius: BorderRadius.circular(30.0),
                                    ),
                                    // color: Color(0xFFFA8940),
                                  ),
                                ],
                              )
                          ),
                          Expanded(
                              child:Column(
                                children: <Widget>[
                                  CircleAvatar(
                                    radius: kSpacingUnit.w * 3,
                                    backgroundImage: AssetImage('assets/profile.jpg'),
                                  ),
                                  SizedBox(height:kSpacingUnit.w*2),
                                  Text(
                                    _email,
                                    style: kTitleTextStyle.copyWith(
                                      fontWeight: FontWeight.w500,
                                      fontSize: kSpacingUnit.w*1,
                                    ),

                                  ),
                                  SizedBox(height:kSpacingUnit.w*1),
                                  Text(
                                    "DRIVER",
                                    style: kTitleTextStyle.copyWith(
                                      fontWeight: FontWeight.w500,
                                      fontSize: kSpacingUnit.w*1,
                                    ),
                                  )


                                ],
                              )
                          ),

                        ],
                      )
                  )
              ),
              SizedBox(height:40),
              _isDelivery==false?Container(
                height: 320.0,
                child: Row(
                  children: <Widget>[
                    Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 16.0),
                      child: Container(
                        height: 240,
                        width: MediaQuery
                            .of(context)
                            .size
                            .width * 0.90,
                        child: Image(
                          color: Theme.of(context).primaryColor,
                          fit: BoxFit.fill,
                          image: AssetImage("assets/delivery_truck.png"),
                          colorBlendMode: BlendMode.dstATop,
                        ),
                      ),
                    ),
                  ],
                ),
              )
                  :Column(
                children: [
                  SizedBox(height: 15),
                  Container(
                      child: Padding(
                        padding: const EdgeInsets.symmetric(vertical: 0,horizontal: 16),
                        child: Row(

                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Container(
                              child: Text(
                                "DELIVERY ID: ",
                                style: kTitleTextStyle.copyWith(
                                    fontWeight: FontWeight.w400,
                                    fontSize: 20
                                ),
                              ),
                            ),
                            Container(
                              child: Text(
                                _delivery.deliveryID,
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
                                "STATUS of Order: ",
                                style: kTitleTextStyle.copyWith(
                                    fontWeight: FontWeight.w400,
                                    fontSize: 20
                                ),
                              ),
                            ),
                            Container(
                              child: Text(
                                _delivery.deliveryStatus,
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
                                "Customer: ",
                                style: kTitleTextStyle.copyWith(
                                    fontWeight: FontWeight.w400,
                                    fontSize: 20
                                ),
                              ),
                            ),
                            Container(
                              child: Text(
                                _delivery.customerID,
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
                                "Store name: ",
                                style: kTitleTextStyle.copyWith(
                                    fontWeight: FontWeight.w400,
                                    fontSize: 20
                                ),
                              ),

                            ),
                            Container(
                              child: Text(
                                _delivery.pickUpLocation.address,
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
                                "User's address: ",
                                style: kTitleTextStyle.copyWith(
                                    fontWeight: FontWeight.w400,
                                    fontSize: 20
                                ),
                              ),

                            ),
                            Container(
                              child: Text(
                                _delivery.dropOffLocation.address,
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
                ],
              ),
              _isDelivery?SizedBox(height: 50):Container(),
              _onShift?RaisedButton(onPressed: (){
                if(_isDelivery){
                  // _deliveryService.addDeliveryDetail("CollectingFromStore", "Driver has accepted delivery and collecting it from the store", _delivery.deliveryID, context);
                  // _deliveryService.UpdateDeliveryStatus(deliveryID, "CollectingFromStore", context);
                  Navigator.of(context).push(MaterialPageRoute(
                      builder: (BuildContext context) => DriverHomeScreen(0)//ProductPage(product: product),
                  ));
                }else {
                  _deliveryService.getNextOrderForDriver(context)
                      .then((value) =>
                  {
                    print(value),
                    if(value != ""){
                      setState(() {
                        deliveryID = value;
                        showDialog(
                          context: context,
                          builder: (BuildContext context) =>
                              _popUpSuccessfulFoundDeliveries(
                                  context),
                        );
                      })
                    } else
                      {
                        ScaffoldMessenger.of(context)
                            .showSnackBar(SnackBar(content: Text(
                            "No deliveries")))
                      }
                  }
                  );
                }
              },
                padding: EdgeInsets.all(15.0),
                shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.circular(30.0),
                ),
                color: Theme.of(context).backgroundColor,
                elevation: 5.0,
                child: Text(
                  _isDelivery==false?"Get Next Delivery":_startedDelivery==false?"Start delivery":_collectedFromStore?"Complete delivery":"Complete collection from store",
                  style: TextStyle(
                    color: Color(0xFFE9884A),
                    letterSpacing: 1.5,
                    fontSize: 18.0,
                    fontWeight: FontWeight.bold,
                    fontFamily: 'OpenSans',
                  ),
                ),
              ):Container(
                child: Text(
                    "Start shift to get deliveries",
                    style: kTitleTextStyle.copyWith(
                      fontSize: 25,
                    )
                ),
              ),
              SizedBox(height:13),
              _isDelivery&&(_collectedFromStore==false)?RaisedButton(onPressed: (){
                Navigator.of(context).push(MaterialPageRoute(
                    builder: (BuildContext context) => DriverHomeScreen(0) //ProductPage(product: product),
                ));
              },
                  padding: EdgeInsets.all(15.0),
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(30.0),
                  ),
                  color: Theme.of(context).backgroundColor,
                  elevation: 5.0,
                  child:Text("Cancel delivery",
                    style: TextStyle(
                      color: Color(0xFFE9884A),
                      letterSpacing: 1.5,
                      fontSize: 14.0,
                      fontWeight: FontWeight.w600,
                      fontFamily: 'OpenSans',
                    ),
                  )
              ):Container()
            ],
          )
      ),
    );
  }

}