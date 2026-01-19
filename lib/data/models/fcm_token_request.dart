import 'package:json_annotation/json_annotation.dart';
import 'request_info.dart';

part 'fcm_token_request.g.dart';

@JsonSerializable(explicitToJson: true)
class FcmTokenRequest {
  @JsonKey(name: 'RequestInfo')
  final RequestInfo requestInfo;

  @JsonKey(name: 'DevicePushInfoToken')
  final String devicePushInfoToken;

  FcmTokenRequest({
    required this.requestInfo,
    required this.devicePushInfoToken,
  });

  factory FcmTokenRequest.fromJson(Map<String, dynamic> json) =>
      _$FcmTokenRequestFromJson(json);

  Map<String, dynamic> toJson() => _$FcmTokenRequestToJson(this);
}









