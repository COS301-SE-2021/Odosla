import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_employee_app/services/AnalyticsService.dart';
import 'package:flutter_employee_app/utilities/mobile.dart';
import 'package:get_it/get_it.dart';
import 'package:intl/intl.dart';
import 'package:loading_overlay/loading_overlay.dart';

class AnalyticsScreen extends StatefulWidget {
  @override
  _AnalyticsScreenState createState() => _AnalyticsScreenState();
}

class _AnalyticsScreenState extends State<AnalyticsScreen> {
  double itemWidth = 110.0;
  double itemWidth2 = 20;
  int selected = 1;
  int itemCount = 3;
  String _chosenValue = "PDF";
  bool _isInAsyncCall = false;

  final List<String> _pictures = <String>[
    "assets/analytics/financialReport.png",
    "assets/analytics/monthlyReport.png",
    "assets/analytics/userReport.png",
  ];

  final List<String> _listOfRoles = <String>[
    "FINANCIAL",
    "MONTHLY",
    "USER",
  ];

  AnalyticsService _analyticsService = GetIt.I.get();

  FixedExtentScrollController _scrollController =
      FixedExtentScrollController(initialItem: 1);

  Duration duration = Duration(days: 10000);
  Duration duration_month = Duration(days: 31);

  DateTime _dateTimeEnd = DateTime.now();
  DateTime _dateTimeStart = DateTime.now();

  Widget _dropDown() {
    return Column(
      children: [
        Text(
          "Type of Report",
          textAlign: TextAlign.center,
          style: TextStyle(fontSize: 18, fontWeight: FontWeight.bold),
        ),
        SizedBox(
          height: 10,
        ),
        Theme(
          data: Theme.of(context).copyWith(canvasColor: Colors.black),
          child: Container(
            decoration: BoxDecoration(
                borderRadius: BorderRadius.circular(10.0),
                color: Colors.black,
                border: Border.all()),
            child: DropdownButtonHideUnderline(
              child: DropdownButton<String>(
                elevation: 5,
                iconEnabledColor: Colors.black,
                iconDisabledColor: Colors.black,
                focusColor: Colors.black,
                value: _chosenValue,
                //elevation: 5,
                style: TextStyle(
                    color: Colors.white,
                    fontWeight: FontWeight.bold,
                    fontSize: 19),
                items: <String>['PDF', 'CSV']
                    .map<DropdownMenuItem<String>>((String value) {
                  return DropdownMenuItem<String>(
                    value: value,
                    child: SizedBox(
                        width: 90,
                        child: Text(value,
                            style: TextStyle(color: Colors.white),
                            textAlign: TextAlign.end)),
                  );
                }).toList(),
                hint: Text(
                  "Choose report type",
                  style: TextStyle(
                      color: Colors.black,
                      fontSize: 14,
                      fontWeight: FontWeight.w500),
                ),
                onChanged: (String? value) {
                  setState(() {
                    _chosenValue = value!;
                  });
                },
              ),
            ),
          ),
        ),
      ],
    );
  }

  Widget _calendarPickerStartDate() {
    // _dateTimeStart=_dateTimeEnd.subtract(duration_month);
    return Column(
      children: <Widget>[
        RaisedButton(
          elevation: 5.0,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(30.0),
          ),
          color: Colors.black,
          child: Text(
            'Start Date',
            style: TextStyle(fontSize: 15, color: Colors.white),
          ),
          onPressed: () {
            showDatePicker(
                context: context,
                initialDate: _dateTimeStart,
                firstDate: DateTime.now().subtract(duration),
                lastDate: DateTime.now(),
                builder: (BuildContext context, Widget? child) {
                  return Theme(
                    data: ThemeData(
                      primarySwatch: Colors.grey,
                      splashColor: Colors.black,
                      textTheme: TextTheme(
                        subtitle1: TextStyle(color: Colors.black),
                        button: TextStyle(color: Colors.black),
                      ),
                      accentColor: Colors.black,
                      colorScheme: ColorScheme.light(
                          primary: Colors.deepOrangeAccent,
                          primaryVariant: Colors.black,
                          secondaryVariant: Colors.black,
                          onSecondary: Colors.black,
                          onPrimary: Colors.white,
                          surface: Colors.black,
                          onSurface: Colors.black,
                          secondary: Colors.black),
                      dialogBackgroundColor: Colors.white,
                    ),
                    child: child ?? Text(""),
                  );
                }).then((date) {
              setState(() {
                print("WEE HEEREE");
                _dateTimeStart = date!;
              });
            });
          },
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
                  child: ListWheelScrollView(
                    itemExtent: itemWidth,
                    onSelectedItemChanged: (x) {
                      setState(() {
                        selected = x;
                      });
                    },
                    controller: _scrollController,
                    children: List.generate(
                        itemCount,
                        (x) => RotatedBox(
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
                                borderRadius:
                                    BorderRadius.all(Radius.circular(15.0)),
                              ),
                            ))),
                  ))),
          SizedBox(height: 19.0),
          Container(
              alignment: Alignment.centerLeft,
              height: MediaQuery.of(context).size.height * 0.15,
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: <Widget>[
                  Center(
                      child: Text(
                    _listOfRoles[selected],
                    style: TextStyle(
                      color: Colors.black,
                      fontWeight: FontWeight.bold,
                      fontSize: MediaQuery.of(context).size.height * 0.05,
                    ),
                  )),
                  Center(
                      child: Text(
                    "REPORT",
                    style: TextStyle(
                      color: Colors.black,
                      fontWeight: FontWeight.bold,
                      fontSize: MediaQuery.of(context).size.height * 0.05,
                    ),
                  ))
                ],
              ))
        ]);
  }

  Widget _calendarPickerEndDate() {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: <Widget>[
        RaisedButton(
          elevation: 5.0,
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(30.0),
          ),
          color: Colors.black,
          child: Text(
            'End Date',
            style: TextStyle(color: Colors.white, fontSize: 15),
          ),
          onPressed: () {
            showDatePicker(
                context: context,
                initialDate: _dateTimeEnd,
                firstDate: _dateTimeStart,
                lastDate: DateTime.now(),
                builder: (BuildContext context, Widget? child) {
                  return Theme(
                    data: ThemeData(
                      primarySwatch: Colors.grey,
                      splashColor: Colors.black,
                      textTheme: TextTheme(
                        subtitle1: TextStyle(color: Colors.black),
                        button: TextStyle(color: Colors.black),
                      ),
                      accentColor: Colors.black,
                      colorScheme: ColorScheme.light(
                          primary: Colors.deepOrangeAccent,
                          primaryVariant: Colors.black,
                          secondaryVariant: Colors.black,
                          onSecondary: Colors.black,
                          onPrimary: Colors.white,
                          surface: Colors.black,
                          onSurface: Colors.black,
                          secondary: Colors.black),
                      dialogBackgroundColor: Colors.white,
                    ),
                    child: child ?? Text(""),
                  );
                }).then((date) {
              setState(() {
                _dateTimeEnd = date!;
              });
            });
          },
        )
      ],
    );
  }

  Widget _createReportButton() {
    return Container(
      padding: EdgeInsets.all(24),
      child: RaisedButton(
        elevation: 5.0,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(30.0),
        ),
        color: Theme.of(context).backgroundColor,
        child: Text(
          'GENERATE REPORT',
          style: TextStyle(
            color: Color(0xFFE9884A),
            letterSpacing: 1.5,
            fontSize: 22.0,
            fontWeight: FontWeight.bold,
            fontFamily: 'OpenSans',
          ),
        ),
        onPressed: () async {
          String startMonth = _dateTimeStart.month.toString();
          String endMonth = _dateTimeEnd.month.toString();
          String startDay = _dateTimeStart.month.toString();
          String endDay = _dateTimeEnd.month.toString();
          if (startMonth.length == 1) {
            startMonth = "0" + startMonth;
          }
          if (endMonth.length == 1) {
            endMonth = "0" + endMonth;
          }
          if (startDay.length == 1) {
            startDay = "0" + startDay;
          }
          if (endDay.length == 1) {
            endDay = "0" + endDay;
          }
          String startDate = _dateTimeStart.year.toString() +
              "/" +
              startMonth +
              "/" +
              startDay +
              " 00:00:00";
          String endDate = _dateTimeEnd.year.toString() +
              "/" +
              endMonth +
              "/" +
              endDay +
              " 00:00:00";
          if (_chosenValue == "PDF" && selected == 0) {
            setState(() {
              _isInAsyncCall = true;
            });
            await _analyticsService
                .createFinancialPDFReport(context, startDate, endDate)
                .then((value) => {
                      if (value != null)
                        {
                          saveAndLaunchPDFFile(
                              context, value, "financialReport.pdf")
                        },
                      setState(() {
                        _isInAsyncCall = false;
                      })
                    });
          } else if (_chosenValue == "CSV" && selected == 0) {
            setState(() {
              _isInAsyncCall = true;
            });
            await _analyticsService
                .createFinancialCSVReport(context, startDate, endDate)
                .then((value) => {
                      if (value != null)
                        {
                          saveCSVFile(context, value, "financialReport.csv"),
                        },
                      setState(() {
                        _isInAsyncCall = false;
                      })
                    });
          } else if (_chosenValue == "PDF" && selected == 1) {
            setState(() {
              _isInAsyncCall = true;
            });
            await _analyticsService
                .createMonthlyPDFReport(context)
                .then((value) => {
                      if (value != null)
                        {
                          saveAndLaunchPDFFile(
                              context, value, "monthlyReport.pdf")
                        },
                      setState(() {
                        _isInAsyncCall = false;
                      })
                    });
          } else if (_chosenValue == "CSV" && selected == 1) {
            setState(() {
              _isInAsyncCall = true;
            });

            await _analyticsService
                .createMonthlyCSVReport(context)
                .then((value) => {
                      if (value != null)
                        {
                          saveCSVFile(context, value, "financialReport.csv"),
                        },
                      setState(() {
                        _isInAsyncCall = false;
                      })
                    });
          } else if (_chosenValue == "PDF" && selected == 2) {
            setState(() {
              _isInAsyncCall = true;
            });
            await _analyticsService
                .createUserPDFReport(context, startDate, endDate)
                .then((value) => {
                      if (value != null)
                        {
                          saveAndLaunchPDFFile(context, value, "userReport.pdf")
                        },
                      setState(() {
                        _isInAsyncCall = false;
                      })
                    });
          } else if (_chosenValue == "CSV" && selected == 2) {
            setState(() {
              _isInAsyncCall = true;
            });
            await _analyticsService
                .createFinancialCSVReport(context, startDate, endDate)
                .then((value) => {
                      if (value != null)
                        {
                          saveCSVFile(context, value, "userReport.csv"),
                        },
                      setState(() {
                        _isInAsyncCall = false;
                      })
                    });
          }
        },
      ),
    );
  }

  String returnMonth(DateTime date) {
    return new DateFormat.MMMM().format(date);
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
                  height: MediaQuery.of(context).size.height * 0.55,
                  child: Column(
                    children: [
                      SizedBox(
                          height: MediaQuery.of(context).size.height * 0.11),
                      Container(
                        width: MediaQuery.of(context).size.height * 0.3,
                        alignment: Alignment.center,
                        //color: Theme.of(context).backgroundColor,
                        child: Text(
                          'CHOOSE \n REPORT TYPE',
                          textAlign: TextAlign.center,
                          style: TextStyle(
                            fontFamily: 'OpenSans',
                            fontSize: 30.0,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ),
                      SizedBox(
                          height: MediaQuery.of(context).size.height * 0.05),
                      _decisionWheel(),
                    ],
                  ),
                ),
                Container(
                  height: MediaQuery.of(context).size.height * 0.255,
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.start,
                    children: [
                      Column(children: <Widget>[
                        Row(
                          mainAxisAlignment: MainAxisAlignment.center,
                          children: [
                            selected == 1
                                ? Container()
                                : _calendarPickerStartDate(),
                            SizedBox(
                              width: 5,
                            ),
                            selected == 1
                                ? Container()
                                : _calendarPickerEndDate(),
                          ],
                        ),
                        SizedBox(height: 10),
                        _dropDown(),
                        SizedBox(
                          height: 10,
                        ),
                        selected == 1
                            ? Container()
                            : Text(
                                "Generate " +
                                    _chosenValue +
                                    " report: \n Start Date: " +
                                    _dateTimeStart.day.toString() +
                                    " " +
                                    returnMonth(_dateTimeStart).toString() +
                                    " " +
                                    _dateTimeStart.year.toString() +
                                    "\nEnd Date:" +
                                    _dateTimeEnd.day.toString() +
                                    " " +
                                    returnMonth(_dateTimeEnd).toString() +
                                    " " +
                                    _dateTimeEnd.year.toString(),
                                style: TextStyle(
                                    fontWeight: FontWeight.w800, fontSize: 17),
                                textAlign: TextAlign.center,
                              ),
                      ]),
                    ],
                  ),
                ),
                Container(
                    height: MediaQuery.of(context).size.height * 0.11,
                    child: _createReportButton()),
              ],
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
    ));
  }
}
