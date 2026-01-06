import 'package:json_annotation/json_annotation.dart';
import 'response_info.dart';

part 'sign_in_response.g.dart';

@JsonSerializable()
class SignInResponse {
  @JsonKey(name: 'responceInfo')
  final ResponseInfo responseInfo;

  @JsonKey(name: 'sessionKey')
  final String? sessionKey;

  @JsonKey(name: 'signInActionCode')
  final int signInActionCode;

  SignInResponse({
    required this.responseInfo,
    this.sessionKey,
    required this.signInActionCode,
  });

  factory SignInResponse.fromJson(Map<String, dynamic> json) =>
      _$SignInResponseFromJson(json);

  Map<String, dynamic> toJson() => _$SignInResponseToJson(this);

  bool get isSuccess => responseInfo.responseType == 0;
}

