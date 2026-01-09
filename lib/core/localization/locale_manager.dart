import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';
import '../constants/app_constants.dart';

/// Manages app locale and syncs with SharedPreferences
class LocaleManager {
  /// Get the current locale from SharedPreferences
  /// Returns: Locale('az', 'AZ') for index 0, Locale('en', 'US') for index 1, Locale('ru', 'RU') for index 2
  /// Defaults to English (index 1)
  static Future<Locale> getLocale() async {
    try {
      final prefs = await SharedPreferences.getInstance();
      final langIndex = prefs.getInt(AppConstants.appLanguage) ?? 1;
      
      switch (langIndex) {
        case 0:
          return const Locale('az', 'AZ');
        case 1:
          return const Locale('en', 'US');
        case 2:
          return const Locale('ru', 'RU');
        default:
          return const Locale('en', 'US');
      }
    } catch (e) {
      return const Locale('en', 'US');
    }
  }

  /// Set the locale and save to SharedPreferences
  /// langIndex: 0 for Azerbaijani, 1 for English, 2 for Russian
  static Future<void> setLocale(int langIndex) async {
    try {
      final prefs = await SharedPreferences.getInstance();
      await prefs.setInt(AppConstants.appLanguage, langIndex);
    } catch (e) {
      // Handle error silently
    }
  }

  /// Get locale from language index
  static Locale getLocaleFromIndex(int langIndex) {
    switch (langIndex) {
      case 0:
        return const Locale('az', 'AZ');
      case 1:
        return const Locale('en', 'US');
      case 2:
        return const Locale('ru', 'RU');
      default:
        return const Locale('en', 'US');
    }
  }

  /// Get language index from locale
  static int getIndexFromLocale(Locale locale) {
    if (locale.languageCode == 'az') return 0;
    if (locale.languageCode == 'en') return 1;
    if (locale.languageCode == 'ru') return 2;
    return 1; // Default to English
  }
}

