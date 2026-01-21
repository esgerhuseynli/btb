import 'package:json_annotation/json_annotation.dart';

part 'mobile_user.g.dart';

@JsonSerializable(explicitToJson: true, includeIfNull: false)
class MobileUser {
  @JsonKey(name: 'Username')
  final String? username;

  @JsonKey(name: 'PasswordHash')
  final String? passwordHash;

  @JsonKey(name: 'sessionKey', includeIfNull: false)
  final String? sessionKey;

  @JsonKey(name: 'SaltSignature', includeIfNull: false)
  final String? saltSignature;

  @JsonKey(name: 'pinCode', includeIfNull: false)
  final String? pinCode;

  @JsonKey(name: 'phoneNumber', includeIfNull: false)
  final String? phoneNumber;

  @JsonKey(name: 'birthDate', includeIfNull: false)
  final String? birthDate;

  MobileUser({
    this.username,
    this.passwordHash,
    this.sessionKey,
    this.saltSignature,
    this.pinCode,
    this.phoneNumber,
    this.birthDate,
  });

  factory MobileUser.fromJson(Map<String, dynamic> json) =>
      _$MobileUserFromJson(json);

  Map<String, dynamic> toJson() => _$MobileUserToJson(this);
}

