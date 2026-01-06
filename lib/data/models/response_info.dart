import 'package:json_annotation/json_annotation.dart';

part 'response_info.g.dart';

@JsonSerializable()
class ResponseInfo {
  @JsonKey(name: 'responseType')
  final int? responseType;

  @JsonKey(name: 'responseMessage')
  final String? responseMessage;

  @JsonKey(name: 'errorCode')
  final int? errorCode;

  @JsonKey(name: 'errorMessage')
  final String? errorMessage;

  @JsonKey(name: 'saltSignature')
  final String? saltSignature;

  ResponseInfo({
    this.responseType,
    this.responseMessage,
    this.errorCode,
    this.errorMessage,
    this.saltSignature,
  });

  factory ResponseInfo.fromJson(Map<String, dynamic> json) =>
      _$ResponseInfoFromJson(json);

  Map<String, dynamic> toJson() => _$ResponseInfoToJson(this);

  bool get isSuccess => responseType == 0;
  bool get hasError => errorCode != null && errorCode! > 0;
}

