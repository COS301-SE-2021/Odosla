
import 'dart:convert';
import 'dart:io';

import 'package:csv/csv.dart';
import 'package:file_picker/file_picker.dart';
import 'package:flutter/material.dart';
import 'package:flutter_employee_app/services/ImporterService.dart';
import 'package:get_it/get_it.dart';
import 'package:loading_overlay/loading_overlay.dart';

import 'check.dart';

class ImporterScreen extends StatefulWidget {

  @override
  _ImporterScreenState createState() => _ImporterScreenState();


}

class _ImporterScreenState extends State<ImporterScreen> {

  ImporterService _importerService=GetIt.I.get();
  var picked;
  var currentCSV;
  double itemWidth = 110.0;
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
      child: Text('UPLOAD FILE'),
      onPressed: () async {
        picked = await FilePicker.platform.pickFiles(type: FileType.custom,
          allowedExtensions: ['csv'],);
        if (picked != null) {
          print("HERE");
          PlatformFile fileName = picked.files.first;
          setState(() {
            _fileName=fileName.name;
          });
          print(fileName.name);
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
          SizedBox(height: 30.0),
          Container(
              alignment: Alignment.centerLeft,
              height: 84.0,
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  Center(
                      child: Text(
                        "IMPORT",
                        style: TextStyle(
                            color: Colors.black,
                            fontWeight: FontWeight.bold,
                            fontSize: 36.0

                        ),
                      )
                  ),
                  Center(
                      child: Text(
                        _listOfRoles[selected],
                        style: TextStyle(
                            color: Colors.black,
                            fontWeight: FontWeight.bold,
                            fontSize: 36.0

                        ),
                      )
                  ),

                ],
              )
          )
        ]);
  }

  Widget _loadCSV(){
    return RaisedButton(
      elevation: 5.0,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.circular(30.0),
      ),
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
        floatingActionButton: MyHomePage(),
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
              child: Column(
                mainAxisAlignment: MainAxisAlignment.start,
                children: [
                  SizedBox(height: 80.0),
                  Text(
                    'Choose what to import',
                    style: TextStyle(
                      color: Colors.black,
                      fontFamily: 'OpenSans',
                      fontSize: 30.0,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  SizedBox(height: 15.0),
                  _decisionWheel(),
                  SizedBox(height: 35.0),
                  Center(
                    child: Column(
                        mainAxisAlignment: MainAxisAlignment.center,
                        children: <Widget>[
                          _filePicker(),
                          (_fileName=="")?Container():Text(_fileName, textAlign: TextAlign.center,),
                          _loadCSV()
                        ]
                    ),
                  ),
                ],
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