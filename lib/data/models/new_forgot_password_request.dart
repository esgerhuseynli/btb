import 'package:json_annotation/json_annotation.dart';
import 'request_info.dart';

part 'new_forgot_password_request.g.dart';

@JsonSerializable(explicitToJson: true, includeIfNull: false)
class NewForgotPasswordRequest {
  @JsonKey(name: 'RequestInfo')
  final RequestInfo requestInfo;

  @JsonKey(name: 'RequestParametersValidationMessage')
  final String requestParametersValidationMessage;

  @JsonKey(name: 'RequestParametersValidated')
  final bool requestParametersValidated;

  NewForgotPasswordRequest({
    required this.requestInfo,
    required this.requestParametersValidationMessage,
    required this.requestParametersValidated,
  });

  factory NewForgotPasswordRequest.fromJson(Map<String, dynamic> json) =>
      _$NewForgotPasswordRequestFromJson(json);

  Map<String, dynamic> toJson() => _$NewForgotPasswordRequestToJson(this);
}

