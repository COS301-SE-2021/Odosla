
import 'dart:io';

import 'package:csv/csv.dart';
import 'package:flutter/cupertino.dart';
import 'package:flutter/material.dart';
import 'package:open_file/open_file.dart';
import 'package:path_provider/path_provider.dart';
import 'package:ext_storage/ext_storage.dart';
import 'package:permission_handler/permission_handler.dart';

Future <void> saveAndLaunchPDFFile (BuildContext context,List<int> bytes, String filename) async{
  ScaffoldMessenger.of(context)
      .showSnackBar(SnackBar(content: Text("Busy downloading file")));

  Map<Permission, PermissionStatus> statuses = await [
    Permission.storage,
  ].request();

  String dir = await ExtStorage.getExternalStoragePublicDirectory(
      ExtStorage.DIRECTORY_DOWNLOADS);
  String fileNew = "$dir";
  final path = (await getExternalStorageDirectory())!.path;
  final file=File('$path/$filename');
  await file.writeAsBytes(bytes,flush: true);
  OpenFile.open('$path/$filename');

}

Future <void> saveCSVFile (BuildContext context,String csv, String filename) async{

  ScaffoldMessenger.of(context)
      .showSnackBar(SnackBar(content: Text("Busy downloading file")));

  Map<Permission, PermissionStatus> statuses = await [
    Permission.storage,
  ].request();

  String dir = await ExtStorage.getExternalStoragePublicDirectory(
      ExtStorage.DIRECTORY_DOWNLOADS);
  String fileNew = "$dir";
  File f = File(fileNew + "$filename");
  final path = (await getExternalStorageDirectory())!.path;
  final file=File('$path/$filename');
  file.writeAsString(csv);
  OpenFile.open('$path/$filename');

}