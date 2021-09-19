import 'package:flutter/material.dart';

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
            onTap: (){
              Navigator.pop(context);
            },
            child: Container(
              alignment: Alignment.centerLeft,
              child:Icon(Icons.chevron_left,size: 40,),
            ),
          ),
          Container(
            height: MediaQuery.of(context).size.height*0.8,
            child: Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: [
                SizedBox(height: 25,),
                Container(
                  height: MediaQuery.of(context).size.height*0.2,
                  child:Image(
                    fit: BoxFit.fill,
                    image: AssetImage("assets/gifs/customerService.gif"),
                  ),
                ),
                Container(
                    height: MediaQuery.of(context).size.height*0.08,
                    alignment: Alignment.center,
                    width: double.infinity,
                    color: Colors.deepOrangeAccent,
                    child: Text("HELP AND SUPPORT",style: TextStyle(fontWeight: FontWeight.w900,fontSize: 35,color: Colors.white),textAlign: TextAlign.center,)),
                SizedBox(height: 10,),
                Container(
                  width:double.infinity,
                  child: Text("HAVING PROBLEMS?", style:TextStyle(fontWeight: FontWeight.w500,fontSize: 22),textAlign: TextAlign.center,)
                ),
                Container(
                  child: Text("Contact the developers of this app through the social media links and contact details below",textAlign: TextAlign.center,style:TextStyle(fontWeight: FontWeight.w300,fontSize: 18)),
                ),
                SizedBox(height: 12,),
                Container(
                  child: Text("Name: SUPER LEAGUE",textAlign: TextAlign.center,style:TextStyle(fontWeight: FontWeight.w700,fontSize: 20)),
                ),

                Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Container(
                      height: MediaQuery.of(context).size.height*0.1,
                      child:Image(
                        fit: BoxFit.fill,
                        image: AssetImage("assets/mail.png"),
                      ),
                    ),
                    SizedBox(width: 10,),
                    Container(
                      height: MediaQuery.of(context).size.height*0.1,
                      child:Image(
                        fit: BoxFit.fill,
                        image: AssetImage("assets/phone.png"),
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

}