import 'package:json_annotation/json_annotation.dart';

part 'otp_request.g.dart';

@JsonSerializable(explicitToJson: true, includeIfNull: false)
class OtpRequest {
  @JsonKey(name: 'PhoneNumber')
  final String phoneNumber;

  @JsonKey(name: 'Text')
  final String text;

  @JsonKey(name: 'Type')
  final int type;

  @JsonKey(name: 'UserId')
  final String userId;

  OtpRequest({
    required this.phoneNumber,
    required this.text,
    required this.type,
    required this.userId,
  });

  factory OtpRequest.fromJson(Map<String, dynamic> json) =>
      _$OtpRequestFromJson(json);

  Map<String, dynamic> toJson() => _$OtpRequestToJson(this);
}

