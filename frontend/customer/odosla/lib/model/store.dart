class Store {
  final String id;
  final String name;
  final int openTime;
  final int closeTime;
  final bool open;
  final String imgUrl;
  final double lat;
  final double long;

  const Store(
    this.id,
    this.name,
    this.openTime,
    this.closeTime,
    this.open,
    this.imgUrl,
    this.lat,
    this.long,
  );

  Store copy(String id, String name, int openTime, int closeTime, bool open,
          String imgUrl, double lat, double long) =>
      Store(id, name, openTime, closeTime, open, imgUrl, lat, long);

  Store.fromJson(Map<String, dynamic> json)
      : id = json['storeID'],
        name = json['storeBrand'],
        open = json['isOpen'],
        openTime = json['openingTime'],
        closeTime = json['closingTime'],
        imgUrl = json['imageUrl'],
        lat = json['storeLocation']['latitude'],
        long = json['storeLocation']['longitude'];
}
