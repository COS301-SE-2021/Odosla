import 'package:flutter/material.dart';

class AdminWorkScreen extends StatefulWidget {
  @override
  _AdminWorkScreenState createState() => _AdminWorkScreenState();
}

class _AdminWorkScreenState extends State<AdminWorkScreen> {
  Color textColor = Color(0xFF000000);
  Color card1 = Color(0xFFFFBF37);
  Color card2 = Color(0xFF00CECE);
  Color card3 = Color(0xFFFB777A);
  Color card4 = Color(0xFFA5A5A5);


  @override
  Widget build(BuildContext context) {
    var app = Scaffold(
        body: GridView.count(
          crossAxisCount: 2,
          children: <Widget>[
            //Card 1
            Card(
                color: Color(0xFFFFBF37),
                margin: EdgeInsets.only(
                    left: 10.0, right: 5.0, top: 20.0, bottom: 20.0),
                elevation: 10.0,
                shape: RoundedRectangleBorder(
                    borderRadius: BorderRadius.circular(15.0)),
                child: Align(
                  alignment: Alignment.center,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: <Widget>[
                      Expanded(
                        flex: 8,
                        child: Padding(
                          padding: EdgeInsets.only(top: 30.0),
                          child: Icon(Icons.receipt_outlined,
                              size: 30.0, color: Colors.white),
                        ),
                      ),
                      Expanded(
                        flex: 2,
                        child: Text(
                          'Generate reports' ,
                          style: TextStyle(
                              color: Colors.white,
                              fontSize: 14.0,
                              fontWeight: FontWeight.bold),
                          textAlign: TextAlign.center,
                        ),
                      ),
                    ],
                  ),
                )),
            // Card 2
            Card(
              color: Color(0xFF00CECE),
              margin:
              EdgeInsets.only(left: 5.0, right: 10.0, top: 20.0, bottom: 20.0),
              elevation: 10.0,
              shape:
              RoundedRectangleBorder(borderRadius: BorderRadius.circular(15.0)),
              child: Align(
                  alignment: Alignment.center,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: <Widget>[
                      Expanded(
                          flex: 8,
                          child: Padding(
                            padding: EdgeInsets.only(top: 30.0),
                            child: Icon(Icons.people_rounded,
                                size: 30.0, color: Colors.white),
                          )),
                      Expanded(
                        flex: 2,
                        child: Text(
                          'Manage users' ,
                          style: TextStyle(
                              color: Colors.white,
                              fontSize: 14.0,
                              fontWeight: FontWeight.bold),
                          textAlign: TextAlign.center,
                        ),
                      ),
                    ],
                  )),
            ),
            //Card 3
            Card(
              //margin: EdgeInsets.only(left: 40.0, top: 40.0, right: 40.0, bottom: 20.0),
              color: Color(0xFFFB777A),
              margin:
              EdgeInsets.only(left: 10.0, right: 5.0, top: 10.0, bottom: 20.0),
              elevation: 10.0,
              shape:
              RoundedRectangleBorder(borderRadius: BorderRadius.circular(15.0)),
              child: Align(
                  alignment: Alignment.center,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: <Widget>[
                      Expanded(
                        flex: 8,
                        child: Padding(
                          padding: EdgeInsets.only(top: 30.0),
                          child: Icon(Icons.auto_graph_rounded,
                              size: 30.0, color: Colors.white),
                        ),
                      ),
                      Expanded(
                        flex: 2,
                        child: Text(
                          'See stat' ,
                          style: TextStyle(
                              color: Colors.white,
                              fontSize: 14.0,
                              fontWeight: FontWeight.bold),
                          textAlign: TextAlign.center,
                        ),
                      ),
                    ],
                  )),
            ),
            //Card 4
            Card(
              //   margin: EdgeInsets.only(left: 40.0, top: 40.0, right: 40.0, bottom: 10.0),
              margin:
              EdgeInsets.only(left: 5.0, right: 10.0, top: 10.0, bottom: 20.0),
              color: card4,
              elevation: 10.0,
              shape:
              RoundedRectangleBorder(borderRadius: BorderRadius.circular(15.0)),
              child: Align(
                  alignment: Alignment.center,
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.center,
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: <Widget>[
                      Expanded(
                          flex: 8,
                          child: Padding(
                            padding: const EdgeInsets.only(top: 30.0),
                            child: Icon(Icons.store,
                                size: 30.0, color: Colors.white),
                          )),
                      Expanded(
                        flex: 2,
                        child: Text(
                          'Manage Stores ',
                          style: TextStyle(
                              color: Colors.white,
                              fontSize: 14.0,
                              fontWeight: FontWeight.bold),
                          textAlign: TextAlign.center,
                        ),
                      ),
                    ],
                  )),
            ),
          ],
        ));
    Widget progress() {
      return Center(
        child: CircularProgressIndicator(),
      );
    }
    return app;
    // return dashboardModel.totalApprovedProduct == null ? progress() : app;
  }
}