import 'package:flutter/material.dart';

class AppTheme {
  // Colors
  static const Color colorPrimary = Color(0xFFFFFFFF);
  static const Color colorPrimaryDark = Color(0xFFED3338);
  static const Color colorAccent = Color(0xFFED3338);
  static const Color textColor = Color(0xFF383336);
  static const Color textColorLight = Color(0xFF262626);
  static const Color hintColor = Color(0xFFABABAB);
  static const Color mainBackground = Color(0xFFFCFCFC);
  static const Color white = Color(0xFFFFFFFF);
  static const Color border = Color(0xFFAFAFAF);
  static const Color mainColor = Color(0xFFED3338);
  static const Color mainRippleColor = Color(0x42ED3338);
  static const Color mainColorDark = Color(0x80ED3338);
  static const Color red = Color(0xFFFF0005);
  static const Color green = Color(0xFF17CF00);
  static const Color blue = Color(0xFF001A91);
  static const Color purple = Color(0xFF9E00BB);
  static const Color borderColor = Color(0xFFF5F5F5);
  static const Color bottomBarMenuItemTint = Color(0xFF848484);

  // Button Colors
  static const Color buttonPrimaryBackground = Color(0xFFD32F2F);
  static const Color buttonDisabledBackground = Color(0xFFE5E5E5);
  static const Color buttonDisabledForeground = Color(0xFF757575);
  static const Color buttonPressedOverlay = Color(0xFFA91217);
  static const Color buttonSignInBackground = Color(0xFFFF3F3B);
  
  // Border Colors
  static const Color borderLight = Color(0xFFD1D1D6);
  
  // Text Colors
  static const Color textDark = Color(0xFF1A1A1A);
  static const Color textSecondary = Color(0xFF312E49);
  static const Color textTertiary = Color(0xFFA4A3AA);
  static const Color textPlaceholder = Color(0xFF8E8E93);
  
  // Border Colors
  static const Color borderOtpField = Color(0xFFE5E7EB);

  // Card/Account Item Colors
  static const Color cardBankAccountItem1Color1 = Color(0xFFED1C24);
  static const Color cardBankAccountItem2Color1 = Color(0xFF429268);
  static const Color cardBankAccountItem3Color1 = Color(0xFF5D1178);
  static const Color cardBankAccountItem4Color1 = Color(0xFF006FFF);

  static const Color cardBankAccountItem1Color2 = Color(0xFFF5B7B9);
  static const Color cardBankAccountItem2Color2 = Color(0xFFC2D8CC);
  static const Color cardBankAccountItem3Color2 = Color(0xFFCBB5D2);
  static const Color cardBankAccountItem4Color2 = Color(0xFFB0CFF6);

  static const Color registeredOrder = Color(0xFF039BE5);
  static const Color successOrder = Color(0xFF4CAF50);

  static ThemeData get lightTheme {
    return ThemeData(
      useMaterial3: true,
      primaryColor: colorPrimary,
      primaryColorDark: colorPrimaryDark,
      colorScheme: ColorScheme.light(
        primary: mainColor,
        secondary: colorAccent,
        surface: white,
        background: mainBackground,
        error: red,
        onPrimary: white,
        onSecondary: white,
        onSurface: textColor,
        onBackground: textColor,
        onError: white,
      ),
      scaffoldBackgroundColor: mainBackground,
      fontFamily: 'SFPro',
      textTheme: const TextTheme(
        displayLarge: TextStyle(
          fontSize: 32,
          fontWeight: FontWeight.bold,
          color: textColor,
        ),
        displayMedium: TextStyle(
          fontSize: 28,
          fontWeight: FontWeight.bold,
          color: textColor,
        ),
        displaySmall: TextStyle(
          fontSize: 24,
          fontWeight: FontWeight.bold,
          color: textColor,
        ),
        headlineLarge: TextStyle(
          fontSize: 22,
          fontWeight: FontWeight.w600,
          color: textColor,
        ),
        headlineMedium: TextStyle(
          fontSize: 20,
          fontWeight: FontWeight.w600,
          color: textColor,
        ),
        headlineSmall: TextStyle(
          fontSize: 18,
          fontWeight: FontWeight.w600,
          color: textColor,
        ),
        titleLarge: TextStyle(
          fontSize: 16,
          fontWeight: FontWeight.w600,
          color: textColor,
        ),
        titleMedium: TextStyle(
          fontSize: 14,
          fontWeight: FontWeight.w600,
          color: textColor,
        ),
        titleSmall: TextStyle(
          fontSize: 12,
          fontWeight: FontWeight.w600,
          color: textColor,
        ),
        bodyLarge: TextStyle(
          fontSize: 16,
          fontWeight: FontWeight.normal,
          color: textColor,
        ),
        bodyMedium: TextStyle(
          fontSize: 14,
          fontWeight: FontWeight.normal,
          color: textColor,
        ),
        bodySmall: TextStyle(
          fontSize: 12,
          fontWeight: FontWeight.normal,
          color: textColor,
        ),
        labelLarge: TextStyle(
          fontSize: 14,
          fontWeight: FontWeight.w600,
          color: textColor,
        ),
        labelMedium: TextStyle(
          fontSize: 12,
          fontWeight: FontWeight.w600,
          color: textColor,
        ),
        labelSmall: TextStyle(
          fontSize: 10,
          fontWeight: FontWeight.w600,
          color: textColor,
        ),
      ),
      appBarTheme: const AppBarTheme(
        backgroundColor: white,
        elevation: 0,
        iconTheme: IconThemeData(color: textColor),
        titleTextStyle: TextStyle(
          color: textColor,
          fontSize: 18,
          fontWeight: FontWeight.w600,
          fontFamily: 'SFPro',
        ),
      ),
      inputDecorationTheme: InputDecorationTheme(
        filled: true,
        fillColor: white,
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(8),
          borderSide: const BorderSide(color: borderColor),
        ),
        enabledBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(8),
          borderSide: const BorderSide(color: borderColor),
        ),
        focusedBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(8),
          borderSide: const BorderSide(color: mainColor, width: 2),
        ),
        errorBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(8),
          borderSide: const BorderSide(color: red),
        ),
        hintStyle: const TextStyle(color: hintColor),
      ),
      elevatedButtonTheme: ElevatedButtonThemeData(
        style: ElevatedButton.styleFrom(
          backgroundColor: mainColor,
          foregroundColor: white,
          elevation: 0,
          padding: const EdgeInsets.symmetric(horizontal: 24, vertical: 16),
          shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.circular(8),
          ),
          textStyle: const TextStyle(
            fontSize: 16,
            fontWeight: FontWeight.w600,
            fontFamily: 'SFPro',
          ),
        ),
      ),
      cardTheme: CardThemeData(
        color: white,
        elevation: 2,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(12),
        ),
      ),
    );
  }
}
