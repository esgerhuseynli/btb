import 'package:json_annotation/json_annotation.dart';
import 'response_info.dart';

part 'change_keystore_response.g.dart';

@JsonSerializable(explicitToJson: true)
class ChangeKeystoreResponse {
  @JsonKey(name: 'responceInfo') // Note: Android has typo "responceInfo"
  final ResponseInfo responseInfo;

  @JsonKey(name: 'passwordHash')
  final String passwordHash;

  ChangeKeystoreResponse({
    required this.responseInfo,
    required this.passwordHash,
  });

  factory ChangeKeystoreResponse.fromJson(Map<String, dynamic> json) =>
      _$ChangeKeystoreResponseFromJson(json);

  Map<String, dynamic> toJson() => _$ChangeKeystoreResponseToJson(this);
}








