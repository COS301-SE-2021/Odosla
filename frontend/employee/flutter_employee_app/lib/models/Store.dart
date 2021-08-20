import 'package:flutter/foundation.dart';
import 'package:meta/meta.dart';

class Store {
  String id;
  String name;
  int openTime;
  int closeTime;
  bool open;
  String imageUrl;

  Store(this.id, this.name, this.openTime, this.closeTime, this.open,this.imageUrl);

  Store copy(String id, String name, int openTime, int closeTime, bool open,String imgUrl) =>
      Store(id, name, openTime, closeTime, open,imgUrl);

  Store.fromJson(Map<String, dynamic> json)
      : id = json['storeID'],
        name = json['storeBrand'],
        open = json['isOpen'],
        openTime = json['openingTime'],
        closeTime = json['closingTime'],
        imageUrl = json["imageUrl"];
}