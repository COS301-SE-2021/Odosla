
import 'package:bezier_chart/bezier_chart.dart';
import 'package:flutter/material.dart';
import 'package:flutter_employee_app/utilities/constants.dart';
class WeeklyGraphWidget extends StatelessWidget{
  final DateTime fromDate;
  final DateTime toDate;

  const WeeklyGraphWidget({Key? key, required this.fromDate, required this.toDate}) : super(key: key);
  @override
  Widget build(BuildContext context) {
    final date1 = toDate.subtract(Duration(days: 30));
    final date2 = toDate.subtract(Duration(days: 25));
    final date3 = toDate.subtract(Duration(days: 20));
    final date4 = toDate.subtract(Duration(days: 15));
    final date5 = toDate.subtract(Duration(days: 10));
    final date6 = toDate.subtract(Duration(days: 5));
    final date7 = toDate.subtract(Duration(days: 0));
    return BezierChart(
      bezierChartScale: BezierChartScale.CUSTOM,
      selectedValue: 1,
      xAxisCustomValues: [5, 5, 10, 15, 20, 25, 30],
      series:  [
        BezierLine(
          label: "june",
          lineColor: ColorConstants.korangeColor,
          dataPointStrokeColor: ColorConstants.kwhiteColor,
          dataPointFillColor: ColorConstants.korangeColor,
          lineStrokeWidth: 3,
          data: const [
            DataPoint<double>(value: 100, xAxis: 5),
            DataPoint<double>(value: 130, xAxis: 5),
            DataPoint<double>(value: 300, xAxis: 10),
            DataPoint<double>(value: 150, xAxis: 15),
            DataPoint<double>(value: 75, xAxis: 20),
            DataPoint<double>(value: 100, xAxis: 25),
            DataPoint<double>(value: 250, xAxis: 30),
          ],
        ),
      ],
      config: BezierChartConfig(
        showDataPoints: true,
        startYAxisFromNonZeroValue: true,
        verticalIndicatorFixedPosition: false,
        pinchZoom: true,
        bubbleIndicatorColor: ColorConstants.gblackColor,
        bubbleIndicatorLabelStyle:
        TextStyle(color: ColorConstants.kwhiteColor),
        bubbleIndicatorTitleStyle:
        TextStyle(color: ColorConstants.kwhiteColor),
        bubbleIndicatorValueStyle:
        TextStyle(color: ColorConstants.kwhiteColor),
        footerHeight: 40,
        showVerticalIndicator: true,
        displayYAxis: false,
        stepsYAxis: 15,
        displayLinesXAxis: false,
        xAxisTextStyle: TextStyle(
          color: ColorConstants.kblackColor,
        ),
        backgroundGradient: LinearGradient(
          colors: [
            ColorConstants.kblackColor,
            ColorConstants.kblackColor,
            ColorConstants.kblackColor,
            ColorConstants.kblackColor
          ],
          begin: Alignment.topCenter,
          end: Alignment.bottomCenter,
        ),
        snap: false,
      ),
    );
  }

}
