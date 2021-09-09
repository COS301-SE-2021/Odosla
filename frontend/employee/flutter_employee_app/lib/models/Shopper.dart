import 'User.dart';

class Shopper extends User{

  final int numberOfOrderCompleted;

  Shopper(this.numberOfOrderCompleted) : super('', '', '', '', '');

  Shopper.fromJson(Map<String, dynamic> json):
       numberOfOrderCompleted=json["numberOfOrdersCompleted"],
        super(json["name"],json["surname"],json["email"],json["SHOPPER"],json["phoneNumber"]);
}