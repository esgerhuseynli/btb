// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'new_forgot_password_request.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

NewForgotPasswordRequest _$NewForgotPasswordRequestFromJson(
        Map<String, dynamic> json) =>
    NewForgotPasswordRequest(
      requestInfo:
          RequestInfo.fromJson(json['RequestInfo'] as Map<String, dynamic>),
      requestParametersValidationMessage:
          json['RequestParametersValidationMessage'] as String,
      requestParametersValidated: json['RequestParametersValidated'] as bool,
    );

Map<String, dynamic> _$NewForgotPasswordRequestToJson(
        NewForgotPasswordRequest instance) =>
    <String, dynamic>{
      'RequestInfo': instance.requestInfo.toJson(),
      'RequestParametersValidationMessage':
          instance.requestParametersValidationMessage,
      'RequestParametersValidated': instance.requestParametersValidated,
    };
