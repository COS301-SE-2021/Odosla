
import 'dart:convert';
import 'package:flutter_employee_app/utilities/settings.dart';
import 'package:http/http.dart' as http;

class AnalyticsService{

  Future<String> registerDriver() async {

    final url = Uri.parse(endPoint+"user/registerDriver");

    Map<String,String> headers =new Map<String,String>();

    headers =
    {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS"
    };

    final data = {
    };

    final response = await http.post(url, headers: headers, body: jsonEncode(data)).timeout(
        Duration(seconds: 15),
        onTimeout:(){ return(http.Response('TimeOut',500));
        }
    );

    if (response.statusCode==200) {
    }else if(response.statusCode==500){
      print(response);
      return "timeout";
    }else{
      return "false";
    }
  }

}