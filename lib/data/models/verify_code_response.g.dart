// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'verify_code_response.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

VerifyCodeResponse _$VerifyCodeResponseFromJson(Map<String, dynamic> json) =>
    VerifyCodeResponse(
      responseInfo:
          ResponseInfo.fromJson(json['responceInfo'] as Map<String, dynamic>),
      verificationCodeResult: (json['verificationCodeResult'] as num?)?.toInt(),
      mobileUserSignUpStatus: (json['mobileUserSignUpStatus'] as num?)?.toInt(),
    );

Map<String, dynamic> _$VerifyCodeResponseToJson(VerifyCodeResponse instance) {
  final val = <String, dynamic>{
    'responceInfo': instance.responseInfo.toJson(),
  };

  void writeNotNull(String key, dynamic value) {
    if (value != null) {
      val[key] = value;
    }
  }

  writeNotNull('verificationCodeResult', instance.verificationCodeResult);
  writeNotNull('mobileUserSignUpStatus', instance.mobileUserSignUpStatus);
  return val;
}
