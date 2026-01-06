// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'fcm_token_request.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

FcmTokenRequest _$FcmTokenRequestFromJson(Map<String, dynamic> json) =>
    FcmTokenRequest(
      requestInfo:
          RequestInfo.fromJson(json['RequestInfo'] as Map<String, dynamic>),
      devicePushInfoToken: json['DevicePushInfoToken'] as String,
    );

Map<String, dynamic> _$FcmTokenRequestToJson(FcmTokenRequest instance) =>
    <String, dynamic>{
      'RequestInfo': instance.requestInfo.toJson(),
      'DevicePushInfoToken': instance.devicePushInfoToken,
    };
