import 'dart:io';

import 'package:flutter/material.dart';
import 'package:open_mail_app/open_mail_app.dart';
import 'package:url_launcher/url_launcher.dart';

class ContactPage extends StatefulWidget {
  @override
  _ContactPageState createState() => _ContactPageState();
}

class _ContactPageState extends State<ContactPage> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Column(
        children: [
          SizedBox(height: 35),
          GestureDetector(
            onTap: () {
              Navigator.pop(context);
            },
            child: Container(
              alignment: Alignment.centerLeft,
              child: Icon(
                Icons.chevron_left,
                size: 40,
              ),
            ),
          ),
          Container(
            height: MediaQuery.of(context).size.height * 0.8,
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                SizedBox(
                  height: 25,
                ),
                Container(
                  height: MediaQuery.of(context).size.height * 0.2,
                  child: Image(
                    fit: BoxFit.fill,
                    image: AssetImage("assets/gifs/customerService.gif"),
                  ),
                ),
                Container(
                    height: MediaQuery.of(context).size.height * 0.08,
                    alignment: Alignment.center,
                    width: double.infinity,
                    color: Colors.deepOrangeAccent,
                    child: Text(
                      "HELP AND SUPPORT",
                      style: TextStyle(
                          fontWeight: FontWeight.w900,
                          fontSize: 35,
                          color: Colors.white),
                      textAlign: TextAlign.center,
                    )),
                SizedBox(
                  height: 10,
                ),
                Container(
                    width: double.infinity,
                    child: Text(
                      "HAVING PROBLEMS?",
                      style:
                          TextStyle(fontWeight: FontWeight.w500, fontSize: 22),
                      textAlign: TextAlign.center,
                    )),
                Container(
                  child: Text(
                      "Contact the developers of this app through the social media links and contact details below",
                      textAlign: TextAlign.center,
                      style:
                          TextStyle(fontWeight: FontWeight.w300, fontSize: 18)),
                ),
                SizedBox(
                  height: 12,
                ),
                Container(
                  child: Text("Name: SUPER LEAGUE",
                      textAlign: TextAlign.center,
                      style:
                          TextStyle(fontWeight: FontWeight.w700, fontSize: 20)),
                ),
                SizedBox(
                  height: 12,
                ),
                Container(
                  child: Text("Email: superleague301@gmail.com",
                      textAlign: TextAlign.center,
                      style:
                          TextStyle(fontWeight: FontWeight.w700, fontSize: 15)),
                ),
                SizedBox(
                  height: 5,
                ),
                Container(
                  child: Text("Contact number: 0737111738",
                      textAlign: TextAlign.center,
                      style:
                          TextStyle(fontWeight: FontWeight.w700, fontSize: 15)),
                ),
                SizedBox(
                  height: 12,
                ),
                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    GestureDetector(
                      onTap: () async {
                        EmailContent email = EmailContent(
                          to: [
                            'superleague301@gmail.com',
                          ],
                          subject: 'HELP AND SUPPORT: ODOSLA',
                          body: 'Assistance needed',
                        );

                        OpenMailAppResult result =
                            await OpenMailApp.composeNewEmailInMailApp(
                                nativePickerTitle:
                                    'Select email app to compose',
                                emailContent: email);
                        if (!result.didOpen && !result.canOpen) {
                          ScaffoldMessenger.of(context).showSnackBar(
                              SnackBar(content: Text("No mail apps")));
                        } else if (!result.didOpen && result.canOpen) {
                          showDialog(
                            context: context,
                            builder: (_) => MailAppPickerDialog(
                              mailApps: result.options,
                              emailContent: email,
                            ),
                          );
                        }
                      },
                      child: Container(
                        height: MediaQuery.of(context).size.height * 0.1,
                        child: Image(
                          fit: BoxFit.fill,
                          image: AssetImage("assets/mail.png"),
                        ),
                      ),
                    ),
                    SizedBox(
                      width: 10,
                    ),
                    GestureDetector(
                      onTap: () {
                        openwhatsapp();
                      },
                      child: Container(
                        height: MediaQuery.of(context).size.height * 0.1,
                        child: Image(
                          fit: BoxFit.fill,
                          image: AssetImage("assets/phone.png"),
                        ),
                      ),
                    ),
                  ],
                )
              ],
            ),
          ),
        ],
      ),
    );
  }

  openwhatsapp() async {
    var whatsapp = "+27737111379";
    var whatsappURl_android =
        "whatsapp://send?phone=" + whatsapp + "&text=Help and Support Odosla";
    var whatappURL_ios =
        "https://wa.me/$whatsapp?text=${Uri.parse("Help and Support Odosla")}";
    if (Platform.isIOS) {
      // for iOS phone only
      if (await canLaunch(whatappURL_ios)) {
        await launch(whatappURL_ios, forceSafariVC: false);
      } else {
        ScaffoldMessenger.of(context)
            .showSnackBar(SnackBar(content: new Text("whatsapp no installed")));
      }
    } else {
      // android , web
      if (await canLaunch(whatsappURl_android)) {
        await launch(whatsappURl_android);
      } else {
        ScaffoldMessenger.of(context)
            .showSnackBar(SnackBar(content: new Text("whatsapp no installed")));
      }
    }
  }
}
