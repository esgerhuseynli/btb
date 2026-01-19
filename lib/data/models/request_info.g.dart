// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'request_info.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

RequestInfo _$RequestInfoFromJson(Map<String, dynamic> json) => RequestInfo(
      mobileUser: json['MobileUser'] == null
          ? null
          : MobileUser.fromJson(json['MobileUser'] as Map<String, dynamic>),
      phoneNumber: json['phoneNumber'] as String?,
      pinCode: json['pinCode'] as String?,
      deviceInfo:
          DeviceInfo.fromJson(json['DeviceInfo'] as Map<String, dynamic>),
      appInfo: AppInfo.fromJson(json['AppInfo'] as Map<String, dynamic>),
      language: (json['Language'] as num).toInt(),
    );

Map<String, dynamic> _$RequestInfoToJson(RequestInfo instance) {
  final val = <String, dynamic>{
    'MobileUser': instance.mobileUser?.toJson(),
  };

  void writeNotNull(String key, dynamic value) {
    if (value != null) {
      val[key] = value;
    }
  }

  writeNotNull('phoneNumber', instance.phoneNumber);
  writeNotNull('pinCode', instance.pinCode);
  val['DeviceInfo'] = instance.deviceInfo.toJson();
  val['AppInfo'] = instance.appInfo.toJson();
  val['Language'] = instance.language;
  return val;
}
