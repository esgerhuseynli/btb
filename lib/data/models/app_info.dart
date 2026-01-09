import 'package:json_annotation/json_annotation.dart';

part 'app_info.g.dart';

@JsonSerializable()
class AppInfo {
  @JsonKey(name: 'AppFor')
  final int appFor;

  @JsonKey(name: 'AppName')
  final String appName;

  @JsonKey(name: 'AppVersion')
  final String appVersion;

  @JsonKey(name: 'AppHash')
  final String? apiHash;

  AppInfo({
    required this.appFor,
    required this.appName,
    required this.appVersion,
    this.apiHash,
  });

  factory AppInfo.fromJson(Map<String, dynamic> json) =>
      _$AppInfoFromJson(json);

  Map<String, dynamic> toJson() => _$AppInfoToJson(this);
}



