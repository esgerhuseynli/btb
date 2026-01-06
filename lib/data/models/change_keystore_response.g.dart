// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'change_keystore_response.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

ChangeKeystoreResponse _$ChangeKeystoreResponseFromJson(
        Map<String, dynamic> json) =>
    ChangeKeystoreResponse(
      responseInfo:
          ResponseInfo.fromJson(json['responceInfo'] as Map<String, dynamic>),
      passwordHash: json['passwordHash'] as String,
    );

Map<String, dynamic> _$ChangeKeystoreResponseToJson(
        ChangeKeystoreResponse instance) =>
    <String, dynamic>{
      'responceInfo': instance.responseInfo.toJson(),
      'passwordHash': instance.passwordHash,
    };
