// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'bank_accounts_list_request.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

BankAccountsListRequest _$BankAccountsListRequestFromJson(
        Map<String, dynamic> json) =>
    BankAccountsListRequest(
      requestInfo:
          RequestInfo.fromJson(json['requestInfo'] as Map<String, dynamic>),
      requestParametersValidationMessage:
          json['requestParametersValidationMessage'] as String?,
      requestParametersValidated: json['requestParametersValidated'] as bool?,
    );

Map<String, dynamic> _$BankAccountsListRequestToJson(
    BankAccountsListRequest instance) {
  final val = <String, dynamic>{
    'requestInfo': instance.requestInfo.toJson(),
  };

  void writeNotNull(String key, dynamic value) {
    if (value != null) {
      val[key] = value;
    }
  }

  writeNotNull('requestParametersValidationMessage',
      instance.requestParametersValidationMessage);
  writeNotNull(
      'requestParametersValidated', instance.requestParametersValidated);
  return val;
}
