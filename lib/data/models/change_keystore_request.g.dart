// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'change_keystore_request.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

ChangeKeystoreRequest _$ChangeKeystoreRequestFromJson(
        Map<String, dynamic> json) =>
    ChangeKeystoreRequest(
      requestInfo:
          RequestInfo.fromJson(json['RequestInfo'] as Map<String, dynamic>),
      keystoreType: (json['KeystoreType'] as num).toInt(),
      mobileDeviceSpecifications: MobileDeviceSpecifications.fromJson(
          json['MobileDeviceSpecifications'] as Map<String, dynamic>),
    );

Map<String, dynamic> _$ChangeKeystoreRequestToJson(
        ChangeKeystoreRequest instance) =>
    <String, dynamic>{
      'RequestInfo': instance.requestInfo.toJson(),
      'KeystoreType': instance.keystoreType,
      'MobileDeviceSpecifications':
          instance.mobileDeviceSpecifications.toJson(),
    };
