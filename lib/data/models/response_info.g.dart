// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'response_info.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

ResponseInfo _$ResponseInfoFromJson(Map<String, dynamic> json) => ResponseInfo(
      responseType: (json['responseType'] as num?)?.toInt(),
      responseMessage: json['responseMessage'] as String?,
      errorCode: (json['errorCode'] as num?)?.toInt(),
      errorMessage: json['errorMessage'] as String?,
      saltSignature: json['saltSignature'] as String?,
    );

Map<String, dynamic> _$ResponseInfoToJson(ResponseInfo instance) =>
    <String, dynamic>{
      'responseType': instance.responseType,
      'responseMessage': instance.responseMessage,
      'errorCode': instance.errorCode,
      'errorMessage': instance.errorMessage,
      'saltSignature': instance.saltSignature,
    };
