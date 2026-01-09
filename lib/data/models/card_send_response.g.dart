// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'card_send_response.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

CardSendResponse _$CardSendResponseFromJson(Map<String, dynamic> json) =>
    CardSendResponse(
      responseInfo:
          ResponseInfo.fromJson(json['responceInfo'] as Map<String, dynamic>),
      mobileNumber: json['mobileNumber'] as String?,
      maskedMobileNumber: json['maskedMobileNumber'] as String?,
      email: json['email'] as String?,
    );

Map<String, dynamic> _$CardSendResponseToJson(CardSendResponse instance) {
  final val = <String, dynamic>{
    'responceInfo': instance.responseInfo.toJson(),
  };

  void writeNotNull(String key, dynamic value) {
    if (value != null) {
      val[key] = value;
    }
  }

  writeNotNull('mobileNumber', instance.mobileNumber);
  writeNotNull('maskedMobileNumber', instance.maskedMobileNumber);
  writeNotNull('email', instance.email);
  return val;
}
