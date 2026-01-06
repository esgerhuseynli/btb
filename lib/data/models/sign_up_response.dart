import 'package:json_annotation/json_annotation.dart';
import 'response_info.dart';

part 'sign_up_response.g.dart';

@JsonSerializable()
class SignUpResponse {
  @JsonKey(name: 'responceInfo')
  final ResponseInfo responseInfo;

  SignUpResponse({
    required this.responseInfo,
  });

  factory SignUpResponse.fromJson(Map<String, dynamic> json) =>
      _$SignUpResponseFromJson(json);

  Map<String, dynamic> toJson() => _$SignUpResponseToJson(this);

  bool get isSuccess => responseInfo.responseType == 0;
}



