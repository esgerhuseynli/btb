// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'mobile_user_data_request.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

MobileUserDataRequest _$MobileUserDataRequestFromJson(
        Map<String, dynamic> json) =>
    MobileUserDataRequest(
      requestInfo:
          RequestInfo.fromJson(json['requestInfo'] as Map<String, dynamic>),
      requestParametersValidationMessage:
          json['requestParametersValidationMessage'] as String?,
      requestParametersValidated: json['requestParametersValidated'] as bool?,
    );

Map<String, dynamic> _$MobileUserDataRequestToJson(
    MobileUserDataRequest instance) {
  final val = <String, dynamic>{
    'requestInfo': instance.requestInfo.toJson(),
  };

  void writeNotNull(String key, dynamic value) {
    if (value != null) {
      val[key] = value;
    }
  }

  writeNotNull('requestParametersValidationMessage',
      instance.requestParametersValidationMessage);
  writeNotNull(
      'requestParametersValidated', instance.requestParametersValidated);
  return val;
}
