class User{
  String name;
  String surname;
  String email;
  String userType;
  String phoneNumber;

  bool onShift=false;
  String ordersCompleted="0";

  User(this.name, this.surname, this.email, this.userType, this.phoneNumber);
  User.withParameters(this.name, this.surname, this.email, this.userType, this.phoneNumber,this.onShift,this.ordersCompleted);
  User.fromJson(Map<String, dynamic> json):
      name = json['name'],
      surname=json["surname"],
      email=json["email"],
      userType=json["userType"],
      phoneNumber=json["phoneNumber"];

  void setOrdersCompleted(String orders){
    ordersCompleted=orders;
  }

  String getOrdersCompleted(){
    return ordersCompleted;
  }

  void setOnShift(bool onShift){
    this.onShift=onShift;
  }

  bool getOnShift(bool onShift){
    return onShift;
  }
}