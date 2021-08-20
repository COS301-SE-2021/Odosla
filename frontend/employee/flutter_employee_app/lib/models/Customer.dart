class Customer{

  String name;
  String surname;
  String email;
  String userType;
  String phoneNumber;

  Customer(
      this.name, this.surname, this.email, this.userType, this.phoneNumber);

  Customer.fromJson(Map<String, dynamic> json):
        name = json['name'],
        surname=json["surname"],
        email=json["email"],
        userType=json["userType"],
        phoneNumber=json["phoneNumber"];
}