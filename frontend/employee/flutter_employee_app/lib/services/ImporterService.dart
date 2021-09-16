import 'dart:convert';

import 'package:flutter/material.dart';
import 'package:flutter_employee_app/utilities/settings.dart';
import 'package:get_it/get_it.dart';
import 'package:http/http.dart' as http;

import 'UserService.dart';
class ImporterService {

  UserService _userService = GetIt.I.get();

  Future<bool?> importItems(BuildContext context, String csv) async {
    final loginURL = Uri.parse(importerEndPoint + "importer/itemsCSVImporter");

    Map<String, String> headers = new Map<String, String>();
    String jwt = "";

    // await _userService.getJWTAsString(context).then((value) => {
    //   jwt=value!
    // });
    jwt =
    "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrZWxseW1vcnJpc29uNzQ1OEBnbWFpbC5jb20iLCJ1c2VyVHlwZSI6IkFETUlOIiwiZXhwIjoxNjMxNTc5MzQxLCJpYXQiOjE2MzE1NDMzNDEsImVtYWlsIjoia2VsbHltb3JyaXNvbjc0NThAZ21haWwuY29tIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiIzZTYyNTQ3Ny1mMWY1LTRiMzAtOTA0ZC1iMjEyZGU5ODEwOGIifQ.j1nnDAcMNVoWAVuJ983LctwLEIBZI45MupGF-Gh4PsE";

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
        loginURL, headers: headers, body: jsonEncode(data));
    print(response.body);

    if (response.statusCode == 200) {
      Map<String, dynamic> responseData = json.decode(response.body);
      print("RESPONSE DATA");
      if (responseData["success"] == true) {
        return true;
      } else {
        ScaffoldMessenger.of(context)
            .showSnackBar(
            SnackBar(content: Text("Server couldn't process request")));
      }
    } else if (response.statusCode == 403) {
      ScaffoldMessenger.of(context)
          .showSnackBar(
          SnackBar(content: Text("You are unauthorized to use this app")));
    } else if (response.statusCode == 500) {
      ScaffoldMessenger.of(context)
          .showSnackBar(SnackBar(content: Text("Request timed out")));
    } else
      return null;
  }

  Future<bool?> importStores(BuildContext context, String csv) async {
    final loginURL = Uri.parse(importerEndPoint + "importer/storesCSVImporter");

    Map<String, String> headers = new Map<String, String>();
    String jwt = "";

    // await _userService.getJWTAsString(context).then((value) => {
    //   jwt=value!
    // });
    jwt =
    "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJrZWxseW1vcnJpc29uNzQ1OEBnbWFpbC5jb20iLCJ1c2VyVHlwZSI6IkFETUlOIiwiZXhwIjoxNjMxNTc5MzQxLCJpYXQiOjE2MzE1NDMzNDEsImVtYWlsIjoia2VsbHltb3JyaXNvbjc0NThAZ21haWwuY29tIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRE1JTiJdLCJqdGkiOiIzZTYyNTQ3Ny1mMWY1LTRiMzAtOTA0ZC1iMjEyZGU5ODEwOGIifQ.j1nnDAcMNVoWAVuJ983LctwLEIBZI45MupGF-Gh4PsE";

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
        loginURL, headers: headers, body: jsonEncode(data));
    print(response.body);

    if (response.statusCode == 200) {
      Map<String, dynamic> responseData = json.decode(response.body);
      print("RESPONSE DATA");
      if (responseData["success"] == true) {
        return true;
      } else {
        ScaffoldMessenger.of(context)
            .showSnackBar(
            SnackBar(content: Text("Server couldn't process request")));
      }
    } else if (response.statusCode == 403) {
      ScaffoldMessenger.of(context)
          .showSnackBar(
          SnackBar(content: Text("You are unauthorized to use this app")));
    } else if (response.statusCode == 500) {
      ScaffoldMessenger.of(context)
          .showSnackBar(SnackBar(content: Text("Request timed out")));
    } else
      return null;
  }
}