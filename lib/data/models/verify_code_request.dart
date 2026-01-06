import 'package:json_annotation/json_annotation.dart';
import 'request_info.dart';

part 'verify_code_request.g.dart';

@JsonSerializable(explicitToJson: true, includeIfNull: false)
class VerifyCodeRequest {
  @JsonKey(name: 'RequestInfo')
  final RequestInfo requestInfo;

  @JsonKey(name: 'SignUpType')
  final int signUpType;

  @JsonKey(name: 'PAN')
  final String? pan;

  @JsonKey(name: 'CustomerNumber')
  final String? customerNumber;

  @JsonKey(name: 'CustomerBirthdate')
  final String? customerBirthdate;

  @JsonKey(name: 'VerificationCode')
  final String verificationCode;

  VerifyCodeRequest({
    required this.requestInfo,
    required this.signUpType,
    this.pan,
    this.customerNumber,
    this.customerBirthdate,
    required this.verificationCode,
  });

  factory VerifyCodeRequest.fromJson(Map<String, dynamic> json) =>
      _$VerifyCodeRequestFromJson(json);

  Map<String, dynamic> toJson() => _$VerifyCodeRequestToJson(this);
}



