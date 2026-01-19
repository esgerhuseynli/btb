// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'otp_request.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

OtpRequest _$OtpRequestFromJson(Map<String, dynamic> json) => OtpRequest(
      phoneNumber: json['PhoneNumber'] as String,
      text: json['Text'] as String,
      type: (json['Type'] as num).toInt(),
      userId: json['UserId'] as String,
    );

Map<String, dynamic> _$OtpRequestToJson(OtpRequest instance) =>
    <String, dynamic>{
      'PhoneNumber': instance.phoneNumber,
      'Text': instance.text,
      'Type': instance.type,
      'UserId': instance.userId,
    };
