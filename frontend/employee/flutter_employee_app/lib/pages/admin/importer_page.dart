import 'dart:convert';
import 'dart:io';

import 'package:csv/csv.dart';
import 'package:file_picker/file_picker.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_employee_app/services/ImporterService.dart';
import 'package:get_it/get_it.dart';
import 'package:loading_overlay/loading_overlay.dart';

class ImporterScreen extends StatefulWidget {
  @override
  _ImporterScreenState createState() => _ImporterScreenState();
}

class _ImporterScreenState extends State<ImporterScreen> {

  ImporterService _importerService=GetIt.I.get();
  var picked;
  var currentCSV;
  double itemWidth = 120.0;
  double itemWidth2 = 20;
  int selected = 1;
  int itemCount = 2;
  bool _isInAsyncCall = false;
  String _fileName="";

  FixedExtentScrollController _scrollController =
  FixedExtentScrollController(initialItem: 1);

  final List<String> _pictures = <String>[
    "assets/importer/store.png",
    "assets/grocery.jpg"
  ];

  final List<String> _listOfRoles = <String>[
    "STORE",
    "ITEMS"
  ];

  Widget _filePicker(){
    return RaisedButton(
      elevation: 5.0,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(30.0),
      ),
      color: Colors.black,
      child: Text('UPLOAD FILE', style: TextStyle(color: Colors.white),),
      onPressed: () async {
        picked = await FilePicker.platform.pickFiles(type: FileType.custom,
          allowedExtensions: ['csv'],);
        if (picked != null) {
          PlatformFile fileName = picked.files.first;
          setState(() {
            _fileName=fileName.name;
          });
          File file = File(picked.files.single.path);
          currentCSV=getCSVString(file);
        }
      },
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
                                  width: x == selected ? 220 : 150,
                                  height: x == selected ? 220 : 150,
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
          SizedBox(height: 30.0),
          Container(
              alignment: Alignment.centerLeft,
              height: MediaQuery.of(context).size.height*0.15,
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  Center(
                      child: Text(
                        "IMPORT",
                        style: TextStyle(
                            color: Colors.black,
                            fontWeight: FontWeight.bold,
                            fontSize: MediaQuery.of(context).size.height*0.05

                        ),
                      )
                  ),
                  Center(
                      child: Text(
                        _listOfRoles[selected],
                        style: TextStyle(
                            color: Colors.black,
                            fontWeight: FontWeight.bold,
                            fontSize: MediaQuery.of(context).size.height*0.05

                        ),
                      )
                  ),

                ],
              )
          )
        ]);
  }

  Widget _loadCSV(){
    return Container(
      padding: EdgeInsets.all(43),
      child: RaisedButton(
        elevation: 5.0,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(30.0),
        ),
        color: Theme.of(context).backgroundColor,
        child: Text('IMPORT '+_listOfRoles[selected],style: TextStyle(
            color: Color(0xFFE9884A),
            letterSpacing: 1.5,
            fontSize: 22.0,
            fontWeight: FontWeight.bold,
            fontFamily: 'OpenSans',
            ),),
        onPressed: () async {
          final String? csv = await getCSV();
          if(currentCSV==null){
            ScaffoldMessenger.of(context)
                .showSnackBar(SnackBar(content: Text("Something went wrong with processing CSV")));
          }else if (selected==1){
            setState(() {
              _isInAsyncCall=true;
            });
            await _importerService.importItems(context, csv!).then((value) => {
              if(value==true){
                setState(() {
                  _fileName="";
                  picked=null;
                  ScaffoldMessenger.of(context)
                      .showSnackBar(SnackBar(content: Text("Successfully imported Items")));
                })
              }
            });
            setState(() {
              _isInAsyncCall=false;
            });
          }else if(selected==0){
            setState(() {
              _isInAsyncCall=true;
            });
            await _importerService.importStores(context, csv!).then((value) => {
              if(value==true){
                setState(() {
                  _fileName="";
                  picked=null;
                  ScaffoldMessenger.of(context)
                      .showSnackBar(SnackBar(content: Text("Successfully imported Stores")));
                })
              }
            });
            setState(() {
              _isInAsyncCall=false;
            });
          }
        },
      ),
    );
  }

  Future<String> getCSVString(File picked) async {
    final csvFile = picked.openRead();
    List<List<dynamic>> data=await csvFile.transform(utf8.decoder).transform(CsvToListConverter(eol: '\n')).toList();
    String res = const ListToCsvConverter(eol: '\n', fieldDelimiter: ";").convert(data);
    print(res);
    return res;
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
        body: Stack(
          children: [
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
              child: Container(
                height: double.infinity,
                width: double.infinity,
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.start,
                  children: [
                    Container(
                      height: MediaQuery.of(context).size.height*0.57,
                      child: Column(
                        children: [
                          SizedBox(height:MediaQuery.of(context).size.height*0.11),
                          Container(
                            width: MediaQuery.of(context).size.height*0.43,
                            alignment: Alignment.center,
                            //color: Theme.of(context).backgroundColor,
                            // decoration: BoxDecoration(
                            //     color: Theme.of(context).backgroundColor,
                            //     boxShadow: [
                            //       BoxShadow(
                            //         color: Colors.grey,
                            //         offset: Offset(0.0, 1.0), //(x,y)
                            //         blurRadius: 6.0,
                            //       ),
                            //     ],
                            //     borderRadius: BorderRadius.all(Radius.circular(20))
                            // ),
                            child: Text(
                              'CHOOSE \nIMPORT TYPE',
                              textAlign: TextAlign.center,
                              style: TextStyle(
                                fontFamily: 'OpenSans',
                                fontSize: 30.0,
                                fontWeight: FontWeight.bold,

                              ),
                            ),
                          ),
                          SizedBox(height:MediaQuery.of(context).size.height*0.05),
                          _decisionWheel(),
                        ],
                      ),
                    ),
                    Container(
                      height: MediaQuery.of(context).size.height*0.22,
                      child: Column(
                        children: [
                          _filePicker(),
                          (_fileName=="")?Container():Text(_fileName, textAlign: TextAlign.center,),
                        ],
                      ),
                    ),
                    Container(
                      height: MediaQuery.of(context).size.height*0.15,
                      child: _loadCSV()

                    ),
                  ],
                ),
              ),
              isLoading: _isInAsyncCall,
              opacity: 0.5,
              color: Colors.white,
              progressIndicator: CircularProgressIndicator(
                color: Colors.orange,
            ),
            )
          ],
        )
    );
  }

  Future<String?> getCSV() async {
    return currentCSV;
  }

}