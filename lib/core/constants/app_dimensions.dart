import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';

/// App dimensions constants for consistent spacing, padding, and radius values.
/// 
/// All values use ScreenUtil extensions (.h, .w, .r) for responsive sizing.
/// This ensures consistent spacing across different screen sizes.
class AppDimensions {
  AppDimensions._();

  // Spacing (vertical/horizontal gaps)
  static double get spacing4 => 4.h;
  static double get spacing8 => 8.h;
  static double get spacing12 => 12.h;
  static double get spacing16 => 16.h;
  static double get spacing24 => 24.h;
  static double get spacing32 => 32.h;
  static double get spacing40 => 40.h;

  // Padding
  static EdgeInsets get padding8 => EdgeInsets.all(8.w);
  static EdgeInsets get padding12 => EdgeInsets.all(12.w);
  static EdgeInsets get padding16 => EdgeInsets.all(16.w);
  static EdgeInsets get padding24 => EdgeInsets.all(24.w);
  
  // Horizontal padding
  static EdgeInsets get paddingHorizontal8 => EdgeInsets.symmetric(horizontal: 8.w);
  static EdgeInsets get paddingHorizontal12 => EdgeInsets.symmetric(horizontal: 12.w);
  static EdgeInsets get paddingHorizontal16 => EdgeInsets.symmetric(horizontal: 16.w);
  static EdgeInsets get paddingHorizontal24 => EdgeInsets.symmetric(horizontal: 24.w);
  
  // Vertical padding
  static EdgeInsets get paddingVertical8 => EdgeInsets.symmetric(vertical: 8.h);
  static EdgeInsets get paddingVertical12 => EdgeInsets.symmetric(vertical: 12.h);
  static EdgeInsets get paddingVertical16 => EdgeInsets.symmetric(vertical: 16.h);
  static EdgeInsets get paddingVertical24 => EdgeInsets.symmetric(vertical: 24.h);
  
  // Specific padding combinations
  static EdgeInsets get paddingLeft8 => EdgeInsets.only(left: 8.w);
  static EdgeInsets get paddingLeft8Bottom8 => EdgeInsets.only(left: 8.w, bottom: 8.h);
  static EdgeInsets get paddingHorizontal16Vertical8 => EdgeInsets.symmetric(horizontal: 16.w, vertical: 8.h);

  // Border radius
  static BorderRadius get radius8 => BorderRadius.circular(8.r);
  static BorderRadius get radius12 => BorderRadius.circular(12.r);
  static BorderRadius get radius16 => BorderRadius.circular(16.r);
  static BorderRadius get radius20 => BorderRadius.circular(20.r);
  static BorderRadius get radius24 => BorderRadius.circular(24.r);

  // SizedBox helpers for common spacing
  static SizedBox get sizedBox4 => SizedBox(height: 4.h);
  static SizedBox get sizedBox8 => SizedBox(height: 8.h);
  static SizedBox get sizedBox12 => SizedBox(height: 12.h);
  static SizedBox get sizedBox16 => SizedBox(height: 16.h);
  static SizedBox get sizedBox24 => SizedBox(height: 24.h);
  static SizedBox get sizedBox32 => SizedBox(height: 32.h);
  static SizedBox get sizedBox40 => SizedBox(height: 40.h);

  // Width spacing
  static SizedBox get sizedBoxWidth4 => SizedBox(width: 4.w);
  static SizedBox get sizedBoxWidth8 => SizedBox(width: 8.w);
  static SizedBox get sizedBoxWidth12 => SizedBox(width: 12.w);
  static SizedBox get sizedBoxWidth16 => SizedBox(width: 16.w);
  static SizedBox get sizedBoxWidth24 => SizedBox(width: 24.w);
}

