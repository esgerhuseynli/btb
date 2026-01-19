# Generating Localization Files

The localization files need to be generated before the code will compile. 

## Quick Fix

Run this command in your terminal from the `flutter_btb_mobile` directory:

```bash
cd flutter_btb_mobile
flutter pub get
```

This will:
1. Generate the localization files in `.dart_tool/flutter_gen/gen_l10n/`
2. Resolve all the import errors in `app.dart`
3. Make `AppLocalizations` available throughout your app

## What Gets Generated

After running `flutter pub get`, Flutter will automatically create:
- `lib/.dart_tool/flutter_gen/gen_l10n/app_localizations.dart`
- `lib/.dart_tool/flutter_gen/gen_l10n/app_localizations_az.dart`
- `lib/.dart_tool/flutter_gen/gen_l10n/app_localizations_en.dart`
- `lib/.dart_tool/flutter_gen/gen_l10n/app_localizations_ru.dart`

These files are generated from your ARB files in `lib/l10n/`.

## If You Still See Errors

1. **Clean and rebuild:**
   ```bash
   flutter clean
   flutter pub get
   ```

2. **Restart your IDE** to refresh the analysis server

3. **Verify your ARB files** are valid JSON (no syntax errors)

## Note

The errors you're seeing are expected until the files are generated. Once you run `flutter pub get`, all the red squiggly lines should disappear!

