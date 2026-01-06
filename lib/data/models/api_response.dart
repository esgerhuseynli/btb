import 'package:json_annotation/json_annotation.dart';
import 'response_info.dart';

part 'api_response.g.dart';

@JsonSerializable(genericArgumentFactories: true)
class ApiResponse<T> {
  @JsonKey(name: 'ResponseInfo')
  final ResponseInfo responseInfo;

  @JsonKey(name: 'Data')
  final T? data;

  ApiResponse({
    required this.responseInfo,
    this.data,
  });

  factory ApiResponse.fromJson(
    Map<String, dynamic> json,
    T Function(Object? json) fromJsonT,
  ) =>
      _$ApiResponseFromJson(json, fromJsonT);

  Map<String, dynamic> toJson(Object? Function(T value) toJsonT) =>
      _$ApiResponseToJson(this, toJsonT);
}



