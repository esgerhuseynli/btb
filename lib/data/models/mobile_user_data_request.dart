import 'package:json_annotation/json_annotation.dart';
import 'request_info.dart';

part 'mobile_user_data_request.g.dart';

@JsonSerializable(explicitToJson: true, includeIfNull: false)
class MobileUserDataRequest {
  @JsonKey(name: 'requestInfo')
  final RequestInfo requestInfo;

  @JsonKey(name: 'requestParametersValidationMessage', includeIfNull: false)
  final String? requestParametersValidationMessage;

  @JsonKey(name: 'requestParametersValidated', includeIfNull: false)
  final bool? requestParametersValidated;

  MobileUserDataRequest({
    required this.requestInfo,
    this.requestParametersValidationMessage,
    this.requestParametersValidated,
  });

  factory MobileUserDataRequest.fromJson(Map<String, dynamic> json) =>
      _$MobileUserDataRequestFromJson(json);

  Map<String, dynamic> toJson() => _$MobileUserDataRequestToJson(this);
}
