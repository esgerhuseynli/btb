// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'bank_account.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

BankAccount _$BankAccountFromJson(Map<String, dynamic> json) => BankAccount(
      branchCode: (json['branchCode'] as num?)?.toInt(),
      branchName: json['branchName'] as String?,
      accountNumber: json['accountNumber'] as String,
      ibanAccount: json['ibanAccount'] as String?,
      accountName: json['accountName'] as String?,
      accountAltName: json['accountAltName'] as String?,
      accountColor: (json['accountColor'] as num?)?.toInt(),
      currency: (json['currency'] as num).toInt(),
      balanceInLC: (json['balanceInLC'] as num?)?.toDouble(),
      balanceInFC: (json['balanceInFC'] as num?)?.toDouble(),
      dateOpen: json['dateOpen'] as String?,
      dateClose: json['dateClose'] as String?,
      dateCloseFromTaxes: json['dateCloseFromTaxes'] as String?,
      dateCloseFromPension: json['dateCloseFromPension'] as String?,
      dateCloseFromJustice: json['dateCloseFromJustice'] as String?,
    );

Map<String, dynamic> _$BankAccountToJson(BankAccount instance) =>
    <String, dynamic>{
      'branchCode': instance.branchCode,
      'branchName': instance.branchName,
      'accountNumber': instance.accountNumber,
      'ibanAccount': instance.ibanAccount,
      'accountName': instance.accountName,
      'accountAltName': instance.accountAltName,
      'accountColor': instance.accountColor,
      'currency': instance.currency,
      'balanceInLC': instance.balanceInLC,
      'balanceInFC': instance.balanceInFC,
      'dateOpen': instance.dateOpen,
      'dateClose': instance.dateClose,
      'dateCloseFromTaxes': instance.dateCloseFromTaxes,
      'dateCloseFromPension': instance.dateCloseFromPension,
      'dateCloseFromJustice': instance.dateCloseFromJustice,
    };
