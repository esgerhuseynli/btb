import 'package:json_annotation/json_annotation.dart';
import 'request_info.dart';

part 'sign_in_request.g.dart';

@JsonSerializable(explicitToJson: true, includeIfNull: false)
class SignInRequest {
  @JsonKey(name: 'RequestInfo')
  final RequestInfo requestInfo;

  @JsonKey(name: 'KeystoreType')
  final int keystoreType;

  @JsonKey(name: 'SignInType')
  final int signInType;

  @JsonKey(name: 'MobileNumber', includeIfNull: false)
  final String? mobileNumber;

  @JsonKey(name: 'MobileNumberSecretCode', includeIfNull: false)
  final String? mobileNumberSecretCode;

  SignInRequest({
    required this.requestInfo,
    required this.keystoreType,
    required this.signInType,
    this.mobileNumber,
    this.mobileNumberSecretCode,
  });

  factory SignInRequest.fromJson(Map<String, dynamic> json) =>
      _$SignInRequestFromJson(json);

  Map<String, dynamic> toJson() => _$SignInRequestToJson(this);
}

