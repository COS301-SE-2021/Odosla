import 'package:flutter/foundation.dart';
import 'package:meta/meta.dart';

class Store {
  final String id;
  final String name;
  final int openTime;
  final int closeTime;
  final bool open;
  final String imgUrl;

  const Store(
    this.id,
    this.name,
    this.openTime,
    this.closeTime,
    this.open,
    this.imgUrl,
  );

  Store copy(String id, String name, int openTime, int closeTime, bool open,
          String imgUrl) =>
      Store(id, name, openTime, closeTime, open, imgUrl);

  Store.fromJson(Map<String, dynamic> json)
      : id = json['storeID'],
        name = json['storeBrand'],
        open = json['isOpen'],
        openTime = json['openingTime'],
        closeTime = json['closingTime'],
        imgUrl = json['imageUrl'];
}
