// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'request_info.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

RequestInfo _$RequestInfoFromJson(Map<String, dynamic> json) => RequestInfo(
      mobileUser: json['MobileUser'] == null
          ? null
          : MobileUser.fromJson(json['MobileUser'] as Map<String, dynamic>),
      deviceInfo:
          DeviceInfo.fromJson(json['DeviceInfo'] as Map<String, dynamic>),
      appInfo: AppInfo.fromJson(json['AppInfo'] as Map<String, dynamic>),
      language: (json['Language'] as num).toInt(),
    );

Map<String, dynamic> _$RequestInfoToJson(RequestInfo instance) =>
    <String, dynamic>{
      'MobileUser': instance.mobileUser?.toJson(),
      'DeviceInfo': instance.deviceInfo.toJson(),
      'AppInfo': instance.appInfo.toJson(),
      'Language': instance.language,
    };
