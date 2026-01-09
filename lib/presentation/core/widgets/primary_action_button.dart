import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import '../../../core/theme/app_theme.dart';

class PrimaryActionButton extends StatelessWidget {
  final String text;
  
  final VoidCallback? onPressed;
  
  final bool isEnabled;
  
  final double? width;
  
  final double? height;

  const PrimaryActionButton({
    super.key,
    required this.text,
    this.onPressed,
    this.isEnabled = true,
    this.width,
    this.height,
  });

  @override
  Widget build(BuildContext context) {
    return SizedBox(
      width: width ?? double.infinity,
      height: height ?? 56.h,
      child: ElevatedButton(
        onPressed: isEnabled ? onPressed : null,
        style: ButtonStyle(
          backgroundColor: MaterialStateProperty.resolveWith<Color>(
            (Set<MaterialState> states) {
              if (states.contains(MaterialState.disabled) || !isEnabled) {
                return AppTheme.buttonDisabledBackground;
              }
              return AppTheme.buttonPrimaryBackground;
            },
          ),
          foregroundColor: MaterialStateProperty.resolveWith<Color>(
            (Set<MaterialState> states) {
              if (states.contains(MaterialState.disabled) || !isEnabled) {
                return AppTheme.buttonDisabledForeground;
              }
              return AppTheme.white;
            },
          ),
          overlayColor: MaterialStateProperty.resolveWith<Color?>(
            (Set<MaterialState> states) {
              if (states.contains(MaterialState.pressed) && isEnabled) {
                return AppTheme.buttonPressedOverlay;
              }
              return null;
            },
          ),
          shape: MaterialStateProperty.all<OutlinedBorder>(
            RoundedRectangleBorder(
              borderRadius: BorderRadius.circular(20.r),
            ),
          ),
          padding: MaterialStateProperty.all<EdgeInsets>(
            EdgeInsets.symmetric(horizontal: 16.w),
          ),
          elevation: MaterialStateProperty.all<double>(0),
        ),
        child: Text(
          text,
          style: TextStyle(
            fontSize: 16.sp,
            fontWeight: FontWeight.w600,
          ),
        ),
      ),
    );
  }
}

