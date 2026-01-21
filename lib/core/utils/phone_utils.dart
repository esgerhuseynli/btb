/// Utility class for phone number normalization and formatting.
/// 
/// Handles business logic for phone number processing,
/// keeping it separate from the UI layer.
class PhoneUtils {
  PhoneUtils._();

  /// Normalizes a phone number to username format (9 digits without country code).
  /// 
  /// This is the format expected by the API for authentication.
  /// 
  /// Examples:
  ///   "+994501234567" -> "501234567"
  ///   "994501234567" -> "501234567"
  ///   "501234567" -> "501234567"
  ///   "+994 50 123 45 67" -> "501234567"
  /// 
  /// Returns the normalized username (9 digits) or the original string if normalization fails.
  static String normalizeToUsername(String phone) {
    if (phone.isEmpty) return phone;

    // Remove all non-digit characters
    final phoneDigits = phone.replaceAll(RegExp(r'\D'), '');

    // If it's already 9 digits, return as is
    if (phoneDigits.length == 9) {
      return phoneDigits;
    }

    // If it's 12 digits and starts with 994, extract the last 9 digits
    if (phoneDigits.length == 12 && phoneDigits.startsWith('994')) {
      return phoneDigits.substring(3);
    }

    // If it's 13 digits and starts with 994 (with leading 0), extract the last 9 digits
    if (phoneDigits.length == 13 && phoneDigits.startsWith('994')) {
      return phoneDigits.substring(4);
    }

    // Return as is if format doesn't match expected patterns
    // This allows the API to handle edge cases
    return phoneDigits;
  }

  /// Normalizes a phone number to full format with country code (+994XXXXXXXXX).
  /// 
  /// This is useful for display or OTP API calls that require the full format.
  /// 
  /// Examples:
  ///   "501234567" -> "+994501234567"
  ///   "994501234567" -> "+994501234567"
  ///   "+994501234567" -> "+994501234567"
  static String normalizeToFullFormat(String phone) {
    if (phone.isEmpty) return phone;

    // Remove all non-digit characters except +
    final cleaned = phone.replaceAll(RegExp(r'[^\d+]'), '');

    // If already has +994, return as is
    if (cleaned.startsWith('+994')) {
      return cleaned;
    }

    // If starts with 994 (without +), add +
    if (cleaned.startsWith('994')) {
      return '+$cleaned';
    }

    // If it's a 9-digit number, add +994 prefix
    if (cleaned.length == 9) {
      return '+994$cleaned';
    }

    // Return as is if format is unclear
    return cleaned;
  }

  /// Masks a phone number for display purposes, showing only the last 2 digits.
  /// 
  /// This is useful for privacy when displaying phone numbers in the UI.
  /// 
  /// Examples:
  ///   "+994501234567" -> "+994 50 *** ** 67"
  ///   "501234567" -> "+994 50 *** ** 67"
  ///   null or empty -> "+994 50 *** ** 00"
  static String maskPhoneNumber(String? phone) {
    if (phone == null || phone.isEmpty) {
      return '+994 ** *** ** 00';
    }

    // Remove all non-digit characters
    final phoneDigits = phone.replaceAll(RegExp(r'\D'), '');
    
    if (phoneDigits.length >= 9) {
      // Extract the last 2 digits
      final lastTwo = phoneDigits.substring(phoneDigits.length - 2);
      return '+994 ** *** ** $lastTwo';
    }
    
    return '+994 ** *** ** 00';
  }
}

