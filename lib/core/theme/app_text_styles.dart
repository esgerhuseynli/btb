import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import 'app_theme.dart';

/// Global text styles for the application.
/// 
/// All text styles use ScreenUtil for responsive sizing.
/// This ensures consistent typography across different screen sizes.
class AppTextStyles {
  AppTextStyles._();

  /// Screen title style - H2/Medium
  /// Used for main screen titles
  /// - Font size: 20.sp
  /// - Font weight: Semibold (590)
  /// - Line height: 28px (1.4)
  /// - Color: Black (#000000)
  static TextStyle screenTitle(BuildContext context) {
    return TextStyle(
      fontFamily: 'SFPro',
      fontSize: 20.sp,
      fontWeight: FontWeight.w600, // 590 = Semibold
      height: 1.4, // 28px / 20px
      letterSpacing: 0,
      color: const Color(0xFF000000),
    );
  }

  /// Phone number display style
  /// Used for displaying phone numbers and country codes
  /// - Font size: 24.sp
  /// - Font weight: Bold
  /// - Color: AppTheme.textColor
  static TextStyle phoneNumberDisplay(BuildContext context) {
    return TextStyle(
      fontSize: 24.sp,
      fontWeight: FontWeight.bold,
      color: AppTheme.textColor,
      letterSpacing: 0,
    );
  }

  /// Phone number input style
  /// Used for phone number input fields
  /// - Font size: 24.sp
  /// - Font weight: Bold
  /// - Color: AppTheme.textColor
  static TextStyle phoneNumberInput(BuildContext context) {
    return TextStyle(
      color: AppTheme.textColor,
      fontSize: 24.sp,
      fontWeight: FontWeight.bold,
      letterSpacing: 0,
    );
  }

  /// Legal disclaimer style
  /// Used for legal text and disclaimers
  /// - Font size: 12.sp
  /// - Line height: 1.4
  /// - Color: AppTheme.textColor
  /// - Font family: SFPro
  static TextStyle legalDisclaimer(BuildContext context) {
    return Theme.of(context).textTheme.bodySmall?.copyWith(
          color: AppTheme.textColor,
          fontSize: 12.sp,
          height: 1.4,
          fontFamily: "SFPro",
        ) ??
        TextStyle(
          color: AppTheme.textColor,
          fontSize: 12.sp,
          height: 1.4,
          fontFamily: "SFPro",
        );
  }

  /// Button title style - Body/Large
  /// Used for button titles and primary action text
  /// - Font size: 16.sp
  /// - Font weight: Semibold (590)
  /// - Line height: 100% (1.0)
  /// - Custom color can be applied
  static TextStyle buttonTitle(BuildContext context, {Color? color}) {
    return TextStyle(
      fontFamily: 'SFPro',
      fontSize: 12.sp,
      fontWeight: FontWeight.w600, // 590 = Semibold
      height: 1.0, // 100%
      letterSpacing: 0,
      color: color,
    );
  }

  /// Button subtitle style - Body/Small
  /// Used for button subtitles and secondary action text
  /// - Font size: 14.sp
  /// - Font weight: Medium (510)
  /// - Line height: 24px (1.71)
  /// - Custom color can be applied
  static TextStyle buttonSubtitle(BuildContext context, {Color? color}) {
    return TextStyle(
      fontFamily: 'SFPro',
      fontSize: 12.sp,
      fontWeight: FontWeight.w500, // 510 = Medium
      height: 1.71, // 24px / 14px
      letterSpacing: 0,
      color: color,
    );
  }

  /// Input label style - Body/Small/Inter
  /// Used for input field labels
  /// - Font size: 14.sp
  /// - Font weight: Regular (400)
  /// - Line height: 20px (1.43)
  /// - Font family: Inter
  /// - Color: #BDBDBD (icon-disabled)
  static TextStyle inputLabel(BuildContext context) {
    return TextStyle(
      fontFamily: 'Inter',
      fontSize: 14.sp,
      fontWeight: FontWeight.w400,
      height: 1.43, // 20px / 14px
      letterSpacing: 0,
      color: const Color(0xFFBDBDBD),
    );
  }

  /// Display style - Large display text
  /// Used for large phone number displays
  /// - Font size: 32.sp
  /// - Font weight: Bold (700)
  /// - Line height: 28px (0.875)
  /// - Color: Black (#000000)
  static TextStyle display(BuildContext context, {Color? color}) {
    return TextStyle(
      fontFamily: 'SFPro',
      fontSize: 32.sp,
      fontWeight: FontWeight.bold, // 700
      height: 0.875, // 28px / 32px
      letterSpacing: 0,
      color: color ?? const Color(0xFF000000),
    );
  }

  /// Input text style - Body/Large
  /// Used for text input fields
  /// - Font size: 16.sp
  /// - Font weight: Semibold (590)
  /// - Font family: SFPro
  /// - Custom color can be applied
  static TextStyle inputText(BuildContext context, {Color? color}) {
    return TextStyle(
      fontFamily: 'SFPro',
      fontSize: 16.sp,
      fontWeight: FontWeight.w600, // 590 = Semibold
      letterSpacing: 0,
      color: color,
    );
  }

  /// Input hint style - Body/Large
  /// Used for input field hint text
  /// - Font size: 16.sp
  /// - Font weight: Semibold (590)
  /// - Font family: SFPro
  /// - Color: #C4C4C4 (disabled background)
  static TextStyle inputHint(BuildContext context) {
    return TextStyle(
      fontFamily: 'SFPro',
      fontSize: 16.sp,
      fontWeight: FontWeight.w600, // 590 = Semibold
      letterSpacing: 0,
      color: const Color(0xFFC4C4C4),
    );
  }

  /// OTP field text style - H2/Bold
  /// Used for OTP input fields
  /// - Font size: 20.sp
  /// - Font weight: Medium (500)
  /// - Line height: 1.2
  /// - Font family: SFPro
  /// - Color: #8E8E93 (text placeholder)
  static TextStyle otpFieldText(BuildContext context) {
    return TextStyle(
      fontFamily: 'SFPro',
      fontSize: 20.sp,
      fontWeight: FontWeight.w500, // Medium (510 in design)
      height: 1.2,
      letterSpacing: 0,
      color: AppTheme.textPlaceholder,
    );
  }

  /// Phone number bold style - Inter
  /// Used for phone numbers in descriptions
  /// - Font size: 16.sp
  /// - Font weight: Semibold (600)
  /// - Line height: 1.11
  /// - Font family: Inter
  static TextStyle phoneNumberBold(BuildContext context) {
    return TextStyle(
      fontFamily: 'Inter',
      fontSize: 16.sp,
      fontWeight: FontWeight.w600,
      height: 1.11,
      letterSpacing: 0,
    );
  }
}

