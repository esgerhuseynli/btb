import '../../l10n/app_localizations.dart';

/// Maps backend error messages to user-friendly localized messages.
/// 
/// In banking apps, errors should be:
/// - Styled and user-friendly
/// - Not raw backend messages
/// - Localized based on user's language preference
class ErrorMessageMapper {
  ErrorMessageMapper._();

  /// Maps a backend error message to a user-friendly localized message.
  /// 
  /// Returns a localized error message based on common error patterns.
  /// If no pattern matches, returns a generic error message.
  static String mapError(String backendMessage, AppLocalizations l10n) {
    final lowerMessage = backendMessage.toLowerCase().trim();

    // Network/Connection errors
    if (lowerMessage.contains('network') ||
        lowerMessage.contains('connection') ||
        lowerMessage.contains('timeout') ||
        lowerMessage.contains('socket')) {
      return l10n.errorNetwork;
    }

    // Authentication errors
    if (lowerMessage.contains('invalid') && lowerMessage.contains('password')) {
      return l10n.errorInvalidPassword;
    }
    if (lowerMessage.contains('wrong') && lowerMessage.contains('password')) {
      return l10n.errorWrongPassword;
    }
    if (lowerMessage.contains('incorrect') && lowerMessage.contains('password')) {
      return l10n.errorWrongPassword;
    }
    if (lowerMessage.contains('invalid') && lowerMessage.contains('username')) {
      return l10n.errorInvalidUsername;
    }
    if (lowerMessage.contains('user') && lowerMessage.contains('not found')) {
      return l10n.errorUserNotFound;
    }
    if (lowerMessage.contains('unauthorized') || lowerMessage.contains('authentication failed')) {
      return l10n.errorAuthenticationFailed;
    }

    // PIN/Code errors
    if (lowerMessage.contains('wrong') && lowerMessage.contains('pin')) {
      return l10n.errorWrongPin;
    }
    if (lowerMessage.contains('invalid') && lowerMessage.contains('code')) {
      return l10n.errorInvalidCode;
    }
    if (lowerMessage.contains('code') && lowerMessage.contains('expired')) {
      return l10n.errorCodeExpired;
    }

    // OTP errors
    if (lowerMessage.contains('otp') && lowerMessage.contains('invalid')) {
      return l10n.errorInvalidOtp;
    }
    if (lowerMessage.contains('otp') && lowerMessage.contains('expired')) {
      return l10n.errorOtpExpired;
    }

    // Account/Device errors
    if (lowerMessage.contains('device') && lowerMessage.contains('not registered')) {
      return l10n.errorDeviceNotRegistered;
    }
    if (lowerMessage.contains('account') && lowerMessage.contains('locked')) {
      return l10n.errorAccountLocked;
    }
    if (lowerMessage.contains('account') && lowerMessage.contains('suspended')) {
      return l10n.errorAccountSuspended;
    }

    // Server errors
    if (lowerMessage.contains('server error') ||
        lowerMessage.contains('internal server error') ||
        lowerMessage.contains('500')) {
      return l10n.errorServerError;
    }
    if (lowerMessage.contains('service unavailable') ||
        lowerMessage.contains('503')) {
      return l10n.errorServiceUnavailable;
    }

    // Generic fallback
    // Check if message is already in a user-friendly format (not a technical error)
    if (!lowerMessage.contains('exception') &&
        !lowerMessage.contains('error') &&
        !lowerMessage.contains('failed') &&
        !lowerMessage.contains('null') &&
        backendMessage.length < 100) {
      // Might already be a user-friendly message, return as-is
      return backendMessage;
    }

    // Default generic error
    return l10n.errorGeneric;
  }
}

