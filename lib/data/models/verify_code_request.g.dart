// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'verify_code_request.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

VerifyCodeRequest _$VerifyCodeRequestFromJson(Map<String, dynamic> json) =>
    VerifyCodeRequest(
      requestInfo:
          RequestInfo.fromJson(json['RequestInfo'] as Map<String, dynamic>),
      signUpType: (json['SignUpType'] as num).toInt(),
      pan: json['PAN'] as String?,
      customerNumber: json['CustomerNumber'] as String?,
      customerBirthdate: json['CustomerBirthdate'] as String?,
      verificationCode: json['VerificationCode'] as String,
    );

Map<String, dynamic> _$VerifyCodeRequestToJson(VerifyCodeRequest instance) {
  final val = <String, dynamic>{
    'RequestInfo': instance.requestInfo.toJson(),
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
  return val;
}
