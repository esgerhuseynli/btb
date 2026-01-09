// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'sign_in_request.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

SignInRequest _$SignInRequestFromJson(Map<String, dynamic> json) =>
    SignInRequest(
      requestInfo:
          RequestInfo.fromJson(json['RequestInfo'] as Map<String, dynamic>),
      keystoreType: (json['KeystoreType'] as num).toInt(),
      signInType: (json['SignInType'] as num).toInt(),
      mobileNumber: json['MobileNumber'] as String?,
      mobileNumberSecretCode: json['MobileNumberSecretCode'] as String?,
    );

Map<String, dynamic> _$SignInRequestToJson(SignInRequest instance) {
  final val = <String, dynamic>{
    'RequestInfo': instance.requestInfo.toJson(),
    'KeystoreType': instance.keystoreType,
    'SignInType': instance.signInType,
  };

  void writeNotNull(String key, dynamic value) {
    if (value != null) {
      val[key] = value;
    }
  }

  writeNotNull('MobileNumber', instance.mobileNumber);
  writeNotNull('MobileNumberSecretCode', instance.mobileNumberSecretCode);
  return val;
}
