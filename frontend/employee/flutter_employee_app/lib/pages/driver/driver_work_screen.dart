import 'package:flutter/material.dart';
import 'package:flutter_employee_app/models/Customer.dart';
import 'package:flutter_employee_app/models/Delivery.dart';
import 'package:flutter_employee_app/models/GeoPoint.dart';
import 'package:flutter_employee_app/models/User.dart';
import 'package:flutter_employee_app/pages/driver/driver_map.dart';
import 'package:flutter_employee_app/provider/UtilityProvider.dart';
import 'package:flutter_employee_app/provider/delivery_provider.dart';
import 'package:flutter_employee_app/provider/user_provider.dart';
import 'package:flutter_employee_app/services/DeliveryService.dart';
import 'package:flutter_employee_app/services/UserService.dart';
import 'package:flutter_employee_app/utilities/constants.dart';
import 'package:flutter_employee_app/utilities/my_navigator.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'package:get_it/get_it.dart';
import 'package:loading_overlay/loading_overlay.dart';
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
  String _name="";
  bool _startedDelivery=false;
  bool _collectedFromStore=false;
  bool _startedDeliveringToCustomer=false;
  bool _collectedByCustomer=false;
  bool _delivered=false;
  bool _isLoading=true;
  DeliveryService _deliveryService=GetIt.I.get();

  Widget _popUpSuccessfulFoundDeliveries(BuildContext context){

    Delivery _delivery=Provider.of<DeliveryProvider>(context).delivery;

    return new AlertDialog(
      title: const Text('FOUND A DELIVERY',textAlign: TextAlign.center),
      content: new Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.center,
        children: <Widget>[
          Text("There is a delivery from \n"+_delivery.pickUpLocation.address+" \n to \n"+_delivery.dropOffLocation.address+"\n Accept it?",
              textAlign: TextAlign.center
          ),
        ],
      ),
      actionsPadding: EdgeInsets.only(right:30),
      actions: <Widget>[
        new FlatButton(
          onPressed: () async {
            await _deliveryService.assignDriverToDelivery(_delivery.deliveryID, context).then((value) =>
            {
              setState((){
                if(value==true){
                  _isDelivery=true;
                  Navigator.pop(context, false);
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
          child: const Text('Accept',textAlign: TextAlign.left),
        ),
        new FlatButton(
          onPressed: () async {
            Provider.of<DeliveryProvider>(context,listen: false).delivery=Delivery("", new GeoPoint(0.0, 0.0, ""),new GeoPoint(0.0, 0.0, ""), "","", "", "", "", 0.0, false);
            Navigator.pop(context, false);
          },
          child: const Text('Decline'),
        ),
      ],
    );
  }

  Widget _popUpGoToMap(BuildContext context){
    return new AlertDialog(
      title: const Text('Go to map'),
      content: new Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Text("Go to map to get direction ",
              textAlign: TextAlign.center
          ),
        ],
      ),
      actions: <Widget>[
        new FlatButton(
          onPressed: () async {
            Navigator.of(context).push(MaterialPageRoute(
                builder: (BuildContext context) => DriverHomeScreen(0) //ProductPage(product: product),
            ));
          },
          child: const Text('Go to map'),
        ),
        new FlatButton(
          onPressed: () async {
            Navigator.pop(context, false);
          },
          child: Icon(
            Icons.cancel_rounded,
            color: Colors.red,
          ),
        ),
      ],
    );
  }


  initState(){
    bool isUser = Provider.of<UserProvider>(context,listen: false).isUser();
    if (isUser){
      User user = Provider.of<UserProvider>(context,listen: false).user;
      _email=user.email;
      _name=user.name;
      _onShift=user.onShift;
    }else {
      _userService.getCurrentUser(context).then((value) =>
      {
        setState(() {
          _email = value!.email;
          _name=value.name;
          _onShift = value.onShift;
          if (_onShift == null) {
            _onShift = false;
          }
        })
      }
      );
    }
    setDelivery();
  }

  void setDelivery() async {
    Delivery delivery=await Provider.of<DeliveryProvider>(context,listen: false).delivery;
    if(delivery.deliveryID==""){
      setState(() {
        _isDelivery=false;
      });
    }
    else if(delivery.deliveryStatus=="WaitingForShoppers"){
      setState(() {
        _isDelivery=true;
      });
    }
    else if(delivery.deliveryStatus=="CollectingFromStore"){
      setState(() {
        _isDelivery=true;
        _startedDelivery=true;
      });

    }else if(delivery.deliveryStatus=="CollectedByDriver"){
      setState(() {
        _isDelivery=true;
        _startedDelivery=true;
        _collectedFromStore=true;
      });

    }else if(delivery.deliveryStatus=="DeliveringToCustomer"){
      setState(() {
        _isDelivery=true;
        _startedDelivery=true;
        _collectedFromStore=true;
        _startedDeliveringToCustomer=true;
      });

    }else if(delivery.deliveryStatus=="Collected"){
      setState(() {
        _isDelivery=false;
        _startedDelivery=false;
        _collectedFromStore=false;
        _startedDeliveringToCustomer=false;
        Provider.of<DeliveryProvider>(context,listen: false).delivery=Delivery("", new GeoPoint(0.0, 0.0, ""),new GeoPoint(0.0, 0.0, ""), "","", "", "", "", 0.0, false);
      });
    }
    setState(() {
      _isLoading=false;
    });
  }

  @override
  Widget build(BuildContext context) {
    _onShift=true;
    ScreenUtil.init(
        BoxConstraints(
            maxWidth: MediaQuery.of(context).size.width,
            maxHeight: MediaQuery.of(context).size.height),
        designSize: Size(414, 896),
        orientation: Orientation.portrait);
    Delivery _delivery=Provider.of<DeliveryProvider>(context,listen: false).delivery;
    Customer _customer=Provider.of<DeliveryProvider>(context,listen: false).customer;
    return Scaffold(
      body: LoadingOverlay(
        child: Container(
          height: MediaQuery.of(context).size.height,
          child: Column(
            children: [
              SizedBox(height: MediaQuery.of(context).size.height*0.06),
              Card(
                  shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(30.0),
                  ),
                  margin: EdgeInsets.symmetric(horizontal: kSpacingUnit.w*1.7,vertical: kSpacingUnit.w*1),
                  clipBehavior: Clip.antiAlias,
                  color:Theme.of(context).backgroundColor,
                  shadowColor: Colors.grey,
                  elevation: 5.0,
                  child: Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 5.0,vertical: 20.0),
                      child: Row(
                        children: <Widget>[
                          Container(
                              height: MediaQuery.of(context).size.height*0.15,
                              width: MediaQuery.of(context).size.width*0.43,
                              alignment: Alignment.centerRight,
                              child:Column(
                                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                children: <Widget>[
                                  Container(
                                    height: MediaQuery.of(context).size.height*0.06,
                                    width: MediaQuery.of(context).size.width*0.3,
                                    child: FittedBox(
                                      fit: BoxFit.fitWidth,
                                      child: Text(
                                        _name,
                                        style: kTitleTextStyle.copyWith(
                                          fontWeight: FontWeight.w500,
                                        ),
                                      ),
                                    ),
                                  ),

                                  Text(
                                    _email,
                                    style: kTitleTextStyle.copyWith(
                                      fontWeight: FontWeight.w500,
                                      fontSize: kSpacingUnit.w*1,
                                    ),
                                  ),
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
                                          _isDelivery=false;
                                        })
                                      }
                                      else{
                                          ScaffoldMessenger.of(context)
                                              .showSnackBar(SnackBar(content: Text(
                                              "Couldn't update shift")))
                                        }
                                    });
                                  },
                                    child: Text(
                                        "END SHIFT",
                                        style: TextStyle(color: Colors.deepOrangeAccent, fontWeight: FontWeight.w900)
                                    ),
                                    color: Theme.of(context).backgroundColor,
                                    splashColor: Colors.grey,
                                    elevation: 5.0,
                                    shape: RoundedRectangleBorder(
                                      borderRadius: BorderRadius.circular(30.0),
                                    ),
                                    // color: Color(0xFFFA8940),
                                  ):RaisedButton(
                                    onPressed: () async {
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
                                      });
                                      },
                                    child: Text(
                                      "START SHIFT",
                                      style: TextStyle(color: Colors.deepOrangeAccent, fontWeight: FontWeight.w900),
                                    ),
                                    color: Theme.of(context).backgroundColor,
                                    splashColor: Colors.grey,
                                    elevation: 5.0,
                                    shape: RoundedRectangleBorder(
                                      borderRadius: BorderRadius.circular(10.0),
                                    ),
                                    // color: Color(0xFFFA8940),
                                  ),

                                ],
                              )
                          ),
                          Container(
                              height: MediaQuery.of(context).size.height*0.15,
                              width: MediaQuery.of(context).size.width*0.42,
                              child:Column(
                                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                                children: <Widget>[
                                  Text(
                                    "DRIVER",
                                    style: kTitleTextStyle.copyWith(
                                      fontWeight: FontWeight.w500,
                                      fontSize: kSpacingUnit.w*2,
                                    ),
                                  ),
                                  Container(
                                    height: MediaQuery.of(context).size.height*0.08,
                                    child:Image(
                                      fit: BoxFit.fill,
                                      image: _onShift?AssetImage('assets/gifs/deliveryTruck.gif'):AssetImage('assets/delivery/staticDeliveryTruck.png'),
                                    ),
                                  ),
                                  SizedBox(height: 5,),
                                  Text(
                                    _onShift?"• On Shift":"• Not on shift",
                                    style: kTitleTextStyle.copyWith(
                                      fontWeight: FontWeight.w500,
                                      fontSize: kSpacingUnit.w*1.5,
                                    ),
                                    textAlign: TextAlign.center,
                                  ),
                                ],
                              )
                          ),


                        ],
                      )
                  ),
              ),
              SizedBox(height: 15,),
              Container(
                color: Colors.deepOrangeAccent,
                width: double.infinity,
                height: 50,
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Text("Current Delivery ",style: TextStyle(fontWeight: FontWeight.w700,fontSize: 27, color: Colors.white),textAlign: TextAlign.center,),
                  ],
                ),
              ),
              _isDelivery==false||_onShift==false?Container(
                height: 320.0,
                child: Row(
                  children: <Widget>[
                    Padding(
                      padding: const EdgeInsets.symmetric(horizontal: 80.0),
                      child: Container(
                        height: MediaQuery
                            .of(context)
                            .size
                            .height * 0.3,
                        width: MediaQuery
                            .of(context)
                            .size
                            .width * 0.6,
                        child: Image(
                          fit: BoxFit.fill,
                          image: AssetImage("assets/gifs/deliveryGif.gif"),
                        ),
                      ),
                    ),
                  ],
                ),
              )
                  :Column(
                children: [
                  SizedBox(height: 25),
                  Container(
                      child: Padding(
                        padding: const EdgeInsets.symmetric(vertical: 0,horizontal: 16),
                        child: Row(

                          mainAxisAlignment: MainAxisAlignment.spaceBetween,
                          children: [
                            Container(
                              child: Text(
                                "Status:",
                                style: kTitleTextStyle.copyWith(
                                    fontWeight: FontWeight.w400,
                                    fontSize: 14
                                ),
                              ),
                            ),
                            Container(
                              child: Text(
                                _delivery.deliveryStatus,
                                style: kTitleTextStyle.copyWith(
                                    fontWeight: FontWeight.w700,
                                    fontSize: 16
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
                                "Customer's name: ",
                                style: kTitleTextStyle.copyWith(
                                    fontWeight: FontWeight.w400,
                                    fontSize: 14
                                ),
                              ),
                            ),
                            Container(
                              child: Text(
                                _customer.name +" "+_customer.surname,
                                style: kTitleTextStyle.copyWith(
                                    fontWeight: FontWeight.w700,
                                    fontSize: 16
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
                                "Customer's phone number: ",
                                style: kTitleTextStyle.copyWith(
                                    fontWeight: FontWeight.w400,
                                    fontSize: 14
                                ),
                              ),
                            ),
                            Container(
                              child: Text(
                                _customer.phoneNumber,
                                style: kTitleTextStyle.copyWith(
                                    fontWeight: FontWeight.w700,
                                    fontSize: 16
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
                                "Store address: ",
                                style: kTitleTextStyle.copyWith(
                                    fontWeight: FontWeight.w400,
                                    fontSize: 14
                                ),
                              ),

                            ),
                            Container(
                              child: Text(
                                _delivery.pickUpLocation.address,
                                style: kTitleTextStyle.copyWith(
                                    fontWeight: FontWeight.w700,
                                    fontSize: 16
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
                                "Customer address: ",
                                style: kTitleTextStyle.copyWith(
                                    fontWeight: FontWeight.w400,
                                    fontSize: 14
                                ),
                              ),

                            ),
                            Container(
                              child: Text(
                                _delivery.dropOffLocation.address,
                                style: kTitleTextStyle.copyWith(
                                    fontWeight: FontWeight.w700,
                                    fontSize: 16
                                ),
                              ),
                            ),
                          ],
                        ),
                      )
                  ),
                ],
              ),
              _onShift==false?Container():_isDelivery?SizedBox(height: 50):Container(),
              _onShift?RaisedButton(onPressed: () async {
                if(_isDelivery){
                  //Provider.of<UtilityProvider>(context, listen: false).redo=true;
                  if(_startedDelivery==false) {
                    await _deliveryService.UpdateDeliveryStatus(
                        deliveryID, "CollectingFromStore", context).then((
                        value) =>
                    {
                      if(value == true){
                        setState(() {
                          _startedDelivery = true;
                        }),
                        Provider.of<UtilityProvider>(context, listen: false).redo=true,
                        showDialog(
                          context: context,
                          builder: (BuildContext context) =>
                              _popUpGoToMap(
                                  context),
                        )

                      }
                      else{
                        ScaffoldMessenger.of(context)
                            .showSnackBar(SnackBar(content: Text(
                            "Couldn't start delivery")))
                      }
                    }
                    );
                  }
                  else if(_collectedFromStore==false){
                    await _deliveryService.UpdateDeliveryStatus(
                        deliveryID, "CollectedByDriver", context).then((
                        value) =>
                    {
                      if(value == true){
                        setState(() {
                          _collectedFromStore= true;
                        }),
                      }
                      else{
                        ScaffoldMessenger.of(context)
                            .showSnackBar(SnackBar(content: Text(
                            "Couldn't update status of delivery")))
                      }
                    }
                    );

                  }
                  else if(_startedDeliveringToCustomer==false){
                    await _deliveryService.UpdateDeliveryStatus(
                        deliveryID, "DeliveringToCustomer", context).then((
                        value) =>
                    {
                      if(value == true){
                        setState(() {
                          _startedDeliveringToCustomer= true;
                        }),
                        Provider.of<UtilityProvider>(context, listen: false).redo=true,
                        showDialog(
                          context: context,
                          builder: (BuildContext context) =>
                              _popUpGoToMap(
                                  context),
                        )

                      }
                      else{
                        ScaffoldMessenger.of(context)
                            .showSnackBar(SnackBar(content: Text(
                            "Couldn't update status of delivery")))
                      }
                    }
                    );
                  } else if(_collectedByCustomer==false){
                    await _deliveryService.UpdateDeliveryStatus(
                        deliveryID, "CollectedByCustomer", context).then((
                        value) =>
                    {
                      if(value == true){
                        setState(() {
                          _collectedByCustomer= true;
                        }),
                      }
                      else{
                        ScaffoldMessenger.of(context)
                            .showSnackBar(SnackBar(content: Text(
                            "Couldn't update status of delivery")))
                      }
                    }
                    );
                  }
                  else if(_delivered==false){
                    _deliveryService.UpdateDeliveryStatus(
                        deliveryID, "Delivered", context).then((
                        value) =>
                    {
                      if(value == true){
                        setState(() {
                          _isDelivery=false;
                          _delivered=false;
                          _collectedByCustomer=false;
                          _startedDeliveringToCustomer=false;
                          _collectedFromStore=false;
                          _startedDelivery=false;

                        }),
                    // Provider.of<UtilityProvider>(context,listen: false).redo=true,
                    Provider.of<DeliveryProvider>(context,listen: false).delivery=Delivery("",new GeoPoint(0, 0, ""),new GeoPoint(0, 0, ""),"","","","","",0,false),
                }
                      else{
                        ScaffoldMessenger.of(context)
                            .showSnackBar(SnackBar(content: Text(
                            "Couldn't update status of delivery")))
                      }
                    }
                    );
                  }

                }else {
                  _deliveryService.getNextOrderForDriver(context)
                      .then((value) =>
                  {
                    if(value != ""){
                      setState(() {
                        deliveryID = value;
                        showDialog(
                          context: context,
                          builder: (BuildContext context) =>
                              _popUpSuccessfulFoundDeliveries(
                                  context),
                        );})
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
                  _isDelivery==false?"Get Next Delivery":_startedDelivery==false?"Start delivery":_collectedFromStore==false?"Complete collection from store":_startedDeliveringToCustomer==false?"Start delivering to customer":_collectedByCustomer==false?"Customer collected delivery":"Complete delivery",
                  style: TextStyle(
                    color: Colors.deepOrangeAccent,
                    letterSpacing: 1.5,
                    fontSize: 18.0,
                    fontWeight: FontWeight.bold,
                    fontFamily: 'OpenSans',
                  ),
                ),
              ):Column(
                children: [
                  Text(
                    "NOT ON SHIFT",
                    style: TextStyle(
                      fontWeight: FontWeight.w800,
                      fontSize: 20,
                    ),
                    textAlign: TextAlign.center,
                ),
                  Text("Start shift to get new orders",
                    style: TextStyle(
                      // color: Colors.deepOrangeAccent,
                      fontWeight: FontWeight.w400,
                      fontSize: 16,
                    ),
                    textAlign: TextAlign.center,
                  )
              ]
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
          ),
        ),
        isLoading: _isLoading,
        // demo of some additional parameters
        opacity: 0.5,
        color: Colors.white,
        progressIndicator: CircularProgressIndicator(
          color: Colors.deepOrangeAccent,
        ),
      ),
    );
  }

}