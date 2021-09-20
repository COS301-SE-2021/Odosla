import 'package:flutter/foundation.dart';
import 'package:meta/meta.dart';

class Store {

  String id;
  String name;
  int openTime;
  int closeTime;
  bool open;
  String imageUrl;
  String storeLocationLatitude;
  String storeLocationLongitude;
  String address;

  Store(this.id, this.name, this.openTime, this.closeTime, this.open,this.imageUrl,this.storeLocationLatitude,this.storeLocationLongitude,this.address);

  Store copy(String id, String name, int openTime, int closeTime, bool open,String imgUrl, String storeLocationLatitude,String storeLocationLongitude, String address) =>
      Store(id, name, openTime, closeTime, open,imgUrl,storeLocationLatitude,storeLocationLongitude,address);

  Store.fromJson(Map<String, dynamic> json)
      : id = json['storeID'],
        name = json['storeBrand'],
        open = json['isOpen'],
        openTime = json['openingTime'],
        closeTime = json['closingTime'],
        imageUrl = json["imgUrl"],
        storeLocationLatitude = json["storeLocation"]["latitude"].toString(),
        storeLocationLongitude = json["storeLocation"]["longitude"].toString(),
        address=json["storeLocation"]["address"];

}