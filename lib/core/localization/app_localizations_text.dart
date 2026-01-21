import 'package:flutter/material.dart';

import '../../l10n/app_localizations.dart';
// import 'package:flutter_gen/gen_l10n/app_localizations.dart';

/// Extension to easily access localizations from BuildContext
extension AppLocalizationsExtension on BuildContext {
  /// Get the current AppLocalizations instance
  AppLocalizations get l10n => AppLocalizations.of(this)!;
}

