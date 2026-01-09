// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'card_send_request.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

CardSendRequest _$CardSendRequestFromJson(Map<String, dynamic> json) =>
    CardSendRequest(
      requestInfo:
          RequestInfo.fromJson(json['RequestInfo'] as Map<String, dynamic>),
      signUpType: (json['SignUpType'] as num).toInt(),
      pan: json['Pan'] as String,
      customerNumber: json['CustomerNumber'] as String,
      customerBirthdate: json['CustomerBirthdate'] as String,
      mobileNumber: json['MobileNumber'] as String,
      mobileNumberSecretCode: json['MobileNumberSecretCode'] as String,
    );

Map<String, dynamic> _$CardSendRequestToJson(CardSendRequest instance) =>
    <String, dynamic>{
      'RequestInfo': instance.requestInfo.toJson(),
      'SignUpType': instance.signUpType,
      'Pan': instance.pan,
      'CustomerNumber': instance.customerNumber,
      'CustomerBirthdate': instance.customerBirthdate,
      'MobileNumber': instance.mobileNumber,
      'MobileNumberSecretCode': instance.mobileNumberSecretCode,
    };
