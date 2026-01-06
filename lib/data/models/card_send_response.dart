import 'package:json_annotation/json_annotation.dart';
import 'response_info.dart';

part 'card_send_response.g.dart';

@JsonSerializable(explicitToJson: true, includeIfNull: false)
class CardSendResponse {
  @JsonKey(name: 'responceInfo')
  final ResponseInfo responseInfo;

  @JsonKey(name: 'mobileNumber')
  final String? mobileNumber;

  @JsonKey(name: 'maskedMobileNumber')
  final String? maskedMobileNumber;

  @JsonKey(name: 'email')
  final String? email;

  CardSendResponse({
    required this.responseInfo,
    this.mobileNumber,
    this.maskedMobileNumber,
    this.email,
  });

  factory CardSendResponse.fromJson(Map<String, dynamic> json) =>
      _$CardSendResponseFromJson(json);

  Map<String, dynamic> toJson() => _$CardSendResponseToJson(this);

  bool get isSuccess => responseInfo.responseType == 0;
}



