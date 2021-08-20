

import 'package:flutter/material.dart';
import 'package:flutter_employee_app/services/UserService.dart';
import 'package:flutter_test/flutter_test.dart';

import 'package:flutter_employee_app/main.dart';
import 'package:get_it/get_it.dart';
import 'package:mockito/annotations.dart';

@GenerateMocks([UserService])
void main() {

  GetIt.I.registerSingleton(userService);
  testWidgets('Counter increments smoke test', (WidgetTester tester) async {
    // Build our app and trigger a frame.
    await tester.pumpWidget(OdoslaApp());

    // Verify that our counter starts at 0.
    expect(find.text('0'), findsOneWidget);
    expect(find.text('1'), findsNothing);

    // Tap the '+' icon and trigger a frame.
    await tester.tap(find.byIcon(Icons.add));
    await tester.pump();

    // Verify that our counter has incremented.
    expect(find.text('0'), findsNothing);
    expect(find.text('1'), findsOneWidget);
  });
}
