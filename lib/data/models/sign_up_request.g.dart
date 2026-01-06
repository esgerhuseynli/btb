// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'sign_up_request.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

SignUpRequest _$SignUpRequestFromJson(Map<String, dynamic> json) =>
    SignUpRequest(
      requestInfo:
          RequestInfo.fromJson(json['RequestInfo'] as Map<String, dynamic>),
      usernameType: (json['UsernameType'] as num).toInt(),
      signUpType: (json['SignUpType'] as num).toInt(),
      verificationCode: json['VerificationCode'] as String,
      pan: json['PAN'] as String?,
      customerNumber: json['CustomerNumber'] as String?,
      customerBirthdate: json['CustomerBirthdate'] as String?,
      mobileNumber: json['MobileNumber'] as String?,
      mobileNumberSecretCode: json['MobileNumberSecretCode'] as String?,
    );

Map<String, dynamic> _$SignUpRequestToJson(SignUpRequest instance) {
  final val = <String, dynamic>{
    'RequestInfo': instance.requestInfo.toJson(),
    'UsernameType': instance.usernameType,
    'SignUpType': instance.signUpType,
  };

  void writeNotNull(String key, dynamic value) {
    if (value != null) {
      val[key] = value;
    }
  }

  writeNotNull('PAN', instance.pan);
  writeNotNull('CustomerNumber', instance.customerNumber);
  writeNotNull('CustomerBirthdate', instance.customerBirthdate);
  val['VerificationCode'] = instance.verificationCode;
  writeNotNull('MobileNumber', instance.mobileNumber);
  writeNotNull('MobileNumberSecretCode', instance.mobileNumberSecretCode);
  return val;
}
