import 'package:flutter/material.dart';
import 'package:flutter_screenutil/flutter_screenutil.dart';
import '../../../core/theme/app_theme.dart';
import '../../../data/models/bank_card.dart';
import '../../../data/models/bank_account.dart';

class BankCardAccountItem extends StatelessWidget {
  final dynamic item; // BankCard or BankAccount
  final bool isCardItem;

  const BankCardAccountItem({
    super.key,
    required this.item,
    required this.isCardItem,
  });

  Color _getCardBackgroundColor(int colorIndex) {
    switch (colorIndex) {
      case 0:
      case 1:
        return AppTheme.cardBankAccountItem1Color1;
      case 2:
        return AppTheme.cardBankAccountItem2Color1;
      case 3:
        return AppTheme.cardBankAccountItem3Color1;
      case 4:
        return AppTheme.cardBankAccountItem4Color1;
      default:
        return AppTheme.cardBankAccountItem2Color1;
    }
  }

  String _formatCardNumber(String cardNumber) {
    if (cardNumber.length >= 8) {
      final first4 = cardNumber.substring(0, 4);
      final last4 = cardNumber.substring(cardNumber.length - 4);
      return '$first4 **** **** $last4';
    }
    return cardNumber;
  }

  String _formatBalance(double balance, int currency) {
    String currencySymbol = 'AZN';
    switch (currency) {
      case 1:
        currencySymbol = 'AZN';
        break;
      case 2:
        currencySymbol = 'USD';
        break;
      case 3:
        currencySymbol = 'EUR';
        break;
      default:
        currencySymbol = 'AZN';
    }
    return '${balance.toStringAsFixed(2)} $currencySymbol';
  }

  @override
  Widget build(BuildContext context) {
    final isCard = isCardItem && item is BankCard;
    final cardColor = isCard
        ? (item as BankCard).cardColor
        : (item as BankAccount).accountColor ?? 2;
    final backgroundColor = _getCardBackgroundColor(cardColor);

    final balance = isCard
        ? (item as BankCard).cardBalance
        : (item as BankAccount).balanceInLC ?? 0.0;
    final currency = isCard
        ? (item as BankCard).currency
        : (item as BankAccount).currency;
    final altName = isCard
        ? (item as BankCard).cardAltName ?? ''
        : (item as BankAccount).accountAltName ?? '';
    final number = isCard
        ? (item as BankCard).cardNumber
        : (item as BankAccount).accountNumber;
    final formattedNumber = isCard ? _formatCardNumber(number) : number;
    final expireDate = isCard ? (item as BankCard).cardExpiryDate : null;
    final cardType = isCard ? (item as BankCard).bankCardType : null;

    // Get gradient colors - all cards use red gradient, accounts use green
    Gradient? gradient;
    if (isCard) {
      // All cards use the same red gradient design
      gradient = const LinearGradient(
        begin: Alignment.topLeft,
        end: Alignment.bottomRight,
        colors: [
          Color(0xFF424242), // Dark gray
          Color(0xFFEE3F3E), // Red
        ],
      );
    } else if (cardColor == 2) {
      // Green gradient for accounts
      gradient = const LinearGradient(
        begin: Alignment.topLeft,
        end: Alignment.bottomRight,
        colors: [
          Color(0xFF61A778), // Green
          Color(0xFF385D43), // Dark green
        ],
      );
    }

    return Container(
      width: 276.w,
      height: 157.395.h,
      padding: EdgeInsets.symmetric(
        horizontal: 18.649.w,
        vertical: 25.362.h,
      ),
      decoration: BoxDecoration(
        gradient: gradient ?? LinearGradient(
          begin: Alignment.topLeft,
          end: Alignment.bottomRight,
          colors: [backgroundColor, backgroundColor],
        ),
        borderRadius: BorderRadius.circular(12.r),
      ),
      child: Stack(
        children: [
          // BTB BANK text at top left
          Positioned(
            top: 0,
            left: 0,
            child: Text(
              'BTB BANK',
              style: TextStyle(
                color: Colors.white,
                fontSize: 9.82.sp,
                fontWeight: FontWeight.w600,
                fontFamily: 'Cormorant Garamond',
                letterSpacing: 0,
              ),
            ),
          ),
          // WiFi icon at top right (rotated ~86.5 degrees)
          Positioned(
            top: 0,
            right: 0,
            child: Transform.rotate(
              angle: 1.51, // ~86.5 degrees in radians
              child: Icon(
                Icons.wifi,
                size: 26.697.sp,
                color: Colors.white,
              ),
            ),
          ),
          // Card number in the middle (for cards)
          if (isCard)
            Positioned(
              left: 0,
              top: 59.78.h, // Position from top
              child: Row(
                children: [
                  Text(
                    formattedNumber.split(' ')[0], // First 4 digits
                    style: TextStyle(
                      color: Colors.white,
                      fontSize: 11.94.sp,
                      fontWeight: FontWeight.normal,
                      fontFamily: 'Poppins',
                      letterSpacing: 0,
                    ),
                  ),
                  SizedBox(width: 8.w),
                  Text(
                    formattedNumber.split(' ').length > 3 
                        ? formattedNumber.split(' ')[3] // Last 4 digits
                        : '',
                    style: TextStyle(
                      color: Colors.white,
                      fontSize: 11.94.sp,
                      fontWeight: FontWeight.normal,
                      fontFamily: 'Poppins',
                      letterSpacing: 0,
                    ),
                  ),
                ],
              ),
            ),
          // Balance at bottom
          Positioned(
            bottom: 0,
            left: 0,
            child: Text(
              _formatBalance(balance, currency),
              style: TextStyle(
                color: Colors.white,
                fontSize: 14.92.sp,
                fontWeight: FontWeight.w600,
                fontFamily: 'Poppins',
                letterSpacing: 0,
              ),
            ),
          ),
          // Account number (for accounts, not cards)
          if (!isCard)
            Positioned(
              left: 0,
              bottom: 0,
              child: Text(
                number,
                style: TextStyle(
                  color: Colors.white,
                  fontSize: 11.19.sp,
                  fontWeight: FontWeight.normal,
                  fontFamily: 'SF Pro',
                  letterSpacing: 0,
                ),
              ),
            ),
          // VISA logo at bottom right (for cards)
          if (isCard && cardType == 1)
            Positioned(
              bottom: 0,
              right: 0,
              child: Container(
                padding: EdgeInsets.symmetric(horizontal: 4.w, vertical: 2.h),
                decoration: BoxDecoration(
                  color: Colors.white.withOpacity(0.2),
                  borderRadius: BorderRadius.circular(4.r),
                ),
                child: Text(
                  'VISA',
                  style: TextStyle(
                    color: Colors.white,
                    fontSize: 10.sp,
                    fontWeight: FontWeight.bold,
                    letterSpacing: 1,
                  ),
                ),
              ),
            ),
        ],
      ),
    );
  }
}

