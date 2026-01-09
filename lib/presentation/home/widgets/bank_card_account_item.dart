import 'package:flutter/material.dart';
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

    return Container(
      width: 250,
      height: 150,
      padding: const EdgeInsets.all(12),
      decoration: BoxDecoration(
        color: backgroundColor,
        borderRadius: BorderRadius.circular(8),
      ),
      child: Stack(
        children: [
          // Card name at top left
          Positioned(
            top: 0,
            left: 0,
            right: 40,
            child: Text(
              altName,
              style: const TextStyle(
                color: Colors.white,
                fontSize: 15,
                fontWeight: FontWeight.normal,
              ),
              maxLines: 1,
              overflow: TextOverflow.ellipsis,
            ),
          ),
          // BTB logo at top right
          Positioned(
            top: 0,
            right: 0,
            child: Image.asset(
              'assets/images/ic_btb.png',
              width: 40,
              height: 20,
              errorBuilder: (context, error, stackTrace) {
                return const Icon(Icons.account_balance, color: Colors.white, size: 20);
              },
            ),
          ),
          // Balance centered
          Positioned(
            bottom: 30,
            left: 0,
            right: 0,
            child: Text(
              _formatBalance(balance, currency),
              style: const TextStyle(
                color: Colors.white,
                fontSize: 20,
                fontWeight: FontWeight.bold,
              ),
              textAlign: TextAlign.center,
            ),
          ),
          // Card number
          Positioned(
            bottom: 50,
            left: 0,
            right: 0,
            child: Text(
              formattedNumber,
              style: const TextStyle(
                color: Colors.white,
                fontSize: 11,
              ),
              textAlign: TextAlign.left,
            ),
          ),
          // Expire date (only for cards)
          if (isCard && expireDate != null)
            Positioned(
              bottom: 8,
              left: 0,
              child: Text(
                expireDate,
                style: const TextStyle(
                  color: Colors.white,
                  fontSize: 9,
                ),
              ),
            ),
          // Card type icon (Visa/Mastercard) at bottom right
          if (isCard && cardType != null)
            Positioned(
              bottom: 0,
              right: 0,
              child: Image.asset(
                cardType == 1
                    ? 'assets/images/ic_visa.png'
                    : 'assets/images/ic_mastercard.png',
                width: 40,
                height: 20,
                errorBuilder: (context, error, stackTrace) {
                  return const SizedBox.shrink();
                },
              ),
            ),
        ],
      ),
    );
  }
}

