class User {
  String name = "";
  String surname = "";
  String email = "";
  String userType = "";
  String phoneNumber = "";
  String storeID = "";
  bool onShift = false;
  String ordersCompleted = "0";

  String deliveriesCompleted = "0";
  String rating = "0.0";

  User(String name, String surname, String email, String userType,
      String phoneNumber) {
    if (name != null) {
      this.name = name;
    }
    if (surname != null) {
      this.surname = surname;
    }
    if (email != null) {
      this.email = email;
    }
    if (userType != null) {
      this.userType = userType;
    }
    if (phoneNumber != null) {
      this.phoneNumber = phoneNumber;
    }
  }

  User.withParameters(this.name, this.surname, this.email, this.userType,
      this.phoneNumber, this.onShift, this.ordersCompleted);

  User.fromJson(Map<String, dynamic> json)
      : name = json['name'],
        surname = json["surname"],
        email = json["email"],
        userType = json["userType"],
        phoneNumber = json["phoneNumber"];

  void setOrdersCompleted(String orders) {
    ordersCompleted = orders;
  }

  void setStoreID(String storeID) {
    this.storeID = storeID;
  }

  String getStoreID() {
    return storeID;
  }

  String getOrdersCompleted() {
    return ordersCompleted;
  }

  void setDeliveriesCompleted(String deliveriesCompleted) {
    deliveriesCompleted = deliveriesCompleted;
  }

  String getDeliveriesCompleted() {
    return deliveriesCompleted;
  }

  void setRating(String rating) {
    this.rating = rating;
  }

  String getRating() {
    return rating;
  }

  void setOnShift(bool onShift) {
    this.onShift = onShift;
  }

  bool getOnShift(bool onShift) {
    return onShift;
  }
}
