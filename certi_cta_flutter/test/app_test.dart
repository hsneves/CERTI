// This is a basic Flutter widget test.
//
// To perform an interaction with a widget in your test, use the WidgetTester
// utility in the flutter_test package. For example, you can send tap and scroll
// gestures. You can also use WidgetTester to find child widgets in the widget
// tree, read text, and verify that the values of widget properties are correct.

import 'package:certi_cta_flutter/main.dart';

import 'package:flutter_test/flutter_test.dart';
import 'package:test/test.dart';
import 'package:http/http.dart' as http;
import 'package:nock/nock.dart';
import 'package:certi_cta_flutter/utils/constants.dart';

void main() {
  testWidgets('capturar pokemon pikachu', (WidgetTester tester) async {
    nock.cleanAll();
    nock.init();

    final interceptor =
        nock(host).get("/rest/pokemon/pokeballs/").reply(200, "");

    // Build our app and trigger a frame.
    await tester.pumpWidget(const CertiCtaApp());

    //expect(find.bySubtype<TextButton>(), findsOneWidget);

    //await tester.tap(find.byElementType(TextButton));

    // Verify that our counter starts at 0.
    //expect(find.text('0'), findsOneWidget);
    //expect(find.text('1'), findsNothing);

    // Tap the '+' icon and trigger a frame.
    //await tester.tap(find.byIcon(Icons.add));
    //await tester.pump();

    // Verify that our counter has incremented.
    //expect(find.text('0'), findsNothing);
    //expect(find.text('1'), findsOneWidget);
  });
}
