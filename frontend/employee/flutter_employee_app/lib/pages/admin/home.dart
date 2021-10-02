import 'package:flutter/material.dart';
import 'package:flutter_employee_app/utilities/constants.dart';
import 'package:flutter_employee_app/widgets/graph_card_widget.dart';
import 'package:flutter_employee_app/widgets/weekly_graph.dart';
import 'package:google_fonts/google_fonts.dart';
import 'package:intl/intl.dart';

class HomePageScreen extends StatefulWidget {
  @override
  _HomePageScreenState createState() => _HomePageScreenState();
}

class _HomePageScreenState extends State<HomePageScreen> {
  final numberFormat = new NumberFormat("##,###.00#", "en_US");
  Color color = ColorConstants.gblackColor;
  Color fcolor = ColorConstants.kgreyColor;
  bool isActive = false;
  final fromDate = DateTime(2018, 11, 22);
  final toDate = DateTime.now();

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: ColorConstants.kblackColor,
      body: SingleChildScrollView(
        child: Padding(
          padding: const EdgeInsets.only(
            left: 15,
            right: 15,
            top: 30,
          ),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Icon(
                    Icons.short_text,
                    color: ColorConstants.kwhiteColor,
                  ),
                  Icon(
                    Icons.more_vert,
                    color: ColorConstants.kwhiteColor,
                  )
                ],
              ),
              SizedBox(
                height: 30,
              ),
              Text(
                "Orders Price",
                style: GoogleFonts.spartan(
                  fontSize: 25,
                  fontWeight: FontWeight.w700,
                  color: ColorConstants.kwhiteColor,
                ),
              ),
              SizedBox(
                height: 20,
              ),
              Text(
                "Total price of all Orders",
                style: GoogleFonts.spartan(
                  fontSize: 15,
                  fontWeight: FontWeight.w500,
                  color: ColorConstants.kgreyColor,
                ),
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text(
                    r'R' + "${numberFormat.format(27802.05)}",
                    style: GoogleFonts.openSans(
                      fontSize: 40,
                      fontWeight: FontWeight.w600,
                      color: ColorConstants.kwhiteColor,
                    ),
                  ),
                  Row(
                    children: [
                      Text(
                        "15%",
                        style: GoogleFonts.spartan(
                          fontSize: 12,
                          fontWeight: FontWeight.w500,
                          color: ColorConstants.kwhiteColor,
                        ),
                      ),
                      Icon(
                        Icons.arrow_upward,
                        color: ColorConstants.kwhiteColor,
                      ),
                    ],
                  )
                ],
              ),
              Center(
                child: Container(
                  height: MediaQuery.of(context).size.height / 2.5,
                  width: MediaQuery.of(context).size.width,
                  child: WeeklyGraphWidget(toDate: toDate, fromDate: fromDate),
                ),
              ),
              Row(
                children: [
                  Container(
                    height: 50,
                    width: MediaQuery.of(context).size.width / 3.4,
                    decoration: BoxDecoration(
                      borderRadius: BorderRadius.circular(20.0),
                      color: ColorConstants.korangeColor,
                    ),
                    child: Center(
                      child: Text(
                        "Apr to Jun",
                        style: GoogleFonts.spartan(
                          fontSize: 15,
                          fontWeight: FontWeight.w600,
                          color: ColorConstants.kwhiteColor,
                        ),
                      ),
                    ),
                  ),
                  SizedBox(
                    width: 10,
                  ),
                  GraphCardWidget(
                    title: Constants.strList[0],
                    activeColor: color,
                    fontColor: fcolor,
                    isActive: isActive,
                  ),
                  SizedBox(
                    width: 10,
                  ),
                  GraphCardWidget(
                    title: Constants.strList[1],
                    activeColor: color,
                    fontColor: fcolor,
                    isActive: isActive,
                  ),
                  SizedBox(
                    width: 10,
                  ),
                  GraphCardWidget(
                    title: Constants.strList[2],
                    activeColor: color,
                    fontColor: fcolor,
                    isActive: isActive,
                  ),
                ],
              ),
              SizedBox(
                height: 20,
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text(
                    "Income",
                    style: GoogleFonts.spartan(
                      fontSize: 15,
                      fontWeight: FontWeight.w500,
                      color: ColorConstants.kgreyColor,
                    ),
                  ),
                  Row(
                    children: [
                      Text(
                        "75%",
                        style: GoogleFonts.spartan(
                          fontSize: 15,
                          fontWeight: FontWeight.w500,
                          color: ColorConstants.kgreyColor,
                        ),
                      ),
                      Icon(
                        Icons.arrow_downward,
                        color: ColorConstants.kwhiteColor,
                      ),
                    ],
                  )
                ],
              ),
              SizedBox(
                height: 20,
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.spaceBetween,
                children: [
                  Text(
                    "Outcome",
                    style: GoogleFonts.spartan(
                      fontSize: 15,
                      fontWeight: FontWeight.w500,
                      color: ColorConstants.kgreyColor,
                    ),
                  ),
                  Row(
                    children: [
                      Text(
                        "25%",
                        style: GoogleFonts.spartan(
                          fontSize: 15,
                          fontWeight: FontWeight.w500,
                          color: ColorConstants.kgreyColor,
                        ),
                      ),
                      Icon(
                        Icons.arrow_upward,
                        color: ColorConstants.kwhiteColor,
                      ),
                    ],
                  )
                ],
              ),
              SizedBox(
                height: 10,
              )
            ],
          ),
        ),
      ),
    );
  }
}
