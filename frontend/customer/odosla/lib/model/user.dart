class User {
  String name;
  String surname;
  String email;
  String phoneNumber;

  User(this.name, this.surname, this.email, this.phoneNumber);
  User.withParameters(this.name, this.surname, this.email, this.phoneNumber);
  User.fromJson(Map<String, dynamic> json)
      : name = json['name'],
        surname = json["surname"],
        email = json["email"],
        phoneNumber = json["phoneNumber"];
}
