import 'package:json_annotation/json_annotation.dart';
import 'request_info.dart';

part 'sign_up_request.g.dart';

@JsonSerializable(explicitToJson: true, includeIfNull: false)
class SignUpRequest {
  @JsonKey(name: 'RequestInfo')
  final RequestInfo requestInfo;

  @JsonKey(name: 'UsernameType')
  final int usernameType;

  @JsonKey(name: 'SignUpType')
  final int signUpType;

  @JsonKey(name: 'PAN', includeIfNull: false)
  final String? pan;

  @JsonKey(name: 'CustomerNumber', includeIfNull: false)
  final String? customerNumber;

  @JsonKey(name: 'CustomerBirthdate', includeIfNull: false)
  final String? customerBirthdate;

  @JsonKey(name: 'VerificationCode')
  final String verificationCode;

  @JsonKey(name: 'MobileNumber', includeIfNull: false)
  final String? mobileNumber;

  @JsonKey(name: 'MobileNumberSecretCode', includeIfNull: false)
  final String? mobileNumberSecretCode;

  SignUpRequest({
    required this.requestInfo,
    required this.usernameType,
    required this.signUpType,
    required this.verificationCode,
    this.pan,
    this.customerNumber,
    this.customerBirthdate,
    this.mobileNumber,
    this.mobileNumberSecretCode,
  });

  factory SignUpRequest.fromJson(Map<String, dynamic> json) =>
      _$SignUpRequestFromJson(json);

  Map<String, dynamic> toJson() => _$SignUpRequestToJson(this);
}



