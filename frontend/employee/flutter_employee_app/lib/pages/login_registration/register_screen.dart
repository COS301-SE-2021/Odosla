import 'dart:async';

import 'package:flutter/material.dart';

import 'package:email_validator/email_validator.dart';
import 'package:flutter_employee_app/services/UserService.dart';
import 'package:flutter_employee_app/utilities/constants.dart';
import 'package:flutter_employee_app/utilities/functions.dart';
import 'package:flutter_employee_app/utilities/my_navigator.dart';
import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:flutter/services.dart';
import 'package:get_it/get_it.dart';
import 'package:loading_overlay/loading_overlay.dart';

class RegisterScreen extends StatefulWidget {
  @override
  _RegisterScreenState createState() => _RegisterScreenState();
}

class _RegisterScreenState extends State<RegisterScreen>{

  final _storage = new FlutterSecureStorage();
  final UserService _userService = GetIt.I.get();
  String _jwtToken="";
  bool _rememberMe = false;
  bool _isValidEmail = false;
  bool _emailAlreadyInUse = false;
  String _email = "";
  String _password = "";
  bool _isValidPassword = false;
  bool _isInAsyncCall = false;
  bool _passwordMatches=false;
  bool _isValidPhoneNumber=true;
  double itemWidth = 100.0;
  double itemWidth2 = 20;
  int itemCount = 2;
  int selected = 1;
  String _name="";
  String _surname="";
  String _phoneNumber="";
  bool _showAdditionalWidgets=false;
  bool _showPassword=false;

  final List<String> _pictures = <String>[
    "assets/shopper.png",
    "assets/driver.jpg",
  ];

  final List<String> _listOfRoles = <String>[
    "Shopper",
    "Driver",
  ];


  FixedExtentScrollController _scrollController =
  FixedExtentScrollController(initialItem: 1);

  Widget _buildNameTF() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: <Widget>[
        Text(
          'First Name',
          style: kLabelStyle,
        ),
        SizedBox(height: 10.0),
        Container(
          alignment: Alignment.centerLeft,
          decoration: kBoxDecorationStyle,
          height: 50.0,
          child: TextField(
            keyboardType: TextInputType.emailAddress,
            style: TextStyle(
              color: Colors.white,
              fontFamily: 'OpenSans',
            ),
            onChanged: (value) =>
            {
              setState(() {
                _name = value;
              }),
            },
            decoration: InputDecoration(
              border: InputBorder.none,
              contentPadding: EdgeInsets.only(top: 14.0),
              prefixIcon: Icon(
                Icons.create_rounded,
                color: Colors.white,
              ),
              hintText: 'Enter your Name',
              hintStyle: kHintTextStyle,
            ),
          ),
        ),
      ],
    );
  }

  Widget _buildSurnameTF() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: <Widget>[
        Text(
          'Surname',
          style: kLabelStyle,
        ),
        SizedBox(height: 10.0),
        Container(
          alignment: Alignment.centerLeft,
          decoration: kBoxDecorationStyle,
          height: 50.0,
          child: TextField(
            keyboardType: TextInputType.emailAddress,
            style: TextStyle(
              color: Colors.white,
              fontFamily: 'OpenSans',
            ),
            onChanged: (value) =>
            {
              setState(() {
                _surname = value;
              }),
            },
            decoration: InputDecoration(
              border: InputBorder.none,
              contentPadding: EdgeInsets.only(top: 14.0),
              prefixIcon: Icon(
                Icons.create_rounded,
                color: Colors.white,
              ),
              hintText: 'Enter your Surname',
              hintStyle: kHintTextStyle,
            ),
          ),
        ),
      ],
    );
  }

  Widget _buildEmailTF() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: <Widget>[
        Text(
          'Email',
          style: kLabelStyle,
        ),
        SizedBox(height: 10.0),
        Container(
          alignment: Alignment.centerLeft,
          decoration: kBoxDecorationStyle,
          height: 50.0,
          child: TextField(
            keyboardType: TextInputType.emailAddress,
            style: TextStyle(
              color: Colors.white,
              fontFamily: 'OpenSans',
            ),
            onChanged: (value) =>
            {
              setState(() {
                _isValidEmail = EmailValidator.validate(value);
                _email = value;
              }),
            },
            decoration: InputDecoration(
              border: InputBorder.none,
              contentPadding: EdgeInsets.only(top: 14.0),
              prefixIcon: Icon(
                Icons.email,
                color: Colors.white,
              ),
              hintText: 'Enter your Email',
              hintStyle: kHintTextStyle,
            ),
          ),
        ),
        SizedBox(height: 5.0),
        _isValidEmail?Container():
        Container(
          alignment: Alignment.centerLeft,
          height: 16.0,
          child: Text(
            _isValidEmail ? "" : "Email is not valid",
            style: TextStyle(
              color: Colors.white,
              fontFamily: 'OpenSans',
            ),
          ),
        ),

      ],
    );
  }

  Widget _buildPasswordTF() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: <Widget>[
        Text(
          'Password',
          style: kLabelStyle,
        ),
        SizedBox(height: 10.0),
        Container(
          alignment: Alignment.centerLeft,
          decoration: kBoxDecorationStyle,
          height: 50.0,
          child: TextField(
            obscureText: _showPassword?false:true,
            style: TextStyle(
              color: Colors.white,
              fontFamily: 'OpenSans',
            ),
            onChanged: (value) =>
            {
              setState(() {
                _isValidPassword = validateStructure(value);
                _password = value;
              }),
            },
            decoration: InputDecoration(
              suffixIcon:
              IconButton(
                  onPressed: (){
                    setState(() {
                      _showPassword=!_showPassword;
                    });
                  },
                  icon: Icon(
                    Icons.remove_red_eye,
                    color: Colors.grey,
                  )),
              border: InputBorder.none,
              contentPadding: EdgeInsets.only(top: 14.0),
              prefixIcon: Icon(
                Icons.lock,
                color: Colors.white,
              ),
              hintText: 'Enter your Password',
              hintStyle: kHintTextStyle,
            ),
          ),
        ),
        SizedBox(height: 5.0),
        _isValidPassword?Container():
        Container(
          alignment: Alignment.centerLeft,
          height: 16.0,
          child: Text(
            _isValidPassword ? "" : "Password is not valid",
            style: TextStyle(
              color: Colors.white,
              fontFamily: 'OpenSans',
            ),
          ),
        ),
      ],
    );
  }

  Widget _buildReTypePasswordTF() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: <Widget>[
        Text(
          'Re-enter password',
          style: kLabelStyle,
        ),
        SizedBox(height: 10.0),
        Container(
          alignment: Alignment.centerLeft,
          decoration: kBoxDecorationStyle,
          height: 50.0,
          child: TextField(
            obscureText: _showPassword?false:true,
            style: TextStyle(
              color: Colors.white,
              fontFamily: 'OpenSans',
            ),
            onChanged: (value) =>
            {
              setState(() {
                _passwordMatches = validatePasswordMatches(value,_password);
              }),
            },
            decoration: InputDecoration(
              border: InputBorder.none,
              contentPadding: EdgeInsets.only(top: 14.0),
              prefixIcon: Icon(
                Icons.lock,
                color: Colors.white,
              ),
              hintText: 'Re-enter password',
              hintStyle: kHintTextStyle,
            ),
          ),
        ),
        SizedBox(height: 5.0),
        _passwordMatches?Container():
        Container(
          alignment: Alignment.centerLeft,
          height: 16.0,
          child: Text(
            _passwordMatches ? "" : "Passwords do not match",
            style: TextStyle(
              color: Colors.white,
              fontFamily: 'OpenSans',
            ),
          ),
        ),
      ],
    );
  }

  Widget _buildPhoneNumberTF() {
    return Column(
      crossAxisAlignment: CrossAxisAlignment.start,
      children: <Widget>[
        Text(
          'Phone Number',
          style: kLabelStyle,
        ),
        SizedBox(height: 10.0),
        Container(
          alignment: Alignment.centerLeft,
          decoration: kBoxDecorationStyle,
          height: 50.0,
          child: TextField(
            keyboardType: TextInputType.emailAddress,
            style: TextStyle(
              color: Colors.white,
              fontFamily: 'OpenSans',
            ),
            onChanged: (value) =>
            {
              setState(() {
                _isValidPhoneNumber = validatePhoneNumber(value);
                _phoneNumber = value;
              }),
            },
            decoration: InputDecoration(
              border: InputBorder.none,
              contentPadding: EdgeInsets.only(top: 14.0),
              prefixIcon: Icon(
                Icons.phone_iphone_rounded,
                color: Colors.white,
              ),
              hintText: 'Enter your Phone Number',
              hintStyle: kHintTextStyle,
            ),
          ),
        ),
        SizedBox(height: 5.0),
        _isValidPhoneNumber?Container():
        Container(
            alignment: Alignment.centerLeft,
            height: 16.0,
            child: Text(
            _isValidPhoneNumber ? "" : "Not valid Phone Number",
            style: TextStyle(
            color: Colors.white,
            fontFamily: 'OpenSans',
        ),
      )
        )
      ],
    );
  }

  Widget _showAdditionalWidgetsOption() {
    return GestureDetector(
      onTap: () => setState(() {
        if(_showAdditionalWidgets==false) {
          _showAdditionalWidgets = true;
        }else{
          _showAdditionalWidgets = false;
        }
      }),
      child: Align(
        alignment: Alignment.topRight,
        child: RichText(
          text: TextSpan(
            children: [
              TextSpan(
                text: _showAdditionalWidgets?'Show less ':"Show additional fields",
                style: TextStyle(
                  color: Colors.white,
                  fontSize: 15.0,
                  fontWeight: FontWeight.bold,
                  decoration: TextDecoration.underline,
                ),
              ),
            ],
          ),
        ),
      ),
    );
  }

  Widget _popUpSuccessfulRegisteration(BuildContext context){
    return new AlertDialog(
      title: const Text('Successful registration'),
      content: new Column(
        mainAxisSize: MainAxisSize.min,
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          Text("Please verify your account from the email sent to you at $_email"+"\nLogin to your account after verifying"),
        ],
      ),
      actions: <Widget>[
        new FlatButton(
          onPressed: () {
            selected==0?MyNavigator.goToShopperActivateAccount(context):MyNavigator.goToDriverActivateAccount(context);
          },
          child: const Text('Activate account'),
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

  Widget _buildRegisterBtn() {
    return Container(
      padding: EdgeInsets.symmetric(vertical: 25.0),
      width: double.infinity,
      child: RaisedButton(
        elevation: 5.0,
        onPressed: () {
          setState(() {
            if(_isValidEmail && _isValidPassword && _isValidPhoneNumber) {
              setState(() {
                _isInAsyncCall = true;
              });
              if(selected==1) {
                _userService.registerDriver(
                    _name, _surname, _email, _password, _phoneNumber).then(
                        (success) async {
                      if (success == "true") {
                        await Future.wait([
                          _storage.write(key: "passwordRegistration", value: _password),
                        ]);
                        showDialog(
                          context: context,
                          builder: (BuildContext context) => _popUpSuccessfulRegisteration(context),
                        );
                      } else if (success == "Email has already been used") {
                        setState(() {
                          _emailAlreadyInUse = true;
                        });
                        ScaffoldMessenger.of(context)
                            .showSnackBar(SnackBar(content: Text(
                            "Email has already been used for a Driver")));
                      } else if(success=="timeout"){
                        ScaffoldMessenger.of(context)
                            .showSnackBar(SnackBar(content: Text(
                            "Request timed out - please retry")));
                      }
                      else {
                        ScaffoldMessenger.of(context)
                            .showSnackBar(SnackBar(content: Text(
                            "Failed to Register Driver")));
                      }
                      setState(() {
                        _isInAsyncCall = false;
                      });
                    }
                );
              }
              else{
                {
                  _userService.registerShopper(
                      _name, _surname, _email, _password, _phoneNumber).then(
                          (success) async {
                        if (success == "true") {
                          _userService.setRegistrationPassword(_password);
                          showDialog(
                            context: context,
                            builder: (BuildContext context) => _popUpSuccessfulRegisteration(context),
                          );
                        } else if (success == "Email has already been used") {
                          setState(() {
                            _emailAlreadyInUse = true;
                          });
                          ScaffoldMessenger.of(context)
                              .showSnackBar(SnackBar(content: Text(
                              "Email has already been used for a Shopper account")));
                        } else if(success=="timeout"){
                          ScaffoldMessenger.of(context)
                              .showSnackBar(SnackBar(content: Text(
                              "Request timed out - please retry")));
                        }
                        else {
                          ScaffoldMessenger.of(context)
                              .showSnackBar(SnackBar(content: Text(
                              "Failed to Register Shopper")));
                        }
                        setState(() {
                          _isInAsyncCall = false;
                        });
                      }
                  );
                }
              }
            }else{
              var text="";
              if(!_isValidEmail){
                text+="Please enter a valid email";
              }else if(!_isValidPassword){
                text+="Please enter a valid password";
              }else if(!_isValidPhoneNumber){
                text+="Please enter a valid phone number";
              }
              ScaffoldMessenger.of(context)
                  .showSnackBar(SnackBar(content: Text(text)));
            }
          });
        },
        padding: EdgeInsets.all(15.0),
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(30.0),
        ),
        color: Colors.white,
        child: Text(
          'REGISTER',
          style: TextStyle(
            color: Color(0xFFE9884A),
            letterSpacing: 1.5,
            fontSize: 18.0,
            fontWeight: FontWeight.bold,
            fontFamily: 'OpenSans',
          ),
        ),
      ),
    );
  }

  Widget _buildSignInWithText() {
    return Column(
      children: <Widget>[
        Text(
          '- OR -',
          style: TextStyle(
            color: Colors.white,
            fontWeight: FontWeight.w400,
          ),
        ),
        SizedBox(height: 20.0),
        Text(
          'Sign in with',
          style: kLabelStyle,
        ),
      ],
    );
  }

  Widget _decisionWheel() {
    return Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: <Widget>[
          SizedBox(height: 5.0),
          Container(
              alignment: Alignment.center,
              height: 100.0,
              child: RotatedBox(
                  quarterTurns: -1,
                  child: ListWheelScrollView(itemExtent: itemWidth,
                    onSelectedItemChanged: (x) {
                      setState(() {
                        selected = x;
                      });
                    },
                    controller: _scrollController,
                    children: List.generate(
                        itemCount,
                            (x) =>
                            RotatedBox(
                                quarterTurns: 1,
                                child: AnimatedContainer(
                                  duration: Duration(milliseconds: 400),
                                  width: x == selected ? 190 : 140,
                                  height: x == selected ? 190 : 140,
                                  decoration: BoxDecoration(
                                    image: DecorationImage(
                                      scale: 2,
                                      image: new AssetImage(_pictures[x]),

                                    ),
                                    borderRadius: BorderRadius.all(
                                        Radius.circular(20.0)),

                                  ),
                                )
                            )
                    ),
                  )
              )
          ),
          Container(
              alignment: Alignment.centerLeft,
              height: 64.0,
              child: Column(

                mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  Center(
                      child: Text(
                        _listOfRoles[selected],
                        style: TextStyle(
                            color: Colors.white,
                            fontWeight: FontWeight.bold,
                            fontSize: 48.0

                        ),
                      )
                  )
                ],
              )
          )
        ]);
  }

  Widget _buildActivateAccountBtn() {
    return Column(
      children: <Widget>[
          Text(
            '-',
            style: TextStyle(
              color: Colors.white,
              fontWeight: FontWeight.w400,
            ),
          ),
          Container(
            alignment: Alignment.center,
            child: FlatButton(
              onPressed: () => selected==0?MyNavigator.goToShopperActivateAccount(context):MyNavigator.goToDriverActivateAccount(context),
              padding: EdgeInsets.only(right: 0.0),
              child: Text(selected==0?
                'Activate shopper account?':"Activate driver account?",
                style: TextStyle(
                  color: Colors.white,
                  fontWeight: FontWeight.bold,
                  fontFamily: 'OpenSans',
                  fontSize: 15.0,
                ),
          ),
        ),
      )]
    );
  }

  Widget _buildLoginBtn() {
    return GestureDetector(
      onTap: () => MyNavigator.goToLogin(context),
      child: RichText(
        text: TextSpan(
          children: [
            TextSpan(
              text: selected!=2?'Already have an Account? ':"",
              style: TextStyle(
                color: Colors.white,
                fontSize: 15.0,
                fontWeight: FontWeight.w400,
              ),
            ),
            TextSpan(
              text: 'Login',
              style: TextStyle(
                color: Colors.white,
                fontSize: 18.0,
                fontWeight: FontWeight.bold,
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _nameSurnamePhoneNumberWidget(){
      return Column(
          children: <Widget>[
            SizedBox(
              height: 7.0,
            ),
            _buildNameTF(),
            SizedBox(
              height: 10.0,
            ),
            _buildSurnameTF(),
            SizedBox(
              height: 10.0,
            ),
            _buildPhoneNumberTF(),
            SizedBox(
              height: 7.0,
            ),
          ]
      );

  }



  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body:
      AnnotatedRegion<SystemUiOverlayStyle>(
        value: SystemUiOverlayStyle.light,
        child: GestureDetector(
          behavior: HitTestBehavior.translucent,
          onTap: () => FocusScope.of(context).unfocus(),
          child: Stack(
            children: <Widget>[
              Container(
                height: double.infinity,
                width: double.infinity,
                decoration: BoxDecoration(
                  gradient: LinearGradient(
                    begin: Alignment.topCenter,
                    end: Alignment.bottomCenter,
                    colors: [
                      Color(0xFFE8E8E7),
                      Color(0xFFB6A08A),
                      Color(0xD3CB964C),
                      Color(0xFFC78760),
                    ],
                    stops: [0.1, 0.4, 0.7, 0.9],
                  ),
                ),
              ),
              LoadingOverlay(
              child:Container(
                height: double.infinity,
                child: SingleChildScrollView(
                  physics: AlwaysScrollableScrollPhysics(),
                  padding: EdgeInsets.symmetric(
                    horizontal: 60.0,
                    vertical: 60.0,
                  ),
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: <Widget>[
                      SizedBox(height: 12.0),
                      Text(
                        'Register ',
                        style: TextStyle(
                          color: Colors.white,
                          fontFamily: 'OpenSans',
                          fontSize: 30.0,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      SizedBox(height: 5.0),
                      _decisionWheel(),
                      SizedBox(
                        height: 5.0,
                      ),
                      _buildEmailTF(),
                      SizedBox(
                        height: 12.0,
                      ),
                      _buildPasswordTF(),
                      SizedBox(
                        height: 12.0,
                      ),

                      _buildReTypePasswordTF(),
                      SizedBox(
                        height: 12.0,
                      ),
                      _nameSurnamePhoneNumberWidget(),
                      //_showAdditionalWidgetsOption(),
                      _buildRegisterBtn(),
                      _buildSignInWithText(),
                      _buildLoginBtn(),
                      _buildActivateAccountBtn(),
                    ],
                  ),
                ),
              ),
                isLoading: _isInAsyncCall,
                opacity: 0.5,
                color: Colors.white,
                progressIndicator: CircularProgressIndicator(
                  color: Colors.orange,
                ),
               ),
            ],
          ),
        ),
      ),
    );
  }

}
