class GeoPoint{

  double latitude=0.0;
  double longitude=0.0;
  String address="";

  GeoPoint(this.latitude, this.longitude, this.address);

  GeoPoint.fromJson(Map<String, dynamic> json)
      : latitude=json["latitude"],
        longitude=json["longitude"],
        address=json["address"];
}