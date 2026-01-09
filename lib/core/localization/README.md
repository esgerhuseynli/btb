# Localization Setup

This Flutter app supports three languages:
- Azerbaijani (az) - Default
- English (en)
- Russian (ru)

## Files Structure

- `lib/l10n/` - Contains ARB (Application Resource Bundle) files
  - `app_az.arb` - Azerbaijani translations
  - `app_en.arb` - English translations (template)
  - `app_ru.arb` - Russian translations
- `lib/core/localization/` - Localization utilities
  - `app_localizations_ext.dart` - Extension for easy access to localizations
  - `locale_manager.dart` - Manages locale persistence and retrieval

## Usage

### Accessing Localizations

Use the extension method to access localizations from any widget:

```dart
import 'package:btb_mobile_flutter/core/localization/app_localizations_ext.dart';

// In your widget
Text(context.l10n.appTitle)
Text(context.l10n.signIn)
Text(context.l10n.password)
```

### Changing Locale

To change the app locale programmatically:

```dart
import 'package:btb_mobile_flutter/core/localization/locale_manager.dart';

// Set locale (0 = az, 1 = en, 2 = ru)
await LocaleManager.setLocale(1); // Switch to English

// Get current locale
final locale = await LocaleManager.getLocale();
```

### Adding New Strings

**Important:** Avoid using Dart reserved keywords as string keys (e.g., `continue`, `class`, `if`, `for`, etc.). Use descriptive names like `continueButton` instead.

1. Add the string to `app_en.arb` (template file):
```json
{
  "myNewString": "My New String",
  "@myNewString": {
    "description": "Description of the string"
  }
}
```

2. Add translations to `app_az.arb` and `app_ru.arb`:
```json
{
  "myNewString": "Yeni Sətir"  // Azerbaijani
}
```

3. Run `flutter gen-l10n` or `flutter pub get` to regenerate localization files

4. Use in code:
```dart
Text(context.l10n.myNewString)
```

## Generated Files

After running `flutter gen-l10n` or `flutter pub get`, Flutter will automatically generate:
- `lib/.dart_tool/flutter_gen/gen_l10n/app_localizations.dart`
- `lib/.dart_tool/flutter_gen/gen_l10n/app_localizations_az.dart`
- `lib/.dart_tool/flutter_gen/gen_l10n/app_localizations_en.dart`
- `lib/.dart_tool/flutter_gen/gen_l10n/app_localizations_ru.dart`

These files are automatically generated and should not be edited manually.

