import 'package:flutter/material.dart';
import 'app_theme.dart';

class AppTextStyles {
  AppTextStyles._();

  // ==================== TITLES ====================

  static TextStyle screenTitle(BuildContext context) {
    return Theme.of(context).textTheme.titleLarge!.copyWith(
      fontFamily: 'SFPro',
      fontWeight: FontWeight.w600,
      color: const Color(0xFF000000),
    );
  }

  static TextStyle display(BuildContext context, {Color? color}) {
    return Theme.of(context).textTheme.headlineLarge!.copyWith(
      fontFamily: 'SFPro',
      fontWeight: FontWeight.bold,
      color: color ?? const Color(0xFF000000),
    );
  }

  // ==================== PHONE ====================

  static TextStyle phoneNumberDisplay(BuildContext context) {
    return Theme.of(context).textTheme.headlineMedium!.copyWith(
      fontWeight: FontWeight.bold,
      color: AppTheme.textColor,
    );
  }

  static TextStyle phoneNumberInput(BuildContext context) {
    return Theme.of(context).textTheme.headlineMedium!.copyWith(
      fontWeight: FontWeight.bold,
      color: AppTheme.textColor,
    );
  }

  static TextStyle phoneNumberBold(BuildContext context) {
    return Theme.of(context).textTheme.bodyLarge!.copyWith(
      fontFamily: 'Inter',
      fontWeight: FontWeight.w600,
      color: AppTheme.textColor,
    );
  }

  // ==================== BUTTONS ====================

  static TextStyle buttonTitle(BuildContext context, {Color? color}) {
    return Theme.of(context).textTheme.labelLarge!.copyWith(
      fontFamily: 'SFPro',
      fontWeight: FontWeight.w700,
      color: color,
    );
  }

  static TextStyle buttonSubtitle(BuildContext context, {Color? color}) {
    return Theme.of(context).textTheme.bodyMedium!.copyWith(
      fontFamily: 'SFPro',
      fontWeight: FontWeight.w500,
      color: color,
    );
  }

  // ==================== INPUT ====================

  static TextStyle inputText(BuildContext context, {Color? color}) {
    return Theme.of(context).textTheme.bodyLarge!.copyWith(
      fontFamily: 'SFPro',
      fontWeight: FontWeight.w600,
      color: color ?? AppTheme.textColor,
    );
  }

  static TextStyle inputHint(BuildContext context) {
    return Theme.of(context).textTheme.bodyLarge!.copyWith(
      fontFamily: 'SFPro',
      fontWeight: FontWeight.w600,
      color: const Color(0xFFC4C4C4),
    );
  }

  static TextStyle inputLabel(BuildContext context) {
    return Theme.of(context).textTheme.bodySmall!.copyWith(
      fontFamily: 'Inter',
      fontWeight: FontWeight.w400,
      color: const Color(0xFFBDBDBD),
    );
  }

  // ==================== OTP ====================

  static TextStyle otpFieldText(BuildContext context) {
    return Theme.of(context).textTheme.titleLarge!.copyWith(
      fontFamily: 'SFPro',
      fontWeight: FontWeight.w500,
      color: AppTheme.textPlaceholder,
    );
  }

  // ==================== LEGAL ====================

  static TextStyle legalDisclaimer(BuildContext context) {
    return Theme.of(context).textTheme.bodySmall!.copyWith(
      fontFamily: 'SFPro',
      color: AppTheme.textColor,
      height: 1.4,
    );
  }

  static TextStyle caption(BuildContext context, {Color? color}) {
    return Theme.of(context).textTheme.bodySmall!.copyWith(
      fontFamily: 'SFPro',
      fontWeight: FontWeight.w600,
      color: color ?? AppTheme.textDark,
    );
  }

  // ==================== HOME PAGE ====================

  /// Welcome text style - Headline/Small/Bold
  /// Used for welcome messages on home page
  static TextStyle welcomeText(BuildContext context, {Color? color}) {
    return Theme.of(context).textTheme.headlineSmall!.copyWith(
      fontFamily: 'SFPro',
      fontWeight: FontWeight.bold,
      color: color ?? AppTheme.textDark,
      letterSpacing: 0.24,
    );
  }

  /// User name style - Body/Medium
  /// Used for displaying user names
  static TextStyle userName(BuildContext context, {Color? color}) {
    return Theme.of(context).textTheme.bodyMedium!.copyWith(
      fontFamily: 'SFPro',
      fontWeight: FontWeight.normal,
      color: color ?? AppTheme.textDark,
      letterSpacing: 0.24,
    );
  }

  /// Balance label style - Body/Medium
  /// Used for balance labels
  static TextStyle balanceLabel(BuildContext context, {Color? color}) {
    return Theme.of(context).textTheme.bodyMedium!.copyWith(
      fontFamily: 'SFPro',
      fontWeight: FontWeight.normal,
      color: color ?? AppTheme.textColor,
    );
  }

  /// Balance amount style - Display/Large/Bold
  /// Used for displaying large balance amounts
  static TextStyle balanceAmount(BuildContext context, {Color? color}) {
    return Theme.of(context).textTheme.displayLarge!.copyWith(
      fontFamily: 'SFPro',
      fontWeight: FontWeight.bold,
      color: color ?? AppTheme.textDark,
      letterSpacing: 1.28,
    );
  }

  /// Card bank name style - Label/Small
  /// Used for bank names on cards
  static TextStyle cardBankName(BuildContext context, {Color? color, String? fontFamily}) {
    return Theme.of(context).textTheme.labelSmall!.copyWith(
      fontFamily: fontFamily ?? 'Cormorant Garamond',
      fontWeight: FontWeight.w600,
      color: color ?? Colors.white,
    );
  }

  /// Card number style - Body/Small
  /// Used for card numbers on bank cards
  static TextStyle cardNumber(BuildContext context, {Color? color, String? fontFamily}) {
    return Theme.of(context).textTheme.bodySmall!.copyWith(
      fontFamily: fontFamily ?? 'Poppins',
      fontWeight: FontWeight.normal,
      color: color ?? Colors.white,
    );
  }

  /// Card balance style - Title/Medium
  /// Used for balance amounts on cards
  static TextStyle cardBalance(BuildContext context, {Color? color, String? fontFamily}) {
    return Theme.of(context).textTheme.titleMedium!.copyWith(
      fontFamily: fontFamily ?? 'Poppins',
      fontWeight: FontWeight.w600,
      color: color ?? Colors.white,
    );
  }

  /// Card visa label style - Label/Small/Bold
  /// Used for VISA labels on cards
  static TextStyle cardVisaLabel(BuildContext context, {Color? color}) {
    return Theme.of(context).textTheme.labelSmall!.copyWith(
      fontFamily: 'SFPro',
      fontWeight: FontWeight.bold,
      color: color ?? Colors.white,
      letterSpacing: 1,
    );
  }

  /// Action button label style - Label/Large
  /// Used for action button labels on home page
  static TextStyle actionButtonLabel(BuildContext context, {Color? color}) {
    return Theme.of(context).textTheme.labelLarge!.copyWith(
      fontFamily: 'SF Pro',
      fontWeight: FontWeight.w600,
      color: color ?? AppTheme.textDark,
    );
  }

  /// Transactions header style - Headline/Small/Medium
  /// Used for section headers like "Transactions"
  static TextStyle transactionsHeader(BuildContext context, {Color? color}) {
    return Theme.of(context).textTheme.headlineSmall!.copyWith(
      fontFamily: 'SF Pro',
      fontWeight: FontWeight.w500,
      color: color ?? AppTheme.textColor,
    );
  }

  /// See all link style - Body/Small
  /// Used for "See all" links
  static TextStyle seeAllLink(BuildContext context, {Color? color}) {
    return Theme.of(context).textTheme.bodySmall!.copyWith(
      fontFamily: 'SF Pro',
      fontWeight: FontWeight.normal,
      color: color ?? AppTheme.textSecondaryGray,
    );
  }

  /// Date label style - Body/Small/Medium
  /// Used for date labels in transaction groups
  static TextStyle dateLabel(BuildContext context, {Color? color}) {
    return Theme.of(context).textTheme.bodySmall!.copyWith(
      fontFamily: 'SF Pro',
      fontWeight: FontWeight.w500,
      color: color ?? AppTheme.textDateGray,
    );
  }

  /// Transaction merchant style - Body/Large/Medium
  /// Used for merchant names in transactions
  static TextStyle transactionMerchant(BuildContext context, {Color? color}) {
    return Theme.of(context).textTheme.bodyLarge!.copyWith(
      fontFamily: 'Inter',
      fontWeight: FontWeight.w500,
      color: color ?? AppTheme.transactionMerchantText,
    );
  }

  /// Transaction date style - Body/Small
  /// Used for transaction dates and categories
  static TextStyle transactionDate(BuildContext context, {Color? color}) {
    return Theme.of(context).textTheme.bodySmall!.copyWith(
      fontFamily: 'SF Pro',
      fontWeight: FontWeight.normal,
      color: color ?? AppTheme.textDateGray,
    );
  }

  /// Transaction amount style - Body/Medium/Medium
  /// Used for transaction amounts
  static TextStyle transactionAmount(BuildContext context, {Color? color}) {
    return Theme.of(context).textTheme.bodyMedium!.copyWith(
      fontFamily: 'SF Pro',
      fontWeight: FontWeight.w500,
      color: color,
    );
  }

  /// Empty state text style - Body/Medium
  /// Used for empty state messages
  static TextStyle emptyStateText(BuildContext context, {Color? color}) {
    return Theme.of(context).textTheme.bodyMedium!.copyWith(
      fontFamily: 'SFPro',
      color: color ?? AppTheme.textColor,
    );
  }

}
