import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import '../theme/app_theme.dart';
import '../constants/app_dimensions.dart';
import '../localization/app_localizations_ext.dart';
import '../utils/error_message_mapper.dart';

/// Styled error SnackBar for banking apps.
/// 
/// Provides user-friendly, localized error messages with proper styling.
class ErrorSnackBar {
  ErrorSnackBar._();

  /// Shows a styled error SnackBar with user-friendly message.
  /// 
  /// Maps backend error messages to localized, user-friendly messages
  /// and displays them in a styled SnackBar suitable for banking apps.
  static void show(
    BuildContext context,
    String backendErrorMessage, {
    Duration duration = const Duration(seconds: 4),
  }) {
    final l10n = context.l10n;
    final userFriendlyMessage = ErrorMessageMapper.mapError(
      backendErrorMessage,
      l10n,
    );

    ScaffoldMessenger.of(context).showSnackBar(
      SnackBar(
        content: Row(
          children: [
            Icon(
              Icons.error_outline,
              color: AppTheme.white,
              size: 24.sp,
            ),
            SizedBox(width: 12.w),
            Expanded(
              child: Text(
                userFriendlyMessage,
                style: TextStyle(
                  color: AppTheme.white,
                  fontSize: 14.sp,
                  fontWeight: FontWeight.w500,
                  fontFamily: 'SFPro',
                ),
              ),
            ),
          ],
        ),
        backgroundColor: AppTheme.red,
        behavior: SnackBarBehavior.floating,
        margin: EdgeInsets.all(AppDimensions.spacing16),
        padding: AppDimensions.padding16,
        shape: RoundedRectangleBorder(
          borderRadius: AppDimensions.radius12,
        ),
        duration: duration,
        elevation: 6,
      ),
    );
  }
}

