// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'sign_in_response.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

SignInResponse _$SignInResponseFromJson(Map<String, dynamic> json) =>
    SignInResponse(
      responseInfo:
          ResponseInfo.fromJson(json['responceInfo'] as Map<String, dynamic>),
      sessionKey: json['sessionKey'] as String?,
      signInActionCode: (json['signInActionCode'] as num).toInt(),
    );

Map<String, dynamic> _$SignInResponseToJson(SignInResponse instance) =>
    <String, dynamic>{
      'responceInfo': instance.responseInfo,
      'sessionKey': instance.sessionKey,
      'signInActionCode': instance.signInActionCode,
    };
