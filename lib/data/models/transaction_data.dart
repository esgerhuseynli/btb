import 'package:flutter/material.dart';

/// Transaction data model for home page transactions
class TransactionData {
  final String merchant;
  final String dateAndCategory;
  final String amount;
  final IconData icon;
  final bool isPositive;

  TransactionData({
    required this.merchant,
    required this.dateAndCategory,
    required this.amount,
    required this.icon,
    required this.isPositive,
  });

  @override
  bool operator ==(Object other) =>
      identical(this, other) ||
      other is TransactionData &&
          runtimeType == other.runtimeType &&
          merchant == other.merchant &&
          dateAndCategory == other.dateAndCategory &&
          amount == other.amount &&
          icon == other.icon &&
          isPositive == other.isPositive;

  @override
  int get hashCode =>
      merchant.hashCode ^
      dateAndCategory.hashCode ^
      amount.hashCode ^
      icon.hashCode ^
      isPositive.hashCode;
}
