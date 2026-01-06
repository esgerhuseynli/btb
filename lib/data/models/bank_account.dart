import 'package:json_annotation/json_annotation.dart';

part 'bank_account.g.dart';

@JsonSerializable()
class BankAccount {
  @JsonKey(name: 'branchCode')
  final int? branchCode;

  @JsonKey(name: 'branchName')
  final String? branchName;

  @JsonKey(name: 'accountNumber')
  final String accountNumber;

  @JsonKey(name: 'ibanAccount')
  final String? ibanAccount;

  @JsonKey(name: 'accountName')
  final String? accountName;

  @JsonKey(name: 'accountAltName')
  final String? accountAltName;

  @JsonKey(name: 'accountColor')
  final int? accountColor;

  @JsonKey(name: 'currency')
  final int currency;

  @JsonKey(name: 'balanceInLC')
  final double? balanceInLC;

  @JsonKey(name: 'balanceInFC')
  final double? balanceInFC;

  @JsonKey(name: 'dateOpen')
  final String? dateOpen;

  @JsonKey(name: 'dateClose')
  final String? dateClose;

  @JsonKey(name: 'dateCloseFromTaxes')
  final String? dateCloseFromTaxes;

  @JsonKey(name: 'dateCloseFromPension')
  final String? dateCloseFromPension;

  @JsonKey(name: 'dateCloseFromJustice')
  final String? dateCloseFromJustice;

  BankAccount({
    this.branchCode,
    this.branchName,
    required this.accountNumber,
    this.ibanAccount,
    this.accountName,
    this.accountAltName,
    this.accountColor,
    required this.currency,
    this.balanceInLC,
    this.balanceInFC,
    this.dateOpen,
    this.dateClose,
    this.dateCloseFromTaxes,
    this.dateCloseFromPension,
    this.dateCloseFromJustice,
  });

  factory BankAccount.fromJson(Map<String, dynamic> json) =>
      _$BankAccountFromJson(json);

  Map<String, dynamic> toJson() => _$BankAccountToJson(this);
}



