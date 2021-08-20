bool validateStructure(String value) {
  String pattern =
      r'^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[!@#\$&*~]).{8,}$';
  RegExp regExp = new RegExp(pattern);
  return regExp.hasMatch(value);
}

bool validatePasswordMatches(String value, String previousPassword) {
  if (value == (previousPassword)) {
    return true;
  } else {
    return false;
  }
}

bool validatePhoneNumber(String value) {
  if (value == "") {
    return true;
  }
  String pattern = r'[0-9]{10}';
  RegExp regExp = new RegExp(pattern);
  return regExp.hasMatch(value);
}
