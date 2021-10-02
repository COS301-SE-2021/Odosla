import 'package:flutter/material.dart';

class CircularButton extends StatelessWidget {
  double? width;
  double? height;
  Color? color;
  Icon? icon;
  Function()? onClick;

  CircularButton(
      {this.color, this.width, this.height, this.icon, this.onClick});

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(color: color, shape: BoxShape.circle),
      width: width,
      height: height,
      child: IconButton(icon: icon!, enableFeedback: true, onPressed: onClick),
    );
  }
}
