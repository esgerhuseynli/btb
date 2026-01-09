import 'package:json_annotation/json_annotation.dart';

part 'mobile_user.g.dart';

@JsonSerializable(explicitToJson: true, includeIfNull: false)
class MobileUser {
  @JsonKey(name: 'Username')
  final String? username;

  @JsonKey(name: 'PasswordHash')
  final String? passwordHash;

  @JsonKey(name: 'SessionKey', includeIfNull: false)
  final String? sessionKey;

  @JsonKey(name: 'SaltSignature', includeIfNull: false)
  final String? saltSignature;

  MobileUser({
    this.username,
    this.passwordHash,
    this.sessionKey,
    this.saltSignature,
  });

  factory MobileUser.fromJson(Map<String, dynamic> json) =>
      _$MobileUserFromJson(json);

  Map<String, dynamic> toJson() => _$MobileUserToJson(this);
}

