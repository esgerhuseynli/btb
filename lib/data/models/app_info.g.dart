// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'app_info.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

AppInfo _$AppInfoFromJson(Map<String, dynamic> json) => AppInfo(
      appFor: (json['AppFor'] as num).toInt(),
      appName: json['AppName'] as String,
      appVersion: json['AppVersion'] as String,
      apiHash: json['AppHash'] as String?,
    );

Map<String, dynamic> _$AppInfoToJson(AppInfo instance) => <String, dynamic>{
      'AppFor': instance.appFor,
      'AppName': instance.appName,
      'AppVersion': instance.appVersion,
      'AppHash': instance.apiHash,
    };
