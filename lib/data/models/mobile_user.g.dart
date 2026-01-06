// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'mobile_user.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

MobileUser _$MobileUserFromJson(Map<String, dynamic> json) => MobileUser(
      username: json['Username'] as String?,
      passwordHash: json['PasswordHash'] as String?,
      sessionKey: json['SessionKey'] as String?,
      saltSignature: json['SaltSignature'] as String?,
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
  writeNotNull('SessionKey', instance.sessionKey);
  writeNotNull('SaltSignature', instance.saltSignature);
  return val;
}
