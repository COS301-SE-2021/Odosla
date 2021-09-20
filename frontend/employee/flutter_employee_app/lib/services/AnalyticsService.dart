import 'dart:convert';
import 'package:csv/csv.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:flutter_employee_app/utilities/settings.dart';
import 'package:get_it/get_it.dart';
import 'package:http/http.dart' as http;
import 'package:syncfusion_flutter_pdf/pdf.dart';

import 'UserService.dart';

class AnalyticsService{

  UserService _userService=GetIt.I.get();

  Future<String?> createFinancialCSVReport(BuildContext context, String startDate, String endDate) async {

    final url = Uri.parse(analyticsEndPoint+"analytics/createFinancialReport");

    Map<String,String> headers =new Map<String,String>();

    String jwt="";
    await _userService.getJWTAsString(context).then((value) => {
      jwt=value!
    });

    print(startDate);
    print(endDate);
    //jwt="Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrZWxseW1vcnJpc29uNzQ1OEBnbWFpbC5jb20iLCJ1c2VyVHlwZSI6IkFETUlOIiwiZXhwIjoxNjMxNzU5Mzg0LCJpYXQiOjE2MzE3MjMzODQsImVtYWlsIjoia2VsbHltb3JyaXNvbjc0NThAZ21haWwuY29tIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiIzZTYyNTQ3Ny1mMWY1LTRiMzAtOTA0ZC1iMjEyZGU5ODEwOGIifQ.njtkTrQJYDNxeavWV7uTLsxNN3ARCRE_8g5JK2FvQrI";
    headers =
    {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS",
      "Authorization":jwt
    };

    final data = {
      "startDate":startDate,
      "endDate":endDate,
      "reportType":"CSV"
    };

    final response = await http.post(url, headers: headers, body: jsonEncode(data)).timeout(
        Duration(seconds: 45),
        onTimeout:(){ return(http.Response('TimeOut',500));
        }
    );
    print(response.body);
    if (response.statusCode==200) {
      Map<String,dynamic> responseData = json.decode(response.body);
      print(responseData);
      if(responseData["success"]==true){
        String csv=responseData["csv"];
        return csv;
      }else if(response.statusCode==403){
        ScaffoldMessenger.of(context)
            .showSnackBar(SnackBar(content: Text("You are unauthorized to use this app")));
      }else if(response.statusCode==500){
        ScaffoldMessenger.of(context)
            .showSnackBar(SnackBar(content: Text("Request timed out")));
      }
    }else return null;
  }

  Future<List<int>?> createFinancialPDFReport(BuildContext context, String startDate, String endDate) async {

    final url = Uri.parse(analyticsEndPoint+"analytics/createFinancialReport");

    Map<String,String> headers =new Map<String,String>();

    String jwt="";
    await _userService.getJWTAsString(context).then((value) => {
      jwt=value!
    });

    headers =
    {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS",
      "Authorization":jwt
    };

    final data = {
      "startDate":startDate,
      "endDate":endDate,
      "reportType":"PDF"
    };

    final response = await http.post(url, headers: headers, body: jsonEncode(data)).timeout(
        Duration(seconds: 15),
        onTimeout:(){ return(http.Response('TimeOut',500));
        }
    );
    print(response.body);
    if (response.statusCode==200) {
      Map<String,dynamic> responseData = json.decode(response.body);
      print(responseData);
      if(responseData["success"]==true){
        PdfDocument document = PdfDocument.fromBase64String(responseData["pdf"]);
        return document.save();
      }

    }else if(response.statusCode==403){
      ScaffoldMessenger.of(context)
          .showSnackBar(SnackBar(content: Text("You are unauthorized to use this app")));
    }else if(response.statusCode==500){
      ScaffoldMessenger.of(context)
          .showSnackBar(SnackBar(content: Text("Request timed out")));
    }else return null;
  }

  Future<String?> createMonthlyCSVReport(BuildContext context) async {

    final url = Uri.parse(analyticsEndPoint+"analytics/createMonthlyReport");

    Map<String,String> headers =new Map<String,String>();

    String jwt="";
    await _userService.getJWTAsString(context).then((value) => {
      jwt=value!
    });


    jwt="Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrZWxseW1vcnJpc29uNzQ1OEBnbWFpbC5jb20iLCJ1c2VyVHlwZSI6IkFETUlOIiwiZXhwIjoxNjMxNzU5Mzg0LCJpYXQiOjE2MzE3MjMzODQsImVtYWlsIjoia2VsbHltb3JyaXNvbjc0NThAZ21haWwuY29tIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiIzZTYyNTQ3Ny1mMWY1LTRiMzAtOTA0ZC1iMjEyZGU5ODEwOGIifQ.njtkTrQJYDNxeavWV7uTLsxNN3ARCRE_8g5JK2FvQrI";

    headers =
    {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS",
      "Authorization":jwt
    };

    final data = {
      "reportType":"CSV"
    };

    final response = await http.post(url, headers: headers, body: jsonEncode(data)).timeout(
        Duration(seconds: 45),
        onTimeout:(){ return(http.Response('TimeOut',500));
        }
    );
    print(response.body);
    if (response.statusCode==200) {
      Map<String,dynamic> responseData = json.decode(response.body);
      print(responseData);
      if(responseData["success"]==true){
        String csv=responseData["csv"];
        return csv;
      }else if(response.statusCode==403){
        ScaffoldMessenger.of(context)
            .showSnackBar(SnackBar(content: Text("You are unauthorized to use this app")));
      }else if(response.statusCode==500){
        ScaffoldMessenger.of(context)
            .showSnackBar(SnackBar(content: Text("Request timed out")));
      }
    }else return null;
  }

  Future<List<int>?> createMonthlyPDFReport(BuildContext context) async {

    final url = Uri.parse(analyticsEndPoint+"analytics/createMonthlyReport");

    Map<String,String> headers =new Map<String,String>();

    String jwt="";
    await _userService.getJWTAsString(context).then((value) => {
      jwt=value!
    });
    //jwt="Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrZWxseW1vcnJpc29uNzQ1OEBnbWFpbC5jb20iLCJ1c2VyVHlwZSI6IkFETUlOIiwiZXhwIjoxNjMxNzU5Mzg0LCJpYXQiOjE2MzE3MjMzODQsImVtYWlsIjoia2VsbHltb3JyaXNvbjc0NThAZ21haWwuY29tIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiIzZTYyNTQ3Ny1mMWY1LTRiMzAtOTA0ZC1iMjEyZGU5ODEwOGIifQ.njtkTrQJYDNxeavWV7uTLsxNN3ARCRE_8g5JK2FvQrI";
    //jwt="Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrZWxseW1vcnJpc29uNzQ1OEBnbWFpbC5jb20iLCJ1c2VyVHlwZSI6IkFETUlOIiwiZXhwIjoxNjMxNTc5MzQxLCJpYXQiOjE2MzE1NDMzNDEsImVtYWlsIjoia2VsbHltb3JyaXNvbjc0NThAZ21haWwuY29tIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiIzZTYyNTQ3Ny1mMWY1LTRiMzAtOTA0ZC1iMjEyZGU5ODEwOGIifQ.j1nnDAcMNVoWAVuJ983LctwLEIBZI45MupGF-Gh4PsE";
    headers =
    {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS",
      "Authorization":jwt
    };

    final data = {
      "reportType":"PDF"
    };

    final response = await http.post(url, headers: headers, body: jsonEncode(data)).timeout(
        Duration(seconds: 45),
        onTimeout:(){ return(http.Response('TimeOut',500));
        }
    );
    if (response.statusCode==200) {
      Map<String,dynamic> responseData = json.decode(response.body);
      print(responseData);
      if(responseData["success"]==true){
        PdfDocument document = PdfDocument.fromBase64String(responseData["pdf"]);
        return document.save();
      }
    }else if(response.statusCode==403){
      ScaffoldMessenger.of(context)
          .showSnackBar(SnackBar(content: Text("You are unauthorized to use this app")));
    }else if(response.statusCode==500){
      ScaffoldMessenger.of(context)
          .showSnackBar(SnackBar(content: Text("Request timed out")));
    }else return null;
  }

  Future<String?> createUserCSVReport(BuildContext context, String startDate, String endDate) async {

    final url = Uri.parse(analyticsEndPoint+"analytics/createUserReport");

    Map<String,String> headers =new Map<String,String>();

    String jwt="";
    await _userService.getJWTAsString(context).then((value) => {
      jwt=value!
    });

    headers =
    {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS",
      "Authorization":jwt
    };

    final data = {
      "startDate":startDate,
      "endDate":endDate,
      "reportType":"CSV"
    };

    final response = await http.post(url, headers: headers, body: jsonEncode(data)).timeout(
        Duration(seconds: 45),
        onTimeout:(){ return(http.Response('TimeOut',500));
        }
    );
    print(response.body);
    if (response.statusCode==200) {
      Map<String,dynamic> responseData = json.decode(response.body);
      print(responseData);
      if(responseData["success"]==true){
        String csv=responseData["csv"];
        return csv;
      }else if(response.statusCode==403){
        ScaffoldMessenger.of(context)
            .showSnackBar(SnackBar(content: Text("You are unauthorized to use this app")));
      }else if(response.statusCode==500){
        ScaffoldMessenger.of(context)
            .showSnackBar(SnackBar(content: Text("Request timed out")));
      }
    }else return null;
  }

  Future<List<int>?> createUserPDFReport(BuildContext context, String startDate, String endDate) async {

    final url = Uri.parse(analyticsEndPoint+"analytics/createUserReport");

    Map<String,String> headers =new Map<String,String>();

    String jwt="";
    await _userService.getJWTAsString(context).then((value) => {
      jwt=value!
    });

    print(startDate);
    print(endDate);
    //jwt="Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrZWxseW1vcnJpc29uNzQ1OEBnbWFpbC5jb20iLCJ1c2VyVHlwZSI6IkFETUlOIiwiZXhwIjoxNjMxNzU5Mzg0LCJpYXQiOjE2MzE3MjMzODQsImVtYWlsIjoia2VsbHltb3JyaXNvbjc0NThAZ21haWwuY29tIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiIzZTYyNTQ3Ny1mMWY1LTRiMzAtOTA0ZC1iMjEyZGU5ODEwOGIifQ.njtkTrQJYDNxeavWV7uTLsxNN3ARCRE_8g5JK2FvQrI";
   // jwt="Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrZWxseW1vcnJpc29uNzQ1OEBnbWFpbC5jb20iLCJ1c2VyVHlwZSI6IkFETUlOIiwiZXhwIjoxNjMxNTc5MzQxLCJpYXQiOjE2MzE1NDMzNDEsImVtYWlsIjoia2VsbHltb3JyaXNvbjc0NThAZ21haWwuY29tIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiIzZTYyNTQ3Ny1mMWY1LTRiMzAtOTA0ZC1iMjEyZGU5ODEwOGIifQ.j1nnDAcMNVoWAVuJ983LctwLEIBZI45MupGF-Gh4PsE";
    headers =
    {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS",
      "Authorization":jwt
    };

    final data = {
      "startDate":startDate,
      "endDate":endDate,
      "reportType":"PDF"
    };

    final response = await http.post(url, headers: headers, body: jsonEncode(data)).timeout(
        Duration(seconds: 45),
        onTimeout:(){ return(http.Response('TimeOut',500));
        }
    );
    if (response.statusCode==200) {
      Map<String,dynamic> responseData = json.decode(response.body);
      print(responseData);
      if(responseData["success"]==true){
        PdfDocument document = PdfDocument.fromBase64String(responseData["pdf"]);
        return document.save();
      }
    }else if(response.statusCode==403){
      ScaffoldMessenger.of(context)
          .showSnackBar(SnackBar(content: Text("You are unauthorized to use this app")));
    }else if(response.statusCode==500){
      ScaffoldMessenger.of(context)
          .showSnackBar(SnackBar(content: Text("Request timed out")));
    }else return null;
  }


}