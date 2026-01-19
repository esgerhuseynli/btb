import 'package:flutter/material.dart';
import 'package:flutter/foundation.dart';
import 'injection/injection.dart';
import 'app.dart';

void main() async {
  WidgetsFlutterBinding.ensureInitialized();

  // Initialize dependency injection
  await configureDependencies();

  // Test log to verify console output
  debugPrint('🚀 App starting...');
  if (kDebugMode) {
    print('Flutter app initialized - logs should appear in Xcode console');
  }

  runApp(const App());
}
