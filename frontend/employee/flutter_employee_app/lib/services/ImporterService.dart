import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_employee_app/utilities/settings.dart';
import 'package:get_it/get_it.dart';
import 'package:http/http.dart' as http;

import 'UserService.dart';

class ImporterService {

  UserService _userService = GetIt.I.get();

  Future<bool?> importItems(BuildContext context, String csv) async {

    final URL = Uri.parse(importerEndPoint + "importer/itemsCSVImporter");

    Map<String, String> headers = new Map<String, String>();
    String jwt = "";

    await _userService.getJWTAsString(context).then((value) => {
      jwt=value!
    });

    headers =
    {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS",
      "Authorization": jwt,
    };

    final data = {
      "file": csv
    };

    final response = await http.post(
        URL, headers: headers, body: jsonEncode(data)).timeout(
        Duration(seconds: 15),
        onTimeout:(){ return(http.Response('TimeOut',408));
        });

    if (response.statusCode == 200) {

      Map<String, dynamic> responseData = json.decode(response.body);

      if (responseData["success"] == true) {
        return true;
      }
      else {
        ScaffoldMessenger.of(context)
            .showSnackBar(
            SnackBar(content: Text("Server couldn't process request")));
      }
    }
    else if (response.statusCode == 403) {
      ScaffoldMessenger.of(context)
          .showSnackBar(
          SnackBar(content: Text("You are unauthorized to use this app")));
    }
    else if (response.statusCode == 500) {
      ScaffoldMessenger.of(context)
          .showSnackBar(SnackBar(content: Text("Internal server error")));
    }
    else if (response.statusCode==408){
      ScaffoldMessenger.of(context)
          .showSnackBar(SnackBar(content: Text("Request timed out")));
    }
    else return null;
  }

  Future<bool?> importStores(BuildContext context, String csv) async {
    final URL = Uri.parse(importerEndPoint + "importer/storesCSVImporter");

    Map<String, String> headers = new Map<String, String>();
    String jwt = "";

    await _userService.getJWTAsString(context).then((value) => {
      jwt=value!
    });
    print(jwt);

    headers =
    {
      "Accept": "application/json",
      "content-type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "POST, OPTIONS",
      "Authorization": jwt,
    };

    final data = {
      "file": csv
    };

    final response = await http.post(
        URL, headers: headers, body: jsonEncode(data)).timeout(
        Duration(seconds: 15),
        onTimeout:(){ return(http.Response('TimeOut',408));
        });;

    if (response.statusCode == 200) {

      Map<String, dynamic> responseData = json.decode(response.body);

      if (responseData["success"] == true) {
        return true;
      }
      else {
        ScaffoldMessenger.of(context)
            .showSnackBar(
            SnackBar(content: Text("Server couldn't process request")));
      }
    }
    else if (response.statusCode == 403) {
      ScaffoldMessenger.of(context)
          .showSnackBar(
          SnackBar(content: Text("You are unauthorized to use this app")));
    }
    else if (response.statusCode == 500) {
      ScaffoldMessenger.of(context)
          .showSnackBar(SnackBar(content: Text("Internal server error")));
    }
    else if (response.statusCode==408){
      ScaffoldMessenger.of(context)
          .showSnackBar(SnackBar(content: Text("Request timed out")));
    }
    else return null;
  }
}