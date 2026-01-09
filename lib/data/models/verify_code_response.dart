import 'package:json_annotation/json_annotation.dart';
import 'response_info.dart';

part 'verify_code_response.g.dart';

@JsonSerializable(explicitToJson: true, includeIfNull: false)
class VerifyCodeResponse {
  @JsonKey(name: 'responceInfo')
  final ResponseInfo responseInfo;

  @JsonKey(name: 'verificationCodeResult')
  final int? verificationCodeResult;

  @JsonKey(name: 'mobileUserSignUpStatus')
  final int? mobileUserSignUpStatus;

  VerifyCodeResponse({
    required this.responseInfo,
    this.verificationCodeResult,
    this.mobileUserSignUpStatus,
  });

  factory VerifyCodeResponse.fromJson(Map<String, dynamic> json) =>
      _$VerifyCodeResponseFromJson(json);

  Map<String, dynamic> toJson() => _$VerifyCodeResponseToJson(this);

  bool get isSuccess => responseInfo.responseType == 0;
}



