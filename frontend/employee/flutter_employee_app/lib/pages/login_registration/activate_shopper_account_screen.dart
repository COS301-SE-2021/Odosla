

import 'package:email_validator/email_validator.dart';
import 'package:flutter/material.dart';
import 'package:flutter_employee_app/services/UserService.dart';
import 'package:flutter_employee_app/utilities/my_navigator.dart';
import 'package:get_it/get_it.dart';
import 'package:loading_overlay/loading_overlay.dart';

class ActivateShopperAccountScreen extends StatefulWidget {
  @override
  _ActivateShopperAccountState createState() => _ActivateShopperAccountState();
}

class _ActivateShopperAccountState extends State<ActivateShopperAccountScreen> {


  final UserService _userService = GetIt.I.get();
  bool _isInAsyncCall=false;
  bool _isValidEmail=false;
  String _email="";
  String _activationCode="";
  String _password="";

  Widget _popUpSuccessfulActivation(BuildContext context){

    return new AlertDialog(
      title: const Text('Successful activation'),
      content: new Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Text("Account with $_email"+"\n has been successfully activated",
             textAlign: TextAlign.center
          ),
        ],
      ),
      actions: <Widget>[
        new FlatButton(
          onPressed: () {
            MyNavigator.goToShopperIntro(context);
          },
          child: const Text('Proceed to app'),
        ),
        new FlatButton(
          onPressed: () {
            Navigator.pop(context, false);
          },
          textColor: Theme.of(context).primaryColor,
          child: Icon(
              Icons.close_rounded,
              color:Colors.red
          ),
        ),
      ],
    );
  }
  Widget _popUpSuccessfulActivationLoginScreen(BuildContext context){

    return new AlertDialog(
      title: const Text('Successful activation'),
      content: new Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Text("Account with $_email"+"\n has been successfully activated",
              textAlign: TextAlign.center
          ),
        ],
      ),
      actions: <Widget>[
        new FlatButton(
          onPressed: () {
            MyNavigator.goToLogin(context);
          },
          child: const Text('Please login'),
        ),
        new FlatButton(
          onPressed: () {
            Navigator.pop(context, false);
          },
          textColor: Theme.of(context).primaryColor,
          child: Icon(
              Icons.close_rounded,
              color:Colors.red
          ),
        ),
      ],
    );
  }
  Widget _popUpActivationExpired(BuildContext context){
    return new AlertDialog(
      title: const Text('Successful activation'),
      content: new Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Text("Activation code for $_email has expired \n Request a new activation code to generate a new code"),
        ],
      ),
      actions: <Widget>[
        new FlatButton(
          onPressed: () {
            Navigator.pop(context, false);
          },
          textColor: Theme.of(context).primaryColor,
          child: Icon(
              Icons.close_rounded,
              color:Colors.red
          ),
        ),
      ],
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      resizeToAvoidBottomInset: false,
      appBar: AppBar(
        title: Text("Activate Account"),
      ),
      body: LoadingOverlay(
        child: Column(
          mainAxisSize: MainAxisSize.min,
          crossAxisAlignment: CrossAxisAlignment.start,
          children: <Widget>[
            Container(
              child: Stack(
                children: <Widget>[
                  Container(
                    padding: EdgeInsets.fromLTRB(15.0, 30.0, 0.0, 0.0),
                    child: Text('Activate',
                        style: TextStyle(
                            fontSize: 60.0, fontWeight: FontWeight.bold)),
                  ),
                  Container(
                    padding: EdgeInsets.fromLTRB(16.0, 78.0, 0.0, 0.0),
                    child: Text('Account',
                        style: TextStyle(
                            fontSize: 60.0, fontWeight: FontWeight.bold)),
                  ),
                  Container(
                    padding: EdgeInsets.fromLTRB(285.0, 60.0, 0.0, 0.0),
                    child: Text('!',
                        style: TextStyle(
                            fontSize: 70.0,
                            fontWeight: FontWeight.bold,
                            color: Color(0xFFE9884A))),
                  )
                ],
              ),
            ),
            Container(
                padding: EdgeInsets.only(top: 10.0, left: 20.0, right: 20.0),
                child: Column(
                  children: <Widget>[
                    Column(
                        children: <Widget>[Text(
                          'Enter the email address and activation code associated with your shopper account.',
                          style: TextStyle(
                              fontSize: 23.0,
                              fontWeight: FontWeight.bold,
                              color: Color(0xFF8D5E3E)),
                        ),
                          TextField(
                              decoration: InputDecoration(
                                  labelText: 'EMAIL',
                                  labelStyle: TextStyle(
                                      fontWeight: FontWeight.bold, color: Colors.grey),
                                  focusedBorder: UnderlineInputBorder(
                                      borderSide: BorderSide(color: Colors.orange))),
                              onChanged: (value) =>
                              {
                                setState(() {
                                  _isValidEmail = EmailValidator.validate(value);
                                  _email = value;
                                }),
                              }
                          ),
                          SizedBox(height: 6.0),
                          TextField(
                            enableInteractiveSelection: true,
                              decoration: InputDecoration(
                                  labelText: 'ACTIVATION CODE',
                                  labelStyle: TextStyle(
                                      fontWeight: FontWeight.bold, color: Colors.grey),
                                  focusedBorder: UnderlineInputBorder(
                                      borderSide: BorderSide(color: Colors.orange))),
                              onChanged: (value) =>
                              {
                                setState(() {
                                  _activationCode = value;
                                }),
                              }
                          ),
                          SizedBox(height: 25.0),
                          Container(
                            height: 40.0,
                            child: Material(
                              borderRadius: BorderRadius.circular(20.0),
                              shadowColor: Colors.orangeAccent,
                              color: Color(0xFFE9884A),
                              elevation: 7.0,
                              child: GestureDetector(
                                  onTap: () async {
                                    if(!_isValidEmail){
                                      ScaffoldMessenger.of(context)
                                          .showSnackBar(SnackBar(content: Text(
                                          "Please enter a valid email")));
                                    } else{
                                      setState(() {
                                        _isInAsyncCall = true;
                                      });
                                      await _userService.getRegistrationPassword().then((value) => {
                                        print(value),
                                        setState(() {
                                          _password=value!;
                                        }
                                        )
                                      });
                                      await _userService.verifyAccount(_email, _activationCode, "SHOPPER").then(
                                              (success) async {
                                                if (success == "true") {
                                                  if(_password!="") {
                                                    await _userService
                                                        .loginUser(
                                                        _email, _password,
                                                        "SHOPPER", context)
                                                        .then(
                                                            (success) {
                                                          if (success) {
                                                            _userService
                                                                .getJWTAsString(
                                                                context).then((
                                                                value) =>
                                                            {
                                                              showDialog(
                                                                context: context,
                                                                builder: (
                                                                    BuildContext context) =>
                                                                    _popUpSuccessfulActivation(
                                                                        context),
                                                              )
                                                            });
                                                          }
                                                          setState(() {
                                                            _isInAsyncCall =
                                                            false;
                                                          });
                                                        }
                                                    );
                                                  } else{
                                                    showDialog(
                                                      context: context,
                                                      builder: (
                                                          BuildContext context) =>
                                                          _popUpSuccessfulActivationLoginScreen(
                                                              context),
                                                    );
                                                  }
                                                } else if (success ==
                                                    "Activated") {
                                                  ScaffoldMessenger.of(context)
                                                      .showSnackBar(
                                                      SnackBar(content: Text(
                                                          "Already activated you're account - try logging in")));
                                                }
                                                else {
                                                  ScaffoldMessenger.of(context)
                                                      .showSnackBar(
                                                      SnackBar(content: Text(
                                                          "Failed to Verify Account - try resending activation code")));
                                                }
                                                setState(() {
                                                  _isInAsyncCall = false;
                                                }
                                                );
                                          }
                                      );
                                    }
                                  },
                                  child: Center(
                                      child: Text(
                                        'ACTIVATE ACCOUNT',
                                        style: TextStyle(
                                            fontWeight: FontWeight.bold, color: Colors.white),
                                      ))),
                            ),
                          ),
                          SizedBox(height:5.0),
                          Container(
                            height: 20.0,
                            child: Material(
                              // borderRadius: BorderRadius.circular(20.0),
                              // shadowColor: Colors.orangeAccent,
                              // color: Color(0xFFE9884A),
                              // elevation: 7.0,
                              child: GestureDetector(
                                  onTap: () {
                                    if(!_isValidEmail){
                                      ScaffoldMessenger.of(context)
                                          .showSnackBar(SnackBar(content: Text(
                                          "Please enter a valid email")));
                                    }
                                  },
                                  child: Center(
                                      child: Text(
                                        'RESEND ACTIVATION CODE',
                                        style: TextStyle(
                                          fontSize: 15.0,
                                          fontWeight: FontWeight.w400,
                                          color: Colors.black,
                                          decoration: TextDecoration.underline,
                                        ),
                                      ))),
                            ),
                          )
                        ]
                    )
                  ],
                )
            )
          ],

        ),
        isLoading: _isInAsyncCall,
        // demo of some additional parameters
        opacity: 0.5,
        color: Colors.white,
        progressIndicator: CircularProgressIndicator(
          color: Colors.orange,
        ),
      ),
    );
  }

}