import 'package:email_validator/email_validator.dart';
import 'package:flutter/material.dart';
import 'package:flutter_employee_app/services/UserService.dart';
import 'package:flutter_employee_app/utilities/constants.dart';
import 'package:flutter_employee_app/utilities/functions.dart';
import 'package:flutter_employee_app/utilities/my_navigator.dart';
import 'package:flutter/services.dart';
import 'package:loading_overlay/loading_overlay.dart';
import 'package:get_it/get_it.dart';

class LoginScreen extends StatefulWidget {
  @override
  _LoginScreenState createState() => _LoginScreenState();
}

class _LoginScreenState extends State<LoginScreen> {

  final UserService _userService = GetIt.I.get();

  bool _rememberMe = false;
  bool _isValidEmail = false;
  String _email = "";
  String _password = "";
  bool _isValidPassword = false;
  double itemWidth = 100.0;
  double itemWidth2 = 20;
  int itemCount = 3;
  int selected = 1;
  bool _isInAsyncCall = false;
  bool _showPassword=false;

  final List<String> _pictures = <String>[
    "assets/shopper.png",
    "assets/driver.jpg",
    "assets/admin.png",
  ];

  final List<String> _listOfRoles = <String>[
    "Shopper",
    "Driver",
    "Admin",
  ];

  final List<String> _typeOfUser = <String>[
    "SHOPPER",
    "DRIVER",
    "ADMIN",
  ];

  FixedExtentScrollController _scrollController =
  FixedExtentScrollController(initialItem: 1);

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
          height: 60.0,
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
          height: 60.0,
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

  Widget _buildForgotPasswordBtn() {
    return Container(
      alignment: Alignment.centerRight,
      child: FlatButton(
        onPressed: () => MyNavigator.goToForgotPassword(context),
        padding: EdgeInsets.only(right: 0.0),
        child: Text(
          'Forgot Password?',
          style: kLabelStyle,
        ),
      ),
    );
  }

  Widget _buildRememberMeCheckbox() {
    return Container(
      height: 20.0,
      child: Row(
        children: <Widget>[
          Theme(
            data: ThemeData(unselectedWidgetColor: Colors.white),
            child: Checkbox(
              value: _rememberMe,
              checkColor: Colors.green,
              activeColor: Colors.white,
              onChanged: (value) {
                setState(() {
                  _rememberMe = value!;
                });
              },
            ),
          ),
          Text(
            'Remember me',
            style: kLabelStyle,
          ),
        ],
      ),
    );
  }

  Widget _buildLoginBtn() {
    return Container(
      padding: EdgeInsets.symmetric(vertical: 25.0),
      width: double.infinity,
      child: RaisedButton(
        elevation: 5.0,
        onPressed: () {
          setState(() async {
            if(_isValidEmail && _isValidPassword){
              setState(() {
                _isInAsyncCall = true;
              });
              await _userService.loginUser(_email, _password, _typeOfUser[selected],context).then(
                  (success) async {
                    if(success){
                      await _userService.getCurrentUser(context);
                      selected==0?MyNavigator.goToShopperHomePage(context): selected==1?MyNavigator.goToDriverHomePage(context):MyNavigator.goToAdminHomePage(context);
                    }
                    setState(() {
                      _isInAsyncCall = false;
                    });
                  }

              );

            } else{
              var text="";
              if(!_isValidEmail){
                text+="Please enter a valid email";
              }else if(!_isValidPassword){
                text+="Please enter a valid password";
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
          'LOGIN',
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

  Widget _buildDifferentUserSignIns() {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: <Widget>[
        CircleAvatar(
          backgroundColor: Colors.white,
          radius: 50.0,
          child: Icon(
            Icons.shopping_cart,
            color: Colors.deepOrange,
            size: 50.0,
          ),
        ),
        Padding(
          padding: EdgeInsets.only(top: 10.0),
        ),
        Text(
          "Odosla",
          style: TextStyle(
              color: Colors.white,
              fontWeight: FontWeight.bold,
              fontSize: 24.0),
        )
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
                                        Radius.circular(15.0)),

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


  Widget _buildSignupBtn() {
    return GestureDetector(
      onTap: () => MyNavigator.goToRegister(context),
      child: RichText(
        text: TextSpan(
          children: [
            TextSpan(
              text: selected!=2?'Don\'t have a ' + _listOfRoles[selected] + ' Account? ':"",
              style: TextStyle(
                color: Colors.white,
                fontSize: 15.0,
                fontWeight: FontWeight.w400,
              ),
            ),
            TextSpan(
              text: 'Sign Up',
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
                    horizontal: 40.0,
                    vertical: 60.0,
                  ),
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: <Widget>[
                      SizedBox(height: 17.0),
                      Text(
                        'Sign In',
                        style: TextStyle(
                          color: Colors.white,
                          fontFamily: 'OpenSans',
                          fontSize: 30.0,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      SizedBox(height: 5.0),
                      _decisionWheel(),
                      // _buildDifferentUserSignIns(),
                      _buildEmailTF(),
                      SizedBox(
                        height: 20.0,
                      ),
                      _buildPasswordTF(),
                      _buildForgotPasswordBtn(),
                      _buildRememberMeCheckbox(),
                      _buildLoginBtn(),
                      _buildSignInWithText(),
                      _buildSignupBtn(),
                    ],
                  ),
                ),
              ),
                isLoading: _isInAsyncCall,
                // demo of some additional parameters
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
