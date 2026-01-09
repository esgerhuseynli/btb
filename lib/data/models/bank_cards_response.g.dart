// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'bank_cards_response.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

BankCardsResponse _$BankCardsResponseFromJson(Map<String, dynamic> json) =>
    BankCardsResponse(
      responseInfo:
          ResponseInfo.fromJson(json['responceInfo'] as Map<String, dynamic>),
      bankCards: (json['bankCards'] as List<dynamic>?)
          ?.map((e) => BankCard.fromJson(e as Map<String, dynamic>))
          .toList(),
    );

Map<String, dynamic> _$BankCardsResponseToJson(BankCardsResponse instance) =>
    <String, dynamic>{
      'responceInfo': instance.responseInfo,
      'bankCards': instance.bankCards,
    };
