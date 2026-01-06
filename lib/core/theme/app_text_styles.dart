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
  /// - Font weight: Semibold (600)
  /// - Line height: 1.4
  /// - Color: Black (#000000)
  static TextStyle screenTitle(BuildContext context) {
    return TextStyle(
      fontFamily: 'SFPro',
      fontSize: 20.sp,
      fontWeight: FontWeight.w600,
      height: 1.4,
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
  /// - Font weight: Semibold (600)
  /// - Line height: 1.0
  /// - Custom color can be applied
  static TextStyle buttonTitle(BuildContext context, {Color? color}) {
    return TextStyle(
      fontFamily: 'SFPro',
      fontSize: 16.sp,
      fontWeight: FontWeight.w600,
      height: 1.0,
      letterSpacing: 0,
      color: color,
    );
  }

  /// Button subtitle style - Body/Small
  /// Used for button subtitles and secondary action text
  /// - Font size: 14.sp
  /// - Font weight: Medium (500)
  /// - Line height: 1.71
  /// - Custom color can be applied
  static TextStyle buttonSubtitle(BuildContext context, {Color? color}) {
    return TextStyle(
      fontFamily: 'SFPro',
      fontSize: 14.sp,
      fontWeight: FontWeight.w500,
      height: 1.71,
      letterSpacing: 0,
      color: color,
    );
  }
}

