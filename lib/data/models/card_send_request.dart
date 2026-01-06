import 'package:json_annotation/json_annotation.dart';
import 'request_info.dart';

part 'card_send_request.g.dart';

@JsonSerializable(explicitToJson: true, includeIfNull: false)
class CardSendRequest {
  @JsonKey(name: 'RequestInfo')
  final RequestInfo requestInfo;

  @JsonKey(name: 'SignUpType')
  final int signUpType;

  @JsonKey(name: 'Pan')
  final String pan;

  @JsonKey(name: 'CustomerNumber')
  final String customerNumber;

  @JsonKey(name: 'CustomerBirthdate')
  final String customerBirthdate;

  @JsonKey(name: 'MobileNumber')
  final String mobileNumber;

  @JsonKey(name: 'MobileNumberSecretCode')
  final String mobileNumberSecretCode;

  CardSendRequest({
    required this.requestInfo,
    required this.signUpType,
    required this.pan,
    required this.customerNumber,
    required this.customerBirthdate,
    required this.mobileNumber,
    required this.mobileNumberSecretCode,
  });

  factory CardSendRequest.fromJson(Map<String, dynamic> json) =>
      _$CardSendRequestFromJson(json);

  Map<String, dynamic> toJson() => _$CardSendRequestToJson(this);
}


