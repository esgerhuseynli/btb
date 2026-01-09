import 'dart:convert';
import 'dart:math';
import 'package:crypto/crypto.dart';
import 'package:intl/intl.dart';

class AppUtils {
  // Language codes mapping
  static const Map<int, String> langCodes = {
    0: 'az',
    1: 'en',
    2: 'ru',
  };

  // For appHash generation (alternates between templates)
  static bool _shouldReverse = false;

  // Date formatters
  static final DateFormat dateFormatter = DateFormat('dd-MM-yyyy');
  static final DateFormat dateTimeFormatter = DateFormat('dd-MM-yyyy HH:mm');
  static final DateFormat timeFormatter = DateFormat('HH:mm');

  /// Hash password using SHA-512
  /// Matches Android Utils.passwordHash() - returns uppercase hex string
  static String passwordHash(String password) {
    final bytes = utf8.encode(password);
    final digest = sha512.convert(bytes);
    return digest.toString().toUpperCase();
  }

  /// Format currency amount
  static String formatCurrency(double amount, {int currency = 0}) {
    final currencySymbol = _getCurrencySymbol(currency);
    return '$amount $currencySymbol';
  }

  /// Get currency symbol
  static String _getCurrencySymbol(int currency) {
    switch (currency) {
      case 0: // AZN
        return '₼';
      case 1: // USD
        return '\$';
      case 2: // EUR
        return '€';
      case 3: // RUB
        return '₽';
      case 4: // GBP
        return '£';
      case 5: // TRY
        return '₺';
      default:
        return '₼';
    }
  }

  /// Format card number (add spaces every 4 digits)
  static String formatCardNumber(String cardNumber) {
    if (cardNumber.isEmpty) return '';
    final cleaned = cardNumber.replaceAll(RegExp(r'\D'), '');
    final buffer = StringBuffer();
    for (int i = 0; i < cleaned.length; i++) {
      if (i > 0 && i % 4 == 0) {
        buffer.write(' ');
      }
      buffer.write(cleaned[i]);
    }
    return buffer.toString();
  }

  /// Mask card number (show only last 4 digits)
  static String maskCardNumber(String cardNumber) {
    if (cardNumber.length < 4) return cardNumber;
    final last4 = cardNumber.substring(cardNumber.length - 4);
    return '**** **** **** $last4';
  }

  /// Format phone number
  static String formatPhoneNumber(String phone) {
    if (phone.isEmpty) return '';
    final cleaned = phone.replaceAll(RegExp(r'\D'), '');
    if (cleaned.length == 9) {
      return '+994 $cleaned';
    } else if (cleaned.length == 12 && cleaned.startsWith('994')) {
      return '+${cleaned.substring(0, 3)} ${cleaned.substring(3)}';
    }
    return phone;
  }

  /// Validate email
  static bool isValidEmail(String email) {
    return RegExp(r'^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$').hasMatch(email);
  }

  /// Validate phone number (Azerbaijan format)
  static bool isValidPhoneNumber(String phone) {
    final cleaned = phone.replaceAll(RegExp(r'\D'), '');
    return cleaned.length == 9 || (cleaned.length == 12 && cleaned.startsWith('994'));
  }

  /// Normalize phone number to include +994 prefix
  /// Removes spaces and ensures +994 prefix is present
  /// Examples:
  ///   "102143434" -> "+994102143434"
  ///   "994102143434" -> "+994102143434"
  ///   "+994102143434" -> "+994102143434"
  static String normalizePhoneNumber(String phone) {
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

  /// Get language code from index
  static String getLanguageCode(int index) {
    return langCodes[index] ?? 'az';
  }

  /// Get language index from code
  static int getLanguageIndex(String code) {
    return langCodes.entries.firstWhere(
      (entry) => entry.value == code,
      orElse: () => const MapEntry(0, 'az'),
    ).key;
  }

  /// Format date string
  static String formatDate(DateTime date) {
    return dateFormatter.format(date);
  }

  /// Parse date string
  static DateTime? parseDate(String dateString) {
    try {
      return dateFormatter.parse(dateString);
    } catch (e) {
      return null;
    }
  }

  /// Check if string is empty or null
  static bool isEmpty(String? value) {
    return value == null || value.trim().isEmpty;
  }

  /// Capitalize first letter
  static String capitalize(String text) {
    if (text.isEmpty) return text;
    return text[0].toUpperCase() + text.substring(1).toLowerCase();
  }

  /// Generate app hash (128-character random string)
  /// Matches Android Utils.appHash() implementation
  static String appHash() {
    String template = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    if (_shouldReverse) {
      template = "AB0CD1EF2GH3IJ4KL5MN6OP7QR8ST9UVWXYZ";
    }
    
    _shouldReverse = !_shouldReverse;
    
    final random = Random.secure();
    const len = 128;
    
    final buffer = StringBuffer();
    for (int i = 0; i < len; i++) {
      buffer.write(template[random.nextInt(template.length)]);
    }
    
    return buffer.toString();
  }
}

