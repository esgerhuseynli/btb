// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'bank_card.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

BankCard _$BankCardFromJson(Map<String, dynamic> json) => BankCard(
      branchCode: (json['branchCode'] as num).toInt(),
      branchName: json['branchName'] as String?,
      cardServiceName: json['cardServiceName'] as String?,
      cardHolderShortName: json['cardHolderShortName'] as String?,
      idCard: json['idCard'] as String,
      currency: (json['currency'] as num).toInt(),
      cardNumber: json['cardNumber'] as String,
      cardExpiryDate: json['cardExpiryDate'] as String?,
      cardStatus: (json['cardStatus'] as num).toInt(),
      cardBalance: (json['cardBalance'] as num).toDouble(),
      cardAltName: json['cardAltName'] as String?,
      cardColor: (json['cardColor'] as num).toInt(),
      bankCardType: (json['bankCardType'] as num).toInt(),
    );

Map<String, dynamic> _$BankCardToJson(BankCard instance) => <String, dynamic>{
      'branchCode': instance.branchCode,
      'branchName': instance.branchName,
      'cardServiceName': instance.cardServiceName,
      'cardHolderShortName': instance.cardHolderShortName,
      'idCard': instance.idCard,
      'currency': instance.currency,
      'cardNumber': instance.cardNumber,
      'cardExpiryDate': instance.cardExpiryDate,
      'cardStatus': instance.cardStatus,
      'cardBalance': instance.cardBalance,
      'cardAltName': instance.cardAltName,
      'cardColor': instance.cardColor,
      'bankCardType': instance.bankCardType,
    };
