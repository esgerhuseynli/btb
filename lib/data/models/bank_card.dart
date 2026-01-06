import 'package:json_annotation/json_annotation.dart';

part 'bank_card.g.dart';

@JsonSerializable()
class BankCard {
  @JsonKey(name: 'branchCode')
  final int branchCode;

  @JsonKey(name: 'branchName')
  final String? branchName;

  @JsonKey(name: 'cardServiceName')
  final String? cardServiceName;

  @JsonKey(name: 'cardHolderShortName')
  final String? cardHolderShortName;

  @JsonKey(name: 'idCard')
  final String idCard;

  @JsonKey(name: 'currency')
  final int currency;

  @JsonKey(name: 'cardNumber')
  final String cardNumber;

  @JsonKey(name: 'cardExpiryDate')
  final String? cardExpiryDate;

  @JsonKey(name: 'cardStatus')
  final int cardStatus;

  @JsonKey(name: 'cardBalance')
  final double cardBalance;

  @JsonKey(name: 'cardAltName')
  final String? cardAltName;

  @JsonKey(name: 'cardColor')
  final int cardColor;

  @JsonKey(name: 'bankCardType')
  final int bankCardType;

  BankCard({
    required this.branchCode,
    this.branchName,
    this.cardServiceName,
    this.cardHolderShortName,
    required this.idCard,
    required this.currency,
    required this.cardNumber,
    this.cardExpiryDate,
    required this.cardStatus,
    required this.cardBalance,
    this.cardAltName,
    required this.cardColor,
    required this.bankCardType,
  });

  factory BankCard.fromJson(Map<String, dynamic> json) =>
      _$BankCardFromJson(json);

  Map<String, dynamic> toJson() => _$BankCardToJson(this);
}



