import 'package:json_annotation/json_annotation.dart';

part 'otp_verify_request.g.dart';

@JsonSerializable(explicitToJson: true, includeIfNull: false)
class OtpVerifyRequest {
  @JsonKey(name: 'OtpCode')
  final String otpCode;

  @JsonKey(name: 'PhoneNumber')
  final String phoneNumber;

  OtpVerifyRequest({
    required this.otpCode,
    required this.phoneNumber,
  });

  factory OtpVerifyRequest.fromJson(Map<String, dynamic> json) =>
      _$OtpVerifyRequestFromJson(json);

  Map<String, dynamic> toJson() => _$OtpVerifyRequestToJson(this);
}

