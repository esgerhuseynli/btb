// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'bank_accounts_response.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

BankAccountsResponse _$BankAccountsResponseFromJson(
        Map<String, dynamic> json) =>
    BankAccountsResponse(
      responseInfo:
          ResponseInfo.fromJson(json['responceInfo'] as Map<String, dynamic>),
      bankAccounts: (json['bankAccounts'] as List<dynamic>?)
          ?.map((e) => BankAccount.fromJson(e as Map<String, dynamic>))
          .toList(),
    );

Map<String, dynamic> _$BankAccountsResponseToJson(
        BankAccountsResponse instance) =>
    <String, dynamic>{
      'responceInfo': instance.responseInfo,
      'bankAccounts': instance.bankAccounts,
    };
