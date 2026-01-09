// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'device_info.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

DeviceInfo _$DeviceInfoFromJson(Map<String, dynamic> json) => DeviceInfo(
      deviceID: json['DeviceID'] as String,
      vendor: json['Vendor'] as String,
      model: json['Model'] as String,
      osName: json['OSName'] as String,
      osVersion: json['OSVersion'] as String,
    );

Map<String, dynamic> _$DeviceInfoToJson(DeviceInfo instance) =>
    <String, dynamic>{
      'DeviceID': instance.deviceID,
      'Vendor': instance.vendor,
      'Model': instance.model,
      'OSName': instance.osName,
      'OSVersion': instance.osVersion,
    };
