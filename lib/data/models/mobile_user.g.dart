// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'mobile_user.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

MobileUser _$MobileUserFromJson(Map<String, dynamic> json) => MobileUser(
      username: json['Username'] as String?,
      passwordHash: json['PasswordHash'] as String?,
      sessionKey: json['sessionKey'] as String?,
      saltSignature: json['SaltSignature'] as String?,
      pinCode: json['pinCode'] as String?,
      phoneNumber: json['phoneNumber'] as String?,
      birthDate: json['birthDate'] as String?,
    );

Map<String, dynamic> _$MobileUserToJson(MobileUser instance) {
  final val = <String, dynamic>{};

  void writeNotNull(String key, dynamic value) {
    if (value != null) {
      val[key] = value;
    }
  }

  writeNotNull('Username', instance.username);
  writeNotNull('PasswordHash', instance.passwordHash);
  writeNotNull('sessionKey', instance.sessionKey);
  writeNotNull('SaltSignature', instance.saltSignature);
  writeNotNull('pinCode', instance.pinCode);
  writeNotNull('phoneNumber', instance.phoneNumber);
  writeNotNull('birthDate', instance.birthDate);
  return val;
}
