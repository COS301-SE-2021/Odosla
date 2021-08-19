import 'package:flutter/material.dart';
import 'package:flutter_employee_app/pages/shopper/shopper_profile_screen.dart';
import 'package:flutter_employee_app/services/UserService.dart';
import 'package:get_it/get_it.dart';

class EditProfilePage extends StatefulWidget {
  @override
  _EditProfilePageState createState() => _EditProfilePageState();
}

class _EditProfilePageState extends State<EditProfilePage> {
  bool showPassword = false;
  String _email="";
  String _name="";
  String _surname="";
  String _phoneNumber="";
  bool _changingPassword=false;
  final UserService _userService=GetIt.I.get();
  void initState() {
    _userService.getCurrentUser(context).then((value) =>
    {
      print(value),
      setState(() {
        _name = value!.name;
        _surname = value.surname;
        _email = value.email;
        _phoneNumber=value.phoneNumber;
      })
    }

    );
  }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        backgroundColor: Colors.deepOrangeAccent,
        elevation: 1,
        leading: IconButton(
          icon: Icon(
            Icons.arrow_back,
            color: Colors.white,
          ),
          onPressed: () {
            Navigator.of(context).push(MaterialPageRoute(
                builder: (BuildContext context) => ShopperProfileScreen()));
          },
        ),
        actions: [
          ElevatedButton(onPressed:(){
            setState(() {
              _changingPassword=!_changingPassword;
            });
          } , child: Text(_changingPassword?"Edit profile":"Change Password",style: TextStyle(color: Colors.white)),style: ButtonStyle(backgroundColor: MaterialStateProperty.all<Color>(Colors.deepOrange),elevation: MaterialStateProperty.all<double>(17.0),shadowColor: MaterialStateProperty.all<Color>(Colors.white)),)
        ],
      ),
      body: Container(
        padding: EdgeInsets.only(left: 16, top: 25, right: 16),
        child: GestureDetector(
          onTap: () {
            FocusScope.of(context).unfocus();
          },
          child: ListView(
            children: [
              Text(
                "Edit Profile",
                style: TextStyle(fontSize: 25, fontWeight: FontWeight.w500),
              ),
              SizedBox(
                height: 15,
              ),
              Center(
                child: Stack(
                  children: [
                    Container(
                      width: 130,
                      height: 130,
                      decoration: BoxDecoration(
                          border: Border.all(
                              width: 4,
                              color: Theme.of(context).scaffoldBackgroundColor),
                          boxShadow: [
                            BoxShadow(
                                spreadRadius: 2,
                                blurRadius: 10,
                                color: Colors.black.withOpacity(0.1),
                                offset: Offset(0, 10))
                          ],
                          shape: BoxShape.circle,
                          image: DecorationImage(
                              fit: BoxFit.cover,
                              image: AssetImage("assets/profile.jpg"))),
                    ),
                    Positioned(
                        bottom: 0,
                        right: 0,
                        child: _changingPassword==false?Container(
                          height: 40,
                          width: 40,
                          decoration: BoxDecoration(
                            shape: BoxShape.circle,
                            border: Border.all(
                              width: 4,
                              color: Theme.of(context).scaffoldBackgroundColor,
                            ),
                            color: Colors.deepOrangeAccent,
                          ),
                          child: Icon(
                            Icons.edit,
                            color: Colors.white,
                          ),
                        ):Container()),
                  ],
                ),
              ),
              SizedBox(
                height: 35,
              ),
              _changingPassword==false?buildTextField("Name",_name, false):buildTextField("Current password","", true),
              _changingPassword==false?buildTextField("Surname", _surname, false):buildTextField("New password","", true),
              _changingPassword==false?buildTextField("Phone number", _phoneNumber, false):Container(),
              _changingPassword==false?buildTextField("Email", _email, false):Container(),
              SizedBox(
                height: 35,
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  OutlineButton(
                    padding: EdgeInsets.symmetric(horizontal: 50),
                    shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(20)),
                    onPressed: () {
                      Navigator.of(context).push(MaterialPageRoute(
                          builder: (BuildContext context) => ShopperProfileScreen()));
                    },
                    child: Text("CANCEL",
                        style: TextStyle(
                            fontSize: 14,
                            letterSpacing: 2.2,
                            color: Colors.black)),
                  ),
                  RaisedButton(
                    onPressed: () {},
                    color: Colors.deepOrangeAccent,
                    padding: EdgeInsets.symmetric(horizontal: 50),
                    elevation: 2,
                    shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.circular(20)),
                    child: Text(
                      "SAVE",
                      style: TextStyle(
                          fontSize: 14,
                          letterSpacing: 2.2,
                          color: Colors.white),
                    ),
                  )
                ],
              )
            ],
          ),
        ),
      ),
    );
  }

  Widget buildTextField(
      String labelText, String placeholder, bool isPasswordTextField) {
    return Padding(
      padding: const EdgeInsets.only(bottom: 35.0),
      child: TextField(
        obscureText: isPasswordTextField ? showPassword : false,
        decoration: InputDecoration(
            suffixIcon: isPasswordTextField
                ? IconButton(
              onPressed: () {
                setState(() {
                  showPassword = !showPassword;
                });
              },
              icon: Icon(
                Icons.remove_red_eye,
                color: Colors.grey,
              ),
            )
                : null,
            contentPadding: EdgeInsets.only(bottom: 3),
            labelText: labelText,
            floatingLabelBehavior: FloatingLabelBehavior.always,
            hintText: placeholder,
            // focusedBorder: new UnderlineInputBorder(
            //   borderSide: new BorderSide(
            //     color: Colors.deepOrangeAccent
            //   )
            // ),
            hintStyle: TextStyle(
              fontSize: 16,
              fontWeight: FontWeight.bold,
              color: Colors.black,
            )),
      ),
    );
  }
}