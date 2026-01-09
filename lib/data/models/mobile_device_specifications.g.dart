// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'mobile_device_specifications.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

MobileDeviceSpecifications _$MobileDeviceSpecificationsFromJson(
        Map<String, dynamic> json) =>
    MobileDeviceSpecifications(
      nfc: json['NFC'] as String,
      faceID: json['FaceID'] as String,
      touchID: json['TouchID'] as String,
    );

Map<String, dynamic> _$MobileDeviceSpecificationsToJson(
        MobileDeviceSpecifications instance) =>
    <String, dynamic>{
      'NFC': instance.nfc,
      'FaceID': instance.faceID,
      'TouchID': instance.touchID,
    };
