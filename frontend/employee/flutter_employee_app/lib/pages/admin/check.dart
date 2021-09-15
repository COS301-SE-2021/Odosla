import 'package:flutter/material.dart';
import 'package:flutter_employee_app/pages/admin/admin_profile_screen.dart';
import 'package:flutter_employee_app/pages/admin/analytics_page.dart';
import 'package:flutter_employee_app/pages/admin/importer_page.dart';
import 'package:flutter_employee_app/widgets/circular_button.dart';


class MyHomePage extends StatefulWidget {
  String _currentPage="";
  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State with SingleTickerProviderStateMixin {

  AnimationController? animationController;
  Animation? degOneTranslationAnimation, degTwoTranslationAnimation, degThreeTranslationAnimation;
  Animation? rotationAnimation;

  double getRadiansFromDegree(double degree) {
    double unitRadian = 57.295779513;
    return degree / unitRadian;
  }

  @override
  void initState() {
    animationController = AnimationController(vsync: this, duration: Duration(milliseconds: 250));
    degOneTranslationAnimation = TweenSequence([
      TweenSequenceItem(tween: Tween(begin: 0.0,end: 1.2), weight: 75.0),
      TweenSequenceItem(tween: Tween(begin: 1.2,end: 1.0), weight: 25.0),
    ])
        .animate(animationController!);
    degTwoTranslationAnimation = TweenSequence([
      TweenSequenceItem(tween: Tween(begin: 0.0,end: 1.4), weight: 55.0),
      TweenSequenceItem(tween: Tween(begin: 1.4,end: 1.0), weight: 45.0)
    ])
        .animate(animationController!);
    degThreeTranslationAnimation = TweenSequence([
      TweenSequenceItem(tween: Tween(begin: 0.0,end: 1.75), weight: 35.0),
      TweenSequenceItem(tween: Tween(begin: 1.75,end: 1.0), weight: 65.0)
    ])
        .animate(animationController!);
    rotationAnimation = Tween(begin: 180.0, end: 0.0)
        .animate(CurvedAnimation(parent: animationController!, curve: Curves.easeOut));
    super.initState();
    animationController!.addListener(() {
      setState(() {});
    });
    // bottomSelectedIndex=1;
    // pageController = PageController(
    //   initialPage: bottomSelectedIndex,
    //   keepPage: true,
    // );
  }

  @override
  void dispose() {
    animationController!.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    Size size = MediaQuery.of(context).size;
        return Stack(
          children: [
            Positioned(
              right: 10,
              bottom: 10,
              child: Stack(
                alignment: Alignment.bottomRight,
                children: [
                  IgnorePointer(
                    child: Container(
                      color: Colors.transparent,
                      height: 150.0,
                      width: 150.0,
                    ),
                  ),
                  Transform.translate(
                    offset: Offset.fromDirection(
                        getRadiansFromDegree(270), degOneTranslationAnimation!.value * 100),
                    child: Transform(
                      transform: Matrix4.rotationZ(getRadiansFromDegree(rotationAnimation!.value))
                        ..scale(degOneTranslationAnimation!.value),
                      alignment: Alignment.center,
                      child: CircularButton(
                        onClick: (){Navigator.of(context).push(MaterialPageRoute(
                       builder: (BuildContext context) => ImporterScreen()));},
                        color: Colors.blue,
                        width: 50,
                        height: 50,
                        icon: Icon(
                          Icons.add,
                          color: Colors.white,
                        ),
                      ),
                    ),
                  ),
                  Transform.translate(
                    offset: Offset.fromDirection(
                        getRadiansFromDegree(225), degOneTranslationAnimation!.value * 100),
                    child: Transform(
                      transform: Matrix4.rotationZ(getRadiansFromDegree(rotationAnimation!.value))
                        ..scale(degOneTranslationAnimation!.value),
                      alignment: Alignment.center,
                      child: CircularButton(
                        onClick: (){Navigator.of(context).push(MaterialPageRoute(
                            builder: (BuildContext context) => AnalyticsScreen()));},
                        color: Colors.black,
                        width: 50,
                        height: 50,
                        icon: Icon(
                          Icons.analytics,
                          color: Colors.white,
                        ),
                      ),
                    ),
                  ),
                  Transform.translate(
                    offset: Offset.fromDirection(
                        getRadiansFromDegree(180), degOneTranslationAnimation!.value * 100),
                    child: Transform(
                      transform: Matrix4.rotationZ(getRadiansFromDegree(rotationAnimation!.value))
                        ..scale(degOneTranslationAnimation!.value),
                      alignment: Alignment.center,
                      child: CircularButton(
                        onClick: (){Navigator.of(context).push(MaterialPageRoute(
                            builder: (BuildContext context) => AdminProfileScreen()));},
                        color: Colors.orangeAccent,
                        width: 50,
                        height: 50,
                        icon: Icon(
                          Icons.person,
                          color: Colors.white,
                        ),

                    ),
                    ),
                  ),
                  Transform(
                    transform: Matrix4.rotationZ(getRadiansFromDegree(rotationAnimation!.value)),
                    alignment: Alignment.center,
                    child: CircularButton(
                      color: Colors.red,
                      width: 60,
                      height: 60,
                      icon: Icon(
                        Icons.menu,
                        color: Colors.white,
                      ),
                      onClick: () {
                        if (animationController!.isCompleted) {
                          animationController!.reverse();
                        } else {
                          animationController!.forward();
                        }
                      },
                    ),
                  ),
                ],
              ),
            )
          ]);
  }
}